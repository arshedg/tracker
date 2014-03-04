

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
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobEventType;
import org.gearman.GearmanJobReturn;
import server.Server;

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
     public NLPClient() {
         Gearman gearman = Gearman.createGearman();
        client = gearman.createGearmanClient();
         client.addServer(gearman.createGearmanServer("localhost", 4567));
        new Thread(this).start(); 
    }
    public void Process() throws InterruptedException, UnsupportedEncodingException{
        while(hasMoreWork||!tasks.isEmpty()){
            String data = tasks.take(); //will create maximum of 5 unattended jobs(capcaity constraint of blocking queue)
            GearmanJobReturn jobReturn = client.submitJob("NLP",data.getBytes("UTF-8"));
            JobListener.addToListener(jobReturn);    
        }   
    } 
    private void produce() throws InterruptedException{
        tasks.add("1:am a good apple");
        tasks.add("2:that is a butter of ibm");
        tasks.add("3:hello");
//        try {
//            MongoClient mongoClient = new MongoClient( HOST, DB_PORT );
//            DB db = mongoClient.getDB("test");
//            DBCollection collection = db.getCollection("articles");
//            try (DBCursor cursor = collection.find()) {
//                while(cursor.hasNext()) {
//                    DBObject object = cursor.next();
//                    String data=object.get("description").toString();
//                    String id=object.get("_id").toString();
//                    tasks.put(id+":"+data);
//                }
//            }
//            hasMoreWork=false;            
//        }   
//        catch (UnknownHostException ex) {
//            Logger.getLogger(NLPClient.class.getName()).log(Level.SEVERE, "Monog DB not started or host/port incorrect", ex);
//        }
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
         NLPClient nlpClient = new NLPClient();
         nlpClient.Process();

    }
}
class JobListener implements Runnable{
    private final GearmanJobReturn job;
    JobListener(GearmanJobReturn job){
        this.job=job;
    }
    @Override
    public void run() {
 
           while(!job.isEOF()) {
               try {
                   GearmanJobEvent event = job.poll();
                   System.out.print(event.getEventType().toString());
                   if(event.getEventType().equals(GearmanJobEventType.GEARMAN_JOB_SUCCESS)){
                       //currently not handling exceptions from the worker; 
                       String result = new String(event.getData());
                       Logger.getLogger(NLPClient.class.getName()).log(Level.INFO,result);
                   }
               } catch (InterruptedException ex) {
                   Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        
    }
    public static void addToListener(GearmanJobReturn job){
        new Thread(new JobListener(job)).start();
    }
    
}