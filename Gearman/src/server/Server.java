/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gearman.Gearman;
import org.gearman.GearmanServer;

/**
 *
 * @author arsh
 */
public class Server {
    public static final int  PORT=4567;
    public static final String HOST="localhost";
    public static void main(String arg[]){
        Gearman gearman = Gearman.createGearman();
        try {
            GearmanServer server= gearman.startGearmanServer(PORT);
        } catch (IOException ex) {
            gearman.shutdown();
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
