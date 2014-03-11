package rhino;

import analyser.BackwardFall;
import analyser.ForwardRecover;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.NSEHelper;
import test.RediffParser;

public class Process extends EquityProcessor implements Runnable {

    private boolean underProcessing = true;
    private boolean buyTriggered=false,sellTriggered=false;
    private boolean isDebugging=Configuration.debugging;
    private float triggeredPrice;
    private RediffParser parser;
    private int chances=2;
    public Process(String stockName) {
        this.name = stockName;
        priceDetails = Analyser.stock.get(name);
        totalInstance = Configuration.samplingTime;
        if(isDebugging){
           parser=new RediffParser(name); 
        }
    }

    @Override
    public void run() {
        while (underProcessing) {
            try {
                boolean status =process();
                if(isDebugging){
                    if(status==false){
                        return;
                    }
                   // Thread.sleep(200);
                }
                else{
                    Thread.sleep(200);
                }
            } catch (Exception ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, "unknown reason", ex);
            }
        }
    }
    
    public void resumeProcessing(){
        underProcessing = true;
    }
    private boolean process() {
        if(isDebugging){
            if(!parser.updateCursor()){
                return false;
            }
        }
        int priceDetialsSize = priceDetails.size();
        if (priceDetialsSize < totalInstance ||getLastInstance()<1110) //not enough data
        {
            return true;
        }
       // System.out.println("is sensex going up"+isSensexGoingUP());
      //  isSensexGoingUP();
        if (getLastInstance() > 1440) { //exchange closed
            underProcessing = false;
            //market closed at 3:30pm
            return false;
        }
        if(Configuration.debugging){
            //System.out.println(bottomTopPercentageDifference(NSEHelper.getNseValue(0), NSEHelper.getNseValue(135)));
        }
        float lastPrice = getLastPrice();
        if(lastPrice<70) return true;
        float openPrice=getOpenPrice();
        float gain = bottomTopPercentageDifference(openPrice, lastPrice);
           if(analyser.BackwardFall.isMatching(name, gain)){
            BackwardFall recover = new BackwardFall(name, lastPrice,this);
            recover.priceDetails=priceDetails;
            recover.process();
          
        }
        if(analyser.ForwardRecover.isMatching(name, gain)){
            ForwardRecover recover = new ForwardRecover(name, lastPrice,this);
            recover.priceDetails=priceDetails;
            recover.process();
      
           
        }
     
        
        if(ForwardRecover.underProcessing.contains(name)&&BackwardFall.underProcessing.contains(name)){
            if(!Configuration.debugging){
               // Analyser.stock.remove(name);
            }
            return false;
        }
        
//        float negativeChange  = bottomTopPercentageDifference(getHighest(), lastPrice);
//        float positiveChange  = bottomTopPercentageDifference(getLeast(), lastPrice);
//        
//        //this is the minimum condition
//    //     System.out.println("XX: "+getLastInstance()+" "+percentageChange);
//        
//        if ((negativeChange > Configuration.percentageForBuy)) {
////            if (isBear(percentageChange)) {
//              //  System.out.println("passing to buy handler. Buy call triggered at time " + name + " " + lastPrice +" time:"+getLastPrice()+ priceDetails.toString());
////                System.out.println("buy quantity: "+Configuration.purchaseAmount/lastPrice);
////                System.out.println("max loss"+Configuration.purchaseAmount*(Configuration.maxAllowedLoss/(float)100));
//                  // new CrashDetector(name,lastPrice,this).priceDetails=this.priceDetails;
//                   //underProcessing=false;
////
////            }
//              float diff=bottomTopPercentageDifference(getOpenPrice(), lastPrice);
////                if(diff<3){
////                   BuyHandler h=new BuyHandler(name,lastPrice,this);
////                   h.priceDetails=this.priceDetails;
////                   h.process();
//                  // underProcessing=false;
////                }
//        } 
//        if (lastPrice>100&&(positiveChange > Configuration.precentageForSell)&&bottomTopPercentageDifference(getOpenPrice(), lastPrice)>1) {
//             // if(isBull(negativeChange)){
//              System.out.println("passing to special buy handler. Buy call triggered at time "+ name);
//              SellHandler h=new SellHandler(name,lastPrice,this);
//                   h.priceDetails=this.priceDetails;
//                   h.process();
//                   return false;
////                   System.out.println("buy quantity: "+Configuration.purchaseAmount/lastPrice);
////                   System.out.println("max loss"+Configuration.purchaseAmount*(Configuration.maxAllowedLoss/(float)100));
////                float diff=bottomTopPercentageDifference(getOpenPrice(), lastPrice);
////                if(diff>1){
////                   SellHandler h=new SellHandler(name,lastPrice,this);
////                   h.priceDetails=this.priceDetails;
////                   h.process();
////                   if(!Configuration.debugging){
////                       try {
////                           Thread.sleep(15*60*1000);
////                       } catch (InterruptedException ex) {
////                           Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
////                       }
////                   }
////                  // underProcessing=false;
////                }
//                   
//                   //underProcessing=false;
//              
//               // }
//        }
        updateLastInstance();
        return true;

    }

    private void updateLastInstance() {
        lastInstanceTime = getLastInstance();

    }
    public RediffParser getDebuggingParser(){
        return parser;
    }

    public int getLastInstance() {
        int lastInstance = priceDetails.size() - 1;
        if (lastInstance < 0) {
            return 0;
        }
        String value = priceDetails.get(lastInstance);
        return Integer.parseInt(value.split(":")[0]);
    }

    private boolean isBear(float percentageChange) {
        int lastInstance = priceDetails.size() - 1;
        int sampleCount = Configuration.samplingTime;//how long should the rally be on. 
        //This is to prvent sudden price rise/drop becuase of single buy/sell order
        int count = (lastInstance + 1) - sampleCount;
        float prices[] = new float[sampleCount];
        for (int i = 0; count <= lastInstance; count++) {
            prices[i++] = getPriceForInstance(count);
        }

        //checking upward linearity
        for (int i = 0; i < Configuration.samplingTime - 1; i++) {
            //steep rise and then steep down creates a spike graph, which is not a clean rally. Hence negligibity check
            if (!negligiblyEqualorGreater(prices[i], prices[i + 1])) {
                return false;
            }
            if (percentageChange * Configuration.maxPercentageStakePerInstance < (bottomTopPercentageDifference(prices[i], prices[i + 1]))) {
                //sample instance of two consequtive instance must not be greater than 1/3rd of the total percentage change
                //sudden deflection detector
                return false;
            }
        }
        return true;
    }

    private boolean isBull(float percentageChange) {
        int lastInstance = priceDetails.size() - 1;
        int sampleCount = Configuration.samplingTime;//how long should the rally be on. 
        //This is to prvent sudden price rise/drop becuase of single buy/sell order
        int count = (lastInstance + 1) - sampleCount;
        float prices[] = new float[sampleCount];
        for (int i = 0; count <= lastInstance; count++) {
            prices[i++] = getPriceForInstance(count);
        }

        //checking upward linearity
        for (int i = 0; i < Configuration.samplingTime - 1; i++) {
            //steep rise and then steep down creates a spike graph, which is not a clean rally. Hence negligibity check
            if (!negligiblyEqualorLesser(prices[i], prices[i + 1])) {
                //return false; //think its not required
            }
            if (percentageChange * Configuration.maxPercentageStakePerInstance > -(bottomTopPercentageDifference(prices[i], prices[i + 1]))) {
                //sample instance of two consequtive instance must not be greater than 1/3rd of the total percentage change
                //sudden deflection detector
                return false;
            }
        }
        return true;
    }
    
    public float getLeast(){
        int i=priceDetails.size()-2;
        float smallest=Float.MAX_VALUE;
        for(int j=0;j<totalInstance-1;i--,j++){
           float price=getPriceForInstance(i); 
           if(price<smallest){
               smallest=price;
           }
        }
        return smallest;
    }
    private float getHighest(){
        int i=priceDetails.size()-2; //dont include current price
        float highest=Float.MIN_VALUE;
        for(int j=0;j<totalInstance-1;i--,j++){
           float price=getPriceForInstance(i); 
           if(price>highest){
               highest=price;
           }
        }
        return highest;
    }
    private void isSensexGoingUP(){
        int up=-1,down=0;
        float last=0;
        for(int i=0;i<80;i++){

            float value=NSEHelper.getNseValue(i);
            if(value>last){
                up++;
            }
            else{
                down++;
            }
            last=value;
        }
//        if(up>down){
//            return true;
//        }
//        else{
//            return false;
//        }
        System.out.print("ups "+up+"v down"+down);
    }
}
