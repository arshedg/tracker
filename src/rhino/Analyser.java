/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.NSEHelper;

/**
 *
 * @author marshed
 */
public class Analyser {
    //Least Risk/ least profit analyser
    /*
     * Features:
     * Buy/ sell will be triggered only for a perfect rally
     * 
     */
    public static Map<String,ArrayList<String>> stock;

    static int totalBuyCall=0;
    public static Map<String,Boolean> patternFound=new HashMap();
    //once pattern is found then we are holding the process, ideally it should be still under process
    //to find the opposite pattern to avoid loss. For example once buy pattern is found, then the stock must be 
    //checked for sell pattern.
    public static boolean isBull;
    public Analyser(){
        //init the stock map
        /*
         * temporary init
         */
        stock = new HashMap<>();
       // ArrayList<String> array=new ArrayList<>();
     //   array.add("930:24");array.add("931:25");array.add("932:27");
       // stock.put("pral",array );
        ArrayList<String> array2=new ArrayList<>();
        array2.add("930:24");array2.add("931:24.5");array2.add("932:24");
        stock.put("tcs",array2);
        execute();
       
    }
    public Analyser(String[] names){
        if(Analyser.stock==null)
            Analyser.stock=new HashMap<>();
        for(String name:names){
            Analyser.stock.put(name, new ArrayList<String>());
        }
      execute();
    }
    private void execute(){
//      isBull=isSensexGoingUP();
//      if(isBull){
//          return;
//      }
        Set<String> stocks= stock.keySet();
        for(String stockName:stocks){
            Process p= new Process(stockName);
            Thread t= new Thread(p);
            t.setName(stockName);
            t.start();
            //System.out.println("analyser started for" + stockName);
        }
        //System.out.println("Analyser started for all the stocks");
    }
     private boolean isSensexGoingUP(){
        int up=-0,down=0;
        float last=NSEHelper.getNseValue(0);
        float first=last;
        for(int i=1;i<80;i++){

            float value=NSEHelper.getNseValue(i);
            if(value>last){
                up++;
            }
            else{
                down++;
            }
            last=value;
        }
        float diff=bottomTopPercentageDifference(first, last);
        System.out.println(diff+"\n\n\n\n\n\n");
        if(diff<-.4){
            return true;
        }
        else{
            return false;
        }
     
    }
        float bottomTopPercentageDifference(float p1, float p2) {
        float percentage = (p1 / p2) * 100;
        return 100 - percentage;

    }
    
}
