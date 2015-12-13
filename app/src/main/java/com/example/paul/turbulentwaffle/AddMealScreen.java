package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddMealScreen extends Activity{
    private boolean refresh = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private Spinner priorSpinner,
                    priorServings,
                    newServings;

    private EditText nameNewMeal, calPerServing;

    private int newServe, calTotal = 0, priorServe;

    private String priorMealString, nameNewMealString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_meal_layout);
        sharedPreferences = AddMealScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();

        if(sharedPreferences.getString(getString(R.string.ITEM_LIST), "3103445").equals("3103445")){
            sharedEditor.putString(getString(R.string.ITEM_LIST),"&");
            sharedEditor.putString(getString(R.string.CAL_LIST),"&");
            sharedEditor.commit();
        }
    }
    public void initializeSpinners(){
        priorSpinner=(Spinner)findViewById(R.id.prior_meal_spinner_id); //name of previous meal
        priorServings=(Spinner)findViewById(R.id.servings_num_prior_spinner_id); //#of servings
        newServings=(Spinner)findViewById(R.id.servings_num_new_spinner_id); //# of servings
    }
    public void initializeTextViews(){
        nameNewMeal=(EditText)findViewById(R.id.new_meal_ET);
        calPerServing=(EditText)findViewById(R.id.cal_per_serving);
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

        String spinnerString = sharedPreferences.getString(getString(R.string.ITEM_LIST), "");
        String[] spinnerArray = spinnerString.split("&");

        ArrayAdapter<String> priorSpinnerAdapter =
                new ArrayAdapter<>(this,   android.R.layout.simple_spinner_item, spinnerArray);
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
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
        priorServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
        newServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
    }
    public void onSubmitFoods(View view) {
        refresh = false;
        calcCals();
    }
    public void calcCals(){
        String warning = "You must select a meal!";
//        nameNewMealString = nameNewMeal.toString();
//        calPerServe = Integer.parseInt(calPerServing.toString());
        if (priorMealString.isEmpty() && nameNewMeal.toString().equals("")) {
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        } else if (!priorMealString.isEmpty()) {
            /*TODO*/
            //-----------temp code--------------------
            calTotal+=500;
            if(!refresh){
                Intent goingBack = new Intent();
                goingBack.putExtra("PageName", "AddMeal");
                goingBack.putExtra("CalsEaten", "500");
                setResult(RESULT_OK, goingBack);
                finish();
            }else{
                nameNewMeal.setText("");
                calPerServing.setText("");
                initializeSpinners();
                addItemsToUnitTypeSpinner();
                addListenerToUnitTypeSpinner();
            }
            //-----------------------------------------
        } else if (!nameNewMeal.toString().isEmpty() && calPerServing.getText().toString().isEmpty()) {
            warning = "You need to state how many calories per serving.";
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        } else if (!nameNewMeal.getText().toString().isEmpty() && !calPerServing.getText().toString().isEmpty()) {
            calTotal += Integer.parseInt(calPerServing.getText().toString())*newServe;

            String currNameList = sharedPreferences.getString(getString(R.string.ITEM_LIST),"");
            String currCalorieList = sharedPreferences.getString(getString(R.string.CAL_LIST),"");
            currNameList+=nameNewMeal.getText().toString()+"&";
            currCalorieList+=calPerServing.getText().toString()+"&";
            sharedEditor.putString(getString(R.string.ITEM_LIST), currNameList);
            sharedEditor.putString(getString(R.string.CAL_LIST), currCalorieList);
            sharedEditor.commit();

            if(!refresh){
                Intent goingBack = new Intent();
                goingBack.putExtra("PageName", "AddMeal");
                goingBack.putExtra("CalsEaten", Integer.toString(calTotal));
                setResult(RESULT_OK, goingBack);
                finish();
            }else{
                nameNewMeal.setText("");
                calPerServing.setText("");
                initializeSpinners();
                addItemsToUnitTypeSpinner();
                addListenerToUnitTypeSpinner();
            }
        } else {
            /*TODO*/
        }
    }

    public void onNextFoods(View view) {
        refresh = true;
        calcCals();
    }
}
