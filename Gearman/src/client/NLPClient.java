

package client;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobEventType;
import org.gearman.GearmanJobReturn;

/**
 *
 * @author marshed
 */
public class NLPClient implements Runnable{
    private Integer MAX_OPEN_WORK=2;
    AtomicInteger openWorkCount = new AtomicInteger(MAX_OPEN_WORK);
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
    public synchronized void Process() throws InterruptedException, UnsupportedEncodingException{
        while(hasMoreWork||!tasks.isEmpty()){
            String data = tasks.take(); 
            if(openWorkCount.get()==MAX_OPEN_WORK){
                this.wait();
            }
            GearmanJobReturn jobReturn = client.submitJob("NLP",data.getBytes("UTF-8"));
            openWorkCount.incrementAndGet();
            JobListener.addToListener(this,jobReturn);    
        }   
    } 
    private  void produce() throws InterruptedException{
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
    public synchronized void workDone(){
        this.openWorkCount.decrementAndGet();
        this.notify();
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
    private final NLPClient client;
    JobListener(NLPClient client,GearmanJobReturn job){
        this.job=job;
        this.client=client;
    }
    @Override
    public void run() {
      handler();
    }
    private  void handler(){
         while(!job.isEOF()) {
               try {
                   GearmanJobEvent event = job.poll();
                   if(event.getEventType().equals(GearmanJobEventType.GEARMAN_JOB_SUCCESS)){
                       //currently not handling exceptions from the worker; 
                       client.workDone();
                       
                       String result = new String(event.getData());
                       Logger.getLogger(NLPClient.class.getName()).log(Level.INFO,result);
                   }
               } catch (InterruptedException ex) {
                   Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
               }
           }    
    }
    public static void addToListener(NLPClient client,GearmanJobReturn job){
        new Thread(new JobListener(client,job)).start();
    }
    
}