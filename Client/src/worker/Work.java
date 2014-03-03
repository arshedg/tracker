/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package worker;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marshed
 */
public class Work implements Serializable{
    private static final long serialVersionUID = 1;
    private final String jobid;
    private long processingTime;
    private String data;
    private List<String> result;
    private Date startDate;
    private Date EndDate;
    private int timeTaken;
    private List<String> ners;
    private Exception exception;
    public Work(String jobid,String data){
        this.jobid = jobid;
        this.data = data;
    }

    /**
     * @return the jobid
     */
    public String getJobid() {
        return jobid;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @return the processedData
     */


    /**
     * @return the result
     */
    public List<String> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<String> result) {
        this.result = result;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the EndDate
     */
    public Date getEndDate() {
        return EndDate;
    }

    /**
     * @param EndDate the EndDate to set
     */
    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    /**
     * @return the timeTaken
     */
    public int getTimeTaken() {
        return timeTaken;
    }

    /**
     * @param timeTaken the timeTaken to set
     */
    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    /**
     * @return the ners
     */
    public List<String> getNers() {
        return ners;
    }

    /**
     * @param ners the ners to set
     */
    public void setNers(List<String> ners) {
        this.ners = ners;
    }



    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void removeLoad() {
        this.data=null;
    }

    /**
     * @return the processingTime
     */
    public long getProcessingTime() {
        return processingTime;
    }

    /**
     * @param processingTime the processingTime to set
     */
    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }
}
