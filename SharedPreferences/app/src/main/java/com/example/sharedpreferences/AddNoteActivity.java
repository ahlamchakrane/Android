package com.example.sharedpreferences;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    static Calendar now = Calendar.getInstance();
    //Date currentTime = Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        MaterialButton saveButton= findViewById(R.id.saveButton);
        MaterialButton deleteButton= findViewById(R.id.deleteButton);

        SharedPreferences sharedPreferences=
                getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        findViewById(R.id.AddNote).setBackgroundColor(sharedPreferences.getInt("colorBack",1));
        titleInput.setHintTextColor(sharedPreferences.getInt("colorText",0));
        descriptionInput.setHintTextColor(sharedPreferences.getInt("colorText",0));
        titleInput.setTextColor(sharedPreferences.getInt("colorText",0));
        descriptionInput.setTextColor(sharedPreferences.getInt("colorText",0));
        saveButton.setOnClickListener(view ->{
                String title= titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                editor.putString("Title",title);
                editor.putString("Description", description);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
        });
        deleteButton.setOnClickListener(view -> {
            editor.remove("Title");
            editor.remove("Description");
            editor.remove("CreatedTime");
            editor.commit();
            Toast.makeText(getApplicationContext(), "Note removed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
        });
    }
}
