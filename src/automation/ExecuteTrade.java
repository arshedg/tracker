/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automation;

import automation.selenium.SquareOFF;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsh
 */
public class ExecuteTrade {
    private static boolean newCall=false;
    
   static String fileName="C:\\chrstn\\executedtrades.data";
     private static  List<String> call=new ArrayList<>();
     static List<String> executedTrades;
     static{
      executedTrades = Arrays.asList(read());
     }
    static String[] read() {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return new String[]{};

            }
            Scanner scan = new Scanner(file);
            String data = null;
        
            
            if (scan.hasNextLine()) {
                data = scan.nextLine();
                return data.split(",");
            }
       
            

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);

        }
        return new String[]{};
    }

     static synchronized  void save(String scripName) {
        try {
            File file = new File(fileName);
            PrintWriter writer = null;
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new PrintWriter(new FileWriter(fileName, true));
            writer.append(scripName + ",");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(ExecuteTrade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getCall(){
        executedTrades = Arrays.asList(read());
        String callString;
        while(true){
            if(call.isEmpty())
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ExecuteTrade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                callString=call.get(0);
                if(executedTrades.contains(callString)){
                    call.remove(0);
                }
                else{
                    break;
                }
            }
        }        
        call.remove(0);
        return new String(callString);
    }
    public static synchronized void executeSellOrder(String scrip,int quantity,float price,float target,float stopLoss){
       BigDecimal bd = new BigDecimal(target);
        BigDecimal TARGET = bd.setScale(2, RoundingMode.UP);
       call.add("Sell Nse "+scrip+" "+quantity+" "+roundOFF(price)+" "+TARGET.doubleValue()+" "+roundOFF(stopLoss));
       newCall=true;
    }
     public static synchronized void executeBuyOrder(String scrip,int quantity,float price,float target,float stopLoss){
       BigDecimal bd = new BigDecimal(target);
        BigDecimal TARGET = bd.setScale(2, RoundingMode.UP);
       call.add("Buy Nse "+scrip+" "+quantity+" "+roundOFF(price)+" "+TARGET.doubleValue()+" "+roundOFF(stopLoss));
       newCall=true;
    }
    private static String roundOFF(float value){
        if(value==0){
            return "0";
        }
       
        String amt=""+value;
        if(amt.indexOf(".")<0){
            return amt;
        }
        String currency[]=amt.split("\\.");
        String ruppe=currency[0];
        String paise=currency[1];
        if(paise.length()>2){
            paise=paise.substring(0, 2);
        }
        else{
            return ruppe+"."+paise;
        }
        int lastDigit=Integer.parseInt(""+paise.charAt(1));
        if(lastDigit>5){
            paise=paise.charAt(0)+"5";
        }
        else{
            paise=paise.charAt(0)+"0";
        }
        return ruppe+"."+paise;
//        BigDecimal bd = new BigDecimal(value);
//        BigDecimal TARGET = bd.setScale(2, RoundingMode.UP);
//        float remainder = (float) (TARGET.floatValue() % 0.05);
//        TARGET.add(new BigDecimal(remainder));
//        return ""+TARGET.doubleValue();
    }
}
