package loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import rhino.Configuration;
import test.Helper;
import test.NSEHelper;
import test.RediffParser;

/**
 *
 * @author arsh
 */
public class NSELoader {
    ArrayList<String> names;
    public NSELoader(int max){
        try {
            if(Configuration.readFromHistory&& Configuration.debugging){
                
            }
            Scanner scan = new Scanner(new FileInputStream("C:\\sources\\EQUITY_L.csv"));
   
             
            names=new ArrayList<String>();
            int count=0;
            while(scan.hasNext()){
                if(count>max){
                    return;
                }
                String name=scan.nextLine();
                for(String nam:name.split(",")){
                    if(nam.length()==0) continue;
                    names.add(nam.trim());
                    count++;
                }
     
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NSELoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String[] getAllStocks(){
        return names.toArray(new String[1]);
    }
    public static  void main(String arg[]){
        NSELoader loader=new NSELoader(1000);//. read();
       loader.saveDay();
       // loader.read();
    }

    public void saveDay() {
        try {
            Configuration.readFromHistory=false;
            Calendar calender=Calendar.getInstance();
            String date=calender.get(Calendar.DATE)+"_"+calender.get(Calendar.MONTH)+"_"+calender.get(Calendar.YEAR);
            String path=Configuration.historyLocation+"stock_history";
            File file=new File(path);
            if(!file.isDirectory()){
                file.mkdir();
            }
            file=new File(path+"/"+date);
            file.mkdir();
            String stocks[]=this.getAllStocks();
            file=new File(Configuration.historyLocation+"stock_history/"+date+"/nse");
            NSEHelper.getNseValue(3);//dummy value to initialize
            NSEHelper nse=new NSEHelper(NSEHelper.nseValues);
            file.createNewFile();
            FileOutputStream w1 = new FileOutputStream(file);
            ObjectOutputStream str = new ObjectOutputStream(w1);
            str.writeObject(nse);
            str.flush();
            str.close();
            for(String stock:stocks){
                if(stock.length()==0) continue;
                stock=stock.trim();
                System.out.println(stock);
                RediffParser parser=new RediffParser(stock);
               Helper data=new Helper( parser.getValues()); //to init - work around
                file=new File(Configuration.historyLocation+"stock_history/"+date+"/"+stock);
                file.createNewFile();
                FileOutputStream writer=new FileOutputStream(file);
                ObjectOutputStream oss=new ObjectOutputStream(writer);
                oss.writeObject(data);
                
            }
            
            
        } catch (IOException ex) {
            
        }     
    }
    public void read(){
        FileInputStream fin = null;
        {
            ObjectInputStream ois = null;
            try {
                Calendar calender=Calendar.getInstance();
                String date=calender.get(Calendar.DATE)+"_"+calender.get(Calendar.MONTH)+"_"+calender.get(Calendar.YEAR);
                String path=Configuration.historyLocation+"stock_history/"+date;
                fin = new FileInputStream(path+"/"+"ACC");
                ois = new ObjectInputStream(fin);
                Helper parser = (Helper) ois.readObject();
                ois.close();
                System.out.println(parser.getValues().toArray(new String[1]));
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally {
                try {
                    fin.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    ois.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
    }
    
}