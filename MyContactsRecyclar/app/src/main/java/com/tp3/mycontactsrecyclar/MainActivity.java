package com.tp3.mycontactsrecyclar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;
    EditText searchedText;
    double numberContacts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE
        },1);




        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyContacts";
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        recyclerView = findViewById(R.id.recycler_view);


        // Initialize database
        database = RoomDB.getInstance(this);
        // store database value in data list
        dataList = database.mainDao().getAll();
        searchedText = findViewById(R.id.text_search);
        searchedText.setHint("Rechercher parmi les "+dataList.size()+" contacts");
        searchedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        // initilize linear layout manager

        linearLayoutManager = new LinearLayoutManager(this);
        // Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        // Initilize Adapter
        adapter = new MainAdapter(MainActivity.this, dataList);
        // set Adapter
        recyclerView.setAdapter(adapter);
    }
 public void ajouterContact(View v){
        Intent intent = new Intent(MainActivity.this, addPersonPage.class);
        startActivity(intent);
        //finish();
    }
    //for reaserch
    private void filter(String text){
        ArrayList<MainData> filterdList = new ArrayList<>();
        for(MainData item: dataList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filterdList.add(item);
            }
        }
        adapter.filterList(filterdList);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}