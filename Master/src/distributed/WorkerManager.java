/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package distributed;



import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import worker.Work;
import worker.Worker;

/**
 *
 * @author marshed
 */
public class WorkerManager  {
    AtomicInteger integer = new AtomicInteger();
    BlockingQueue<Work> tasks = new ArrayBlockingQueue<>(20); 
    //at most 2o articles will be loaded into the memmory
    Set<String> completed = new HashSet<>();
    private static final WorkerManager workerManager = new WorkerManager();
    
    private WorkerManager(){       
    }
    public void addNewJob(Work work){
        try {
            tasks.put(work);
        } catch (InterruptedException ex) {
            //try again, guess stack overflow wont happen
            addNewJob(work);
        }
    }
    public static WorkerManager getManager(){
        return workerManager;
    }
    public void submitWork(Worker worker,Work work){
        String workId= work.getJobid();
        int workerId = worker.getId();
        String workerAddress = worker.getSocket().getInetAddress().toString();
        if(work.getException()!=null){
            String errorText = "Job with id "+workId+" failed "+" worker address "+workerAddress+" worker id "+workerId;
            Logger.getLogger(WorkerManager.class.getName()).log(Level.SEVERE, errorText ,work.getException());
        }
        else{
            String logText="Article ID:"+workId+",time taken:"+work.getProcessingTime()+" worker address "+workerAddress+ " worker id "+workerId;
            int nerCount = work.getResult().size();
            String result = work.getResult().toString();
            String resultString = "  Number of NERs = "+nerCount+" NER :"+result;
            Logger.getGlobal().info(logText+resultString);
        }
        completed.add(work.getJobid());
        try {
            Work newWork = tasks.take();
            worker.startNewJob(newWork);
        } catch (InterruptedException ex) {
            
        }
    }
    public void submitException(Worker worker,Exception ex){
        //probably client got disconnected or timeout.
        System.err.println("Exception from worker : "+worker.getId()+" address:"+worker.getSocket().getInetAddress());
        if(!completed.contains(worker.getJob().getJobid())){
            try {
                /*
                Incase timeout is raised from worker1 and hence the job is re-assigned to worker2. But the reason for time-out
                is because the job is lengthier than the expected time. Hence after some time worker1 will submit  the completed
                work, but again timeout might be raised from worker2. This work  need not to be reprocessed
                */
                tasks.put(worker.getJob());
            } catch (InterruptedException ex1) {
                //no need to handle
            }
        }
    }
    public void submitResignation(Worker work){
        
    }
    public void addNewWorker(Worker worker){
        try {
            worker.startNewJob(tasks.take());
        } catch (InterruptedException ex) {
            //try again in case of interuption
            addNewWorker(worker);
        }
    }
}
