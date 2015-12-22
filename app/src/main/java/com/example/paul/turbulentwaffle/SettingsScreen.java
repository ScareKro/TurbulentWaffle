package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

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

    private ArrayAdapter<CharSequence> monthSpinnerAdapter,
            genderSpinnerAdapter,
            weightAmountSpinnerAdapter,
            weightUnitsSpinnerAdapter,
            heightSpinnerAdapter,
            activitySpinnerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = SettingsScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        setContentView(R.layout.settings_layout);

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();
        makeActList();
        if(sharedPreferences.getBoolean(getString(R.string.ACC_EXIST),false))
            setPrevUnits();
    }

    private void setPrevUnits() {
        //Gender
        String temp = sharedPreferences.getString(getString(R.string.USER_GENDER),"Male");
        int spinnerPosition = genderSpinnerAdapter.getPosition(temp);
        genderSpinner.setSelection(spinnerPosition);

        //Birth Month
        temp = sharedPreferences.getString(getString(R.string.USR_BDAY_MONTH), "January");
        spinnerPosition = monthSpinnerAdapter.getPosition(temp);
        birthDateSpinner.setSelection(spinnerPosition);

        //Weight Unit
        temp = sharedPreferences.getString(getString(R.string.USER_WEIGHT_UNIT), "lbs");
        spinnerPosition = weightUnitsSpinnerAdapter.getPosition(temp);
        weightSpinner.setSelection(spinnerPosition);

        //Height Unit
        temp = sharedPreferences.getString(getString(R.string.USER_HEIGHT_UNIT), "in");
        spinnerPosition = heightSpinnerAdapter.getPosition(temp);
        heightSpinner.setSelection(spinnerPosition);

        //Goal weight amount
        temp = sharedPreferences.getString(getString(R.string.USER_GOAL_WEIGHT), "0");
        spinnerPosition = weightAmountSpinnerAdapter.getPosition(temp);
        goalWeightAmountSpinner.setSelection(spinnerPosition);

        //Goal weight unit
        temp = sharedPreferences.getString(getString(R.string.USER_GOAL_WEIGHT_UNIT), "lbs");
        spinnerPosition = weightUnitsSpinnerAdapter.getPosition(temp);
        goalWeightUnitsSpinner.setSelection(spinnerPosition);

        //Activity amount
        temp = sharedPreferences.getString(getString(R.string.ACTIVITY_SETTING_LEVEL),
                "Sedentary - little or no exercise");
        spinnerPosition = activitySpinnerAdapter.getPosition(temp);
        activityAmountSpinner.setSelection(spinnerPosition);

        //Display Name
        temp = sharedPreferences.getString(getString(R.string.USER_SETTINGS_NAME), "bob");
        EditText editText = (EditText)findViewById(R.id.disp_name_id);
        editText.setText(temp, TextView.BufferType.EDITABLE);

        //Birth Day
        temp = sharedPreferences.getString(getString(R.string.USR_BDAY_DAY),"1");
        editText = (EditText)findViewById(R.id.birth_date_id);
        editText.setText(temp, TextView.BufferType.EDITABLE);

        //Birth Year
        temp = sharedPreferences.getString(getString(R.string.USR_BDAY_YEAR),"2000");
        editText = (EditText)findViewById(R.id.birth_year_id);
        editText.setText(temp, TextView.BufferType.EDITABLE);

        //Weight Num
        temp = sharedPreferences.getString(getString(R.string.USER_SETTINGS_WEIGHT),"150");
        editText = (EditText)findViewById(R.id.weight_number_id);
        editText.setText(temp, TextView.BufferType.EDITABLE);

        //Height Num
        temp = sharedPreferences.getString(getString(R.string.USER_SETTINGS_HEIGHT),"60");
        editText = (EditText)findViewById(R.id.height_number_id);
        editText.setText(temp, TextView.BufferType.EDITABLE);
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

    private void makeActList() {
        for (int i = 0; i < (getResources().getStringArray(R.array.activity_levels).length); i++) {
            actLvlMap.put(getResources().getStringArray(R.array.activity_levels)[i],
                    getResources().getStringArray(R.array.actlvl_constants)[i]);
        }
    }

    public void addItemsToUnitTypeSpinner(){
        monthSpinnerAdapter =    //Set the month spinner to include the months
                ArrayAdapter.createFromResource(this,
                        R.array.month_units,
                        android.R.layout.simple_spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        birthDateSpinner.setAdapter(monthSpinnerAdapter);

        genderSpinnerAdapter = //Set gender spinner to include male and female
                ArrayAdapter.createFromResource(this,
                        R.array.gender_values,
                        android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderSpinnerAdapter);

        weightAmountSpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.weight_amounts,
                        android.R.layout.simple_spinner_item);
        weightAmountSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        goalWeightAmountSpinner.setAdapter(weightAmountSpinnerAdapter);

        weightUnitsSpinnerAdapter = //Set the weight spinners to go between
                ArrayAdapter.createFromResource(this,     //lbs and kg
                        R.array.weight_units,
                        android.R.layout.simple_spinner_item);
        weightUnitsSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        weightSpinner.setAdapter(weightUnitsSpinnerAdapter);
        goalWeightUnitsSpinner.setAdapter(weightUnitsSpinnerAdapter);

        heightSpinnerAdapter =   //allow height to choose between
                ArrayAdapter.createFromResource(this,       //ft and m
                        R.array.height_units,
                        android.R.layout.simple_spinner_item);
        heightSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        heightSpinner.setAdapter(heightSpinnerAdapter);

        activitySpinnerAdapter =
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
            sharedEditor.putBoolean(getString(R.string.ACC_EXIST), true);
            Intent goingBack = new Intent();
            //Prepares to go to Main page if all fields filled in.
            actLvlConstant = actLvlMap.get(activityAmountSelected);
            goingBack.putExtra("PageName", "Settings");
            goingBack.putExtra("DispName", dispName.getText().toString());
            sharedEditor.putString(getString(R.string.USER_SETTINGS_NAME), dispName.getText().toString());
            goingBack.putExtra("BDayDay", bDayDay.getText().toString());
            sharedEditor.putString(getString(R.string.USR_BDAY_DAY), bDayDay.getText().toString());
            goingBack.putExtra("BDayMonth", monthSelected);
            sharedEditor.putString(getString(R.string.USR_BDAY_MONTH), monthSelected);
            goingBack.putExtra("BDayYear", bDayYear.getText().toString());
            sharedEditor.putString(getString(R.string.USR_BDAY_YEAR), bDayYear.getText().toString());
            goingBack.putExtra("Gender", genderSelected);
            sharedEditor.putString(getString(R.string.USER_GENDER), genderSelected);
            goingBack.putExtra("CurrWeight", weightText.getText().toString());
            sharedEditor.putString(getString(R.string.USER_SETTINGS_WEIGHT), weightText.getText().toString());
            goingBack.putExtra("CurrWeightUnit", currWeightUnitSelected);
            sharedEditor.putString(getString(R.string.USER_WEIGHT_UNIT), currWeightUnitSelected);
            goingBack.putExtra("Height", heightText.getText().toString());
            sharedEditor.putString(getString(R.string.USER_SETTINGS_HEIGHT), heightText.getText().toString());
            goingBack.putExtra("HeightUnit", heightUnitSelected);
            sharedEditor.putString(getString(R.string.USER_HEIGHT_UNIT), heightUnitSelected);
            goingBack.putExtra("GoalWeightAmount", goalWeightAmountSelected);
            sharedEditor.putString(getString(R.string.USER_GOAL_WEIGHT), goalWeightAmountSelected);
            goingBack.putExtra("GoalWeightUnit", goalWeightUnitSelected);
            sharedEditor.putString(getString(R.string.USER_GOAL_WEIGHT_UNIT), goalWeightUnitSelected);
            goingBack.putExtra("ActivityLevel", activityAmountSelected);
            sharedEditor.putString(getString(R.string.ACTIVITY_SETTING_LEVEL), activityAmountSelected);
            goingBack.putExtra("ActivityLvlConstant", actLvlConstant);
            //Saves all fields to be returned to main page.
            if(!sharedEditor.commit()){
                String toastMessage = "Error saving settings!";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK, goingBack);
            finish(); //done.
        }
    }
}
