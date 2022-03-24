package com.example.switchactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button secondActivityButton  =  (Button)findViewById(R.id.sign);
        secondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Home.class);
                Bundle b = new Bundle();
                b.putString("Username", username.getText().toString());
                b.putString("Password", password.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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