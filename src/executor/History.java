/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package executor;

import analyser.BackwardFall;
import analyser.ForwardRecover;
import automation.Selenium;
import loader.NSELoader;
import rhino.Analyser;

/**
 *
 * @author arsh
 */
public class History {
      public static void  main(String arg[]) throws InterruptedException{
          String stocks[]=new NSELoader(1000).getAllStocks();
         
       Analyser analyser = new Analyser(stocks);
     //  Selenium.main(arg);
       while (Thread.getAllStackTraces().keySet().size() > 5) {
                    Thread.sleep(5000);
                }
       double backwardinvestment=BackwardFall.trades*BackwardFall.amount;
       double backwardCapital=BackwardFall.capital;
       double fwdInvst=ForwardRecover.trades*ForwardRecover.amount;
       double fwdCap=ForwardRecover.capital;
       System.out.println("amount invested in backward:"+ BackwardFall.trades*BackwardFall.amount);
       System.out.println("amount after trade session "+BackwardFall.capital);
       System.out.println("amount invested in forward :"+ ForwardRecover.trades*ForwardRecover.amount);
        System.out.println("amount after trade session "+ForwardRecover.capital);
          System.out.println("total gain "+((backwardCapital+fwdCap)-(backwardinvestment+fwdInvst)));
       System.out.println("total calls backward : "+BackwardFall.totalCalls);
       System.out.println("total calls forward : "+ForwardRecover.totalCalls);
       //Selenium.main(arg);
    }
}
