package discretos.tec.hammingalgorithm;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import discretos.tec.hammingalgorithm.data.DataManager;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imeManager.showInputMethodPicker();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText hexDigit0 = (EditText) findViewById (R.id.HexDigit0);

        final Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[] { "Even", "Odd"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.textview, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        final Button button = (Button) findViewById(R.id.EncodeButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(dynamicSpinner.getSelectedItem().toString().contains("Even")){
                    DataManager.getInstace().parity = '0';
                }else if(dynamicSpinner.getSelectedItem().toString().contains("Odd")){
                    DataManager.getInstace().parity = '1';
                }

                String binData = hexToBin(hexDigit0.getText().toString());
                Log.d("BinData", "data: "+binData);
                String hammingData = "";
                List<String> data = new Vector<>();
                data.add("RawInput");
                int i = 1;
                for(char c : binData.toCharArray()){
                    while(i > 0 && ((i & (i - 1)) == 0)){
                        data.add(" ");
                        hammingData += " ";
                        i++;
                    }
                    data.add("" + c);
                    hammingData += ""+c;
                    i++;
                }
                DataManager.getInstace().calcTableMatrix.add(data);

                Log.d("anus", "hamingData: "+hammingData);
                for(String string : encode(hammingData, DataManager.getInstace().parity)){
                    Log.d("anus", "Cavity: "+string);
                    List<String> encodedData = new Vector<>();
                    encodedData.add(string.substring(0,2));
                    for(char c : string.substring(2).toCharArray()){
                        encodedData.add(""+c);
                    }
                    DataManager.getInstace().calcTableMatrix.add(encodedData);
                    DataManager.getInstace().encodedData = string.substring(2);
                }

                Intent activityChangeIntent = new Intent(MainActivity.this, HammingActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });

        /*
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());*/
    }

    private String hexToBin(String input){
        char[] hex =  {'A','B','C','D','E','F'};
        String result = "";
        for(char c : input.toCharArray()){
            for(int i=0 ; i<hex.length ; i++){
                if(c == hex[i]){
                    result += Integer.toBinaryString(i+10);
                    Log.d("BinData", "hexToBin: " + hex[i]+ " - "+result);
                }
            }
            if(c < 10){
                result += Integer.toBinaryString(c);
                Log.d("BinData", "int: "+ c + " - "+result);
            }
        }
        return result;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String[] encode(String input, char parity);
}
