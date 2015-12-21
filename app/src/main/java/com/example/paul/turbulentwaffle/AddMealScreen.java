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
    private boolean refresh = false; //If refreshing the page or leaving it (false = leaving)
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor; //persistent memory
    private Spinner priorSpinner, //drop down of previous meals
                    priorServings, //number of servings
                    newServings; //same but for a new unsaved meal

    private EditText nameNewMeal, calPerServing; //when defining a new unsaved meal.

    private int newServe, calTotal = 0, priorServe; //Servings and total calories this meal.

    private String priorMealString; //name of the meal on the previous meal dropdown.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_meal_layout);
        //persistent memory initialization...
        sharedPreferences = AddMealScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        //-----------

        initializeTextViews();
        initializeSpinners();
        addItemsToUnitTypeSpinner();
        addListenerToUnitTypeSpinner();

        if(sharedPreferences.getString(getString(R.string.ITEM_LIST), "3103445").equals("3103445")){
            sharedEditor.putString(getString(R.string.ITEM_LIST),"&");
            sharedEditor.putString(getString(R.string.CAL_LIST),"&");
            sharedEditor.commit();
        } //This basically checks if there are any saved previous meals.
    } //If not then it sets the first element of the dropdown to just be a blank entry.
    public void initializeSpinners(){
        priorSpinner=(Spinner)findViewById(R.id.prior_meal_spinner_id); //name of previous meal
        priorServings=(Spinner)findViewById(R.id.servings_num_prior_spinner_id); //#of servings
        newServings=(Spinner)findViewById(R.id.servings_num_new_spinner_id); //# of servings
    }
    public void initializeTextViews(){ //initialize Edit Text Views
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
        String[] spinnerArray = spinnerString.split("&"); //Change the String in persistent memory
        //to be an array of food items which the drop down will display.

        ArrayAdapter<String> priorSpinnerAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        priorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        priorSpinner.setAdapter(priorSpinnerAdapter); //elements set here.
    }
    public void addListenerToUnitTypeSpinner() {
        priorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorMealString = parent.getItemAtPosition(position).toString();
            } //get the meal on the previous meals dropdown.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
        priorServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            } //get the number of servings of the previously saved meal.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
        newServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newServe = Integer.parseInt(parent.getItemAtPosition(position).toString());
            } //number of servings of the new unsaved meal.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*Never to be touched*/}
        });
    }
    //--------If submitting the total calories vs just entering a new item.-----
    public void onSubmitFoods(View view) {
        refresh = false;
        calcCals();
    }
    public void onNextFoods(View view) {
        refresh = true;
        calcCals();
    } //--------------------------------------------------------------------------

    public void calcCals(){
        String warning = "You must select a meal!"; //To be shown if they submit a blank field.
        if (priorMealString.isEmpty() && nameNewMeal.getText().toString().isEmpty()) {
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show(); //like right now for instance.
        } else if (!priorMealString.isEmpty()) { //if a previous item is selected.
            String currNameList = sharedPreferences.getString(getString(R.string.ITEM_LIST),"");
            String currCalorieList = sharedPreferences.getString(getString(R.string.CAL_LIST),"");
            String[] nameArray = currNameList.split("&");
            String[] calArray = currCalorieList.split("&");
            //Grab the list of previous items and the list of calories per serving of those items.
            int i,tmp=0;
            for(i=0;i<nameArray.length;i++){
                if(nameArray[i].equals(priorMealString)){
                    tmp = i;
                    break;
                }
            } //find which element of the previous items list is the current item.
            calTotal += Integer.parseInt(calArray[tmp])*priorServe; //get calories of current item
            //and multiply it by the number of servings eaten.
            if(!refresh){
                Intent goingBack = new Intent();
                goingBack.putExtra("PageName", "AddMeal");
                goingBack.putExtra("CalsEaten", Integer.toString(calTotal));
                setResult(RESULT_OK, goingBack);
                finish(); //We're done here, submit calories and return to 1st page.
            }else{
                nameNewMeal.setText("");
                calPerServing.setText("");
                initializeSpinners();
                addItemsToUnitTypeSpinner();
                addListenerToUnitTypeSpinner(); //Save total calories and refresh for next item.
            }
        } else if (!nameNewMeal.toString().isEmpty() && calPerServing.getText().toString().isEmpty()) {
            warning = "You need to state how many calories per serving.";
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
            //there's a new item however they didn't say how many cals per serving.
        } else if (
                !nameNewMeal.getText().toString().isEmpty() &&
                !calPerServing.getText().toString().isEmpty()) {
            //New item successfully created.
            calTotal += Integer.parseInt(calPerServing.getText().toString())*newServe;
            //Get total calories of the new item.
            String currNameList = sharedPreferences.getString(getString(R.string.ITEM_LIST),"");
            String currCalorieList = sharedPreferences.getString(getString(R.string.CAL_LIST),"");
            //Get saved items list.
            if(currNameList.equals("&")) currNameList+=nameNewMeal.getText().toString();
            else currNameList+="&"+nameNewMeal.getText().toString();
            if(currCalorieList.equals("&")) currCalorieList+=calPerServing.getText().toString();
            else currCalorieList+="&"+calPerServing.getText().toString();
            //Add current item to the list.
            sharedEditor.putString(getString(R.string.ITEM_LIST), currNameList);
            sharedEditor.putString(getString(R.string.CAL_LIST), currCalorieList);
            sharedEditor.commit();
            //Send it back to saved memory.

            if(!refresh){
                Intent goingBack = new Intent();
                goingBack.putExtra("PageName", "AddMeal");
                goingBack.putExtra("CalsEaten", Integer.toString(calTotal));
                setResult(RESULT_OK, goingBack);
                finish(); //We're done here, submit calories and return to 1st page.
            }else{
                nameNewMeal.setText("");
                calPerServing.setText("");
                initializeSpinners();
                addItemsToUnitTypeSpinner();
                addListenerToUnitTypeSpinner(); //Save total calories and refresh for next item.
            }
        } else {
            Toast.makeText(this, "ERROR! ERROR! ERROR!", Toast.LENGTH_SHORT).show();
            //This should never happen. If it does I did something wrong.
        }
    }
}
