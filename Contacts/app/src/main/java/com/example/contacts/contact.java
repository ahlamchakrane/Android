package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        TextView nom = findViewById(R.id.contactNom);
        TextView telephone = findViewById(R.id.contactNumber);
        TextView job = findViewById(R.id.contactJob);
        TextView email = findViewById(R.id.contactEmail);

        nom.setText(getIntent().getExtras().getString("nom"));
        telephone.setText(getIntent().getExtras().getString("telephone"));
        job.setText(getIntent().getExtras().getString("job"));
        email.setText(getIntent().getExtras().getString("email"));

    }
}