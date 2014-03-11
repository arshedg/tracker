/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MarketWatch;

/**
 *
 * @author chjose
 */
import java.net.*;
import java.io.*;

public class Client {
    
    public static void main(String[] args) throws InterruptedException {
    
    String host = "localhost";
   
    int port = 19999;

    
    System.out.println("Market Watch Client initialized");
    
    System.out.println("!!!!!Synchronising with Trade Tiger!!!!!");
    System.out.println("Shift the control to Trade Tiger");
    Thread.sleep(1000);
    System.out.print("1...");
    Thread.sleep(1000);
    System.out.print("2...");
    Thread.sleep(1000);
    System.out.print("3...");
    Thread.sleep(1000);
    System.out.print("4...");
    Thread.sleep(1000);
    System.out.print("5...");
    Thread.sleep(1000);
    System.out.print("6...");
    Thread.sleep(1000);
    System.out.print("7...");
    Thread.sleep(1000);
    System.out.print("8...");
    Thread.sleep(1000);
    System.out.print("9...");
    Thread.sleep(1000);
    System.out.print("10...");
    
    try {
      
      InetAddress address = InetAddress.getByName(host);
     
      Socket connection = new Socket(address, port);
      
      
      BufferedOutputStream bos = new BufferedOutputStream(connection.
          getOutputStream());

     
      OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
      
           
      while(true)
      {
        String stock = getStockData();
      
        osw.write(stock);
        osw.flush();
      }
      
     
     }
    catch (IOException f) {
      System.out.println("IOException: " + f);
    }
    catch (Exception g) {
      System.out.println("Exception: " + g);
    }
  }
    
    public static String getStockData() throws InterruptedException
    {
        int rand = (int) (Math.random()*10000);
        Thread.sleep(rand);
        String stock =automation.ExecuteTrade.getCall()+" \n";
        return stock;
    }
}