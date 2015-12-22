package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Authors: Kron
 *
 * Program creates a screen from which a user can enter their name, birthdate, weight, height,
 * activity level, and weight goal. This constitutes creating their initial profile which is used
 * to calculate their caloric allowance for the day.
 * Automatically adjusts all values entered to metric units.
 */
public class SettingsScreen extends Activity{
    private Spinner birthDateSpinner,
            genderSpinner,
            weightSpinner,
            heightSpinner,
            goalWeightAmountSpinner,
            goalWeightUnitsSpinner,
            activityAmountSpinner;

    private EditText dispName,
            bDayDay,
            bDayYear,
            weightText,
            heightText;

    private String monthSelected,
            genderSelected,
            currWeightUnitSelected,
            heightUnitSelected,
            goalWeightAmountSelected,
            goalWeightUnitSelected,
            activityAmountSelected,
            actLvlConstant;

    Map<String, String> actLvlMap = new LinkedHashMap<>();

    private void makeActList() {
        for (int i = 0; i < (getResources().getStringArray(R.array.activity_levels).length); i++) {
            actLvlMap.put(getResources().getStringArray(R.array.activity_levels)[i],
                    getResources().getStringArray(R.array.actlvl_constants)[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();
        makeActList();
    }

    public void initializeSpinners(){
        birthDateSpinner=(Spinner)findViewById(R.id.month_date_spinner_id); //birth month
        genderSpinner=(Spinner)findViewById(R.id.gender_spinner_id); //male or female
        weightSpinner=(Spinner)findViewById(R.id.weight_unit_spinner_id); //kg or lbs
        heightSpinner=(Spinner)findViewById(R.id.height_unit_spinner_id); //ft or m
        goalWeightAmountSpinner=(Spinner)findViewById(R.id.goal_weight_amount_spinner_id); //-2:2:0.5
        goalWeightUnitsSpinner=(Spinner)findViewById(R.id.goal_weight_unit_spinner_id); //kg or lbs
        activityAmountSpinner =(Spinner)findViewById(R.id.activity_amount_spinner_id); //general level of daily activity
    }

    public void initializeTextViews(){
        dispName=(EditText)findViewById(R.id.disp_name_id); //Display name
        bDayDay=(EditText)findViewById(R.id.birth_date_id); //The day of their birth
        bDayYear=(EditText)findViewById(R.id.birth_year_id); //The year of their birth
        weightText=(EditText)findViewById(R.id.weight_number_id); //starting weight
        heightText=(EditText)findViewById(R.id.height_number_id); //height
    }

    public void addItemsToUnitTypeSpinner(){
        ArrayAdapter<CharSequence> monthSpinnerAdapter =    //Set the month spinner to include the months
                ArrayAdapter.createFromResource(this,
                        R.array.month_units,
                        android.R.layout.simple_spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        birthDateSpinner.setAdapter(monthSpinnerAdapter);

        ArrayAdapter<CharSequence> genderSpinnerAdapter = //Set gender spinner to include male and female
                ArrayAdapter.createFromResource(this,
                        R.array.gender_values,
                        android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderSpinnerAdapter);

        ArrayAdapter<CharSequence> weightAmountSpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.weight_amounts,
                        android.R.layout.simple_spinner_item);
        weightAmountSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        goalWeightAmountSpinner.setAdapter(weightAmountSpinnerAdapter);

        ArrayAdapter<CharSequence> weightUnitsSpinnerAdapter = //Set the weight spinners to go between
                ArrayAdapter.createFromResource(this,     //lbs and kg
                        R.array.weight_units,
                        android.R.layout.simple_spinner_item);
        weightUnitsSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        weightSpinner.setAdapter(weightUnitsSpinnerAdapter);
        goalWeightUnitsSpinner.setAdapter(weightUnitsSpinnerAdapter);

        ArrayAdapter<CharSequence> heightSpinnerAdapter =   //allow height to choose between
                ArrayAdapter.createFromResource(this,       //ft and m
                        R.array.height_units,
                        android.R.layout.simple_spinner_item);
        heightSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        heightSpinner.setAdapter(heightSpinnerAdapter);

        ArrayAdapter<CharSequence> activitySpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_levels,
                        android.R.layout.simple_spinner_item);
        activitySpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        activityAmountSpinner.setAdapter(activitySpinnerAdapter);
    }

    public void addListenerToUnitTypeSpinner() {
        birthDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currWeightUnitSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightUnitSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        goalWeightAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goalWeightAmountSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        goalWeightUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goalWeightUnitSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });

        activityAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activityAmountSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*If this happened, IT'S A TRAP!*/}
        });
    }

    public void onSubmitProfile(View view){
        boolean pass = true;
        String warning = "You have unfilled fields:\n";
        //Create warning message to be displayed if a field is left blank.
        if(dispName.getText().toString().isEmpty()){
            pass = false;
            warning+="Display Name\n";
        }
        if(bDayDay.getText().toString().isEmpty()){
            pass = false;
            warning+="Birth Day\n";
        }
        if(bDayYear.getText().toString().isEmpty()){
            pass = false;
            warning+="Birth Year\n";
        }
        if(weightText.getText().toString().isEmpty()){
            pass = false;
            warning+="Current Weight\n";
        }
        if(heightText.getText().toString().isEmpty()){
            pass = false;
            warning+="Height\n";
        }
        if(goalWeightAmountSelected.isEmpty()){
            pass = false;
            warning+="Weekly Weight Goal";
        } //warning is edited to specify which fields were ignored.
        //spinners cannot be left blank as they have a default value
        //so those potential warnings were removed.
        if(!pass){
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
            //Only displays if a field was ignored.
        }
        else{
            Intent goingBack = new Intent();
            //Prepares to go to Main page if all fields filled in.
            actLvlConstant = actLvlMap.get(activityAmountSelected);
            goingBack.putExtra("PageName","Settings");
            goingBack.putExtra("DispName",dispName.getText().toString());
            goingBack.putExtra("BDayDay",bDayDay.getText().toString());
            goingBack.putExtra("BDayMonth",monthSelected);
            goingBack.putExtra("BDayYear",bDayYear.getText().toString());
            goingBack.putExtra("Gender",genderSelected);
            goingBack.putExtra("CurrWeight",weightText.getText().toString());
            goingBack.putExtra("CurrWeightUnit", currWeightUnitSelected);
            goingBack.putExtra("Height",heightText.getText().toString());
            goingBack.putExtra("HeightUnit", heightUnitSelected);
            goingBack.putExtra("GoalWeightAmount", goalWeightAmountSelected);
            goingBack.putExtra("GoalWeightUnit", goalWeightUnitSelected);
            goingBack.putExtra("ActivityLevel", activityAmountSelected);
            goingBack.putExtra("ActivityLvlConstant", actLvlConstant);
            //Saves all fields to be returned to main page.
            setResult(RESULT_OK, goingBack);
            finish(); //done.
        }
    }
}
