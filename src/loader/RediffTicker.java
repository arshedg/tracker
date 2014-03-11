/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rhino.Analyser;

/**
 *
 * @author arsh
 */
public class RediffTicker extends Thread {
    final static String gainer="http://money.rediff.com/gainers/bse";
    final static String looser="http://money.rediff.com/losers/bse/daily";
    private  Set<String> loadedStocks;
    public void run(){
        while(true){
            try {
                
                update();
                Thread.sleep(600);
            } catch (InterruptedException ex) {
                Logger.getLogger(RediffTicker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void update() {
        try {
            loadedStocks=Analyser.stock.keySet();
            Document gainerDocument = Jsoup.connect(gainer).get();
            updateHandler(gainerDocument);
            Document looserDocument = Jsoup.connect(looser).get();
             updateHandler(looserDocument);
        } catch (IOException ex) {
            Logger.getLogger(RediffTicker.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    private void updateHandler(Document document){
        Elements tables=document.getElementsByClass("dataTable");
        Element table=tables.get(0);
        Element tableData=table.child(1);
        Elements children=tableData.getElementsByTag("tr");
        for(Element child:children){
            String companyName=getCompanyName(child);
            String currentPrice =""+ getCurentPrice(child);
            addToMap(companyName,currentPrice);
        }
    }
    private boolean isValid(Element data){
       String url=getURL(data);
        try {
            Document document = Jsoup.connect(url).get();
            Element e=document.getElementsByClass("company-graph-wrap").get(0).getElementById("Volume");
            float volume=Float.parseFloat(e.text().replaceAll(",", ""));
            if(volume*getCurentPrice(data)>200000){
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(RediffTicker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public String[] getAllStocks(){
        try {
            ArrayList<String> list=new ArrayList<>();
            Document gainerDocument = Jsoup.connect(gainer).get();
            Elements tables=gainerDocument.getElementsByClass("dataTable");
            Element table=tables.get(0);
            Element tableData=table.child(1);
            Elements children=tableData.getElementsByTag("tr");
            for(Element child:children){
                String companyName=getCompanyName(child);
                if( getGroup(child).equals("A")){
                   list.add(companyName);
                }
            }
            Document looserDocument = Jsoup.connect(looser).get();
            tables=looserDocument.getElementsByClass("dataTable");
             table=tables.get(0);
             tableData=table.child(1);
             children=tableData.getElementsByTag("tr");
            for(Element child:children){
                String companyName=getCompanyName(child);
                if( getGroup(child).equals("A")){
                   list.add(companyName);
                }
            }
            return list.toArray(new String[1]);
        } catch (IOException ex) {
            System.err.println("cannot load stocks from reddif url");
            return null;
        }
        
    }
     public String[] getAllStocks(int looserMax,int gainerMax){
        try {
            int count=0;
            ArrayList<String> list=new ArrayList<>();
            Document gainerDocument = Jsoup.connect(gainer).get();
            Elements tables=gainerDocument.getElementsByClass("dataTable");
            Element table=tables.get(0);
            Element tableData=table.child(1);
            Elements children=tableData.getElementsByTag("tr");
            for(Element child:children){
               if(looserMax<=count){
                   break; 
                }
                String companyName=getCompanyName(child);
                if( getGroup(child).equals("A")){
                   list.add(companyName);
                   count++;
                }
                
            }
            Document looserDocument = Jsoup.connect(looser).get();
            tables=looserDocument.getElementsByClass("dataTable");
             table=tables.get(0);
             tableData=table.child(1);
             children=tableData.getElementsByTag("tr");
             count=0;
             for(Element child:children){
                if(gainerMax<=count){
                   break; 
                }
                String companyName=getCompanyName(child);
                if( getGroup(child).equals("A")){
                   list.add(companyName);
                   count++;
                }
               
            }
            return list.toArray(new String[1]);
        } catch (IOException ex) {
            System.err.println("cannot load stocks from reddif url");
            return null;
        }
        
    }
    public static void main(String arg[]){
        new RediffTicker().run();
    }

    private String getCompanyName(Element data) {
        return data.child(0).text();
    }
    
     private String getURL(Element data) {
        return data.child(0).child(0).attr("href");
    }
    private float getCurentPrice(Element data) {
        return Float.parseFloat(data.child(3).text().replaceAll(",", ""));
    }
    private String getOpenPrice(Element data) {
        return data.child(2).text();
    }
    private String getGroup(Element data) {
        return data.child(1).text();
    }

    private void addToMap(String companyName, String currentPrice) {
        ArrayList<String> list=Analyser.stock.get(companyName);
        if(list==null){
            return;
        }
        else{
            Calendar now = Calendar.getInstance();
            int min=now.get(Calendar.MINUTE);
            String minute;
            if(min<10){
                minute="0"+min;
            }
            else{
                minute=""+min;
            }
            String formattedData=now.get(Calendar.HOUR_OF_DAY)+""+minute+":"+currentPrice;
            list.add(formattedData);
            //loadedStocks.remove(companyName);
        }
    }
    
    
}
