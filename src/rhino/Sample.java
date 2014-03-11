/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marshed
 */
public class Sample {
   public static Map<String,ArrayList<String>> stock; 
   
  //initially when the program start
   void init(){
       stock=new HashMap<>();
       //for each stock, it price and the time
             String stockName="";
             float price=0;
             int time=0;
              ArrayList<String> value=new ArrayList<>();
              String data=time+":"+price;// ':' is used as boundary token
              value.add(data);
              stock.put(stockName, value);
             
       
   }
   
   
   //the method is called when you get new values
   void onUpdate(){
       //for each stock and its price. sample
       //for each {
           String stockName="";
           float price=0;
           int time=0;
           ArrayList<String> curentValuSet=stock.get(stockName);
           String data=time+":"+price;// ':' is used as boundary token
           curentValuSet.add(data);          
       //}
   }
}
