/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analyser;

import static analyser.ForwardRecover.profitCount;
import automation.ExecuteTrade;
import automation.selenium.SquareOFF;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import rhino.Configuration;
import rhino.EquityProcessor;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class BackwardFall extends EquityProcessor implements Runnable{
  public static float amount=Configuration.purchaseAmount;
     public  static   double capital=0;
    public static int trades=0;
    static int profitCount=0;
    static int lossCount=0;
    static float targetPercentage=(float) 1;
    static float stopLossPercentage=(float) .7;
    public static int totalCalls;
    public static  List<String> underProcessing = new ArrayList<>();
  private synchronized  void profit(boolean isProfit){
      synchronized(BackwardFall.class){  
      trades++;
        if(isProfit){
            capital+=amount+(amount*targetPercentage/100);
            
            System.out.println(" backward profit count "+(++profitCount));
        }
        else{
            capital+=amount-(amount*stopLossPercentage/100);
          System.err.println("backwar loss count "+(++lossCount));  
        }
      }
    }

    public static boolean isMatching(String name,float change){
        
        if(underProcessing.contains(name)){
            return false;
        }

        return true;
    }
  
    private final float lastPrice;
    RediffParser parser;
    public BackwardFall(String name,float lastPrice,rhino.Process process){
        this.name = name;
        this.lastPrice = lastPrice;
       if (!isDebugging) {
            parser = loader.RealTimeFeeder.getParser(name);
  
       
        } else {
            parser = process.getDebuggingParser();
       
        }
      try {
         parser = (RediffParser)parser.clone();
      } catch (Exception ex) {
          Logger.getLogger(BackwardFall.class.getName()).log(Level.SEVERE, null, ex);
      }
     
       
    }
//    @Override
//    protected float getOpenPrice(){
//        if(Configuration.readFromHistory){
//            return super.getOpenPrice();
//        }
//        else{
//           return parser.getOpenPrice();
//        }
//    }
    private float dayLow;
    public void process(){
        
        if(lastPrice<100)
            return;
        if((!Configuration.debugging)&&Arrays.asList(read()).contains(name+":incomplete")){
            return;
        }
        float dayLow = getIntradayLow();
        int timeAtDayLow = getIntradayLowInstance();
        float highPrice=getHighestPriceAfterLow(timeAtDayLow);
        float fluctuation=bottomTopPercentageDifference(dayLow, highPrice);
        float openPrice = getOpenPrice();
        float change = bottomTopPercentageDifference(dayLow, getOpenPrice());
      
        if(change<.5){//&&fluctuation>change/3){
            underProcessing.add(name);
            this.dayLow=dayLow;
            totalCalls++;
            dayLow+=dayLow*(.1/100);
            float targetPrice=dayLow-(dayLow*(targetPercentage/100));
            float min=(float) (dayLow-((0.1/100)*dayLow));
            ExecuteTrade.executeSellOrder(name, (int) (amount/dayLow),min,0,dayLow);
         //   System.out.println("Sell "+ name + "below "+ dayLow);
            //ExecuteTrade.executeBuyOrder(name, (int) (amount/dayLow),targetPrice,0,0);
           // 
           waitForHigher();
         //new Thread(this).start();
         save(name);
            //waitForHigher(dayLow);
        }

        
    }
    private float getHighestPriceAfterLow(int highInstance){
        float least=getPriceForInstance(highInstance++);
        float value=0;
        while(highInstance<parser.getCursor()){
             value=getPriceForInstance(highInstance++);
            if(value>least){
                least=value;
            }
        }
        return value;
    }
    private void waitForHigher() {
        int cursor=parser.getCursor();
      dayLow+=dayLow*(.1/100);
        boolean hurdle1=false;
        
        while(!parser.isMarketClosed()){
            float lastPrice = getLastPrice();
           
            if(lastPrice<=dayLow){
                if(true){
                    analyzeTransaction(dayLow);
                    break;
                }
 
             
            }
            else{
                hurdle1=false;
            }
        }
        parser.setCursor(cursor);
       // priceDetails = priceDetails.subList(0, cursor);
    }
    private void cutdownLoss(float soldPrice){
        
    }
    private void analyzeTransaction(float sellPrice){
        System.out.println("Sold "+name+" at price "+sellPrice+" at time "+ getCurrentTime());
        float targetPrice=sellPrice-(sellPrice*(targetPercentage/100));
        float stopLoss = sellPrice+(sellPrice*(stopLossPercentage/100));
        int ticker=0;
        while(!parser.isMarketClosed()){
           float lastPrice=getLastPrice();
           if(lastPrice<targetPrice){
               System.out.println("target acheived for "+name+" price:"+lastPrice);
               System.out.println("time taken "+ticker);
               profit(true);
               return;
           }
           else if(lastPrice>stopLoss){
               System.err.println("Stop loss triggered for "+name+" price:"+lastPrice);
                System.err.println("time taken "+ticker);
               profit(false);
               return;
               
           }
           ticker++;
        }
//        System.out.println("market closed before acheiving target of "+name);
//         System.out.println("time taken "+ticker);
//        float acheived = bottomTopPercentageDifference(getLastPrice(), sellPrice);
//        capital+=amount+(amount*acheived/100);
//        trades++;
//        if(acheived<0){
//            System.err.println
//                    ("partial profit percentage "+acheived);
//        }else{
//            System.out.println("partial profit percentage "+acheived); 
//        }
    }
    String time="NAN";
    public float getLastPrice() {
        String value = parser.getCurrentValue();
        time = value.split(":")[0];
        return Float.parseFloat(value.split(":")[1]);
    }
    private String getCurrentTime(){
        return time;
    }    

    @Override
    public void run() {
        waitForHigher();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        void save(String scripName) {
        try {
            File file = new File("C:\\chrstn\\backward.data");
            PrintWriter writer = null;
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new PrintWriter(new FileWriter("C:\\chrstn\\backward.data", true));
            writer.append(scripName + ":incomplete" + ",");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String[] read() {
        try {
            File file = new File("C:\\chrstn\\backward.data");
            if (!file.exists()) {
                return new String[]{};

            }
            Scanner scan = new Scanner(file);
            String data = null;
            String datas[] = null;

            if (scan.hasNextLine()) {
                data = scan.next();
                return data.split(",");
            }
            for (String value : datas) {
            }


        } catch (FileNotFoundException ex) {
            System.err.println("file not found ");
            // Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);

        }
        return new String[]{};
    }
}
