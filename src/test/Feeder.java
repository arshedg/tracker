/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import rhino.Analyser;

/**
 *
 * @author arsh
 */
/*
 * if this class is run parrelely, user can input sample values to check the analyser
 * 
 */
public class Feeder extends Thread {
  //http://money.rediff.com/money1/chartnse_1day_new.php?companyCode=infy
  private FeederInput inputType;
  private String scripts[]={"RKFORGE"};
  public Feeder(String inputType){
      //this.inputType=inputType;
      
  }
   public Feeder(){
      this.inputType=FeederInput.KEYBOARD;
      this.start();
  }
  @Override
  public void run(){
     switch(inputType){
         case KEYBOARD: handleKeyBoard();
                        break;
         case FILESYSTEM :handleRediff("");
     } 
  }

    private void handleKeyBoard() {
        System.out.println("ENTER SCRIPT NAME AND SET OF VALUES OF TIME AND PRICE\n ex paral 915:234 916 244\n type change to enter new value \n type exit to exit from the tester");
        Scanner keyboard=new Scanner(System.in);
        Map<String,ArrayList<String>> stock=Analyser.stock;
        String scriptName;
        ArrayList<String> values = null;
        while(keyboard.hasNext()){
            String input=keyboard.next();
      
            if(input.indexOf(":")<0){
                //script name or exit
                if(input.compareToIgnoreCase("exit")==0){
                    return;
                }
                else{
                    scriptName=input;
                    values=stock.get(scriptName);
                    if(values==null){
                        System.out.println("Script name doesnot exist. Injecting new values at runtime is not supported");
                    }
                }
            }
            else{
                values.add(input);
            }
        }
    }

    public Map<String,ArrayList<String>>  handleRediff(String script) {
         //To change body of generated methods, choose Tools | Templates.
        Map<String,ArrayList<String>> stock=Analyser.stock;
      
            RediffParser parser = new RediffParser(script);
            ArrayList<String> list=stock.get(script);
            values=parser.getValues();
            if(values==null){
                return null;
            }
            new ValueSimulator(list, values).start();
        
        return stock;
    }
    private ArrayList<String> values;
}
class ValueSimulator extends Thread{
    ArrayList<String> values,array;
    public ValueSimulator(ArrayList<String> array,ArrayList<String> values){
        this.values=values;
        this.array=array;
    }
    public void run(){
        try {
            int arrayCount=array.size();
            int valueCount= values.size();
            for(int i=arrayCount;i<valueCount;i++){
                try {
                    array.add(values.get(i));
                    Thread.sleep(150);
                } catch (Exception ex) {
                    Logger.getLogger(ValueSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           // 
//            for(String value:values){
//                try {
//                    array.add(value);
//         
//                    Thread.sleep(250);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ValueSimulator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        } catch (Exception ex) {
            Logger.getLogger(ValueSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

