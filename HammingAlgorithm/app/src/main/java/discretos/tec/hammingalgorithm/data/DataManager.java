package discretos.tec.hammingalgorithm.data;

import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

/**
 * Created by josea on 18/3/2018.
 */

public class DataManager {

    static DataManager instace = null;

    public List<List<String>> calcTableMatrix = new Vector<>();
    public List<List<String>> compTableMatrix = new Vector();

    public char parity;

    DataManager(){
        this.calcTableMatrix.add(new Vector<String>());
        this.calcTableMatrix.get(0).add("");
        int j = 1;
        int p = 1;
        for(int i=1 ; i < 18 ; i++){
            if(i > 0 && ((i & (i - 1)) == 0)){
                this.calcTableMatrix.get(0).add("P"+p++);
            } else{
                this.calcTableMatrix.get(0).add("D"+j++);
            }
        }

        this.compTableMatrix.add(new Vector<String>());
        this.compTableMatrix.get(0).add("");
        for(int i=1 ; i <= 18 ; i++){
            if(i > 0 && ((i & (i - 1)) == 0)){
                this.compTableMatrix.get(0).add("P"+i);
            } else{
                this.compTableMatrix.get(0).add("D"+i);
            }
        }
        this.compTableMatrix.get(0).add("ParityTest");
        this.compTableMatrix.get(0).add("ParityBit");
    }

    public static DataManager getInstace(){
        if(instace == null) instace = new DataManager();
        return instace;
    }


}
