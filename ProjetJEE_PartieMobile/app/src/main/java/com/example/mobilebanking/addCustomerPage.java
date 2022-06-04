package com.example.mobilebanking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import config.AppConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addCustomerPage extends AppCompatActivity {
    EditText editText1, editText2;
    JSONPlaceholder jsonPlaceholder ;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms_customer);
        editText1 = findViewById(R.id.nameID);
        editText2 = findViewById(R.id.emailID);
        recyclerView = findViewById(R.id.recycler_view);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));

    }

    public void createCustomer() {
        String name = editText1.getText().toString().trim();
        String email = editText2.getText().toString().trim();
        if (!name.isEmpty() || email.isEmpty()) {
            Customer customer = new Customer(name, email);
            Call<Customer> call = jsonPlaceholder.createCustomer(customer);
            call.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(addCustomerPage.this, response.code() + "Response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Customer> taskList = new ArrayList<>();
                    taskList.add(response.body());
                    CustomerAdapter taskAdapter = new CustomerAdapter(addCustomerPage.this, taskList);
                    recyclerView.setAdapter(taskAdapter);
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Toast.makeText(addCustomerPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(addCustomerPage.this, "Ooops! il faut remplire les champs", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View v){
        Intent intent = new Intent(addCustomerPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void reset(View v){
        editText1.setText("");
        editText2.setText("");
    }
}
