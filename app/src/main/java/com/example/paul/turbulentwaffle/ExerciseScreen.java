package com.example.paul.turbulentwaffle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Yam on 12/10/2015.
*/
public class ExerciseScreen extends Activity{
    private ArrayList<activitiesList> activityList;

    private Spinner type_Spinner,
                    activity_Spinner;
                    //MET_Spinner;

    private String  selectActivity_string,
                    typeSelected,
                    activitySelected,
                    timeSpent_string;

    private EditText timeSpent_EditText;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_layout);


        activityList = importActivityList();
        createSpinners();
        createEditTexts();
        addItemToTypeSpinner();
        addListenerToSpinners();
    }
    public void createSpinners() {
        type_Spinner=(Spinner)findViewById(R.id.type_spinner_id);
        activity_Spinner=(Spinner)findViewById(R.id.activity_spinner_id);
        activity_Spinner.setEnabled(false);
        //MET_Spinner=(Spinner)findViewById(R.id.MET_spinner_id);
    }
    public void createEditTexts() {
        timeSpent_EditText=(EditText)findViewById(R.id.timeSpent_editText_id);
    }
    public void addItemToTypeSpinner() {
        ArrayAdapter<CharSequence> type_Adapter = ArrayAdapter.createFromResource(this,
                R.array.activityTypes,
                android.R.layout.simple_spinner_item);
        type_Adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        type_Spinner.setAdapter(type_Adapter);
    }


    public void addItemToActivtySpinner(String selectedType) {
        ArrayList<String> tempList = new ArrayList<>();
        ArrayList<activitiesList> sortedList = new ArrayList<>();
        for (int x = 0; x < activityList.size(); x++) {
            if (activityList.get(x).getType().equals(selectedType)) {
                sortedList.add(activityList.get(x));
            }
        }
        for (int x = 0; x < sortedList.size(); x++) {
            tempList.add(sortedList.get(x).getActivity());
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
                addItemToActivtySpinner(typeSelected);
                activity_Spinner.setEnabled(true);
            }
            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/}
        });
        activity_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySelected = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {/*WHAT DID YOU DO?????*/}
        });



    }

    //Import the activity data from the activityData text file
    public ArrayList<activitiesList> importActivityList() {
        ArrayList<activitiesList> activityArray = new ArrayList<>();
        ArrayList<activitiesList> sortedArray = new ArrayList<>();
        Scanner grabber = null;
        String iType = null;
        String iActivity = null;
        double iMET = 0;

        try {
            grabber = new Scanner(new FileReader("activityData"));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        grabber.useDelimiter(";|\r\n");
        do {
            iType = grabber.next();
            iActivity = grabber.next();
            iMET = grabber.nextDouble();

            activitiesList temp = new activitiesList(iType, iActivity, iMET );
            activityArray.add(temp);
        } while (grabber.hasNext());


        return activityArray;
    }
    public void onCalculate() {

    }
}

class activitiesList {

    public activitiesList(String iType, String iActivity, double iMET) {
        type = iType;
        activity = iActivity;
        MET = iMET;
    }

    String type;
    String activity;
    double MET;

    public String getType() {
        return type;
    }

    public String getActivity() {
        return activity;
    }

    public double getMET() {
        return MET;
    }
}

