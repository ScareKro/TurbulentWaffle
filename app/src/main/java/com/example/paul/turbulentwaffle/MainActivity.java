package com.example.paul.turbulentwaffle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static boolean accPresent = false; /*TODO replace with perm mem*/

    String  displayName,
            birthDay,
            birthMonth,
            birthYear,
            currWeightUnit,
            heightUnit,
            goalWeightUnit;
    int age;
    double heightCM, weightKG, userBMR, goalWeightKG, remainingCals, todayCals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(accPresent){     //Check for pre-existing account
            setContentView(R.layout.activity_main);
        } else {            //Sends user to settings if there isn't one to make one.
            String toastMessage = "----Create Profile----";
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            Intent getNameScreenIntent = new Intent(this,SettingsScreen.class);
            final int result = 1;
            startActivityForResult(getNameScreenIntent, result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.activity_main);
        if(data.getStringExtra("PageName").toString().equals("Settings")) {
            accPresent = true;
            TextView usersGreetingMessage = (TextView) findViewById(R.id.hi_text);
            displayName = data.getStringExtra("DispName"); //Set the greeting message to greet them by name.
            usersGreetingMessage.setText(getString(R.string.hi) + " " + displayName + "!");

            //Grab the other values.----------------------------------------
            birthDay = data.getStringExtra("BDayDay");
            birthMonth = data.getStringExtra("BDayMonth");
            birthYear = data.getStringExtra("BDayYear");
            weightKG = Double.parseDouble(data.getStringExtra("CurrWeight"));
            currWeightUnit = data.getStringExtra("CurrWeightUnit");
            heightCM = Double.parseDouble(data.getStringExtra("Height"));
            heightUnit = data.getStringExtra("HeightUnit");
            goalWeightKG = Double.parseDouble(data.getStringExtra("GoalWeight"));
            goalWeightUnit = data.getStringExtra("GoalWeightUnit");
            //--------------------------------------------------------------
            //Get the user's current age.
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int userBirthYear = Integer.parseInt(birthYear);
            age = year - userBirthYear;
            //Calculate the users height and weight in metric.
            if (currWeightUnit.equals("lbs")) {
                weightKG /= 2.2;
            }
            if (heightUnit.equals("in")) {
                heightCM *= 2.54;
            }
            if (goalWeightUnit.equals("lbs")) {
                goalWeightKG /= 2.2;
            }
            //Calculate the calories burned at rest
            userBMR = (10 * weightKG) + (6.25 * heightCM) - (5.0 * (double) age) + 5;

            TextView RemainingCalsText = (TextView) findViewById(R.id.remaining_cal_id);
            RemainingCalsText.setText(String.valueOf((int)userBMR));
        }else if(data.getStringExtra("PageName").toString().equals("AddMeal")){
            userBMR -= Integer.parseInt(data.getStringExtra("CalsEaten"));
            TextView RemainingCalsText = (TextView) findViewById(R.id.remaining_cal_id);
            RemainingCalsText.setText(String.valueOf((int)userBMR));
        }
    }

    public void onSettingsButton(View view) {
        Intent getNameScreenIntent = new Intent(this,SettingsScreen.class);
        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
    }


    public void onAddMeal(View view) {
        Intent getNameScreenIntent = new Intent(this,AddMealScreen.class);
        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
    }
}
