package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateOption extends AppCompatActivity {
    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss-S");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("foodUsers");
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String username = currentUser.getEmail().split("@")[0];




    HashMap<Date, Integer> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_option);

        //get the name of associate account!
        Intent intent = getIntent();
        String Associate_Name = intent.getStringExtra("associateAccount");
        System.out.println("Associate_Name:" + Associate_Name);

        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        Button todayButton = (Button) findViewById(R.id.today_button);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle Date Label
                String currDate = dateFormat.format(new Date());
                String[] date = currDate.split("-");
                String month = new DateFormatSymbols().getMonths()[Integer.parseInt(date[0])-1];
                String dateLabel = month + " " + date[1] + ", " + date[2];

                // Switch to Daily Record
                Intent myIntent = new Intent(DateOption.this, DailyRecordActivity.class);
                myIntent.putExtra("date", dateLabel);
                startActivity(myIntent);
            }
        });

        Button weekButton = (Button) findViewById(R.id.week_button);
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGraph(7, "Nutrition info for last week");
            }
        });

        Button monthButton = (Button) findViewById(R.id.month_button);
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGraph(30, "Nutrition info for last month");
            }
        });

        Button yearButton = (Button) findViewById(R.id.year_button);
        yearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGraph(365, "Nutrition info for last year");
            }
        });

    }

    protected void onClickGraph(int days, final String graph_title) {
        final LocalDate endDate = LocalDate.now();
        final LocalDate startDate = endDate.minusDays(days); // JUST CHANGE THIS

        myRef.child(username).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // need to refresh dict
                map = new HashMap<>();
                LocalDate minDate = LocalDate.MAX;
                LocalDate maxDate = LocalDate.MIN;

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String timestamp = data.getKey();
                    String[] timestampArr = timestamp.split("-");
                    String timestampDate = timestampArr[0] + timestampArr[1] + timestampArr[2];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
                    LocalDate testDate = LocalDate.parse(timestampDate, formatter);

                    // WITHIN RANGE
                    if (!testDate.isBefore(startDate) && !testDate.isAfter(endDate)) {
                        Date date = Date.from(testDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Long calorie_db = (Long) data.child("calories").getValue();
                        Integer calories = calorie_db.intValue();

                        Integer dayTotal = map.get(date);
                        if (dayTotal == null) {
                            map.put(date, calories);

                        } else {
                            map.put(date, dayTotal + calories);
                        }

                        // update min, max
                        if(testDate.isAfter(maxDate)) {
                            maxDate = testDate;
                        }

                        if(testDate.isBefore(minDate)) {
                            minDate = testDate;
                        }
                    }
                }

                // Switch to Daily Record
                Intent myIntent = new Intent(DateOption.this, GraphActivity.class);
                myIntent.putExtra("dateMap", map);
                //min, max date
                long minDateLong = minDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
                long maxDateLong = maxDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

                myIntent.putExtra("minDateLong", minDateLong);
                myIntent.putExtra("maxDateLong", maxDateLong);
                myIntent.putExtra("title", graph_title);
                startActivity(myIntent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}

