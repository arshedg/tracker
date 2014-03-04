

package client;

import worker.DataProcessor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gearman.client.GearmanClient;
import org.gearman.client.GearmanClientImpl;
import org.gearman.client.GearmanJob;
import org.gearman.client.GearmanJobImpl;
import org.gearman.client.GearmanJobResult;
import org.gearman.common.Constants;
import org.gearman.common.GearmanJobServerConnection;
import org.gearman.common.GearmanNIOJobServerConnection;

/**
 *
 * @author marshed
 */
public class NLPClient implements Runnable{
     private final GearmanClient client;
     BlockingQueue<String> tasks = new ArrayBlockingQueue<>(5); 
     private boolean hasMoreWork=true;
     private final static Integer DB_PORT = 27017;
     private final static String HOST = "localhost";
     public NLPClient(GearmanJobServerConnection conn) {
        client = new GearmanClientImpl();
        client.addJobServer(conn);
        new Thread(this).start(); 
    }
    public void Process() throws InterruptedException, UnsupportedEncodingException{
        String function = DataProcessor.class.getCanonicalName();
        while(hasMoreWork||!tasks.isEmpty()){
            String data = tasks.take(); //will create maximum of 5 unattended jobs(capcaity constraint of blocking queue)
            GearmanJob job = GearmanJobImpl.createJob(function, data.getBytes("utf-8"), null);
            client.submit(job);
            JobListener.addToListener(job);    
        }   
    } 
    private void produce() throws InterruptedException{
        try {
            MongoClient mongoClient = new MongoClient( HOST, DB_PORT );
            DB db = mongoClient.getDB("test");
            DBCollection collection = db.getCollection("articles");
            try (DBCursor cursor = collection.find()) {
                while(cursor.hasNext()) {
                    DBObject object = cursor.next();
                    String data=object.get("description").toString();
                    String id=object.get("_id").toString();
                    tasks.put(id+":"+data);
                }
            }
            hasMoreWork=false;            
        }   
        catch (UnknownHostException ex) {
            Logger.getLogger(NLPClient.class.getName()).log(Level.SEVERE, "Monog DB not started or host/port incorrect", ex);
        }
    }

    @Override
    public void run() {
         try {
             produce();
         } catch (InterruptedException ex) {
             Logger.getLogger(NLPClient.class.getName()).log(Level.SEVERE, "unknown error,blame java", ex);
         }
    }
    public static void main(String arg[]) throws InterruptedException, UnsupportedEncodingException{
        String host = Constants.GEARMAN_DEFAULT_TCP_HOST;
        int port = Constants.GEARMAN_DEFAULT_TCP_PORT;
        NLPClient client = new NLPClient(new GearmanNIOJobServerConnection(host, port));
        client.Process();
    }
}
class JobListener implements Runnable{
    private final GearmanJob job;
    JobListener(GearmanJob job){
        this.job=job;
    }
    @Override
    public void run() {
        try {
            GearmanJobResult result = job.get();
            //currently not handling exceptions from the worker; 
            Logger.getLogger(NLPClient.class.getName()).log(Level.INFO,new String(result.getResults()));
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addToListener(GearmanJob job){
        new Thread(new JobListener(job)).start();
    }
    
}