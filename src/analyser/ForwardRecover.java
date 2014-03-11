/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analyser;

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
import rhino.SellHandler;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class ForwardRecover extends EquityProcessor implements Runnable {

    public static float amount = Configuration.purchaseAmount;
    public static float capital = 0;
    public static int trades = 0;
    static int profitCount = 0;
    static int lossCount = 0;
    static float targetPercentage = (float) 1;
    static float stopLossPercentage = (float) .7;
    public static int totalCalls;
    public static List<String> underProcessing = new ArrayList<>();

    private synchronized void profit(boolean isProfit) {
        synchronized (ForwardRecover.class) {
            trades++;
            if (isProfit) {
                capital += amount + (amount * targetPercentage / 100);

                System.out.println("profit count " + (++profitCount));
            } else {
                capital += amount - (amount * stopLossPercentage / 100);
                System.err.println("loss count " + (++lossCount));
            }
        }
    }

    public static boolean isMatching(String name, float change) {
        if (underProcessing.contains(name)) {
            return false;
        }
        if (true) {
            return true;
        }
        return false;
    }
    private final float lastPrice;
    RediffParser parser;

    public ForwardRecover(String name, float lastPrice, rhino.Process process) {
        this.name = name;
        this.lastPrice = lastPrice;
        if (!isDebugging) {
            parser = loader.RealTimeFeeder.getParser(name);


        } else {
            parser = process.getDebuggingParser();

        }
        try {
            parser = (RediffParser) parser.clone();
        } catch (Exception ex) {
            Logger.getLogger(ForwardRecover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void process() {
        if ((!Configuration.debugging)&&Arrays.asList(read()).contains(name+":incomplete")) {
            return;
        }
        float dayHigh = getIntradayHigh();
        int highInstance = getIntradayHighInstance();
        float leastAfterHigh = getLeastPriceAfterHigh(highInstance);
        float fluctuation = bottomTopPercentageDifference(leastAfterHigh, dayHigh);

        float openPrice = getOpenPrice();
        float change = bottomTopPercentageDifference(openPrice, dayHigh);
        if (change<0.5) {
            //if(fluctuation>change/3){
            underProcessing.add(name);
            this.dayHigh = dayHigh;
            totalCalls++;
            float targetPrice = dayHigh + (dayHigh * (targetPercentage / 100));
            float max = (float) (dayHigh + ((0.1 / 100) * dayHigh));
             ExecuteTrade.executeBuyOrder(name, (int) (amount/dayHigh),max,0,dayHigh);
//             ExecuteTrade.executeSellOrder(name, (int) (amount/dayHigh),targetPrice,0,0);
//            
            // //
            //System.out.println("Buy " + name + "above " + dayHigh);
         waitForHigher();
            //  new Thread(this).start();
            save(name);
            
        }

    }
    private float dayHigh;

    private float getLeastPriceAfterHigh(int highInstance) {
        float least = getPriceForInstance(highInstance++);
        float value = 0;
        while (highInstance < parser.getCursor()) {
            value = getPriceForInstance(highInstance++);
            if (value < least) {
                least = value;
            }
        }
        return value;
    }

    private void waitForHigher() {
        int cursor=parser.getCursor();
        boolean hurdle1 = false;
        while (!parser.isMarketClosed()) 
        {
            // if(profitCount<3&&lossCount>5) return;
            float lastPrice = getLastPrice();

            if (lastPrice >= dayHigh) {
                if (true) {
                    analyzeTransaction(dayHigh);
                    break;
                }




            } else {
                hurdle1 = false;
            }
        }
        parser.setCursor(cursor);
         //priceDetails = priceDetails.subList(0, cursor);
    }

    private void analyzeTransaction(float boughtPrice) {
        System.out.println("bought " + name + " at price " + boughtPrice + " at time " + getCurrentTime());
        float targetPrice = (boughtPrice * (targetPercentage / 100)) + boughtPrice;
        float stopLoss = boughtPrice - (boughtPrice * (stopLossPercentage / 100));

        while (!parser.isMarketClosed()) {
            float lastPrice = getLastPrice();
            if (lastPrice >= targetPrice) {
                System.out.println("target acheived for " + name + " price:" + lastPrice);
                profit(true);
                return;
            } else if (lastPrice <= stopLoss) {
                System.err.println("Stop loss triggered for " + name + " price:" + lastPrice);
                profit(false);
                return;

            }
        }
//        System.out.println("market closed before acheiving target of "+name);
//        float acheived = bottomTopPercentageDifference(boughtPrice, getLastPrice());
//        capital+=amount+(amount*acheived/100);
//        trades++;
//        if(acheived>=0){
//            System.out.println("partial profit percentage "+acheived);
//        }else{
//            System.err.println("partial profit percentage "+acheived); 
//        }
    }
    String time = "NAN";

    public float getLastPrice() {
        String value = parser.getCurrentValue();
        time = value.split(":")[0];
        return Float.parseFloat(value.split(":")[1]);
    }

    private String getCurrentTime() {
        return time;
    }

    @Override
    public void run() {
        waitForHigher(); //To change body of generated methods, choose Tools | Templates.
    }

    void save(String scripName) {
        try {
            File file = new File("C:\\chrstn\\forward.data");
            PrintWriter writer = null;
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new PrintWriter(new FileWriter("C:\\chrstn\\forward.data", true));
            writer.append(scripName + ":incomplete" + ",");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String[] read() {
        try {
            File file = new File("C:\\chrstn\\forward.data");
            if (!file.exists()) {
                return new String[]{};

            }
            Scanner scan = new Scanner(file);
            String data = null;
         

            if (scan.hasNextLine()) {
                data = scan.next();
                return data.split(",");
            }
           


        } catch (FileNotFoundException ex) {
            System.err.println("file not found ");
            // Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);

        }
        return new String[]{};
    }
}

