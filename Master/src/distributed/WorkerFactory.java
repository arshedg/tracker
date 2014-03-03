/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Worker;


/**
 *
 * @author marshed
 * 
 * WorkerFacotry binds the Workers with WorkerManager
 * 
 * new Workers needs to get registered to the WorkerFactory
 * 
 * 
 */
public class WorkerFactory  implements Runnable{
    ServerSocket server;
    Set<Socket> sockets = new HashSet<>();
    private static final Integer portNo = 5678;
    public WorkerFactory(){
        try {  
            server = new ServerSocket(portNo);
            new JobProducer();
        } catch (IOException ex) {
           Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
    public void run(){
      while(true)  
        try {
            //each worker connects to the factory via socket
            Socket socket = server.accept();
            sockets.add(socket);
            //for each connection a worker is created at the server side
            WorkerManager.getManager().addNewWorker(new Worker(socket));
        } catch (IOException ex) {
            Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String arg[]){
        new Thread(new WorkerFactory()).start();
    }
            
}
