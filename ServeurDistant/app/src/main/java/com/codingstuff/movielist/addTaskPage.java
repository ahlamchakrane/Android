package com.codingstuff.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.movielist.config.AppConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addTaskPage extends AppCompatActivity {
    EditText editText1;
    JSONPlaceholder jsonPlaceholder ;
    private RecyclerView recyclerView;
    FloatingActionButton btAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);
        editText1 = findViewById(R.id.taskID);
        btAdd = findViewById(R.id.save_task);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));

    }

    public void createTask() {
        Log.e("test", "test");
        String myTask = editText1.getText().toString().trim();
        if (!myTask.isEmpty()) {
            Task task = new Task(myTask);
            Call<Task> call = jsonPlaceholder.createTask(task);
            call.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(addTaskPage.this, response.code() + "Response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Task> taskList = new ArrayList<>();
                    taskList.add(response.body());
                    TaskAdapter taskAdapter = new TaskAdapter(addTaskPage.this, taskList);
                    recyclerView.setAdapter(taskAdapter);
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    Toast.makeText(addTaskPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(addTaskPage.this, "Ooops! il faut remplire les champs", Toast.LENGTH_SHORT).show();
        }
    }
    public void reset(View v){
        editText1.setText("");
    }
    public void back(View v){
        Intent intent = new Intent(addTaskPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
