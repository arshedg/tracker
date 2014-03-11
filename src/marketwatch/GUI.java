/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MarketWatch;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author saju
 */
public class GUI extends Thread{
    
    StringSelection selection; 
        
    public static void main(String[] args) throws AWTException, IOException, InterruptedException { 
        
        
      
      new GUI().start();
   }

    @Override
   public void run()
   {
      try {
          
          boolean exit = false;
          KeyEvent e = null;
          strace_pointer();
          sleep(5000);
          //System.exit(0);
          //boolean BuyStock;
          //sleep(15000);
          //do
          //{
          //BuyStock = SellStock("1", "TULIP", false, "55", "45", "67");
          
            //if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
              //  exit = true;
          
        
          //}
          //while(exit == true);
      } catch (Throwable t) { }
   }

    
    
    

    private boolean BuyStock(String count, String StockName, boolean bse) throws AWTException
    {
    
        Robot robot = new Robot();

        selection = new StringSelection(StockName);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);


        System.out.println("Buy Call Made ......");
     
        robot.delay(1000); 
        robot.keyPress(KeyEvent.VK_F1);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_F1);

        if(true == bse)
        {
            robot.delay(100); 
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_B);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_B);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            selection = new StringSelection(count);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);


            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(500); 
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
        else
        {
            robot.delay(100); 
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_N);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_N);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            selection = new StringSelection(count);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);


            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);

        }
    return true;

    } 
    
    
    private boolean SellStock(String count, String StockName, boolean bse, String Price, String target, String stop_loss) throws AWTException
    {
    
        Robot robot = new Robot();

        selection = new StringSelection(StockName);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);


        System.out.println("Sell Call made ......");

        robot.keyPress(KeyEvent.VK_F2);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_F2);

        if(true == bse)
        {
            
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);


            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            
            selection = new StringSelection(count);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            selection = new StringSelection(Price);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);

        }
        else
        {
            robot.delay(100); 
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_N);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_N);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            selection = new StringSelection(count);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);


            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);

        }
    return true;

    } 

    private void login() throws AWTException, InterruptedException {
        
        Robot robot = new Robot();
        Clipboard clipboard;
        sleep(12000);
        selection = new StringSelection("nitishsadasivan");
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
            
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        robot.keyPress(KeyEvent.VK_TAB); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_TAB);  
        
        robot.keyPress(KeyEvent.VK_TAB); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_TAB);  

        selection = new StringSelection("abcd22!!");
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
            
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        robot.keyPress(KeyEvent.VK_TAB); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_TAB);

        selection = new StringSelection("I292SU");
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
            
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
            
         robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ENTER); 

                           
    }

    private boolean strace_pointer() throws IOException, InterruptedException, AWTException {
        
       
        Process process = Runtime.getRuntime ().exec ("C:\\Sharekhan\\TradeTigerNew\\TradeTiger");
        sleep(10000);

        login();

        return true;
    }

    
}
