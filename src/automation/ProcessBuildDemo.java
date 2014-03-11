package automation;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public 
        class ProcessBuildDemo { 
    StringSelection selection;
    public static void main(String [] args) throws IOException {
        
        String[] command = {"CMD", "/C", "dir"};
        Thread thread1 = new Thread(new SimpleThread(), "thread1");
        Runtime.getRuntime().exec("cmd /c \"C:\\POINTER\\Pointer.Exe\"");
//        ProcessBuilder probuilder = new ProcessBuilder( "C:\\POINTER\\Pointer.Exe" );
//        //You can set up your work directory
//        probuilder.directory(new File("C:\\sources"));
//        
//        Process process = probuilder.start();
        //sleep(4000);
        thread1.start();
        
        //Read out dir output
//        InputStream is = process.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String line;
//        
//        System.out.printf("Output of running %s is:\n",
//                Arrays.toString(command));
//        
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        
//        //Wait to get exit value
//        try {
//            int exitValue = process.waitFor();
//            System.out.println("\n\nExit Value is " + exitValue);
//        } catch (InterruptedException e) {
//        }
    }
    public void login() throws AWTException {
        Robot robot = new Robot();
        Clipboard clipboard;
        selection = new StringSelection("91126553");
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
            
        selection = new StringSelection("2345zZ");
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

        selection = new StringSelection("2345zX");
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

        selection = new StringSelection("19/06/1989");
        clipboard = Toolkit
                .getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
          while(true){
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100); 
          }
//        robot.keyRelease(KeyEvent.VK_CONTROL);
//        robot.keyRelease(KeyEvent.VK_V);
//            
//         robot.keyPress(KeyEvent.VK_ENTER);
//        robot.delay(100); 
//        robot.keyRelease(KeyEvent.VK_ENTER); 
//
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.delay(100); 
//        robot.keyRelease(KeyEvent.VK_ENTER); 
                   
    }
    
}

class SimpleThread extends ProcessBuildDemo implements Runnable {
    
    public void run() {
        try {
            Thread.sleep(9000);
            login();
        } catch (Exception ex) {
            Logger.getLogger(ProcessBuildDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}