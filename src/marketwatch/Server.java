package MarketWatch;

/**
 *
 * @author chjose
 */
import static MarketWatch.Server.socket1;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.applet.Main;

public class Server implements Runnable{

 static ServerSocket socket1;
 protected final static int port = 19999;
 static Socket connection;

 static String Stock_details;
 
 public static void main(String[] args) throws IOException, AWTException, LineUnavailableException, UnsupportedAudioFileException {
     int i=1;
     boolean status = true;
     Server server = new Server();
    try{
      socket1 = new ServerSocket(port);
      
      System.out.println("Stock Breaker Server Initialized");
      
      connection = socket1.accept();
      System.out.println("Connected to Market Watch.....");
      System.out.println("Waiting for Buy/Sell call.....");
      
      //TRAIL CALL TO SET THE APP
      server.Parse_stock("Sell Nse CADILAHC 12 78646 78174 79117");
      //new Thread(new Server()).start();
      while (true) {
        
        System.out.println("Waiting for Call-"+(i++));
        BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
        InputStreamReader isr = new InputStreamReader(is);
        Scanner input = new Scanner(isr);
        
        Stock_details = input.nextLine();
         
        System.out.println(Stock_details);
        
        status = server.Parse_stock(Stock_details);
     }
    }
    catch (IOException e) {
             connection.close();
          }
    }

    public synchronized boolean Parse_stock(String Stock_details) throws AWTException, LineUnavailableException, IOException, UnsupportedAudioFileException {
        
//        Clip clip = AudioSystem.getClip();
//        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
//          new File("C:\\Users\\chjose\\Desktop\\marketWatch\\ll"));
//        clip.open(inputStream);
//        clip.start();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ESCAPE); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.keyPress(KeyEvent.VK_F2);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_F2);
        robot.keyPress(KeyEvent.VK_ESCAPE); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.delay(2000);
        
        String array[] = Stock_details.split(" ");
        boolean bse = false;
        boolean status = true;
        if(array[0].equals("Sell"))
        {
            if(array[1].equals("Bse"))
                bse = true;
            else if(array[1].equals("Nse"))
                bse = false;
            else
            {
                System.err.println("Invalid Stock Exchange: Please try with NSE or BSE");
                return false;
            }
            System.out.println("Sell Call Made");
            status = SellStock(bse,array[2],array[3],array[4],array[5],array[6]);
            //System.out.println(array[1]+array[2]+array[3]+array[4]+array[5]+array[6]);
        }
        else if(array[0].equals("Buy"))
        {
          if(array[1].equals("Bse"))
                bse = true;
            else if(array[1].equals("Nse"))
                bse = false;
            else
            {
                System.err.println("Invalid Stock Exchange: Please try with NSE or BSE");
                return false;
            }
            
            status = BuyStock(bse,array[2],array[3],array[4],array[5],array[6]);
            //System.out.println(array[1]+array[2]+array[3]+array[4]+array[5]+array[6]);
        }
        else
        {
          System.err.println("Invalid Call");
          return false;
        }
        return status; 
    }
    
    boolean SellStock(boolean bse, String StockName, String count, String Price, String target, String stop_loss) throws AWTException
    {
    
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_ESCAPE); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        
        if(((new Float(target))<(new Float(Price)))!=((new Float(Price))<(new Float(stop_loss))))
        {
            System.out.println("Incorrect Price value");
        }
        
        Float reduced_price = new Float(Price);
        float test_price = reduced_price - (float)(0.1/reduced_price * 100);
        Price = "" + test_price;
        System.out.println("Stock Name:"+StockName);

        System.out.println("\n");

        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_F2);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_F2);

        if(false == bse)  //Stock Exchange is NSE
        {
            type(StockName);
     
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
                    
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(count);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            System.out.println("Price ="+Price);
            
            robot.delay(2000);
            type(Price);
            //type("0");
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(4000);
            
            type(target);
                        
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(stop_loss);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ESCAPE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            
            //robot.delay(1000000);   /Enable this line to debug which will stop the execution
        }
        else
        {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_TAB);
        
            robot.keyPress(KeyEvent.VK_B);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_B);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(StockName);
     
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
                    
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(count);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            System.out.println("Price ="+Price);
            
            robot.delay(2000);
            type(Price);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(4000);
            
            type(target);
                        
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(stop_loss);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ESCAPE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            
            //robot.delay(1000000);   /Enable this line to debug which will stop the execution
        }
    return true;

    } /* SellStock */
    
    boolean BuyStock(boolean bse, String StockName, String count, String Price, String target, String stop_loss) throws AWTException
    {
    
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_ESCAPE); 
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        
        if(((new Float(target))>(new Float(Price)))!=((new Float(Price))>(new Float(stop_loss))))
        {
            System.out.println("Incorrect Price value");
        }
        
        System.out.println("Stock Name:"+StockName);

        System.out.println("\n");

        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_F1);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_F1);

        if(false == bse)  //Stock Exchange is NSE
        {
            type(StockName);
     
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
                    
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(count);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            System.out.println("Price ="+Price);
            
            robot.delay(2000);
            type(Price);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(4000);
            
            type(target);
                        
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(stop_loss);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ESCAPE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            
            //robot.delay(1000000);   /Enable this line to debug which will stop the execution
        }
        else
        {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_TAB);
        
            robot.keyPress(KeyEvent.VK_B);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_B);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(StockName);
     
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
                    
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(count);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            System.out.println("Price ="+Price);
            
            robot.delay(2000);
            type(Price);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_SPACE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_SPACE);
            
            robot.delay(4000);
            
            type(target);
                        
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            type(stop_loss);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_TAB); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_TAB);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ENTER); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ENTER);
            
            robot.delay(1000);
            
            robot.keyPress(KeyEvent.VK_ESCAPE); 
            robot.delay(100); 
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            
            //robot.delay(1000000);   /Enable this line to debug which will stop the execution
        }
    return true;

    } /* BuyStock */
    
    public void type(CharSequence characters) throws AWTException {
    	int length = characters.length();
    	for (int i = 0; i < length; i++) {
    		char character = characters.charAt(i);
    		type(character);
    	}
    }

    public void type(char character) throws AWTException {
     	switch (character) {
    	case 'a': doType(KeyEvent.VK_A); break;
    	case 'b': doType(KeyEvent.VK_B); break;
    	case 'c': doType(KeyEvent.VK_C); break;
    	case 'd': doType(KeyEvent.VK_D); break;
    	case 'e': doType(KeyEvent.VK_E); break;
    	case 'f': doType(KeyEvent.VK_F); break;
    	case 'g': doType(KeyEvent.VK_G); break;
    	case 'h': doType(KeyEvent.VK_H); break;
    	case 'i': doType(KeyEvent.VK_I); break;
    	case 'j': doType(KeyEvent.VK_J); break;
    	case 'k': doType(KeyEvent.VK_K); break;
    	case 'l': doType(KeyEvent.VK_L); break;
    	case 'm': doType(KeyEvent.VK_M); break;
    	case 'n': doType(KeyEvent.VK_N); break;
    
	case 'o': doType(KeyEvent.VK_O); break;
    	case 'p': doType(KeyEvent.VK_P); break;
    	case 'q': doType(KeyEvent.VK_Q); break;
    	case 'r': doType(KeyEvent.VK_R); break;
    	case 's': doType(KeyEvent.VK_S); break;
    	case 't': doType(KeyEvent.VK_T); break;
    	case 'u': doType(KeyEvent.VK_U); break;
    	case 'v': doType(KeyEvent.VK_V); break;
    	case 'w': doType(KeyEvent.VK_W); break;
    	case 'x': doType(KeyEvent.VK_X); break;
    	case 'y': doType(KeyEvent.VK_Y); break;
    	case 'z': doType(KeyEvent.VK_Z); break;
    	case 'A': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
    	case 'B': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
    	case 'C': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
    	case 'D': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
    	case 'E': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
    	case 'F': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
    	case 'G': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
    	case 'H': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
    	case 'I': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
    	case 'J': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
    	case 'K': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
    	case 'L': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
    	case 'M': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
    	case 'N': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
    	case 'O': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
    	case 'P': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
    	case 'Q': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
    	case 'R': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
    	case 'S': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
    	case 'T': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
    	case 'U': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
    	case 'V': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
    	case 'W': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
    	case 'X': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
    	case 'Y': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
    	case 'Z': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
    	case '`': doType(KeyEvent.VK_BACK_QUOTE); break;
    	case '0': doType(KeyEvent.VK_0); break;
    	case '1': doType(KeyEvent.VK_1); break;
    	case '2': doType(KeyEvent.VK_2); break;
    
	case '3': doType(KeyEvent.VK_3); break;
    	case '4': doType(KeyEvent.VK_4); break;
    	case '5': doType(KeyEvent.VK_5); break;
    	case '6': doType(KeyEvent.VK_6); break;
    	case '7': doType(KeyEvent.VK_7); break;
    	case '8': doType(KeyEvent.VK_8); break;
    	case '9': doType(KeyEvent.VK_9); break;
    	case '-': doType(KeyEvent.VK_MINUS); break;
    	case '=': doType(KeyEvent.VK_EQUALS); break;
    	case '~': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
    	case '!': doType(KeyEvent.VK_EXCLAMATION_MARK); break;
    	case '@': doType(KeyEvent.VK_AT); break;
    	case '#': doType(KeyEvent.VK_NUMBER_SIGN); break;
    	case '$': doType(KeyEvent.VK_DOLLAR); break;
    	case '%': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
    	case '^': doType(KeyEvent.VK_CIRCUMFLEX); break;
    	case '&': doType(KeyEvent.VK_AMPERSAND); break;
    	case '*': doType(KeyEvent.VK_ASTERISK); break;
    	case '(': doType(KeyEvent.VK_LEFT_PARENTHESIS); break;
    	case ')': doType(KeyEvent.VK_RIGHT_PARENTHESIS); break;
    	case '_': doType(KeyEvent.VK_UNDERSCORE); break;
    	case '+': doType(KeyEvent.VK_PLUS); break;
    	case '\t': doType(KeyEvent.VK_TAB); break;
    	case '\n': doType(KeyEvent.VK_ENTER); break;
    	case '[': doType(KeyEvent.VK_OPEN_BRACKET); break;
    	case ']': doType(KeyEvent.VK_CLOSE_BRACKET); break;
    	case '\\': doType(KeyEvent.VK_BACK_SLASH); break;
    	case '{': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
    	case '}': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
    	case '|': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
    	case ';': doType(KeyEvent.VK_SEMICOLON); break;
    	case ':': doType(KeyEvent.VK_COLON); break;
    	case '\'': doType(KeyEvent.VK_QUOTE); break;
    	case '"': doType(KeyEvent.VK_QUOTEDBL); break;
    	case ',': doType(KeyEvent.VK_COMMA); break;
    	case '<': doType(KeyEvent.VK_LESS); break;
    	case '.': doType(KeyEvent.VK_PERIOD); break;
    	case '>': doType(KeyEvent.VK_GREATER); break;
    	case '/': doType(KeyEvent.VK_SLASH); break;
    	case '?': doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
    	case ' ': doType(KeyEvent.VK_SPACE); break;
    	default:
    		throw new IllegalArgumentException("Cannot type character " + character);
    	}
    }

    private void doType(int... keyCodes) throws AWTException {
    	doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(int[] keyCodes, int offset, int length) throws AWTException {
        Robot robot = new Robot();
    	if (length == 0) {
    		return;
    	}

    	robot.keyPress(keyCodes[offset]);
    	doType(keyCodes, offset + 1, length - 1);
    	robot.keyRelease(keyCodes[offset]);
    }

    void reset_trade_tiger() throws AWTException
    {
        Robot robot = new Robot();
        
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.delay(100); 
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_F4);
        
    }

    @Override
    public void run() {
        
        while(true)
        {
            try {
                Thread.sleep(60*1000);
                new Server().Parse_stock("Sell Nse DHFL 12 78646 78174 79117");
            } catch (    InterruptedException | AWTException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}