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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Yam on 12/10/2015.
*/
public class ExerciseScreen extends Activity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private ArrayList<activitiesListItem> activityList ,
            sortedList;
    private ArrayList<String> menuList;
    private activitiesListItem selectedActivity;
    private Spinner type_Spinner,
            activity_Spinner;
    //MET_Spinner;
    private String selectActivity_string,
            typeSelected,
            activitySelected,
            calsBurned,
            timeSpent_string;
    private EditText timeSpent_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_layout);

        sharedPreferences = ExerciseScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        createSpinners();
        createEditTexts();
        importActivityList();
        addItemToTypeSpinner();
        addItemToActivitySpinner();
        addListenerToSpinners();
    }
    public void createSpinners() {
        type_Spinner=(Spinner)findViewById(R.id.type_spinner_id);
        activity_Spinner=(Spinner)findViewById(R.id.activity_spinner_id);
        //MET_Spinner=(Spinner)findViewById(R.id.MET_spinner_id);
    }
    public void createEditTexts() {
        timeSpent_EditText=(EditText)findViewById(R.id.timeSpent_editText_id);
    }
    public void addItemToTypeSpinner() {
        ArrayAdapter<CharSequence> typeSpinnerAdapter = //Set the serving spinners to go between
                ArrayAdapter.createFromResource(this,     //the various numbers
                        R.array.activity_data,
                        android.R.layout.simple_spinner_item);
        typeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        type_Spinner.setAdapter(typeSpinnerAdapter);

    }
    public void addItemToActivitySpinner() {
     ArrayList<String> tempList = new ArrayList<>();
        tempList.add("Please select type first");

        ArrayAdapter<String> activity_Adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tempList);
        activity_Adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        activity_Spinner.setAdapter(activity_Adapter);

    }


    public void UpdateActivitySpinner(View view) {


            ArrayList<String> tempList = new ArrayList<>();
            for (int x = 0; x < activityList.size(); x++) {
                if (activityList.get(x).getType().equals(activitySelected)) {
                    sortedList.add(activityList.get(x));
                }
            }
            for (int x = 0; x < sortedList.size(); x++) {
                tempList.add(sortedList.get(x).getActivity());
                //System.out.println(sortedList.get(x).getActivity());
            }

            ArrayAdapter<String> activity_Adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, tempList);
            activity_Adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);


           //activity_Spinner.setAdapter(activity_Adapter);
    }

    public void addListenerToSpinners() {
        type_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelected = parent.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/}

        });

        activity_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySelected = parent.getItemAtPosition(position).toString();
                UpdateActivitySpinner(view);
            }
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


    public void getFinalActivity(String selcectedActivityName) {
        for(int x = 0; x < sortedList.size(); x++) {
            if (sortedList.get(x).getActivity().equals(selcectedActivityName)) {
                selectedActivity = sortedList.get(x);
            }
        }
    }


    public void onCalculate(View view) {
        boolean passChecker = true;
        if (timeSpent_EditText.getText().toString().isEmpty()) {
            passChecker = false;
        }
        if (!passChecker) {
            Toast.makeText(this, "Please enter missing field", Toast.LENGTH_SHORT).show();
        } else {
            calsBurned = Double.toString(selectedActivity.getMET() * R.string.USER_WEIGHT *
                    Double.parseDouble(timeSpent_string));
        }
            //calsBurned = MET * weight * time
    }
}


