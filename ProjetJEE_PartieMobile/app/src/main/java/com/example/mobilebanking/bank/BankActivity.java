package com.example.mobilebanking.bank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebanking.CustomerAdapter;
import com.example.mobilebanking.JSONPlaceholder;
import com.example.mobilebanking.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import config.AppConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BankActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    JSONPlaceholder jsonPlaceholder;
    BankAdapter bankAdapter;

    public  static List<Bank> bankList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view_bank);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));

        Call<List<Bank>> call = jsonPlaceholder.getBanks(CustomerAdapter.idCustomer);
        call.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(BankActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                bankList = response.body();
                bankAdapter = new BankAdapter(BankActivity.this, bankList);
                BankAdapter bankAdapter = new BankAdapter(BankActivity.this, bankList);
                recyclerView.setAdapter(bankAdapter);
            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                Toast.makeText(BankActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for reaserch
    private void filter(String text) {
        ArrayList<Bank> filterdList = new ArrayList<>();
        for (Bank item : bankList) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filterdList.add(item);
            }
        }
        bankAdapter.filterList(filterdList);
    }

    public void addCurrentAccount(View v){
        Intent intent = new Intent(BankActivity.this, addCurrentAccount.class);
        startActivity(intent);
        //finish();
    }
    public void addSavingAccount(View v){
        Intent intent = new Intent(BankActivity.this, addSavingAccount.class);
        startActivity(intent);
        //finish();
    }
}
