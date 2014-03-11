/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automation.selenium;

import automation.Selenium;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import rhino.Configuration;

/**
 *
 * @author arsh
 */
public class SquareOFF implements Runnable {

    List<String> calls = new ArrayList<String>();
    static Map<String, Integer> partial = new HashMap<>();
    static boolean init = false;
    ChromeDriver driver;
    Trader trader;
    Map map = new HashMap();
    List<String> completed;

    private void loadReport() {
        driver.get("https://strade.sharekhan.com/rmmweb/rcs.sk?execute=dynamicreport&pType=902&net=y");
    }

    void save(String scripName,String type) {
        try {
            File file = new File("C:\\chrstn\\temp.data");
            PrintWriter writer = null;
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new PrintWriter(new FileWriter("C:\\chrstn\\temp.data", true));
            writer.append(scripName +":"+type+ ",");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String[] read() {
        try {
            File file = new File("C:\\chrstn\\temp.data");
            if (!file.exists()) {
                return new String[]{};

            }
            Scanner scan = new Scanner(file);
            String data = null;
        
            
            if (scan.hasNextLine()) {
                data = scan.next();
                return data.split(",");
            }
       
            

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);

        }
        return new String[]{};
    }

    public SquareOFF(Trader otherTrader) {
        driver = new ChromeDriver();
       driver.setLogLevel(Level.OFF);
        trader = new Trader(driver,otherTrader.getCookie());
        
        completed = Arrays.asList(read());
      //  init();
        // while(true) {
       // checkCalls();
        //}

//         new Thread(this).start();
    }

    public void init() {
        String baseUrl = "https://strade.sharekhan.com/rmmweb/BRLoginServlet.sk";
        driver.get(baseUrl);
        driver.findElement(By.id("loginid")).sendKeys("19061989");
        driver.findElement(By.id("pwSignup")).sendKeys("a7encs4107");
        driver.findElement(By.id("pwSignup1")).sendKeys("OY4EJ9");
        driver.findElement(By.name("Login")).click();
        // driver.get("https://strade.sharekhan.com/rmmweb/ocs.sk?execute=neworder&ctr=0"); 
    }

    @Override
    public void run() {
      checkCalls(); 
    }

    public static void main(String arg[]) {
        Trader trader = new Trader(new ChromeDriver());
        SquareOFF sq = new SquareOFF(trader);
        sq.run();
        System.out.println(sq.read().toString());
        // sq.addCall("371119576:3000:2100");
    }

    public void addCall(String call) {
        calls.add(call);
    }
    int purchasePrice = Configuration.purchaseAmount;

    private void checkCalls() {
        while (true) {
            trader.sleep();

            loadReport();
//            Select selectBox = new Select(driver.findElement(By.name("ScripCode")));
//            List<WebElement> tradedScrips = selectBox.getOptions();
//            for (WebElement element : tradedScrips) {
//                String scripId = element.getText();
//                if ("Select".equals(scripId) ) {
//                    continue;
//                }
//                selectBox.selectByValue(scripId);
//            }
//            driver.findElement(By.xpath("/html/body/table[1]/tbody/tr[3]/td/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[4]/table/tbody/tr/td[1]/input")).click();

            WebElement table = driver.findElement(By.id("sort"));
            List<WebElement> allRows = table.findElements(By.tagName("tr"));
            for (WebElement row : allRows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() < 6) {
                    continue;
                }
                String id = cells.get(0).getText().trim();
                String type = cells.get(4).getText().trim();
                if(completed.contains(id+":"+type)){
                    continue;
                }
                float price = Float.parseFloat(cells.get(6).getText().trim());
                int quantity = (int) (purchasePrice / price);
                if ("LONG".equals(type)) {
                    float stopLossTrigger = (float) (price - (.007 * price));
                    float stopLoss = (float) (price - (.0075 * price));
                    float target = (float) (price + (0.01 * price));
                    System.out.println("\n\n\n placing square off(buy) for "+ id+" target price :"+target);
                    trader.placeSell(id, roundOFF(target), "0", "" + quantity);
                    trader.placeSell(id, roundOFF(stopLoss), roundOFF(stopLossTrigger), "" + quantity);
                    save(id,type);
                    completed = Arrays.asList(read());
                    break;

                    //Selenium.placeSellOrder(type, type, type, type, type, init);
                } else {
                    float stopLossTrigger = (float) (price + (.007 * price));
                    float stopLoss = (float) (price + (.0075 * price));
                    float target = (float) (price - (.010 * price));
                    System.out.println("\n\n\n placing square off(sell) for "+ id+" target price :"+target);
                    trader.placeBuy(id, roundOFF(target), "0", "" + quantity);
                    trader.placeBuy(id, roundOFF(stopLoss), roundOFF(stopLossTrigger), "" + quantity);
                    save(id,type);
                    completed = Arrays.asList(read());
                    break;
                }


            }

        }
    }

    private static String roundOFF(float value) {
        if (value == 0) {
            return "0";
        }
        String amt = "" + value;
        String currency[] = amt.split("\\.");
        String ruppe = currency[0];
        String paise = currency[1];
        int lastDigit=0;
        if (paise.length() >= 2) {
            paise = paise.substring(0, 2);
            lastDigit = Integer.parseInt("" + paise.charAt(1));
        }


        if (lastDigit > 5) {
            paise = paise.charAt(0) + "5";
        } else {
            paise = paise.charAt(0) + "0";
        }
        return ruppe + "." + paise;
    }
}
