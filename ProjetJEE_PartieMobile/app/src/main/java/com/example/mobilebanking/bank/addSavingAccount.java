package com.example.mobilebanking.bank;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilebanking.CustomerAdapter;
import com.example.mobilebanking.Customer;
import com.example.mobilebanking.R;

import java.util.ArrayList;
import java.util.List;

public class addSavingAccount extends AppCompatActivity {
    EditText editText1, editText2;
    List<Customer> dataList = new ArrayList<>();
    //RoomDB database;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms_saving);
    }
}