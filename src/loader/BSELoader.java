/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import rhino.Stock;

/**
 *
 * @author arsh
 */
public class BSELoader {

    private void loader() {
        try {
            String url = "http://www.moneycontrol.com/stocks/marketinfo/company_groups/index_a.html";
            Document document;
            document = Jsoup.connect(url).get();
            Elements list = document.getElementsByClass("list6");
            for(Element element:list){
                for(Node child:element.childNodes()){
                    String detailsUrl="http://www.moneycontrol.com/"+child.childNode(1).attr("href");
                    new StockLoader(detailsUrl);
                    Thread.sleep(500);
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(BSELoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void main(String arg[]){
        new BSELoader().loader();
    }
}
class StockLoader extends Thread{
    String url;
    StockLoader(String url){
        this.url=url;
        this.start();
    }    
    @Override
    public void run(){
        loadStock();
    }
    private void loadStock(){
        Document document;
        try {
            document = Jsoup.connect(url).get();
            Element element=document.getElementById("nChrtPrc");
            String companyName = element.getElementsByClass("b_42").get(0).text();
            String ids=element.getElementsByClass("gry10").get(0).text();
            String id[]=ids.split(":");
            String bseID=id[1].split(" ")[1];
            String nseID=id[2].split(" ")[1];
            System.out.print(nseID+",");
           // Stock stock=new Stock(companyName,bseID,nseID);
           // StockInit initializer=new StockInit(stock);
        } catch (Exception ex) {
           System.out.print("loading url failed :"+url+"\n");
        }
    }
}

