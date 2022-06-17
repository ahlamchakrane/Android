package com.example.sqlapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PersonneDao personneDao;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView ls = (ListView) findViewById(R.id.liste);
        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "Personnes_db").allowMainThreadQueries().build();
        personneDao = db.personneDao();
        List<Personne> liste = personneDao.getAllPersonnes();
        ArrayAdapter Adapter = new ArrayAdapter<>
                (this,android.R.layout.simple_list_item_1,Listin(liste));
        ls.setAdapter(Adapter);
    }

    public void Enregistrer(View view) {
        List<Personne> liste;
        EditText id = findViewById(R.id.getId);
        TextView first_name = findViewById(R.id.firstName);
        TextView last_name = findViewById(R.id.lastName);
        ListView ls = (ListView) findViewById(R.id.liste);
        liste = personneDao.getAllPersonnes();
        personneDao.insert(new Personne(Integer.parseInt(id.getText().toString()),first_name.getText().toString(),last_name.getText().toString()));
        liste = personneDao.getAllPersonnes();
        ArrayAdapter myAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Listin(liste));
        ls.setAdapter(myAdapter);
        first_name.setText("");
        last_name.setText("");
    }
    public void Update(View view) {
        List<Personne> liste;
        EditText id = findViewById(R.id.getId);
        TextView first_name = findViewById(R.id.firstName);
        TextView last_name = findViewById(R.id.lastName);
        ListView ls = (ListView) findViewById(R.id.liste);
        liste = personneDao.getAllPersonnes();
        Personne personne = liste.get(Integer.parseInt(id.getText().toString())-1);
        personne.setFirstName(first_name.getText().toString());
        personne.setLastName(first_name.getText().toString());
        personneDao.update(personne);
        liste = personneDao.getAllPersonnes();
        ArrayAdapter myAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Listin(liste));
        ls.setAdapter(myAdapter);
        first_name.setText("");
        last_name.setText("");
        id.setText("");
    }
    public void Supprimer(View view) {
        List<Personne> liste;
        EditText id = findViewById(R.id.getId);
        TextView first_name = findViewById(R.id.firstName);
        TextView last_name = findViewById(R.id.lastName);
        ListView ls = (ListView) findViewById(R.id.liste);
        liste = personneDao.getAllPersonnes();
        personneDao.delete(liste.get(Integer.parseInt(id.getText().toString())-1));
        liste = personneDao.getAllPersonnes();
        ArrayAdapter myAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Listin(liste));
        ls.setAdapter(myAdapter);
        id.setText("");
    }
    ArrayList Listin(List<Personne> l){
        ArrayList<String> maliste = new ArrayList<>();
        for(int i=0;i<l.size();i++){
            maliste.add(l.get(i).getId()+". " + l.get(i).getFirstName() + " "+ l.get(i).getLastName());
        }
        return maliste;
    }



}
