/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package executor;

import analyser.BackwardFall;
import analyser.ForwardRecover;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import loader.NSELoader;
import rhino.Analyser;
import rhino.Configuration;
import rhino.SellHandler;
import test.NSEHelper;

/**
 *
 * @author arsh
 */
public class TestSuite {

    public static void main(String arg[]) {
        File f=new File("C:\\stock\\stock_history");
        File files[]=f.listFiles();
        List<String> dates=new ArrayList<>();
        for(File file:files){
            if(file.getName().indexOf("_5_")<0&&file.getName().indexOf("_4_")<0)
            dates.add(file.getName());
        }
       // String dates[]={"19_5_2013","18_5_2013","17_5_2013","14_5_2013","13_5_2013","12_5_2013","11_5_2013","10_5_2013","31_4_2013","5_5_2013","4_5_2013","3_5_2013","6_5_2013","7_5_2013"};
        String stocks[] = new NSELoader(1000).getAllStocks();
        int trades=0,gain=0;
        for (String date : dates) {
            try {
                Configuration.dateOnHistory = date;
               System.out.println("testing on date "+date);
                Analyser analyser = new Analyser(stocks);
               // Thread.sleep(10000);
                while (Thread.getAllStackTraces().keySet().size() > 5) {
                    Thread.sleep(5000);
                }
                       System.out.println("amount invested :"+ BackwardFall.trades*BackwardFall.amount);
       System.out.println("amount after trade session "+BackwardFall.capital);;
       Thread.sleep(100);
       //Selenium.main(arg);
               NSEHelper.update();

               
            }
             catch (InterruptedException ex) {
                Logger.getLogger(TestSuite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         int profit = SellHandler.profitCount, loss = SellHandler.lossCount, nowhere = SellHandler.noWhere;
                System.out.println("total profit " + profit);
                System.out.println("total loss " + loss);
                System.out.println("total nowhere " + nowhere);
                float per = (float) profit / (float) (profit + loss);
                System.out.println("proft percentage" + per);


//        int profit=SellHandler.profitCount,loss=SellHandler.lossCount,nowhere=SellHandler.noWhere;
//        System.out.println("total profit "+profit);
//         System.out.println("total loss "+loss);
//         System.out.println("total nowhere "+nowhere);
//         float per=(float)profit/(float)(profit+loss);
    }
}

