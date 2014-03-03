/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package worker;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author marshed
 */
public class TimeOutListener extends Timer {
    private int duration;
    public TimeOutListener(TimerTask task,int duration){
        this.duration = duration;
        this.schedule(task, duration);
    }
}
