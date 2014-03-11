/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketwatch;



/**
 *
 * @author mubarak
 */
public class MarketWatch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 1; i < 20; i++) {
            ListedCompanies.updateListedValue(String.valueOf(i), "gainer");
            ListedCompanies.updateListedValue(String.valueOf(i), "loser");
//            ListedCompanies.createLists(String.valueOf(i), "gainer");
//            ListedCompanies.createLists(String.valueOf(i), "loser");
        }

    }
}
