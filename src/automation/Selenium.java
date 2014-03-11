/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automation;


import automation.selenium.SquareOFF;
import automation.selenium.Trader;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author arsh
 */
public class Selenium {

 
    //   static SquareOFF sq=new SquareOFF();
    public static boolean locked = false;

    public static void main(String arg[]) {
        Trader trader=new Trader(new ChromeDriver());
        SquareOFF square = new SquareOFF(trader);
        new Thread(square).start();
        while (true) {
            String order = ExecuteTrade.getCall();
            String values[] = order.split(" ");
            if (Integer.parseInt(values[3]) < 1) {
                continue;
            }
            try {
                if ("Sell".equals(values[0])) {
                    trader.placeSell(values[2], values[4], values[6], values[3]);
                } else {
                    trader.placeBuy(values[2], values[4], values[6],values[3]);
                }
                ExecuteTrade.save(order);
            } catch (Exception ex) {
                System.out.print("\nException>>>>>>\n");
                continue;
            }
        }
            // placeSellOrder("infy", "1", "3r00.00", "3690", "3710",false);

         //   placeBuy("RANBAXY","364","360","2",false);
            // }
      //  }
    }
  

   
   

   
   
}
