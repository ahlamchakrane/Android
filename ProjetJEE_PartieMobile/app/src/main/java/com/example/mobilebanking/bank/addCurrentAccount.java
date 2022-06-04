package com.example.mobilebanking.bank;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebanking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class addCurrentAccount extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText balance, description, interestRate;
    Spinner status;
    Button createdAt;
    List<Bank> dataList = new ArrayList<>();
    BankAdapter adapter ;
    String uCreatedAt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms_current);
        balance = findViewById(R.id.edit_balance);
        createdAt = findViewById(R.id.edit_date);
        description = findViewById(R.id.edit_description);
        interestRate = findViewById(R.id.edit_interestRate);
        status = findViewById(R.id.edit_status);
        createdAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    public void back(View v){
        Intent intent = new Intent(addCurrentAccount.this, BankActivity.class);
        startActivity(intent);
        finish();
    }

    public void reset(View v){
       balance.setText("");
       description.setText("");
       interestRate.setText("");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int months = month+1;
        String d, m;
        if(String.valueOf(dayOfMonth).length()==1){
            d = "0"+dayOfMonth;
        }else {
            d=""+dayOfMonth;
        }
        if(String.valueOf(months).length()==1){
            m= "0"+months;
        } else {
            m=""+months;
        }
        String date = d+"-"+m+"-"+year;
        uCreatedAt = date;

    }
}