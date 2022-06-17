package com.example.ahlamchakrane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    static double lon;
    static double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.addMarker(new MarkerOptions().position(latLng));
                // Toast.makeText(MapsActivity.this, latLng.latitude+"-"+latLng.longitude, Toast.LENGTH_SHORT).show();
                    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                    String url="https://api.openweathermap.org/data/2.5/forecast?lat=33.6635&lon=-7.3849&appid=e457293228d5e1465f30bcbe1aea456b";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray=new JSONArray(response);

                                JSONObject list= jsonArray.getJSONObject(0);
                                Log.e("list",list.toString());
                               /* lat = 33.6635;
                                lon = 7.3849;

                                Date date=new Date(list.getJSONObject);
                                SimpleDateFormat simpleDateFormat=
                                        new SimpleDateFormat("dd-MMM-yyyy' T 'HH:mm");
                                String dateString=simpleDateFormat.format(date);


                                JSONObject main=jsonObject.getJSONObject("main");
                                Double Temp=(Double)(main.getDouble("temp")-273.15);
                                Double Feels_like=(Double)(main.getDouble("feels_like")-273.15);
                                Double TempMin=(Double)(main.getDouble("temp_min")-273.15);
                                Double TempMax=(Double)(main.getDouble("temp_max")-273.15);
                                Double Pression=(Double)(main.getDouble("pressure"));
                                Double Humidite=(Double)(main.getDouble("humidity"));

                                JSONObject wind=jsonObject.getJSONObject("wind");
                                Double Speed = (Double)(wind.getDouble("speed"));

                                JSONArray weather= jsonObject.getJSONArray("weather");
                                String weather_main = weather.getJSONObject(0).getString("main");
                                String weather_icon =weather.getJSONObject(0).getString("icon");


                               Log.i("Weather","----------------------------------------------");
                                Log.i("Meteo",weather_main);
                               // setImage(meteo);
                                Toast.makeText(MapsActivity.this,weather_main, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext( ), response, Toast.LENGTH_LONG).show( );
                                //Afficher les infos dans un dialogue

                                TextView udt= findViewById(R.id.dt);
                                TextView umain_temp = findViewById(R.id.main_temp);
                                TextView umain_feels_like = findViewById(R.id.main_feels_like);
                                TextView umain_temp_min = findViewById(R.id.main_temp_min);
                                TextView umain_temp_max = findViewById(R.id.main_temp_max);
                                TextView umain_pressure = findViewById(R.id.main_pressure);
                                TextView umain_humidity = findViewById(R.id.main_humidity);
                                TextView uwind_speed = findViewById(R.id.wind_speed);
                                CircleImageView uicon = findViewById(R.id.icon);
                                //set layout
                                udt.setText("Date : " +String.valueOf(dateString));
                                umain_temp.setText("Temperature : "+String.valueOf(Temp));
                                umain_feels_like.setText("Feels like : "+String.valueOf(Feels_like));
                                umain_temp_min.setText("Min temp : "+String.valueOf(TempMin));
                                umain_temp_max.setText("Max temp : "+String.valueOf(TempMax));
                                umain_pressure.setText("Pressure : "+String.valueOf(Pression));
                                umain_humidity.setText("Humidity : "+String.valueOf(Humidite));
                                uwind_speed.setText("Speed : "+String.valueOf(Speed));
                                //Le path
                                String path = "http://openweathermap.org/img/wn/"+weather_icon+"@2x.png";
                                Picasso.get().load(path).into(uicon);
*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error)
                                {
                                    Toast.makeText(MapsActivity.this,
                                            "City not fond",Toast.LENGTH_LONG).show();


                                }
                            });

                    queue.add(stringRequest);

                }
                //initialisation des variables




        });
    }
    public void item1(View v){

    }
    public void item2(View v){

    }
    public void item3(View v){

    }
    public void item4(View v){

    }
    public void item5(View v){

    }
}
