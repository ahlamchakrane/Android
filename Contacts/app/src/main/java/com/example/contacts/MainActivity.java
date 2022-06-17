package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private monAdaptateur myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.liste);
        //creer une liste
        final ArrayList<ItemModel> arrayList = new ArrayList<>();
        //Remplir la liste par les contacts

        try {
            JSONObject object = new JSONObject(readJSON());
            JSONArray array = object.getJSONArray("contacts");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String first_name = jsonObject.getString("first_name");
                String last_name = jsonObject.getString("last_name");
                String job = jsonObject.getString("job");
                String email = jsonObject.getString("email");
                String phone = jsonObject.getString("phone");
                final ItemModel model = new ItemModel();
                model.setId(id);
                model.setNom(first_name +""+ last_name);
                model.setJob(job);
                model.setEmail(email);
                model.setTelephone(phone);
                arrayList.add(model);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        //Appler l'adaptateur pour adapter la liste à la vue
        myAdapter= new monAdaptateur(arrayList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ItemModel clickedItem = arrayList.get(position);
                Intent intent = new Intent(MainActivity.this, contact.class);
                intent.putExtra("nom",clickedItem.getNom());
                intent.putExtra("telephone",clickedItem.getTelephone());
                intent.putExtra("job",clickedItem.getJob());
                intent.putExtra("email",clickedItem.getEmail());

                startActivity(intent);
            }
        });


    }
    public String readJSON() {
        String json = null;
        try {
// Opening data.json file
            InputStream inputStream = getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
// read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
// convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    class monAdaptateur extends BaseAdapter {
        //creer une liste de model que nous avons deja cree
        ArrayList<ItemModel> eltList = new ArrayList<>();
        //constructeur
        public monAdaptateur(ArrayList<ItemModel> elts) {
            this.eltList = elts;
        }

        public int getCount() {
            return eltList.size();
        }

        public Object getItem(int i) {
            return eltList.get(i).nom;
        }

        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup viewGroup) {
            LayoutInflater linflater = getLayoutInflater();
            //mettre les elements de la vue (textView de nom...) dans "view"
            View view = linflater.inflate(R.layout.item_list, null);

            //Récupérer chaque elt de la vue

            TextView nom = view.findViewById(R.id.nom);
            TextView telephone = view.findViewById(R.id.telephone);
            TextView email = view.findViewById(R.id.email);
            TextView job = view.findViewById(R.id.job);

            //Attribuer à chaque element de la vue sa propre val
            //etlist contient nos contacts
            nom.setText(eltList.get(i).nom);
            telephone.setText(eltList.get(i).telephone);
            job.setText(eltList.get(i).job);
            email.setText(eltList.get(i).email);

            return view;

        }
    }
}