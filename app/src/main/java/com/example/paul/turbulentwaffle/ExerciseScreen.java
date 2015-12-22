package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Yam on 12/10/2015.
 *
 *
*/

public class ExerciseScreen extends Activity {
    private SharedPreferences sharedPreferences; //persistant Memory
    private SharedPreferences.Editor sharedEditor;
    private ArrayList<activitiesListItem>   activityList;   //array of activity items
    private activitiesListItem selectedActivity;            //the selected activity
    private Spinner type_Spinner = null,                    //type dropdown menu
                    activity_Spinner;                       //activity dropdown menu
    private String  typeSelected = null,                    //selected type
                    activitySelected;                       //selected activity
    private EditText timeSpent_EditText;                    //defines time
    private double  MET,                                    //calculation variables for onCalculate
                    weight,
                    calsBurned,
                    time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_layout);
        //initialize persistent memory
        sharedPreferences = ExerciseScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        createTypeSpinner();        //initialize the spinners
        createActivitySpinner();
        createEditTexts();
        importActivityList();       //get data from file
        addItemToTypeSpinner();
        addItemToActivitySpinner(); //create Activity menu items
        addListenerToSpinners();    //add action listeners
    }

    public void createTypeSpinner() {   //initialize type spinner
        type_Spinner=(Spinner)findViewById(R.id.type_spinner_id);
    }
    public void createActivitySpinner() {   //initialize activity spinner
        activity_Spinner=(Spinner)findViewById(R.id.activity_spinner_id);
    }
    public void createEditTexts() {     //initialize the enditTexts
        timeSpent_EditText=(EditText)findViewById(R.id.timeSpent_editText_id);
    }
    public void addItemToTypeSpinner() {    //create type menu items
        ArrayAdapter<CharSequence> typeSpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_data,
                        android.R.layout.simple_spinner_item);
        typeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        type_Spinner.setAdapter(typeSpinnerAdapter);
        typeSelected = type_Spinner.getSelectedItem().toString();

    }
    public void addItemToActivitySpinner() {    //create activity menu items
     ArrayList<String> tempList = new ArrayList<>();
     String selectedType = type_Spinner.getSelectedItem().toString();
        //gets an array of just the items with the selected type
        for (int x = 0; x < activityList.size(); x++) {
            if (activityList.get(x).getType().equalsIgnoreCase(selectedType)) {
                tempList.add(activityList.get(x).getActivity());
            }
        }
        //sets the new array as the activity menu items
                ArrayAdapter<String> activity_Adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, tempList);
            activity_Adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);

            activity_Spinner.setAdapter(activity_Adapter);
    }

    public void addListenerToSpinners() {
        type_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = parent.getItemAtPosition(position).toString();
            }   //Get the selected type

            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/}

        });

        activity_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySelected = parent.getItemAtPosition(position).toString();
            }   //Get the selected activity
            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/

        }
        });
    }
    //Import the activity data from the activityData text file
    public void importActivityList() {
        ArrayList<activitiesListItem> activityArray = new ArrayList<>();
        Scanner grabber = null;
        String iType;
        String iActivity;
        double iMET;
        System.out.println("Test");
            //makes sure the dataFile exsists
            try {
                grabber = new Scanner(getAssets().open("activitiesData"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (grabber != null) {
            grabber.useDelimiter(";|\r\n");
            do {

                iType = grabber.next();
                iActivity = grabber.next();
                iMET = grabber.nextDouble();

                activitiesListItem temp = new activitiesListItem(iType, iActivity, iMET);
                activityArray.add(temp);
            } while (grabber.hasNext()) ;
        }
        activityList = activityArray;
    }

    public void selectTypeButtonAction(View view) { //runs on Select type button and re-creates
        createActivitySpinner();                    //the createActivitySpinner to change the values
        addItemToActivitySpinner();                 //of the drop down menu
        addListenerToSpinners();
    }

    public void getFinalActivity() {       //gets the selected activity and saves it as an activityListItem object
        for(int x = 0; x < activityList.size(); x++) {
            if (activityList.get(x).getActivity().equalsIgnoreCase(activitySelected) && activityList.get(x).getType().equalsIgnoreCase(typeSelected)) {
                selectedActivity = activityList.get(x);
                Toast.makeText(this, selectedActivity.getActivity(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onCalculate(View view) {    //runs when the calculate button is pressed
        String warning = "You must enter a time. Or something like that";
        if (timeSpent_EditText.getText().toString().isEmpty()) { //throws warning if timeSpent_EditText is left empty
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        } else {
            getFinalActivity();
            MET = selectedActivity.getMET();
            System.out.println(MET);
            weight = Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_WEIGHT), 1));
            System.out.println(weight);
            time = Double.parseDouble(timeSpent_EditText.getText().toString());
            System.out.println(time);
            calsBurned = MET * weight * time;    //calculates the number of calories burned in activity
            TextView calsBurnedText = (TextView) findViewById(R.id.cals_burned_displayText_id);
            calsBurnedText.setText(String.format("%.0f", calsBurned));
        }
    }
}


