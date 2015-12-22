package com.example.paul.turbulentwaffle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Authors: Kron
 *
 * The main page, sends the user to the settings page to create their account if there is no
 * pre-existing account. Waits for user to return from any other screen and takes in any values
 * changed while over there. Defines the actions of all the buttons pressed and what page it sends
 * you to.
 */

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

    String  currWeightUnit,
            heightUnit,
            goalWeightUnit;
    double heightCM, weightKG, userBMR, goalWeight, actLvlConstant, calorieChange;
    final int caloriesPerPound = 500;
    final double kilosPerPound = 0.45359237;
    int todayCals = 0;
    DateFormat dateFormat;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = MainActivity.this.getSharedPreferences(
                getString(R.string.PREF_FILE), MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        //----check if the date has changed--------------
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();
        if(!sharedPreferences.getString(getString(R.string.TODAY_DATE),"cat").equals(
                dateFormat.format(date))){
            sharedEditor.putString(getString(R.string.TODAY_DATE), dateFormat.format(date));
            sharedEditor.putInt(getString(R.string.TODAY_CAL),0);
            if(!sharedEditor.commit()){
                String toastMessage = "Error setting today's calories to 0";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
        //-----------------------------------------------

        if(sharedPreferences.getBoolean(getString(R.string.ACC_EXIST), false)){
        //Check for pre-existing account
            setContentView(R.layout.activity_main);

            String[] TipArray = getResources().getStringArray(R.array.healthtip);
            int randNum = (int)(Math.random()*9); //create random health tip
            ((TextView)findViewById(R.id.TipText)).setText(TipArray[randNum]);

            TextView usersGreetingMessage = (TextView) findViewById(R.id.hi_text);
            usersGreetingMessage.setText(getString(R.string.hi) + " " +
                    sharedPreferences.getString(getString(R.string.USER_NAME), "") + "!");
            //Set remaining calories.
            calcBMR();
            userBMR = Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_BMR),1));
            TextView RemainingCalsText = (TextView) findViewById(R.id.remaining_cal_id);
            todayCals=sharedPreferences.getInt(getString(R.string.TODAY_CAL), 0);
            RemainingCalsText.setText(String.valueOf((int)(userBMR-todayCals)));
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
        String[] TipArray = getResources().getStringArray(R.array.healthtip);
        int randNum = (int)(Math.random()*9);
        ((TextView)findViewById(R.id.TipText)).setText(TipArray[randNum]);

        //----check if the date has changed--------------
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();
        if(!sharedPreferences.getString(getString(R.string.TODAY_DATE),"cat").equals(
                dateFormat.format(date))){
            sharedEditor.putString(getString(R.string.TODAY_DATE), dateFormat.format(date));
            sharedEditor.putInt(getString(R.string.TODAY_CAL),0);
            if(!sharedEditor.commit()){
                String toastMessage = "Error setting today's calories to 0";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
        //-----------------------------------------------

        if(data.getStringExtra("PageName").equals("Settings")) {
            //set an account as existing from now on.
            sharedEditor.putBoolean(getString(R.string.ACC_EXIST), true);
            //save the user's display name.
            sharedEditor.putString(getString(R.string.USER_NAME),data.getStringExtra("DispName"));

            //Grab the misc values.----------------------------------------
            weightKG = Double.parseDouble(data.getStringExtra("CurrWeight"));
            currWeightUnit = data.getStringExtra("CurrWeightUnit");
            heightCM = Double.parseDouble(data.getStringExtra("Height"));
            heightUnit = data.getStringExtra("HeightUnit");
            goalWeight = Double.parseDouble(data.getStringExtra("GoalWeightAmount"));
            goalWeightUnit = data.getStringExtra("GoalWeightUnit");
            actLvlConstant = Double.parseDouble(data.getStringExtra("ActivityLvlConstant"));
            //--------------------------------------------------------------

            //Save the User's birthday.
            String retToDate = data.getStringExtra("BDayMonth")+" "+
                    data.getStringExtra("BDayDay")+" "+
                    data.getStringExtra("BDayYear");
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd yyyy");
            Date date=null;
            try {
                date = formatter.parse(retToDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            sharedEditor.putLong(getString(R.string.USER_BDAY), date.getTime());

            //Save the User's gender
            //Determine gender constant for BMR formula
            sharedEditor.putString(getString(R.string.USER_GENDER), data.getStringExtra("Gender"));
            if(data.getStringExtra("Gender").equals("Male")){
                sharedEditor.putInt(getString(R.string.GENDER_CONST),5);
            }
            else {
                sharedEditor.putInt(getString(R.string.GENDER_CONST),-161);
            }

            //Calculate the users height and weight in metric.
            if (currWeightUnit.equals("lbs")) {
                weightKG *= kilosPerPound;
            }
            sharedEditor.putLong(getString(R.string.USER_WEIGHT),
                    Double.doubleToLongBits(weightKG));

            if (heightUnit.equals("in")) {
                heightCM *= 2.54;
            }
            sharedEditor.putLong(getString(R.string.USER_HEIGHT),
                    Double.doubleToLongBits(heightCM));

            if(goalWeightUnit.equals("kg")) {
                calorieChange = caloriesPerPound / kilosPerPound;
            }
            else {
                calorieChange = 500;
            }
            sharedEditor.putLong(getString(R.string.USER_CALORIE_CHANGE),
                    Double.doubleToLongBits(calorieChange));
            sharedEditor.putLong(getString(R.string.GOAL_WEIGHT),
                    Double.doubleToLongBits(goalWeight));

            sharedEditor.putString(getString(R.string.ACTIVITY_LEVEL),
                    data.getStringExtra("Activity Level"));

            sharedEditor.putLong(getString(R.string.ACT_LVL_CONSTANT),
                    Double.doubleToLongBits(actLvlConstant));

            if(!sharedEditor.commit()){
                String toastMessage = "Error saving settings!";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }else if(data.getStringExtra("PageName").equals("AddMeal")){
            todayCals=sharedPreferences.getInt(getString(R.string.TODAY_CAL), 0);
            todayCals+=Integer.parseInt(data.getStringExtra("CalsEaten"));
            sharedEditor.putInt(getString(R.string.TODAY_CAL),
                    todayCals);
            if(!sharedEditor.commit()){
                String toastMessage = "Error saving today calories!";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }else if(data.getStringExtra("PageName").equals("Exercise")){
            todayCals=sharedPreferences.getInt(getString(R.string.TODAY_CAL), 0);
            todayCals-=Integer.parseInt(data.getStringExtra("CalBurn"));
            sharedEditor.putInt(getString(R.string.TODAY_CAL),
                    todayCals);
            if(!sharedEditor.commit()){
                String toastMessage = "Error saving today calories!";
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
        //Set the greeting message with display name.
        TextView usersGreetingMessage = (TextView) findViewById(R.id.hi_text);
        usersGreetingMessage.setText(getString(R.string.hi)+" "+
                sharedPreferences.getString(getString(R.string.USER_NAME),"")+"!");
        //Set remaining calories.
        calcBMR();
        userBMR = Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_BMR), 1));
        TextView RemainingCalsText = (TextView) findViewById(R.id.remaining_cal_id);
        todayCals=sharedPreferences.getInt(getString(R.string.TODAY_CAL), 0);
        RemainingCalsText.setText(String.valueOf((int)(userBMR-todayCals)));
    }

    public void calcBMR(){ //Calculate the daily calories required to maintain a regular weight
        //Calculate the age of the user.
        Calendar tempCalendar = Calendar.getInstance();
        Date bDay = new Date(sharedPreferences.getLong(getString(R.string.USER_BDAY),1));
        Calendar bdayCalendar = Calendar.getInstance();
        bdayCalendar.setTime(bDay);
        int age = tempCalendar.get(Calendar.YEAR) - bdayCalendar.get(Calendar.YEAR);
        if (bdayCalendar.get(Calendar.MONTH) > tempCalendar.get(Calendar.MONTH) ||
                (
                        tempCalendar.get(Calendar.MONTH) == bdayCalendar.get(Calendar.MONTH) &&
                        tempCalendar.get(Calendar.DATE) > bdayCalendar.get(Calendar.DATE))
                ){
            age--;
        }
        //Calculate and save the BMR
        sharedEditor.putLong(
                getString(R.string.USER_BMR),
                Double.doubleToLongBits((
                                (10*Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_WEIGHT),1)))+
                                (6.25*Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_HEIGHT),1)))+
                                (5.0*(double)age)+
                                (sharedPreferences.getInt(getString(R.string.GENDER_CONST),1)))*
                                (Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.ACT_LVL_CONSTANT),1)))+
                                (Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.USER_CALORIE_CHANGE),500))*
                                        Double.longBitsToDouble(sharedPreferences.getLong(getString(R.string.GOAL_WEIGHT),1)))
                )
        );
        //userBMR = (10 * weightKG) + (6.25 * heightCM) - (5.0 * (double) age) + genderConstant;
        //multiply BMR by activity level constant for number of calories needed to maintain current weight
        //then add or subtract 500 calories for every pound user wants to gain or lose

        if(!sharedEditor.commit()){
            String toastMessage = "Error saving settings!";
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public void onSettingsButton(View view) {
        Intent getNameScreenIntent = new Intent(this,SettingsScreen.class);
        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
    }

    public void onAddMeal(View view) {
        Intent getNameScreenIntent = new Intent(this, AddMealScreen.class);
        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
    }
    public void onActivityButton(View view) {
        Intent getNameScreenIntent = new Intent(this, ExerciseScreen.class);
        final int result = 1;
        startActivityForResult(getNameScreenIntent, result);
    }
}
