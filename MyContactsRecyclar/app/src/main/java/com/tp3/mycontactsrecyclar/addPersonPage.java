package com.tp3.mycontactsrecyclar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class addPersonPage extends AppCompatActivity {
   EditText editText1, editText2, editText3, editText4, editText5;
    RecyclerView recyclerView ;


    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager ;
    RoomDB database ;

    MainAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);
        editText1 = findViewById(R.id.firstNameID);
        editText2 = findViewById(R.id.lastNameID);
        editText3 = findViewById(R.id.jobID);
        editText4 = findViewById(R.id.phoneID);
        editText5 = findViewById(R.id.emailID);
    }
    public void back(View v){
        Intent intent = new Intent(addPersonPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void saveContact(View v){
        database = RoomDB.getInstance(this);
        String firstName = editText1.getText().toString().trim();
        String lastName= editText2.getText().toString().trim();
        String job= editText3.getText().toString().trim();
        String phone = editText4.getText().toString().trim();
        String email= editText5.getText().toString().trim();
        //traitement de sauvegarde
        if (!firstName.equals("") && !lastName.equals("") && !job.equals("") && !phone.equals("") && !email.equals("")) {
            // when text is not empty
            // Initialize main data
            MainData data = new MainData();
            // set text on main data
            data.setName(firstName);
            data.setLast_name(lastName);
            data.setJob(job);
            data.setPhone(phone);
            data.setEmail(email);
            //insert text in database
            database.mainDao().insert(data);
            // clear edit text
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText5.setText("");
            //Notify when data is inserted
            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
            //revenir vers la age d'acceuil
            Intent intent = new Intent(addPersonPage.this, MainActivity.class);
            startActivity(intent);
            //finish();
        } else {
            Toast.makeText(this,"Merci de remplir tous les champs",Toast.LENGTH_LONG).show();
        }

        }
    public void reset(View v){
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText5.setText("");
    }
}
