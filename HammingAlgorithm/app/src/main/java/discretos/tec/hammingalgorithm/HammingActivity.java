package discretos.tec.hammingalgorithm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

import discretos.tec.hammingalgorithm.data.DataManager;

public class HammingActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamming);
        activity = this;

        TextView encodedData = (TextView)findViewById(R.id.EncodedData);
        encodedData.setText(DataManager.getInstace().encodedData);

        final Button buttonTest = findViewById(R.id.testInput);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.new_input_fragment);
                dialog.setCancelable(true);

                final EditText hexText = dialog.findViewById(R.id.HexDigit);
                Button btnTest = dialog.findViewById(R.id.testBtn);
                btnTest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("dick", "onClick: PENE");

                        String binData = DataManager.getInstace().hexToBin(hexText.getText().toString());

                        for(String string : compare(DataManager.getInstace().encodedData,
                                binData, DataManager.getInstace().parity)){
                            Log.d("tabla2", "str: "+string);
                        }

                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        final Button reset = findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityChangeIntent = new Intent(HammingActivity.this, MainActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HammingActivity.this.startActivity(activityChangeIntent);
            }
        });

        final Button button1 = (Button) findViewById(R.id.calcutationTableBtn);
        final Button button2 = (Button) findViewById(R.id.comparisonTableBtn);

        View.OnClickListener  onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                });

                dialog.setContentView(R.layout.fragment_parity_bits_calculation);
                dialog.setCancelable(true);

                TableLayout table1 = (TableLayout) dialog.findViewById(R.id.Table1);

                DataManager dataManager = DataManager.getInstace();

                List<List<String>> matrix = new Vector<>();
                if(v.getId() == R.id.comparisonTableBtn){
                    matrix = dataManager.compTableMatrix;
                }else if(v.getId() == R.id.calcutationTableBtn){
                    matrix = dataManager.calcTableMatrix;
                }

                for(List<String> vec : matrix){
                    TableRow tr = new TableRow(v.getContext());
                    tr.setBackgroundResource(R.drawable.border);
                    for(String s : vec){
                        TextView tv = new TextView(v.getContext());
                        tv.setBackgroundResource(R.drawable.border);
                        tv.setText(s);
                        tr.addView(tv);
                    }
                    table1.addView(tr);
                }

                dialog.show();
            }
        };

        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);

    }

    public native String[] compare(String str, String string,  char parity);
}
