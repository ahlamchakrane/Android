package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.mariuszgromada.math.mxparser.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    TextView resultat;
    String operateur = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultat = findViewById(R.id.resultats);
        resultat.setText("");

    }
    public void  numberEvent(View view) {
        String valeur = "";
        if(!(resultat.getText().toString()=="0"))
            valeur  = resultat.getText().toString();
        if(view.getId() == R.id.B1){
            valeur += "1";
        }
        else if(view.getId() == R.id.B2){
            valeur += "2";
        }
        else if(view.getId() == R.id.B3){
            valeur += "3";
        }
        else if(view.getId() == R.id.B4){
            valeur += "4";
        }
        else if(view.getId() == R.id.B5){
            valeur += "5";
        }
        else if(view.getId() == R.id.B6){
            valeur += "6";
        }
        else if(view.getId() == R.id.B7){
            valeur += "7";
        }
        else if(view.getId() == R.id.point){
            valeur += ".";
        }
        else if(view.getId() == R.id.B8){
            valeur += "8";
        }
        else if(view.getId() == R.id.B9){
            valeur += "9";
        }
        else if(view.getId() == R.id.B0){
            valeur += "0";
        }
        else if(view.getId() == R.id.parentheseOuvre){
            valeur += "(";
        }
        else if(view.getId() == R.id.parentheseFerme){
            valeur += ")";
        }
        resultat.setText(valeur);

    }
    public void operationEvent(View view) {
        if (view.getId() == R.id.mult) {
            operateur = "*";
        } else if (view.getId() == R.id.moins) {
            operateur = "-";
        } else if (view.getId() == R.id.div) {
            operateur = "/";
        } else if (view.getId() == R.id.plus) {
            operateur = "+";
        }
        resultat.setText(resultat.getText().toString()+operateur);
    }

        public void egaleEvent(View view) {
          String expressionAr = resultat.getText().toString();
          Expression expression = new Expression(expressionAr);
          resultat.setText(String.valueOf(expression.calculate()));
        }
        public void ACEvent(View view){
        resultat.setText("");
        }
        public void DelEvent(View view) {
        String expression=resultat.getText().toString();
        expression=expression.substring(0,expression.length()-1);
        resultat.setText(expression);
    }
}
