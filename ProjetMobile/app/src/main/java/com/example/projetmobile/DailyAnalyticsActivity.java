package com.example.projetmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class DailyAnalyticsActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String onlineUserId ="";
    private DatabaseReference expensesRef, personalRef;
    private TextView analyticsTransportAmount, analyticsFoodAmount, analyticsHouseAmount,  analyticsEntertainmentAmount, analyticsEducationAmount, analyticsCharityAmount, analyticsApparelAmount, analyticsHealthAmount, analyticsPersonalAmount, analyticsOtherAmount;
    private RelativeLayout relativeLayoutTransport, relativeLayoutFood, relativeLayoutHouse, relativeLayoutEntertainment, relativeLayoutEducation, relativeLayoutCharity, relativeLayoutApparel, relativeLayoutHealth, relativeLayoutPersonal, relativeLayoutOther;
    private ImageView transport_status, food_status, house_status, entertainment_status, education_status, charity_status, apparel_status, health_status, personal_status, other_status;
    private TextView progress_ratio_transport, progress_ratio_food, progress_ratio_house, progress_ratio_entertainment, progress_ratio_education, progress_ratio_charity, progress_ratio_apparel, progress_ratio_health, progress_ratio_personal, progress_ratio_other;
    private AnyChartView anyChartView;
    private TextView monthSpentAmount, monthRatioSpending;
    private ImageView monthRatioSpendingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_analytics);
        firebaseAuth = FirebaseAuth.getInstance();
        onlineUserId = firebaseAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);
        //text view
        analyticsTransportAmount= findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount= findViewById(R.id.analyticsFoodAmount);
        analyticsHouseAmount = findViewById(R.id.analyticsHouseAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_entertainment = findViewById(R.id.progress_ratio_entertainment);
        progress_ratio_education = findViewById(R.id.progress_ratio_education);
        progress_ratio_charity = findViewById(R.id.progress_ratio_charity);
        progress_ratio_apparel = findViewById(R.id.progress_ratio_apparel);
        progress_ratio_health = findViewById(R.id.progress_ratio_health);
        progress_ratio_personal = findViewById(R.id.progress_ratio_personal);
        progress_ratio_other = findViewById(R.id.progress_ratio_other);


        //Relative Layout
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeLayoutFood = findViewById(R.id.relativeLayoutFood);
        relativeLayoutHouse = findViewById(R.id.relativeLayoutHouse);
        relativeLayoutEntertainment = findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutEducation = findViewById(R.id.relativeLayoutEducation);
        relativeLayoutCharity = findViewById(R.id.relativeLayoutCharity);
        relativeLayoutApparel = findViewById(R.id.relativeLayoutApparel);
        relativeLayoutHealth = findViewById(R.id.relativeLayoutHealth);
        relativeLayoutPersonal = findViewById(R.id.relativeLayoutPersonal);
        relativeLayoutOther = findViewById(R.id.relativeLayoutOther);

        //image view
        transport_status = findViewById(R.id.transport_status);
        food_status = findViewById(R.id.food_status);
        house_status = findViewById(R.id.house_status);
        entertainment_status = findViewById(R.id.entertainment_status);
        education_status = findViewById(R.id.education_status);
        charity_status = findViewById(R.id.charity_status);
        apparel_status = findViewById(R.id.apparel_status);
        health_status = findViewById(R.id.health_status);
        personal_status = findViewById(R.id.personal_status);
        other_status = findViewById(R.id.other_status);

        anyChartView = findViewById(R.id.anyChartView);
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpendingImage = findViewById(R.id.monthRatioSpendingImage);

        getTotalWeekTransportExpenses();
        getTotalWeekFoodExpenses();
        getTotalWeekHouseExpenses();
        getTotalWeekEntertainmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekPersonalExpenses();
        getTotalWeekOtherExpenses();
        getTotalDaySpending();

        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
               loadGraph();
               setStatusAndImageResource();
            }
        }, 500);
    }
    private void getTotalWeekTransportExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Transport"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsTransportAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayTransport").setValue(totalAmount);
                } else {
                    relativeLayoutTransport.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekFoodExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Food"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsFoodAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayFood").setValue(totalAmount);
                } else {
                    relativeLayoutFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekHouseExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "House"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsHouseAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayHouse").setValue(totalAmount);
                } else {
                    relativeLayoutHouse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekEntertainmentExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Entertainment"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsEntertainmentAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayEntertainment").setValue(totalAmount);
                } else {
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekEducationExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Education"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsEducationAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayEducation").setValue(totalAmount);
                } else {
                    relativeLayoutEducation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekCharityExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Charity"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsCharityAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayCharity").setValue(totalAmount);
                } else {
                    relativeLayoutCharity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekHealthExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Health"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsHealthAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayHealth").setValue(totalAmount);
                } else {
                    relativeLayoutHealth.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekApparelExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Apparel"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsApparelAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayApparel").setValue(totalAmount);
                } else {
                    relativeLayoutApparel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekPersonalExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Personal"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsPersonalAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayPersonal").setValue(totalAmount);
                } else {
                    relativeLayoutPersonal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekOtherExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemDay = "Other"+date;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemDay").equalTo(itemDay);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalAmount = 0;
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyticsOtherAmount.setText("Spent "+totalAmount);
                    }
                    personalRef.child("dayOther").setValue(totalAmount);
                } else {
                    relativeLayoutOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DailyAnalyticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalDaySpending(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount+=ptotal;
                    }
                    monthSpentAmount.setText("Total Spent : "+totalAmount+"Dhs");
                } else {
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int transportTotal;
                    if(snapshot.hasChild("dayTransport")){
                        transportTotal = Integer.parseInt(snapshot.child("dayTransport").getValue().toString());
                    } else {
                        transportTotal = 0;
                    }
                    int foodTotal;
                    if(snapshot.hasChild("dayFood")){
                        foodTotal = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    int houseTotal;
                    if(snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }
                    int entertainmentTotal;
                    if(snapshot.hasChild("dayEntertainment")){
                        entertainmentTotal = Integer.parseInt(snapshot.child("dayEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal = 0;
                    }
                    int educationTotal;
                    if(snapshot.hasChild("dayEducation")){
                        educationTotal = Integer.parseInt(snapshot.child("dayEducation").getValue().toString());
                    } else {
                        educationTotal = 0;
                    }
                    int charityTotal;
                    if(snapshot.hasChild("dayCharity")){
                        charityTotal = Integer.parseInt(snapshot.child("dayCharity").getValue().toString());
                    } else {
                        charityTotal = 0;
                    }
                    int apparelTotal;
                    if(snapshot.hasChild("dayApparel")){
                        apparelTotal = Integer.parseInt(snapshot.child("dayApparel").getValue().toString());
                    } else {
                        apparelTotal = 0;
                    }
                    int healthTotal;
                    if(snapshot.hasChild("dayHealth")){
                        healthTotal = Integer.parseInt(snapshot.child("dayHealth").getValue().toString());
                    } else {
                        healthTotal = 0;
                    }
                    int personalTotal;
                    if(snapshot.hasChild("dayPersonal")){
                        personalTotal = Integer.parseInt(snapshot.child("dayPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }
                    int otherTotal;
                    if(snapshot.hasChild("dayOther")){
                        otherTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        otherTotal = 0;
                    }
                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport",transportTotal));
                    data.add(new ValueDataEntry("House",houseTotal));
                    data.add(new ValueDataEntry("Food",foodTotal));
                    data.add(new ValueDataEntry("Entertainment",entertainmentTotal));
                    data.add(new ValueDataEntry("Education",educationTotal));
                    data.add(new ValueDataEntry("Charity",charityTotal));
                    data.add(new ValueDataEntry("Apparel",apparelTotal));
                    data.add(new ValueDataEntry("Health",healthTotal));
                    data.add(new ValueDataEntry("Personal",personalTotal));
                    data.add(new ValueDataEntry("Other",otherTotal));
                    pie.data(data);
                    pie.title("Daily Analytics");
                    pie.labels().position("outside");
                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("items Spent On")
                            .padding(0d, 0d, 10d, 0d);
                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);
                    anyChartView.setChart(pie);
                } else {
                    Toast.makeText(DailyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    float transportTotal;
                    if(snapshot.hasChild("dayTransport")){
                        transportTotal = Integer.parseInt(snapshot.child("dayTransport").getValue().toString());
                    } else {
                        transportTotal = 0;
                    }
                    float foodTotal;
                    if(snapshot.hasChild("dayFood")){
                        foodTotal = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    float houseTotal;
                    if(snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }
                    float entertainmentTotal;
                    if(snapshot.hasChild("dayEntertainment")){
                        entertainmentTotal = Integer.parseInt(snapshot.child("dayEntertainment").getValue().toString());
                    } else {
                        entertainmentTotal = 0;
                    }
                    float educationTotal;
                    if(snapshot.hasChild("dayEducation")){
                        educationTotal = Integer.parseInt(snapshot.child("dayEducation").getValue().toString());
                    } else {
                        educationTotal = 0;
                    }
                    float charityTotal;
                    if(snapshot.hasChild("dayCharity")){
                        charityTotal = Integer.parseInt(snapshot.child("dayCharity").getValue().toString());
                    } else {
                        charityTotal = 0;
                    }
                    float apparelTotal;
                    if(snapshot.hasChild("dayApparel")){
                        apparelTotal = Integer.parseInt(snapshot.child("dayApparel").getValue().toString());
                    } else {
                        apparelTotal = 0;
                    }
                    float healthTotal;
                    if(snapshot.hasChild("dayHealth")){
                        healthTotal = Integer.parseInt(snapshot.child("dayHealth").getValue().toString());
                    } else {
                        healthTotal = 0;
                    }
                    float personalTotal;
                    if(snapshot.hasChild("dayPersonal")){
                        personalTotal = Integer.parseInt(snapshot.child("dayPersonal").getValue().toString());
                    } else {
                        personalTotal = 0;
                    }
                    float otherTotal;
                    if(snapshot.hasChild("dayOther")){
                        otherTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        otherTotal = 0;
                    }
                    float monthTotalSpentAmount;
                    if(snapshot.hasChild("today")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("today").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;
                    }
                    //total budget
                    float budgetAmount;
                    if(snapshot.hasChild("budget")){
                        budgetAmount = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        budgetAmount = 0;
                    }

                    //month percent
                    float monthPercent = (monthTotalSpentAmount/budgetAmount)*100;
                    if(monthPercent<50){
                        monthRatioSpending.setText(monthPercent+"%"+" used of "+budgetAmount+"Dhs. Status:");
                        monthRatioSpendingImage.setImageResource(R.drawable.green);
                    } else if(monthPercent>=50 && monthPercent<100) {
                        monthRatioSpending.setText(monthPercent+"%"+" used of "+budgetAmount+"Dhs. Status:");
                        monthRatioSpendingImage.setImageResource(R.drawable.brown);
                    } else {
                        monthRatioSpending.setText(monthPercent+"%"+" used of "+budgetAmount+"Dhs. Status:");
                        monthRatioSpendingImage.setImageResource(R.drawable.red);
                    }
                    //transport percent
                    float transportPercent = (transportTotal/monthTotalSpentAmount)*100;
                    if(transportPercent<50){
                        progress_ratio_transport.setText(transportPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        transport_status.setImageResource(R.drawable.green);
                    } else if(transportPercent>=50 && transportPercent<100) {
                        progress_ratio_transport.setText(transportPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        transport_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_transport.setText(transportPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        transport_status.setImageResource(R.drawable.red);
                    }
                    //food percent
                    float foodPercent = (foodTotal/monthTotalSpentAmount)*100;
                    if(foodPercent<50){
                        progress_ratio_food.setText(foodPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        food_status.setImageResource(R.drawable.green);
                    } else if(foodPercent>=50 && foodPercent<100) {
                        progress_ratio_food.setText(foodPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        food_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_food.setText(foodPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        food_status.setImageResource(R.drawable.red);
                    }
                    //house percent
                    float housePercent = (houseTotal/monthTotalSpentAmount)*100;
                    if(housePercent<50){
                        progress_ratio_house.setText(housePercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        house_status.setImageResource(R.drawable.green);
                    } else if(housePercent>=50 && housePercent<100) {
                        progress_ratio_house.setText(housePercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        house_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_house.setText(housePercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        house_status.setImageResource(R.drawable.red);
                    }
                    //entertainment percent
                    float entertainmentPercent = (entertainmentTotal/monthTotalSpentAmount)*100;
                    if(entertainmentPercent<50){
                        progress_ratio_entertainment.setText(entertainmentPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        entertainment_status.setImageResource(R.drawable.green);
                    } else if(entertainmentPercent>=50 && entertainmentPercent<100) {
                        progress_ratio_entertainment.setText(entertainmentPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        entertainment_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_entertainment.setText(entertainmentPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        entertainment_status.setImageResource(R.drawable.red);
                    }
                    //education percent
                    float educationPercent = (educationTotal/monthTotalSpentAmount)*100;
                    if(educationPercent<50){
                        progress_ratio_education.setText(educationPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        education_status.setImageResource(R.drawable.green);
                    } else if(educationPercent>=50 && educationPercent<100) {
                        progress_ratio_education.setText(educationPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        education_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_education.setText(educationPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        education_status.setImageResource(R.drawable.red);
                    }
                    //charity percent
                    float charityPercent = (charityTotal/monthTotalSpentAmount)*100;
                    if(charityPercent<50){
                        progress_ratio_charity.setText(charityPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs.Status:");
                        charity_status.setImageResource(R.drawable.green);
                    } else if(charityPercent>=50 && charityPercent<100) {
                        progress_ratio_charity.setText(charityPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        charity_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_charity.setText(charityPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        charity_status.setImageResource(R.drawable.red);
                    }
                    //apparel percent
                    float apparelPercent = (apparelTotal/monthTotalSpentAmount)*100;
                    if(apparelPercent<50){
                        progress_ratio_apparel.setText(apparelPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        apparel_status.setImageResource(R.drawable.green);
                    } else if(apparelPercent>=50 && apparelPercent<100) {
                        progress_ratio_apparel.setText(apparelPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        apparel_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_apparel.setText(apparelPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        apparel_status.setImageResource(R.drawable.red);
                    }
                    //health percent
                    float healthPercent = (healthTotal/monthTotalSpentAmount)*100;
                    if(healthPercent<50){
                        progress_ratio_health.setText(healthPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        health_status.setImageResource(R.drawable.green);
                    } else if(healthPercent>=50 && healthPercent<100) {
                        progress_ratio_health.setText(healthPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        health_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_health.setText(healthPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        health_status.setImageResource(R.drawable.red);
                    }
                    //personal percent
                    float personalPercent = (personalTotal/monthTotalSpentAmount)*100;
                    if(personalPercent<50){
                        progress_ratio_personal.setText(personalPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        personal_status.setImageResource(R.drawable.green);
                    } else if(personalPercent>=50 && personalPercent<100) {
                        progress_ratio_personal.setText(personalPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        personal_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_personal.setText(personalPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        personal_status.setImageResource(R.drawable.red);
                    }
                    //other percent
                    float otherPercent = (otherTotal/monthTotalSpentAmount)*100;
                    if(otherPercent<50){
                        progress_ratio_other.setText(otherPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        other_status.setImageResource(R.drawable.green);
                    } else if(otherPercent>=50 && otherPercent<100) {
                        progress_ratio_other.setText(otherPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        other_status.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_other.setText(otherPercent+"%"+" used of "+monthTotalSpentAmount+"Dhs. Status:");
                        other_status.setImageResource(R.drawable.red);
                    }

                } else {
                    Toast.makeText(DailyAnalyticsActivity.this, "Status and image ressource error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}