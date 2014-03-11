/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package executor;

import automation.Selenium;
import loader.NSELoader;
import loader.RealTimeFeeder;
import rhino.Analyser;
import test.NSEHelper;

/**
 *
 * @author marshed
 */
public class RealTimeRunner {
    
    public static void  main(String arg[]) throws InterruptedException{
        NSEHelper helper=new NSEHelper();
        helper.update();
        new Thread(helper).start();
        String stocks[]=new NSELoader(1000).getAllStocks();
        Analyser analyser = new Analyser(stocks);
        new Thread(new NSEHelper()).start();
        RealTimeFeeder feeder = new RealTimeFeeder();
        feeder.start();
        Selenium.main(arg);
      // MarketWatch.Client.main(arg);
    }
}
