/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marshed
 */
public class EquityProcessor {

    protected String name;
    protected List<String> priceDetails;
    protected int lastInstanceTime = 0;
    protected int totalInstance;
    protected boolean isDebugging=Configuration.debugging;
    public EquityProcessor(){
        
    }
    boolean negligiblyEqualorGreater(float p1, float p2) {
        //percentage difference between the instance
        if (p1 < p2) {
            return true;
        }
        float negligibleDiff = (p2 * Configuration.negligiblePercentage) / 100;
        if (p2 + negligibleDiff >= p1) {
            return true;
        } else {
            return false;
        }
    }

    boolean negligiblyEqualorLesser(float p1, float p2) {
        //percentage difference between the instance
        if (p1 > p2) {
            return true;
        }
        float negligibleDiff = (p2 * Configuration.negligiblePercentage) / 100;
        if (p2 - negligibleDiff < p1) {
            return true;
        } else {
            return false;
        }
    }
    void sleepInMins(int time){
        try {
            Thread.sleep(1000*60*time);
        } catch (InterruptedException ex) {
            Logger.getLogger(EquityProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    protected float bottomTopPercentageDifference(float p1, float p2) {
        float percentage = (p1 / p2) * 100;
        return 100 - percentage;

    }

    boolean isUpdated() {
        String lastPriceDetails = priceDetails.get(priceDetails.size() - 1);
        int updatedTime = Integer.parseInt(lastPriceDetails.split(":")[0]);
        if (updatedTime > lastInstanceTime) {
            return true;
        } else {
            return false;
        }
    }

    protected float getLastPrice() {
        return getPriceForInstance(priceDetails.size() - 1);
    }
    int intradayHighInstance;
    public int getIntradayHighInstance(){
        if(intradayHighInstance==0){
            getIntradayHigh();
        }
        return intradayHighInstance;
    }
    protected float getIntradayHigh(){
        int lastTicker = priceDetails.size();
        float highest=-1;
        for(int i=30;i<lastTicker-1;i++){
            float instancePrice = getPriceForInstance(i);
            if(instancePrice>highest){
                highest=instancePrice;
                intradayHighInstance=i;
            }
        }
        return highest;
    }
      protected float getIntradayHigh(int lastTicker){
       // int lastTicker = priceDetails.size();
        float highest=-1;
        for(int i=30;i<lastTicker-1;i++){
            float instancePrice = getPriceForInstance(i);
            if(instancePrice>highest){
                highest=instancePrice;
                intradayHighInstance=i;
            }
        }
        return highest;
    }
    int intradayLowInstance;
    protected int getIntradayLowInstance(){
        if(intradayLowInstance==0){
            getIntradayLow();
        }
        return intradayLowInstance;
    }
    protected float getIntradayLow(){
        int lastTicker = priceDetails.size();
        float lowest=Float.MAX_VALUE;
        for(int i=30;i<lastTicker-1;i++){
            float instancePrice = getPriceForInstance(i);
            if(instancePrice<lowest){
                lowest=instancePrice;
                intradayLowInstance=i;
            }
        }
        return lowest;
    }
     protected float getIntradayLow(int lastTicker){
        float lowest=Float.MAX_VALUE;
        for(int i=30;i<lastTicker-1;i++){
            float instancePrice = getPriceForInstance(i);
            if(instancePrice<lowest){
                lowest=instancePrice;
                intradayLowInstance=i;
            }
        }
        return lowest;
    }
    int getLastInstance() {
        int lastInstance = priceDetails.size() - 1;
        if (lastInstance < 0) {
            return 0;
        }
        String value = priceDetails.get(lastInstance);
        return Integer.parseInt(value.split(":")[0]);
    }
    
   protected  float getPriceForInstance(int instance) {
        try{
        String lastPriceDetails = priceDetails.get(instance);
        //System.err.println("last price "+lastPriceDetails+" ");
        lastPriceDetails=lastPriceDetails.replaceAll(",", "");
        float lastPrice = Float.parseFloat(lastPriceDetails.split(":")[1]);
        return lastPrice;
        }
        catch(Exception e){
            System.out.println(e.getLocalizedMessage());
            return -100;
        }
    }
    protected float getOpenPrice(){
        return getPriceForInstance(0);
    }
    boolean negligiblyEqual(float p1, float p2) {
        //percentage difference between the instance
        if (p2 > p1) {
            float t = p1;
            p1 = p2;
            p2 = t;
        }
        float negligibleDiff = (p2 * Configuration.negligiblePercentage) / 100;
        if (p2 + negligibleDiff > p1) {
            return true;
        } else {
            return false;
        }
    }
    
    int addTime(int t1,int t2){
        int t1sec=t1%100;
        int t2sec=t2%100;
        int secs=t1sec+t2sec;
        int extra=0;
        if(secs>60){
           extra=1;
           secs=secs-60;
        }
        int t1min=t1/100;
        int t2min=t2/100;
        int mins=(t1+t2+extra)*100;
        return mins+secs;
    }
}
