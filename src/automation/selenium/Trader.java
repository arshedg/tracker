/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automation.selenium;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author arsh
 */
public class Trader {

    private WebDriver driver;

    public Trader(WebDriver driver) {
        this.driver = driver;
        init();
    }

    public Trader(ChromeDriver driver, Set<Cookie> cookie) {
        this.driver = driver;
        this.setCookie(cookie);
        driver.get("https://strade.sharekhan.com/rmmweb/ocs.sk?execute=neworder&ctr=1");
    }

    private void init() {
        String baseUrl = "https://strade.sharekhan.com/rmmweb/BRLoginServlet.sk";
        driver.get(baseUrl);
        driver.findElement(By.id("loginid")).sendKeys("19061989");
        driver.findElement(By.id("pwSignup")).sendKeys("a7encs4108");
        driver.findElement(By.id("pwSignup1")).sendKeys("OY4EJ9");
        driver.findElement(By.name("Login")).click();
        driver.get("https://strade.sharekhan.com/rmmweb/ocs.sk?execute=neworder&ctr=1");
    }

    public static void main(String arg[]) {
        Trader trader = new Trader(new ChromeDriver());
        //trader.init();
        trader.placeBuy("RANBAXY", "364", "360", "2");
       // trader.cancelPendingOrder("AMBUJACEM");
        System.out.println("Completed method");
    }

    private void placeGeneric(String name, String price, String stopLoss, String quantity, boolean isSell) {
        try {
            driver.get("https://strade.sharekhan.com/rmmweb/ocs.sk?execute=neworder&ctr=1");
            driver.findElement(By.id("scrip")).sendKeys(name);
            while (true) {
                try {
                    //driver.findElement(By.xpath("//*[@id=\"id0\"]"));
                    sleep1sec();
                    driver.findElement(By.xpath("/html/body/table/tbody/tr[3]/td/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[3]/td[1]")).click();
                    break;
                } catch (Exception ex) {
                    sleep();
                }
            }
            if (!isSell) {
                selectComboValue("buysell", "BM");
            } else {
                selectComboValue("buysell", "SM");
            }
            WebElement quantityfld = driver.findElement(By.name("quantity"));
            quantityfld.sendKeys(quantity);
            driver.findElement(By.cssSelector("tr.orgbg:nth-child(6) > td:nth-child(1)")).click();
            WebElement limit = driver.findElement(By.id("price"));
            limit.clear();
            limit.sendKeys(price);
            WebElement stopLossField = driver.findElement(By.name("tprice"));
            stopLossField.clear();
            stopLossField.sendKeys(stopLoss);
            WebElement order = driver.findElement(By.name("Place Order"));
            order.click();
            Alert alert = driver.switchTo().alert();
            alert.accept();
            driver.switchTo().defaultContent();
        } catch (Exception ex) {
            placeGeneric(name, price, stopLoss, quantity, isSell);
        }

    }

    public boolean placeBuy(String name, String price, String stopLoss, String quantity) {
        placeGeneric(name, price, stopLoss, quantity, false);
        return true;
    }

    public boolean placeSell(String name, String price, String stopLoss, String quantity) {
        placeGeneric(name, price, stopLoss, quantity, true);
        return true;
    }

    public void setCookie(Set<Cookie> cookies) {
        String baseUrl = "https://strade.sharekhan.com/rmmweb/BRLoginServlet.sk";
        driver.get(baseUrl);
        driver.manage().deleteAllCookies();
        for (Cookie coockie : cookies) {
            driver.manage().addCookie(coockie);
        }
    }

    public Set<Cookie> getCookie() {
        return driver.manage().getCookies();
    }

    public static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Trader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sleep1sec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Trader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectComboValue(final String elementId, final String value) {
        final Select selectBox = new Select(driver.findElement(By.id(elementId)));
        selectBox.selectByValue(value);//https://strade.sharekhan.com/rmmweb/rcs.sk?execute=orderreport

    }

    public void selectComboValueByName(final String elementName, final String value) {
        final Select selectBox = new Select(driver.findElement(By.name(elementName)));
        selectBox.selectByValue(value);
    }

    private void cancelPendingOrder(String scripName) {
        int rowCount = 1;
        while (rowCount > 0) {
            driver.get("https://strade.sharekhan.com/rmmweb/rcs.sk?execute=dynamicreport&pType=901");
            selectComboValueByName("ScripCode", scripName);
            driver.findElement(By.xpath("/html/body/table/tbody/tr[3]/td/form/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[5]/input")).click();
            WebElement table = driver.findElement(By.id("sort"));
            WebElement tbody = table.findElement(By.tagName("tbody"));
            List<WebElement> allRows = tbody.findElements(By.tagName("tr"));
            rowCount = allRows.size();
            for (WebElement row : allRows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() < 6) {
                    rowCount--;
                    continue;
                }
                String status = cells.get(6).getText();
                if (!"Pending".equals(status.trim())) {
                    rowCount--;
                    continue;
                }
                WebElement lastColumn = cells.get(15);
                WebElement cancelLink = lastColumn.findElements(By.tagName("a")).get(1);
                cancelLink.click();
                WebElement cancelConf = driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/form/table/tbody/tr[2]/td/div/table/tbody/tr[11]/td/input[1]"));
                cancelConf.click();
                Alert alert = driver.switchTo().alert();
                alert.accept();
                driver.switchTo().defaultContent();
            }
            return;
        }
    }
}
