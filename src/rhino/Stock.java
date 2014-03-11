/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

/**
 *
 * @author arsh
 */
public class Stock extends Thread{
    private float openPrice;
    private String bseID;
    private String nseID;
    private String scriptName;
    private String companyName;
    private String tickerURL;
    private float ucLimit;
    private float lcLimit;
    final static String bseURL="http://www.bseindia.com/stock-share-price/";
    private boolean isFullyLoaded=false;

    public Stock() {
        
    }
    private void load(){
        
    }
    public boolean isFullyLoaded(){
        return isFullyLoaded;
    }
    public void setLoadStatus(boolean value){
        isFullyLoaded=value;
    }
    public Stock(String companyName,String bseID,String nseID){
        this.companyName=companyName;
        this.bseID = bseID;
        this.nseID = nseID;
        setBSEUrl();
    }
    private void setBSEUrl(){
        tickerURL=(bseURL + getCompanyName().replaceAll(" ", "-") + "-ltd/" + getNseID()+ "/" + getBseID());
    }
   
    /**
     * @return the openPrice
     */
    public float getOpenPrice() {
        return openPrice;
    }

    /**
     * @param openPrice the openPrice to set
     */
    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    /**
     * @return the bseID
     */
    public String getBseID() {
        return bseID;
    }

    /**
     * @return the nseID
     */
    public String getNseID() {
        return nseID;
    }

    /**
     * @return the scriptName
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return the ucLimit
     */
    public float getUcLimit() {
        return ucLimit;
    }

    /**
     * @return the lcLimit
     */
    public float getLcLimit() {
        return lcLimit;
    }

    /**
     * @return the tickerURL
     */
    public String getTickerURL() {
        return tickerURL;
    }

    /**
     * @param ucLimit the ucLimit to set
     */
    public void setUcLimit(float ucLimit) {
        this.ucLimit = ucLimit;
    }

    /**
     * @param lcLimit the lcLimit to set
     */
    public void setLcLimit(float lcLimit) {
        this.lcLimit = lcLimit;
    }
}
