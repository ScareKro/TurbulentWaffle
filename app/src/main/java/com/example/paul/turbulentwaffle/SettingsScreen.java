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
        monthSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        birthDateSpinner.setAdapter(monthSpinnerAdapter);

        ArrayAdapter<CharSequence> heightSpinnerAdapter =   //allow height to choose between
                ArrayAdapter.createFromResource(this,       //ft and m
                        R.array.height_units,
                        android.R.layout.simple_spinner_item);
        heightSpinnerAdapter.setDropDownViewResource(
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
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
        goalWeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goalWeightUnitSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
        birthDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                heightUnitSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
    }
    public void onSubmitProfile(View view){
        int pass = 1;
        String warning = "You have unfilled fields:\n";
        if(dispName.getText().toString().isEmpty()){
            pass = 0;
            warning+="Display Name\n";
        }
        if(bDayDay.getText().toString().isEmpty()){
            pass = 0;
            warning+="Birth Day\n";
        }
        if(monthSelected.isEmpty()){
            pass = 0;
            warning+="Birth Month\n";
        }
        if(bDayYear.getText().toString().isEmpty()){
            pass = 0;
            warning+="Birth Year\n";
        }
        if(weightText.getText().toString().isEmpty()){
            pass = 0;
            warning+="Current Weight\n";
        }
        if(currWeightUnitSelected.isEmpty()){
            pass = 0;
            warning+="Units for Current Weight\n";
        }
        if(heightText.getText().toString().isEmpty()){
            pass = 0;
            warning+="Height\n";
        }
        if(heightUnitSelected.isEmpty()){
            pass = 0;
            warning+="Height Units\n";
        }
        if(goalText.getText().toString().isEmpty()){
            pass = 0;
            warning+="Goal Weight\n";
        }
        if(goalWeightUnitSelected.isEmpty()){
            pass = 0;
            warning+="Goal Weight Units\n";
        }
        if(pass==0){
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        }else{
            Intent goingBack = new Intent();

            goingBack.putExtra("PageName","Settings");
            goingBack.putExtra("DispName",dispName.getText().toString());
            goingBack.putExtra("BDayDay",bDayDay.getText().toString());
            goingBack.putExtra("BDayMonth",monthSelected);
            goingBack.putExtra("BDayYear",bDayYear.getText().toString());
            goingBack.putExtra("CurrWeight",weightText.getText().toString());
            goingBack.putExtra("CurrWeightUnit", currWeightUnitSelected);
            goingBack.putExtra("Height",heightText.getText().toString());
            goingBack.putExtra("HeightUnit", heightUnitSelected);
            goingBack.putExtra("GoalWeight",goalText.getText().toString());
            goingBack.putExtra("GoalWeightUnit", goalWeightUnitSelected);

            setResult(RESULT_OK,goingBack);
            finish();
        }
    }
}
