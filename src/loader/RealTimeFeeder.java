/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import test.RediffParser;
import static rhino.Analyser.stock;
/**
 *
 * @author marshed
 */
public class RealTimeFeeder extends Thread{

    private boolean exit=false;
    private static Map<String,RediffParser> parser;

    public void run(){
        try {
            initUpdate();
            while(!exit){
                update();
            }
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public void exit(){
        exit=true;
    }

    private void update() {
        long time=System.currentTimeMillis();
        for(RediffParser rParser:parser.values()){
            String value=rParser.getCurrentValue();
            updateStockEntry(rParser.getName(),value);
        }
    }
    private void initUpdate() {
        parser = new HashMap<>();
        for(String stockName:stock.keySet()){
            RediffParser rParser = new RediffParser(stockName);
            parser.put(stockName, rParser);
            preload(rParser);
        }
        
    }
    private void updateStockEntry(String name,String value){
       ArrayList<String> list=stock.get(name);
       if(list.size()>=1){
           String lastTime=list.get(list.size()-1).split(":")[0];
           String currentTime=value.split(":")[0];
           if(lastTime.equals(currentTime)){
                try {
                    Thread.sleep(5000);
                    return;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
           }
       }
        boolean add = stock.get(name).add(value);

    }
    public static RediffParser getParser(String name){
       return parser.get(name); 
    }

    private void preload(RediffParser parser) {
        ArrayList<String> values=parser.getValues();
        ArrayList<String> destination = stock.get(parser.getName());
        for(String value:values){
            if(value.length()==0) continue;
            destination.add(value);
        }
    }
}
