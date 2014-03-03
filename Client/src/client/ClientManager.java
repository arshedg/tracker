/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Work;

/**
 *
 * @author marshed
 */
public class ClientManager {
    private static final Integer MAX_PARELLEL_THREADS=1;
    //maximum workers a system can hold. Deciede based on the no of cores and memmory
    private static boolean connectionErrorDetected=false;
    public static void main(String arg[]){
        for(int i=0;i<MAX_PARELLEL_THREADS;i++){
            try {
                createNewWorker();
            } catch (IOException ex) {
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
                //some issue with the connection
                break;
            }
        }
    }
    public static void reportDeath(){
        if(connectionErrorDetected){
            //server is down;
            return;
        }
        try {
          
            createNewWorker();
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            connectionErrorDetected=true;
        }
    }
    private static void createNewWorker() throws IOException{
        Worker worker = new Worker();
        new Thread(worker).start();
    }
}
class Worker implements Runnable{
    private static final String ADDRESS="localhost";
    private static final Integer PORT_NO=5678;
    private final Socket socket;
    public Worker() throws IOException{
            socket= new Socket(ADDRESS, PORT_NO);
    }
    @Override
    public void run() {
        try {
            work();
        } catch (IOException|ClassNotFoundException|InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            //crash in the worker
            ClientManager.reportDeath();
        }
    }
    private void work() throws IOException, ClassNotFoundException, InterruptedException{
        while(true){
            Work work = getWork();
            DataProcessor processor = new DataProcessor(work.getData());
            System.out.println(work.getData());
            try {
                long startTime = System.currentTimeMillis();
                List<String> result = processor.doJob();
                long finishTime = System.currentTimeMillis();
                long timeTaken = finishTime-startTime;
                work.setResult(result);
                work.setProcessingTime(timeTaken);
            } catch (Exception ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                work.setException(ex);
            } 
            sendData(work);
        }
    }
    private  Work getWork() throws IOException, ClassNotFoundException{
        InputStream is = socket.getInputStream(); 
        ObjectInputStream ois = new ObjectInputStream(is);  
        Work completedWork = (Work)ois.readObject(); 
        return completedWork;
    }
    private void sendData(Work work) throws IOException{
         OutputStream os = socket.getOutputStream(); 
         ObjectOutputStream oos = new ObjectOutputStream(os);  
         work.removeLoad();
         oos.writeObject(work);
         oos.flush();
    }
}
