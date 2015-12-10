package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddMealScreen extends Activity{
    private Spinner priorSpinner,
                    priorServings,
                    newServings;

    private EditText nameNewMeal, calPerServing;

    private int priorServe, newServe, calPerServe;

    private String priorMealString, nameNewMealString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_meal_layout);

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();
    }
    public void initializeSpinners(){
        priorSpinner=(Spinner)findViewById(R.id.prior_meal_spinner_id); //name of previous meal
        priorServings=(Spinner)findViewById(R.id.servings_num_prior_spinner_id); //#of servings
        newServings=(Spinner)findViewById(R.id.servings_num_new_spinner_id); //# of servings
    }
    public void initializeTextViews(){
        nameNewMeal=(EditText)findViewById(R.id.new_meal_ET);
        calPerServing=(EditText)findViewById(R.id.cal_per_serving_spinner);
    }
    public void addItemsToUnitTypeSpinner(){
        ArrayAdapter<CharSequence> servingSpinnerAdapter = //Set the serving spinners to go between
                ArrayAdapter.createFromResource(this,     //the various numbers
                        R.array.servings_units,
                        android.R.layout.simple_spinner_item);
        servingSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        priorServings.setAdapter(servingSpinnerAdapter);
        newServings.setAdapter(servingSpinnerAdapter);

        ArrayAdapter<CharSequence> priorSpinnerAdapter =    //Set the prior meal spinner to
                ArrayAdapter.createFromResource(this,       //include the meals saved to mem
                        R.array.prior_temp_units,
                        android.R.layout.simple_spinner_item);
        priorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        priorSpinner.setAdapter(priorSpinnerAdapter);
    }
    public void addListenerToUnitTypeSpinner() {
        priorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorMealString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
        priorServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
        newServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*TODO... NEVER*/}
        });
    }
}
