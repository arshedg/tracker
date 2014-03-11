/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhino;

/**
 *
 * @author marshed 28,13,1
 */
public class Configuration {
    public static int samplingTime=8;
    public static float negligiblePercentage=(float) 0.5;
    public static int maxFlatTime=20;
    public static int maxPercentageDipAfterBuy=1;
    public static float percentageForBuy=(float) .5;
    public static float precentageForSell=(float) .5;
    public static float idealHoldPercentage=1;
    public static float maxAllowedLoss=-2;
    public static float maxPercentageStakePerInstance=(float) .5;
    static float maxAllowedSellLoss=2;
    public static int purchaseAmount=100000; 
    public static boolean debugging = true;
    static int assesmentTimeInMins=2;
    static int timeOut=10;
        public static String historyLocation="c:/stock/";
        public static boolean readFromHistory=true;
    public static String dateOnHistory="13_10_2013";
   public static int maxDebuggValue=100000;
}
/*
 * Cool period:
 * if the price went up/down more than the desired percentage
 * within very short span of time like 1-2 mins, then we cannot conclude it as a market rally.
 * In these cases, we must wait for another few mins say 3 mins and find how the market is moving.
 * In this example the 3 mins is called as cool period. Buy/sell must be triggered only after the cool down period.
 * Else, chances of losses are high. 
 * If the stock price raises only gradually, ie 6-10 mins. Then we should not consider cool of period.
 * 
 */