package test;

/**
 *
 * @author arsh
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import rhino.Analyser;
import rhino.Configuration;


public class RediffParser implements Cloneable{
    private String name;
    private static final String REDIFF="http://money.rediff.com/money1/chartnse_1day_new.php?companyCode=";
    DocumentBuilder db;
    public int cursor=0;
    public ArrayList<String> cursorData=new ArrayList<>();
    private ArrayList<String> stock;
    private boolean isDebugging;
    public RediffParser(String name){
        this.name=name;
        isDebugging=rhino.Configuration.debugging;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
           db = dbf.newDocumentBuilder();
         
        } catch (ParserConfigurationException ex) {
            System.err.println("loading data failed for "+name);
        }
        if(isDebugging){
            if(Analyser.stock==null){
                stock=new ArrayList<String>();
            }
            else{
                stock=Analyser.stock.get(name);
            }
        }
    }
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        RediffParser urlReader = new RediffParser("infy");
        
        System.out.println("url:  contents: \n" + urlReader.getPercentageChange());
    }
    public boolean updateCursor(){
        if(getCursorData().isEmpty()){
            cursorData=getValues();
            if(cursorData==null||cursorData.isEmpty()){
                return false;
            }
        }
        if(cursor>=cursorData.size()){
            return false;
        }
        stock.add(getCursorValue());
        cursor++;
        return true;
    }
    public boolean isMarketClosed(){
        if(cursor>=cursorData.size()){
            return true;
        }
        return false;
    }
    private Document getParser() {
        InputStream stream = null;
        Document doc = null;
        try {
            String url=REDIFF+getName();
            stream = new URL(url).openStream();
            doc = db.parse(stream);

        } catch (Exception ex) {
            System.err.println("error loading data for "+getName());
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(RediffParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return doc;
        }
    }
    private String getDocument(String url) {
        InputStream stream = null;
        Document doc = null;
         String data="";
        try {
            stream = new URL(url).openStream();
            Scanner scanner=new Scanner(stream);
           
            while(scanner.hasNext()){
                data=data+scanner.nextLine();
            }

        } catch (Exception ex) {
            System.err.println("error loading data for "+getName());
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(RediffParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return data;
        }
    }
    public ArrayList<String> getValues(){
        if(!cursorData.isEmpty()){
            return cursorData;
        }
        if(isDebugging&&Configuration.readFromHistory){
            return cursorData=read();
        }
        Document doc=getParser();
        if(doc==null) return null;
        Node header=doc.getFirstChild();//rediff graph header
        NodeList list=header.getChildNodes();
        int count=915;
        int hour=9;
        int min=15;
        String time=hour+""+min;
        ArrayList<String> values=new ArrayList<>();
        int cutOffValue=Integer.MAX_VALUE;
        if(isDebugging){
            cutOffValue=Configuration.maxDebuggValue;
        }
        for(int i=0;i<list.getLength()&&cutOffValue>i;i++){
            Element e=((Element)list.item(i));
            String value=e.getAttribute("value");
            float price=Float.parseFloat(value);
            values.add(time+":"+price);
            if(min==59){
                hour++;
                min=0;
                time=hour+"0"+min;
            }
            else{
                min++;
                if(min<10){
                    time = hour+"0"+min;
                    
                }
                else{
                    time=hour+""+min+"";
                }
           
            }
        }
       // System.out.print("last time updayed"+hour);
        cursorData=values;
        return values;
        
    }
    private String getCursorValue(){
        if(getCursor()>=getCursorData().size()){
            return getCursorData().get(getCursorData().size()-1);
        }
        return getCursorData().get(getCursor());
    }
    public String getCurrentValue(){
        if(!isDebugging){
            String url="http://money.rediff.com/money1/currentstatus.php?companycode="+getName().toUpperCase();
            String document=getDocument(url);
            document= document.replace("{", "");
            String[] data=document.split("\",\"");  
            return addTimeToValue(getLastTradedPrice(data));
        }
        else{
           String value=getCursorValue();
           updateCursor();
           return value;
        }
        
    }
    public float getPercentageChange(){
        String url="http://money.rediff.com/money1/currentstatus.php?companycode="+getName().toUpperCase().replace('_','-');
        String document=getDocument(url);
        document= document.replace("{", "");
        String change=document.split("\",\"")[6]; 
        String value=change.split("\":\"")[1];
        return  Float.parseFloat(value);
    }
    public float getOpenPrice(){
        String url="http://money.rediff.com/money1/currentstatus.php?companycode="+getName().toUpperCase().replace('_','-');
        String document=getDocument(url);
        document= document.replace("{", "");
        String change=document.split("\",\"")[11]; 
        String value=change.split("\":\"")[1].replaceAll(",", "").replaceAll("\"}", "");
        return  Float.parseFloat(value);
    }
    private String addTimeToValue(String currentPrice){
        Calendar now = Calendar.getInstance();
        int min=now.get(Calendar.MINUTE);
        String minute;
        if(min<10){
            minute="0"+min;
        }
        else{
            minute=""+min;
        }
        String formattedData=now.get(Calendar.HOUR_OF_DAY)+""+minute+":"+currentPrice;
        return formattedData;
    }
    
    private String getLastTradedPrice(String... data){
        try{
            String lastPrice=data[0].split("\":\"")[1].replaceAll(",", "");
            return lastPrice;
        }
        catch(Exception e){
            System.out.println(data[0]);
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cursor
     */
    public int getCursor() {
        return cursor;
    }
    public void setCursor(int pos){
        this.cursor=pos;
    }

    /**
     * @return the cursorData
     */
    public ArrayList<String> getCursorData() {
        return cursorData;
    }
    
     public ArrayList<String> read(){
        FileInputStream fin = null;
        {
            ObjectInputStream ois = null;
            try {
                Calendar calender=Calendar.getInstance();
                String date=Configuration.dateOnHistory;
                String path=Configuration.historyLocation+"stock_history/"+date;
                fin = new FileInputStream(path+"/"+this.name);
                ois = new ObjectInputStream(fin);
                Helper parser = (Helper) ois.readObject();
                ois.close();
                return parser.getValues();
            } catch (Exception ex) {
               // ex.printStackTrace();
            }finally {
                try {
                    if(fin!=null)
                    fin.close();
                } catch (IOException ex) {
                    //ex.printStackTrace();
                }
                try {
                    if(ois!=null)
                    ois.close();
                } catch (IOException ex) {
                  //  ex.printStackTrace();
                }
            }
        }
        return null;
        
    }
}