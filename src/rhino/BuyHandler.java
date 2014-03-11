/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rhino.SellHandler.lossCount;
import static rhino.SellHandler.noWhere;
import static rhino.SellHandler.profitCount;
import static rhino.SellHandler.totalSellProfit;
import test.NSEHelper;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class BuyHandler extends EquityProcessor implements Runnable {

    private boolean swicthOFF = false;
    float purchasePrice;
 
    float triggeredPrice;
    int sizeOfListAtPurchase = 0;
    RediffParser parser;
    int cursor;
    int lastUpdate;
    Process process;
    private float targetPercentage = (float) 1;
    private float stopLoss = (float) .8;

    public BuyHandler(String name, float lastPrice, Process p) {
        this.name = name;
        this.purchasePrice = lastPrice;
        this.triggeredPrice = lastPrice;
        this.process = p;
        // new Thread(this).start();


    }

   
    @Override
    public void run() {

        process();
    }

    public void process() {
        if (!isDebugging) {
            parser = loader.RealTimeFeeder.getParser(name);
            cursor = priceDetails.size();
            lastUpdate=cursor;
        } else {
            parser = process.getDebuggingParser();
            cursor = parser.getCursor();
        }
       // if(isBadMood()) return;

        float bestBuy =isReadyToBuy(true, getLastPrice());
        this.purchasePrice = bestBuy;
        process.resumeProcessing();
        float change = bottomTopPercentageDifference(this.purchasePrice, bestBuy);
        if (bestBuy == -1) {
            return;
        }
        this.purchasePrice = getLastPrice();
        System.out.println("total buy calls :" + (++Analyser.totalBuyCall));



        sizeOfListAtPurchase = priceDetails.size();
       int shares = (int) (Configuration.purchaseAmount / purchasePrice);
        System.out.println("buy " + shares + " shares of " + name + " at price " + purchasePrice + "instance " + priceDetails.get(sizeOfListAtPurchase - 1));
        System.out.println("sell at:" + (purchasePrice + (purchasePrice * (targetPercentage)) / (float) 100));
        System.out.println("stop loss   at:" + (purchasePrice - (purchasePrice * (float) stopLoss) / (float) 100));
        
////        automation.ExecuteTrade.executeSellOrder(name, shares,purchasePrice, (purchasePrice - (purchasePrice * (float) stopLoss) / (float) 100), (purchasePrice + (purchasePrice * (targetPercentage)) / (float) 100));
        if (isDebugging) {
            debugGainOrLoss((purchasePrice + (purchasePrice * (float) targetPercentage) / (float) 100), (purchasePrice - (purchasePrice * (float) stopLoss) / (float) 100));
        }
        while(!swicthOFF){
            gainOrLoss();
        }
    }

    private void gainOrLoss() {
        float success = (purchasePrice * (float) .5) / (float) 100;
        success = purchasePrice + (float) success;
        //  System.out.println("succes " +name+ " at price "+success);
        float failure = (purchasePrice * (float) .5) / (float) 100;
        failure = purchasePrice - failure;
        //System.out.println("failure " +name+ " at price "+failure);
        int currentSize = priceDetails.size();
        for (int i = sizeOfListAtPurchase; i < currentSize; i++) {
            if (success <= getPriceForInstance(i)) {
                System.out.println("sold " + name + "  at price " + getPriceForInstance(i) + "\n profit count" + (++profitCount) + "\n instance:" + getPriceForInstance(i));
                swicthOFF = true;
                System.out.println("remainin buy calls :" + (--Analyser.totalBuyCall));
                return;
            } else if (failure >= getPriceForInstance(i)) {
                System.err.println("sold " + name + "  at price " + getPriceForInstance(i) + "\n loss count" + (++lossCount) + "\n instance:" + getPriceForInstance(i));
                System.out.println("remainin buy calls :" + (--Analyser.totalBuyCall));
                swicthOFF = true;
                return;
            }
        }
    }

    private boolean isTimeOut() {
        if (isDebugging) {
            if (parser.getCursor() - cursor > 10) {
                return true;
            }
        } else {
            if (priceDetails.size() - cursor > 10) {
                return true;
            }
        }
        return false;
    }

     private void debugGainOrLoss(float target, float stopLoss) {
        int cursor = parser.getCursor(), happen;
        ArrayList<String> datas = parser.getCursorData();
        float max = Float.MIN_VALUE, min = Float.MAX_VALUE;
        int maxCursor = 0, minCursor = 0, count;
        for (int i = cursor; i < datas.size(); i++) {
            String data = datas.get(i);
            float value = Float.parseFloat(data.split(":")[1]);
            if (value > max) {
                max = value;
                maxCursor = parser.getCursor();
            }
            if (value < min) {
                min = value;
                minCursor = parser.getCursor();
            }
            if (value >= target) {
                System.out.println("sold " + name + "  at price " + value + "\n profit count" + (++profitCount));
                System.out.println("time taken " + (i - cursor));
                swicthOFF = true;
                return;

            } else if (value <= stopLoss) {
                System.err.println("sold " + name + "  at price " + value + "\n loss count" + (++lossCount));
                System.err.println("time taken " + (i - cursor));
                swicthOFF = true;
                return;
            }
        }

        System.out.println("no where calls :" + (++noWhere));
        System.out.println(name + " didnt hit any target");
        System.out.println("max value reached :" + max + " at time " + addTime(930, maxCursor));
        System.out.println("min value reached :" + min + " at time " + addTime(930, minCursor));
        swicthOFF = true;

    }
    private boolean isBadMood(){
        int pointer;
        if(isDebugging){
            pointer=parser.getCursor();
        }
        else{
          pointer=priceDetails.size()-1;  
        }
        float upperhalf=0;
        for(float i=pointer;i>=pointer-2&&i>=0;i=(float) (i-.5)){
            upperhalf+=NSEHelper.getNseValue(i);
        }
        upperhalf=upperhalf/7;
        float lowerHalf=0;
        for(float i=pointer-2;i>=pointer-4&&i>=0;i=(float) (i-.5)){
            lowerHalf+=NSEHelper.getNseValue(i);
        }
        lowerHalf=lowerHalf/7;
        
        float diff=bottomTopPercentageDifference(upperhalf, lowerHalf);
        if(diff>0.02){
            
           return true;
        }
        return false;
    }
    private float isReadyToBuy(boolean recheck, float triggeredPrice) {
        //price going down again
//        if (isTimeOut()) {
//             return -1;
//        }
        boolean patternChangedDetected = false;
        float lastPrice = triggeredPrice;
        float currentPrice=getLastPrice();
        float max=lastPrice;
        float min =lastPrice;
        int count=0;
        int requiredMins=60;
        while(negligiblyEqualorGreater(max, lastPrice)){
            if(count==350){
                return -1;
            }
            if(lastPrice>max){
                max=lastPrice;
            }
            lastPrice=getLastPrice();
            count++;
        }
//        if(count>requiredMins-1){
//            System.err.println("patern found " + name+"  "+getLastInstance()+" bought at:"+lastPrice);
//            
//        }
        return lastPrice;
       
//        // recheck = sampleAssesment(lastPrice);
//        if (recheck) {
//            try {
//
//                if (Configuration.debugging) {
//                    Thread.sleep(1000);
//                } else {
//                    Thread.sleep(60 * 1000);
//                }
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SellHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            lastPrice = isReadyToBuy(patternChangedDetected, lastPrice);
//        }
//        return lastPrice;
    }

    private boolean checkForFlatPrice() {
        //its the condition where the price is unchanged for x mins
        int lastInstance = priceDetails.size() - 1;
        if (getLastInstance() > 1520) {
            return true;
        }
        int sampleCount = Configuration.maxFlatTime;
        int count = (lastInstance + 1) - sampleCount;
        float prices[] = new float[sampleCount];
        for (int i = 0; count <= lastInstance; count++) {
            prices[i++] = getPriceForInstance(count);
        }
        float priceChange = bottomTopPercentageDifference(prices[0], prices[sampleCount - 1]);
        if (priceChange < Configuration.idealHoldPercentage) {
            return false;
        }
        if (priceChange > 0 && priceChange < Configuration.maxAllowedSellLoss) {
            float currentProfit = -1 * bottomTopPercentageDifference(purchasePrice, getLastPrice());
            if (currentProfit > .5) {
                return true;//sell
            } else if (currentProfit < Configuration.maxAllowedSellLoss) {
                return true;
            }
        }
        int totalEquals = 0;
        for (int i = 0; i < Configuration.maxFlatTime - 1; i++) {

            if (negligiblyEqual(prices[i], prices[i + 1])) {
                totalEquals++;
            }
            if (totalEquals >= Configuration.maxFlatTime / 2) {
                return true;
            }

        }
        return false;

    }

    private void triggerBuy() {
        System.out.println("square of" + name + " bought price :" + purchasePrice + " sold price:" + getLastPrice() + "profit :" + bottomTopPercentageDifference(purchasePrice, getLastPrice()));
        totalSellProfit += bottomTopPercentageDifference(purchasePrice, getLastPrice());
        System.out.println("current profit by following special algo :" + totalSellProfit);
        swicthOFF = true;
    }

    public float getLastPrice() {
        if(isDebugging){
            int size=priceDetails.size();
            while(size<=lastUpdate){
                size=priceDetails.size();
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SellHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lastUpdate++;
        }
        String value = parser.getCurrentValue();
        return Float.parseFloat(value.split(":")[1]);
    }

    private boolean sampleAssesment(float lastPrice) {
        ArrayList<Float> priceList = new ArrayList<>();
        long time = Configuration.assesmentTimeInMins * 1000 * 60;
        long count = System.currentTimeMillis();
        int sleepTime = 20000;
        int debuggingCount = 0;
        while (debuggingCount < Configuration.assesmentTimeInMins && time > (System.currentTimeMillis() - count)) {
            try {
                priceList.add(getLastPrice());
                if (isDebugging) {
                    debuggingCount++;
                } else {
                    Thread.sleep(sleepTime);
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        for (Float price : priceList) {
            if (price > lastPrice) {
                return true;
            }
        }
        return false;
    }
}
