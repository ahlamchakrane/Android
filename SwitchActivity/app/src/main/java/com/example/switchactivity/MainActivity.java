
package com.example.switchactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //vitesse de passage du splash screen vers l'autre activité
    private static int SPLASH_TIME_OUT = 5000;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide status bare en haut
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        // Appel annimation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image = findViewById(R.id.imageView);
        text = findViewById(R.id.textView);
        // appliquer les annimations sur l'image et le text
        image.setAnimation(topAnim);
        text.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                Pair[] pairs = new Pair[2];
                //the first element we want it to be animated
                pairs[0] = new Pair<View,String>(image,"logo_image");
                //the second element we want it to be animated
                pairs[1] = new Pair<View,String>(text,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                //call the animation for the next screen
                startActivity(intent, options.toBundle());
            }
        }, SPLASH_TIME_OUT);
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