/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rhino.Stock;

/**
 *
 * @author arsh
 */
public class StockInit extends Thread{
    Stock stock;
    StockInit(Stock stock){
        this.stock=stock;
        this.start();
    }
    public void run(){
        init();
        
    }

    private void init() {
        try {
            Document document = Jsoup.connect(stock.getTickerURL()).get();
            
        } catch (IOException ex) {
            System.err.print("invalid url "+ stock.getTickerURL());
        }
       
    }
}
