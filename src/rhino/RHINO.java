/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

import java.util.HashMap;
import java.util.Map;
import marketwatch.ListedCompanies;
import test.Feeder;
import loader.NSELoader;
import loader.RediffTicker;

/**
 *
 * @author marshed
 */
public class RHINO {
//http://money.rediff.com/money1/currentstatus.php?companycode=PNB
    /**
     * @param args the command line arguments
     */
    public static Map<String,Stock> loadedStocks = new HashMap<>();
    public static void main(String[] args) {
       RediffTicker ticker= new RediffTicker();
       String scripts[]=ticker.getAllStocks(10,10);//new NSELoader(100).getAllStocks();
       new Analyser(scripts);
    // new Feeder("").start();
       ticker.start();
//     while(true){
            
//            for(String script:scripts){
//        //        System.out.println("script :" + script);
//                new Feeder("").handleRediff(script);
//            }
//           // System.out.println("round completed");
            
//     }
//     }
      // System.out.println("Analyser started");
     
      // new MarketWatcher().start();
    }
}