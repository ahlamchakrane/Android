package com.tp3.mycontactsrecyclar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.media.ImagePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;
    EditText searchedText;
    ImageView profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchedText = findViewById(R.id.text_search);
        searchedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        profil = findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if(pick==true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    } else {
                        if(!checkStoragePermission()){
                            requestStoragePermission();
                        }
                        else {
                            PickImage();
                        }
                    }
                }
            }
        });



        recyclerView = findViewById(R.id.recycler_view);


        // Initialize database
        database = RoomDB.getInstance(this);
        // store database value in data list
        dataList = database.mainDao().getAll();

        // initilize linear layout manager

        linearLayoutManager = new LinearLayoutManager(this);
        // Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        // Initilize Adapter
        adapter = new MainAdapter(MainActivity.this, dataList);
        // set Adapter
        recyclerView.setAdapter(adapter);
    }

    private void PickImage() {
     /*   CropImage.activity()
                .setGuidlines(CropImageView.Guidelines.ON)
                .start(this); */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    public void ajouterContact(View v){
        Intent intent = new Intent(MainActivity.this, addPersonPage.class);
        startActivity(intent);
        //finish();
    }
    private void filter(String text){
        ArrayList<MainData> filterdList = new ArrayList<>();
        for(MainData item: dataList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filterdList.add(item);
            }
        }
        adapter.filterList(filterdList);
    }

    private  boolean checkCameraPermission(){
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }
    private  boolean checkStoragePermission(){
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }
}