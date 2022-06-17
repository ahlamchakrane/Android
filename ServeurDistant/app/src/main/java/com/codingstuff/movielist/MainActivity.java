package com.codingstuff.movielist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codingstuff.movielist.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    JSONPlaceholder jsonPlaceholder;
    public  static List<Task> taskList = new ArrayList<>();
    public  static TaskAdapter taskAdapter;
    EditText searchedText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));

        Call<List<Task>> call = jsonPlaceholder.getTask();
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                taskList = response.body();
                taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for reaserch
    /*private void filter(String text) {
        ArrayList<Task> filterdList = new ArrayList<>();
        for (Task item : taskList) {
            if (item.getTask().toLowerCase().contains(text.toLowerCase())) {
                filterdList.add(item);
            }
        }
        taskAdapter.filterList(filterdList);
    }*/

    public void addTask(View v) {
        Intent intent = new Intent(MainActivity.this, addTaskPage.class);
        startActivity(intent);
    }
}
/*
    private void createTask(){
        Task task = new Task("task 6");
        Call<Task> call = jsonPlaceholder.createTask(task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if(! response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.code()+ "Response", Toast.LENGTH_SHORT).show();
                    return ;
                }
                List<Task> taskList = new ArrayList<>();
                taskList.add(response.body());
                TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }/*
    private void updateTask(){
        Task task = new Task("test update");
        Call<Task> call = jsonPlaceholder.putTask(1, task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if(! response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.code()+ "Response", Toast.LENGTH_SHORT).show();
                    return ;
                }
                List<Task> taskList = new ArrayList<>();
                taskList.add(response.body());
                TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteTask(){
        Call<Void> call = jsonPlaceholder.deleteTask(1);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(! response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.code()+ "Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "Deleted successfully "+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



}}*/

