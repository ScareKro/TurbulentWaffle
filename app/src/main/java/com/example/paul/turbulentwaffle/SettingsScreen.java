package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;

/**
 * Created by Paul on 12/9/2015.
 */
public class SettingsScreen extends Activity{
    private Spinner birthDateSpinner,
            weightSpinner,
            heightSpinner,
            goalWeightSpinner;

    private EditText dispName,
            bDayDay,
            bDayYear,
            weightText,
            heightText,
            goalText;

    private String monthSelected,
            currWeightUnitSelected,
            heightUnitSelected,
            goalWeightUnitSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();
    }

    public void initializeSpinners(){
        birthDateSpinner=(Spinner)findViewById(R.id.month_date_spinner_id); //birth month
        weightSpinner=(Spinner)findViewById(R.id.weight_unit_spinner_id); //kg or lbs
        heightSpinner=(Spinner)findViewById(R.id.height_unit_spinner_id); //ft or m
        goalWeightSpinner=(Spinner)findViewById(R.id.goal_weight_unit_spinner_id); //kg or lbs
    }

    public void initializeTextViews(){
        dispName=(EditText)findViewById(R.id.disp_name_id); //Display name
        bDayDay=(EditText)findViewById(R.id.birth_date_id); //The day of their birth
        bDayYear=(EditText)findViewById(R.id.birth_year_id); //The year of their birth
        weightText=(EditText)findViewById(R.id.weight_number_id); //starting weight
        heightText=(EditText)findViewById(R.id.height_number_id); //height
        goalText=(EditText)findViewById(R.id.goal_weight_number_id); //goal weight
    }

    public void addItemsToUnitTypeSpinner(){
        ArrayAdapter<CharSequence> weightSpinnerAdapter = //Set the weight spinners to go between
                ArrayAdapter.createFromResource(this,     //lbs and kg
                        R.array.weight_units,
                        android.R.layout.simple_spinner_item);
        weightSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        weightSpinner.setAdapter(weightSpinnerAdapter);
        goalWeightSpinner.setAdapter(weightSpinnerAdapter);

        ArrayAdapter<CharSequence> monthSpinnerAdapter =    //Set the month spinner to include the months
                ArrayAdapter.createFromResource(this,
                        R.array.month_units,
                        android.R.layout.simple_spinner_item);
        weightSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        birthDateSpinner.setAdapter(monthSpinnerAdapter);

        ArrayAdapter<CharSequence> heightSpinnerAdapter =   //allow height to choose between
                ArrayAdapter.createFromResource(this,       //ft and m
                        R.array.height_units,
                        android.R.layout.simple_spinner_item);
        weightSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        heightSpinner.setAdapter(heightSpinnerAdapter);
    }

    public void addListenerToUnitTypeSpinner() {
        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currWeightUnitSelected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO*/}
        });
        goalWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goalWeightUnitSelected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO*/}
        });
        birthDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO*/}
        });
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightUnitSelected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO*/}
        });
    }
}
