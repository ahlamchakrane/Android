package com.example.sharedpreferences;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView titleMain = findViewById(R.id.titleMain);
        SwitchMaterial themeSwitch= findViewById(R.id.themeSwitch);
        TextView descriptionMain = findViewById(R.id.descriptionMain);
        MaterialButton newNoteButton = findViewById(R.id.newNoteButton);

        SharedPreferences sharedPreferences=
                getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        titleMain.setText(sharedPreferences.getString("Title",null));
        titleMain.setTextColor(sharedPreferences.getInt("colorText",0));

        descriptionMain.setText(sharedPreferences.getString("Description", null));
        descriptionMain.setTextColor(sharedPreferences.getInt("colorText",0));

        themeSwitch.setChecked(sharedPreferences.getBoolean("checked",false));
        findViewById(R.id.activityMain).setBackgroundColor(sharedPreferences.getInt("colorBack",1));

        newNoteButton.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this,AddNoteActivity.class));
        });

        themeSwitch.setOnClickListener(view -> {

            final int black = ContextCompat.getColor(this, R.color.black);
            final int white = ContextCompat.getColor(this, R.color.white);
            if(themeSwitch.isChecked()) {
                findViewById(R.id.activityMain).setBackgroundColor(black);
                editor.putInt("colorBack",black);
                editor.putInt("colorText",white);
                descriptionMain.setTextColor(white);
                titleMain.setTextColor(white);
                themeSwitch.setChecked(true);
                editor.putBoolean("checked",true);
                editor.commit();

            }
            else {
                findViewById(R.id.activityMain).setBackgroundColor(white);
                descriptionMain.setTextColor(black);
                editor.putInt("colorText",black);
                editor.putInt("colorBack",white);
                titleMain.setTextColor(black);
                themeSwitch.setChecked(false);
                editor.putBoolean("checked",false);
                editor.commit();
            }


        });


    }
}