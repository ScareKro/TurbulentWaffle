package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HistoryScreen extends Activity{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

    private String monthSelected;
    private Spinner historyMonthSpinner;
    private EditText historyDay, historyYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_history);
        sharedPreferences = HistoryScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();
    }

    public void initializeTextViews(){
        historyDay=(EditText)findViewById(R.id.history_date_id);
        historyYear=(EditText)findViewById(R.id.history_year_id);
    }

    public void initializeSpinners(){
        historyMonthSpinner =(Spinner)findViewById(R.id.history_month_spinner_id);
    }

    public void addItemsToUnitTypeSpinner() {
        ArrayAdapter<CharSequence> historyMonthSpinnerAdapter =    //Set the month spinner to include the months
                ArrayAdapter.createFromResource(this,
                        R.array.month_units,
                        android.R.layout.simple_spinner_item);
        historyMonthSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        historyMonthSpinner.setAdapter(historyMonthSpinnerAdapter);
    }

    public void addListenerToUnitTypeSpinner() {
        historyMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });
    }

    public void onSubmitHistory(View view) {
        boolean pass = true;
        String warning = "You have unfilled fields:\n";
        //Create warning message to be displayed if a field is left blank.

        if(historyDay.getText().toString().isEmpty()){
            pass = false;
            warning+="Birth Day\n";
        }
        if(historyYear.getText().toString().isEmpty()){
            pass = false;
            warning+="Birth Year\n";
        }
        //warning is edited to specify which fields were ignored.
        //spinners cannot be left blank as they have a default value
        //so those potential warnings were removed.
        if(!pass){
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
            //Only displays if a field was ignored.
        }
        else{}
    }
}
