/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analyser;

import automation.selenium.SquareOFF;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsh
 */
public class Extreme {
    String fileName;
    public Extreme(String name){
        fileName=name;
    }
    void save(String scripName) {
        try {
            File file = new File("C:\\chrstn\\"+fileName);
            PrintWriter writer = null;
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new PrintWriter(new FileWriter("C:\\chrstn\\temp.data", true));
            writer.append(scripName +":incomplete"+ ",");
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String[] read() {
        try {
            File file = new File("C:\\chrstn\\"+fileName);
            if (!file.exists()) {
                return new String[]{};

            }
            Scanner scan = new Scanner(file);
            String data = null;
            String datas[]=null;
            
            if (scan.hasNextLine()) {
                data = scan.next();
                return data.split(",");
            }
            for(String value:datas){
                
            }
            

        } catch (FileNotFoundException ex) {
           System.err.println("file not found ");
            // Logger.getLogger(SquareOFF.class.getName()).log(Level.SEVERE, null, ex);

        }
        return new String[]{};
    }
}
