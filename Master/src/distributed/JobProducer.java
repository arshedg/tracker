package distributed;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Work;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marshed
 */
public class JobProducer implements Runnable{
    private final static Integer PORT = 27017;
    private final static String HOST = "localhost";
    public  JobProducer(){
        new Thread(this).start();
    }
    private void produce(){
        WorkerManager manager = WorkerManager.getManager();
        try {
            MongoClient mongoClient = new MongoClient( HOST, PORT );
            DB db = mongoClient.getDB("test");
            DBCollection collection = db.getCollection("articles");
            try (DBCursor cursor = collection.find()) {
                while(cursor.hasNext()) {
                    DBObject object = cursor.next();
                    String data=object.get("description").toString();
                    String id=object.get("_id").toString();
                    manager.addNewJob(new Work(id,data));
                }
            }
            
        }   
        catch (UnknownHostException ex) {
            Logger.getLogger(JobProducer.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    @Override
    public void run() {
        produce();
    }
    public static void main(String arg[]){
        new JobProducer().produce();
    }
}
