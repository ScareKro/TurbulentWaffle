package com.example.paul.turbulentwaffle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private static boolean accPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(accPresent){
            setContentView(R.layout.activity_main);
        } else {
            Intent getNameScreenIntent = new Intent(this,SettingsScreen.class);
            final int result = 1;
            getNameScreenIntent.putExtra("callingActivity", "MainActivity");
            startActivityForResult(getNameScreenIntent, result);
        }

    }
}
