/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loader.NSELoader;
import rhino.Configuration;
import test.NSEHelper;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class CarryForward {
    public static void main(String arg[]){
        Configuration.readFromHistory=true;
        int profit=0,loss=0;
        String dates[]={"19_5_2013","18_5_2013","17_5_2013","14_5_2013","13_5_2013","12_5_2013","11_5_2013","10_5_2013","31_4_2013"};
        Configuration.dateOnHistory="13_10_2013";
       List<String> loosers=new ArrayList<>();
        String stocks[]={"RELIANCE"};// new NSELoader(1000).getAllStocks();
//        Map<String,Float> stockChange=new HashMap<>();
//        for(String stock:stocks){
//            RediffParser parser = new RediffParser(stock);
//            ArrayList<String> values=parser.getValues();
//            float openPrice = Float.parseFloat(values.get(0).split(":")[1]);
//            float closePrice = Float.parseFloat(values.get(values.size()-1).split(":")[1]);
//            stockC/hange.put(stock, bottomTopPercentageDifference(openPrice, closePrice));
//            if(bottomTopPercentageDifference(openPrice, closePrice)>-2){
//                loosers.add(stock);
//            }
//        }
        Configuration.dateOnHistory="13_10_2013";
//        List<String> ext=new ArrayList<>();
//        
//        for(String gainer:stocks){
//            RediffParser parser = new RediffParser(gainer);
//            
//            ArrayList<String> values=parser.getValues();
//            if(values==null) continue;
//          
//            float openPrice = Float.parseFloat(values.get(0).split(":")[1]);
//            float closePrice = Float.parseFloat(values.get(values.size()-60).split(":")[1]);
//            float change = bottomTopPercentageDifference(openPrice, closePrice);
//            if(change>4){
//                ext.add(gainer);
//              
//            }
//        }
        for(String stock:stocks){
            RediffParser parser = new RediffParser(stock);
            ArrayList<String> values=parser.getValues();
              float purchase = Float.parseFloat(values.get(values.size()-60).split(":")[1]);
              System.out.println("shorted "+stock+" at price "+purchase+" at time "+values.get(values.size()-60).split(":")[0]);
              for(int i=0;i<values.size();i++){
                float current = Float.parseFloat(values.get(i).split(":")[1]);
                System.out.print("price"+current+","+Configuration.dateOnHistory);
                  float nse=NSEHelper.getNseValue(i);
                  System.out.println("nse:"+nse+",");
               System.out.println("nse:"+nse/current+",");
                if(bottomTopPercentageDifference(purchase, current)>0.8){
//                    System.err.println("loss triggered for "+stock+"\n time:"+values.get(i));
//                    break;
//                }
//                else if(bottomTopPercentageDifference(purchase, current)<-0.8){
//                   System.out.println("profit booked  for "+stock+"\n time:"+values.get(i));
//                   break;
//                }
              }
        }
        Configuration.dateOnHistory="2_6_2013";
//        int autoCount=0;
//         for(String stock:ext){
//            RediffParser parser = new RediffParser(stock);
//            ArrayList<String> values=parser.getValues();
//            if(values==null){
//                System.out.println(stock+ " cant load");
//                continue;
//            }
//            float open = Float.parseFloat(values.get(0).split(":")[1]);
//            boolean deal=false;
//            float purchasePrice=open;
//            float current=0;
//            boolean autoexit=true;
//            boolean buy=false;
//            int time=0;
//            for(String value:values){
//                 current = Float.parseFloat(value.split(":")[1]);
//                 time++;
//                 if(!buy&&bottomTopPercentageDifference(open, current)<-1){
//                     buy=true;
//                     purchasePrice=current;
//                     System.out.println("biught "+stock+"at price"+current);
//                 }
//                 if(!buy&&time>15){
//                     break;
//                 }
//                 if(buy){
//                    if(bottomTopPercentageDifference(current, purchasePrice)<-1){
//                         System.err.println("loss booked from "+stock+"\n loss count"+(++loss));
//                        autoexit=false;
//                        break;
//                    }
//                    else if(bottomTopPercentageDifference(current, purchasePrice)>1){
//                        System.out.println("profit booked from "+stock+"\n profit count"+(++profit));
//
//                        autoexit=false;
//                        break;
//                    }
//                 }
//            }
//            if(autoexit&&buy){
//                System.out.println("auto exit"+(++autoCount)+" profit/loss :" +bottomTopPercentageDifference(current, purchasePrice));
//            }
//            float closePrice = Float.parseFloat(values.get(values.size()-1).split(":")[1]);
////            if(bottomTopPercentageDifference(openPrice, closePrice)<-2){
////                loosers.add(stock);
////            }
//        }
        } 
    }
    static float bottomTopPercentageDifference(float p1, float p2) {
        float percentage = (p1 / p2) * 100;
        return 100 - percentage;

    }
}
