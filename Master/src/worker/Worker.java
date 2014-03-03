/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package worker;

import distributed.WorkerManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marshed
 */
public class Worker  implements Runnable{
   private final static AtomicInteger wokerIdGenerator = new AtomicInteger();
   private final Socket socket;
   private TimeOutHandler timeOut;
   private Work work;
   private final Integer id;
   private ObjectInputStream objectIn;
   InputStream is;
    OutputStream os ;
    private ObjectOutputStream objectOut;
   
   public Worker(Socket socket){
       this.socket = socket;
       id = wokerIdGenerator.getAndIncrement();
      
       try {
           is = socket.getInputStream();
           os = socket.getOutputStream(); 
          
       } catch (IOException ex) {
           Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
       }
       
   }
    public void startNewJob(Work work) {
        this.work = work;
        new Thread(this).start();
    }
    public boolean isAlive(){
        return getSocket().isConnected();
    }
    public void timeOut(){
        WorkerManager.getManager().submitException(this,null );
    }

    public Socket getSocket() {
        return socket;
    }

    public Work getJob() {
        return work;
    }

    @Override
    public void run() {
      try {
           
           sendWork();
           timeOut = new TimeOutHandler(this,10000);//hard coded : todo :)
           timeOut.start();
           Work doneWork = getCompletedWork();
           timeOut.cancel();
           WorkerManager.getManager().submitWork(this,doneWork);
       } catch (IOException|ClassNotFoundException ex) {
           WorkerManager.getManager().submitException(this,ex);
       }
        
    }
    private Work getCompletedWork() throws IOException, ClassNotFoundException{  
        objectIn = new ObjectInputStream(is); 
        Work completedWork = (Work)objectIn.readObject(); 
        return completedWork;
    }
     public  void sendWork() throws IOException{
         objectOut = new ObjectOutputStream(os);
         objectOut.writeObject(getJob());
         objectOut.flush();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

}
class TimeOutHandler extends TimerTask{
    Worker worker;
    private TimeOutListener timeOut;
    private int time;
    public TimeOutHandler(Worker worker,int time){
        this.worker = worker;
        this.time = time;
    }
    public void start(){
        timeOut = new TimeOutListener(this,time);
    }
    @Override
    public void run() {
        timeOut.cancel();
        worker.timeOut();
    }
    
    
}