package test;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author marshed
 */
public class Helper implements Serializable{
    ArrayList<String> list;
    public Helper(ArrayList<String> list){
        this.list=list;
    }
    public ArrayList<String> getValues(){
        return list;
    }
}