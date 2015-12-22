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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private ArrayList<activitiesListItem>   activityList;

    private activitiesListItem selectedActivity;
    private Spinner type_Spinner = null,
            activity_Spinner;

    private String  typeSelected = null,
                    activitySelected;

    private EditText timeSpent_EditText;
    private double  MET,
                    weight,
                    calsBurned,
                    time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_layout);

        sharedPreferences = ExerciseScreen.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        createTypeSpinner();
        createActivitySpinner();
        createEditTexts();
        importActivityList();
        addItemToTypeSpinner();
        addItemToActivitySpinner();
        addListenerToSpinners();
    }
    public void createTypeSpinner() {
        type_Spinner=(Spinner)findViewById(R.id.type_spinner_id);
    }
    public void createActivitySpinner() {
        activity_Spinner=(Spinner)findViewById(R.id.activity_spinner_id);
    }
    public void createEditTexts() {
        timeSpent_EditText=(EditText)findViewById(R.id.timeSpent_editText_id);
    }
    public void addItemToTypeSpinner() {
        ArrayAdapter<CharSequence> typeSpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_data,
                        android.R.layout.simple_spinner_item);
        typeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        type_Spinner.setAdapter(typeSpinnerAdapter);
        typeSelected = type_Spinner.getSelectedItem().toString();

    }
    public void addItemToActivitySpinner() {
     ArrayList<String> tempList = new ArrayList<>();
     String selectedType = type_Spinner.getSelectedItem().toString();

        for (int x = 0; x < activityList.size(); x++) {
            if (activityList.get(x).getType().equalsIgnoreCase(selectedType)) {
                tempList.add(activityList.get(x).getActivity());
            }
        }

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
            }

            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/}

        });

        activity_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySelected = parent.getItemAtPosition(position).toString();
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
    public void selectTypeButtonAction(View view) {
        createActivitySpinner();
        addItemToActivitySpinner();
        addListenerToSpinners();
    }

    public void getFinalActivity() {
        for(int x = 0; x < activityList.size(); x++) {
            if (activityList.get(x).getActivity().equalsIgnoreCase(activitySelected) && activityList.get(x).getType().equalsIgnoreCase(typeSelected)) {
                selectedActivity = activityList.get(x);
                Toast.makeText(this, selectedActivity.getActivity(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onCalculate(View view) {
        String warning = "You must enter a time. Or something like that";
        if (timeSpent_EditText.getText().toString().isEmpty()) {
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
        } else {
            getFinalActivity();
           MET = selectedActivity.getMET();
            System.out.println(MET);
           weight = Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_WEIGHT),1));
            System.out.println(weight);
           time = Double.parseDouble(timeSpent_EditText.getText().toString());
            System.out.println(time);
           calsBurned = MET * weight * time;
           TextView calsBurnedText = (TextView) findViewById(R.id.cals_burned_displayText_id);
           calsBurnedText.setText(String.format("%.0f", calsBurned));
      }

            //calsBurned = MET * weight * time
    }
}


