package com.assignment1.ashish.converter;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static RadioButton fToC;
    private static RadioButton cToF;
    private static EditText input;
    private static Button convert;
    private static TextView output;
    private static TextView history;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fToC = (RadioButton) findViewById(R.id.FtoC_radio_Id);
        cToF = (RadioButton)findViewById(R.id.CtoF_radio_Id);
        input = (EditText) findViewById(R.id.InputTextId);
        convert = (Button) findViewById(R.id.Convert_button_Id);
        output = (TextView) findViewById(R.id.OutputTextId);
        history = (TextView) findViewById(R.id.HistoryTextId);
        Log.d(TAG, "onCreate: ");
    }

    public void buttonClicked(View v) {
        if(input.getText().toString().matches("")){
            Toast.makeText(MainActivity.this,"Please enter the value upto 1 decimal.",Toast.LENGTH_SHORT).show();
            return;
        }else{
            Double value = Double.parseDouble(input.getText().toString());

            if(fToC.isChecked()){
                Double result = (value - 32.0d)/1.8d;
                output.setGravity(Gravity.CENTER);
                output.setText(String.format("%.1f",result));
                history.setMovementMethod(new ScrollingMovementMethod());
                history.setText(" F to C: " +input.getText()+ "➔" +String.format("%.1f",result)+"\n"+history.getText());
            }else{
                Double result = (value * 1.8d) + 32d;
                output.setGravity(Gravity.CENTER);
                output.setText(String.format("%.1f",result));
                history.setMovementMethod(new ScrollingMovementMethod());
                history.setText(" C to F: " +input.getText()+ "➔" +String.format("%.1f",result)+"\n"+history.getText());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(input.getText().length()!=0) {
            outState.putDouble("INPUT", Double.parseDouble(input.getText().toString()));
        }
        if(!output.getText().toString().matches("")){
            outState.putString("OUTPUT", output.getText().toString());
        }
        if(!history.getText().toString().matches("")){
            outState.putString("HISTORY", history.getText().toString());
        }
        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        history.setMovementMethod(new ScrollingMovementMethod());
        history.setText(savedInstanceState.getString("HISTORY"));
        if(input.getText().length()!=0)
            input.setText(Double.toString(savedInstanceState.getDouble("INPUT")));

        output.setGravity(Gravity.CENTER);
        output.setText(savedInstanceState.getString("OUTPUT"));
    }

}
