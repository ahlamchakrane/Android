package com.example.switchactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle b = getIntent().getExtras();
        String name= b.getString("Username");
        String password = b.getString("Password");
        text = findViewById(R.id.informations);
        text.setText(name+" :"+password);


    }
    public void back(View v){
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }
    protected void onStart(){
        super.onStart();
        Log.i("LogName","on Start()");
        Toast.makeText(this,"On Start",Toast.LENGTH_LONG).show();
    }
    protected void onResume(){
        super.onResume();
        Log.i("LogName","on Resume()");
        Toast.makeText(this,"On Resume",Toast.LENGTH_LONG).show();
    }
    protected void onPause(){
        super.onPause();
        Log.i("LogName","on Pause()");
        Toast.makeText(this,"On Pause",Toast.LENGTH_LONG).show();
    }
    protected void onStop(){
        super.onStop();
        Log.i("LogName","on Stop()");
        Toast.makeText(this,"On Stop",Toast.LENGTH_LONG).show();
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i("LogName","on Destroy()");
        Toast.makeText(this,"On Destroy",Toast.LENGTH_LONG).show();
    }
}