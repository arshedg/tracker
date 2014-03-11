package rhino;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class CrashDetector extends EquityProcessor implements Runnable{

    private boolean swicthOFF=false;
    float purchasePrice;
    static float totalSellProfit=0;
    static int profitCount;
    static int lossCount;
    float triggeredPrice;
    int sizeOfListAtPurchase=0;
    RediffParser parser;
    Process process;
    @Override
    public void run() {
      process();   
    }
    public CrashDetector (String name, float lastPrice,Process p) {
       this.name=name;
       this.purchasePrice=lastPrice;
       this.triggeredPrice=lastPrice;
       if(!isDebugging)
            parser=loader.RealTimeFeeder.getParser(name);
       else
           parser=p.getDebuggingParser();
       this.process=p;
       new Thread(this).start();
    }
     private void process() {
        if(parser.getPercentageChange()<-2){
            swicthOFF=true;
            return;
        }
//        if(parser.getPercentageChange()>1){
//            swicthOFF=true;
//            return;
//        }
        isReadyToBuy(true,getLastPrice());
        System.out.println("total sell calls :"+(++Analyser.totalBuyCall));
        process.resumeProcessing();
        this.purchasePrice=getLastPrice();
        sizeOfListAtPurchase=priceDetails.size();
        System.out.println("Sell stocks " +name+ " at price "+purchasePrice+"instance "+priceDetails.get(sizeOfListAtPurchase-1));
        System.out.println("taget it at:"+( (purchasePrice*(float).5)/(float)100));
        System.out.println("stop loss  at:"+(  (purchasePrice*(float).5)/(float)100));
        while(!swicthOFF){
            gainOrLoss();
        }
    }

   private boolean isReadyToBuy(boolean recheck,float triggeredPrice){
        //price going down again
        boolean patternChangedDetected = false;
        while(triggeredPrice<=getLastPrice()){
            try {
                triggeredPrice=getLastPrice();
                if(recheck==false){
                    patternChangedDetected=true;
                    recheck=true;
                }
                if(Configuration.debugging)
                    Thread.sleep(250);
                else
                   Thread.sleep(1000); 
            } catch (InterruptedException ex) {
                Logger.getLogger(SellHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        float lastPrice=getLastPrice();
       recheck = sampleAssesment(lastPrice);
        if(recheck){ 
            try {
                
                if(Configuration.debugging)
                    Thread.sleep(1000);
                else
                   Thread.sleep(50000); 
            } catch (InterruptedException ex) {
                Logger.getLogger(SellHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            isReadyToBuy(patternChangedDetected,lastPrice);
        }
        return true;
    }
    private void gainOrLoss() {
       return;
    }
     public  float getLastPrice(){
        String value=parser.getCurrentValue();
        return Float.parseFloat(value.split(":")[1]);
    } 
    private boolean sampleAssesment(float lastPrice){
        ArrayList<Float> priceList=new ArrayList<>();
        long time=Configuration.assesmentTimeInMins*1000*60*2;
        long count=System.currentTimeMillis();
        int sleepTime=20000;
        while(time>(System.currentTimeMillis()-count)){
            try {
                priceList.add(getLastPrice());
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        for(Float price:priceList){
            if(price>lastPrice){
                return false;
            }
        }
        return true;
    }

}
