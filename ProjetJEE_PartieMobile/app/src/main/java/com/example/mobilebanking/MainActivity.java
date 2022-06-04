package com.example.mobilebanking;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import config.AppConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    JSONPlaceholder jsonPlaceholder;
    CustomerAdapter customerAdapter;

    public  static List<Customer> customerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));

        Call<List<Customer>> call = jsonPlaceholder.getCustomers();
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                customerList = response.body();
                customerAdapter = new CustomerAdapter(MainActivity.this, customerList);
                CustomerAdapter customerAdapter = new CustomerAdapter(MainActivity.this, customerList);
                recyclerView.setAdapter(customerAdapter);
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for reaserch
    private void filter(String text) {
        ArrayList<Customer> filterdList = new ArrayList<>();
        for (Customer item : customerList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterdList.add(item);
            }
        }
        customerAdapter.filterList(filterdList);
    }

    public void addCustomer(View v) {
        Intent intent = new Intent(MainActivity.this, addCustomerPage.class);
        startActivity(intent);
    }
}

