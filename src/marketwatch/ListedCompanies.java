/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.CompanyDayHistory;
import model.CompanyValue;
import model.ListedCompany;
import org.eclipse.persistence.platform.database.SybasePlatform;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rhino.Analyser;
import loader.NSELoader;

/**
 *
 * @author mubarak
 */
public class ListedCompanies {

    private static String baseUrl = "http://www.bseindia.com/markets/equity/EQReports/mktwatchR.aspx?expandable=2&filter=loser*all%24all%24";
    private static String reportUrl = "http://www.bseindia.com/markets/Equity/EQReports/MarketWatch.aspx";
    private static String gainerUrl = "http://www.bseindia.com/markets/Equity/EQReports/MktWatchR.aspx?filter=gainer*all$all$&Page=";
    private static String loserUrl = "http://www.bseindia.com/markets/equity/EQReports/MktWatchR.aspx?filter=loser*group%24all%24b&Page=";
    private static Elements tableRows;
    @PersistenceContext
    private static EntityManagerFactory emf;
    private static EntityManager em;
    public static Map<String, ArrayList<String>> stock;

    /**
     * Do not need to use this method since it is used only for populating
     * listed companies
     */
    public static void init() {
       
        if(stock==null){
            stock=Analyser.stock;
        //for each stock, it price and the time
//      stoc
//        String[] listedCompanies = new NSELoader(20).getAllStocks();
//        
//        for (int i =0; i < listedCompanies.length; i++) {
//           
//            String stockName = listedCompanies[i];
//            float price = 0;
//            int time = 0;
//            ArrayList<String> value = new ArrayList<>();
//            String data = time + ":" + price;// ':' is used as boundary token
//            value.add(data);
//            stock.put(stockName, value);
//        }
        

        }
    }
    
    public static void onUpdate(String stockName, String _price, long _time){
       
    
     float price= Float.valueOf(_price);
     long time = _time;
     ArrayList<String> curentValuSet=stock.get(stockName);
     String data=time+":"+price;// ':' is used as boundary token
     curentValuSet.add(data);          
      
   }
    
   

    public static void createLists(String pageNo, String type) {
        try {
            emf = Persistence.createEntityManagerFactory("marketWatchPU");
            em = emf.createEntityManager();

            Document document;
            if (type.equals("gainer")) {
                document = Jsoup.connect(gainerUrl + pageNo).get();
            } else {
                document = Jsoup.connect(loserUrl + pageNo).get();
            }
//            document.getElementById("ctl00_ContentPlaceHolder1_grd1");

            Element firstGrid = document.getElementById("ctl00_ContentPlaceHolder1_grd1");

            tableRows = firstGrid.getElementsByClass("TTRow");
            tableRows.size();
            int j = 0;
            for (int i = 0; i < tableRows.size(); i++) {
                em.getTransaction().begin();
                Element tableRow = tableRows.get(i);
                String scripCode = tableRow.child(0).ownText();
                String scripName = tableRow.child(1).child(0).ownText();
                String group = tableRow.child(2).ownText();

                Query query = em.createNamedQuery("ListedCompany.findByScripId");
                query.setParameter("scripId", scripCode);
                ListedCompany newCompany;
                try {
                    newCompany = (ListedCompany) query.getResultList().get(0);
                    newCompany.setScripGroup(group);
                    newCompany.setScripId(scripCode);
                    newCompany.setScripName(scripName);
                } catch (Exception ex) {
                    newCompany = new ListedCompany(Integer.SIZE, scripCode, scripName, group);
                    System.out.print(newCompany);
                }

                System.out.print("\n");
                em.persist(newCompany);
                em.flush();

                System.out.print(scripCode + scripName);
                System.out.print("\n");
                em.getTransaction().commit();
                j = 150 + i;
            }

            Element secondGrid = document.getElementById("ctl00_ContentPlaceHolder1_grd2");
            tableRows = secondGrid.getElementsByClass("TTRow");

            for (int i = 0; i < tableRows.size(); i++) {
                em.getTransaction().begin();
                Element tableRow = tableRows.get(i);
                String scripCode = tableRow.child(0).ownText();
                String scripName = tableRow.child(1).child(0).ownText();
                System.out.print(scripName);
                System.out.print("\n");
                String group = tableRow.child(2).ownText();
                Query query = em.createNamedQuery("ListedCompany.findByScripId");
                query.setParameter("scripId", scripCode);
                ListedCompany newCompany;
                try {
                    newCompany = (ListedCompany) query.getResultList().get(0);
                    newCompany.setScripGroup(group);
                    newCompany.setScripId(scripCode);
                    newCompany.setScripName(scripName);
                } catch (Exception ex) {
                    newCompany = new ListedCompany(Integer.SIZE, scripCode, scripName, group);
                    System.out.print(newCompany);
                }
                em.persist(newCompany);
                em.flush();
                em.getTransaction().commit();
                System.out.print(scripCode + scripName);
                System.out.print("\n");
            }

        } catch (Exception ex) {
        }
    }

    public static void updateListedValue(String pageNo, String type) {
        if(stock==null)
            init(); // Inits the class
        
        try {
   
            Document responseDocument;
            if (type.equals("gainer")) {
                responseDocument = Jsoup.connect(gainerUrl + pageNo).get();
            } else {
                responseDocument = Jsoup.connect(loserUrl + pageNo).get();
            }


            Element firstGrid = responseDocument.getElementById("ctl00_ContentPlaceHolder1_grd2");
            tableRows = firstGrid.getElementsByClass("TTRow");
            System.out.print("\n");
            for (int i = 0; i < tableRows.size(); i++) {
                Element tableRow = tableRows.get(i);
                String scripCode = tableRow.child(0).ownText();
                if(stock.get(scripCode)==null) continue;
                String currentValue = tableRow.child(3).ownText();
                String openValue = tableRow.child(4).ownText();
                try {
                    // Update hashmap value
                   onUpdate(scripCode, currentValue, new Date().getTime());
                    
     

                } catch (Exception ex) {
                }
            }


            firstGrid = responseDocument.getElementById("ctl00_ContentPlaceHolder1_grd1");
            tableRows = firstGrid.getElementsByClass("TTRow");
            System.out.print("\n");
            for (int i = 0; i < tableRows.size(); i++) {
                Element tableRow = tableRows.get(i);
                String scripCode = tableRow.child(0).ownText();
                String currentValue = tableRow.child(3).ownText();
                String openValue = tableRow.child(4).ownText();
                
                try {
                    
                   
                    // Update hashmap value
                    onUpdate(null, currentValue, new Date().getTime());
                    
                    CompanyValue listedValueModel;
                    em.getTransaction().begin();
                    try {

                    } catch (Exception ex) {

                
                    }


                } catch (Exception ex) {
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ListedCompanies.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        }
    }
}
