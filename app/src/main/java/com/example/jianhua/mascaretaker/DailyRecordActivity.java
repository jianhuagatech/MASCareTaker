package com.example.jianhua.mascaretaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

// https://www.dev2qa.com/android-listactivity-example/
public class DailyRecordActivity extends ListActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    List<String> listData = new ArrayList<String>();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("foodUsers");
    String username = "";
    ArrayAdapter<String> listDataAdapter = null;
    Map<String, Food> map = new HashMap<>();
    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss-S");

    Long last_calories = null; // need this to handle entering food, weird bug with timestamp change, cals null originally
    double[] lastNutrientList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_record);

        // Set date label
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        String dateLabel = getIntent().getExtras().getString("date");
        TextView tv1 = (TextView)findViewById(R.id.date_text_view);
        tv1.setText(dateLabel);

        // Create a list data which will be displayed in inner ListView.
//        listData.add("Yogurt");
//        listData.add("1 cup sugar, 2 pieces of bread");
//        listData.add("Tomato soup");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        username = currentUser.getEmail().split("@")[0];

        //FOR FRESH ACCOUNTS UNCOMMENT
//        Food addedFood = new Food("Potato", 80, 1.1, 1.1, 1.1,
//                1.1, 1.1, 1.1, 1.1, 1.1, 1.1);
//        writeFood(addedFood);

        listDataAdapter = new ArrayAdapter<String>(this, R.layout.activity_daily_record_row, R.id.listRowTextView, listData);
        // Set this adapter to inner ListView object.
        this.setListAdapter(listDataAdapter);

        myRef.child(username).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listData.clear();
                LocalDate currTime = LocalDate.now();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String timestamp = data.getKey();
                    String[] timestampArr = timestamp.split("-");
                    String timestampDate = timestampArr[0] + timestampArr[1] + timestampArr[2];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
                    LocalDate testDate = LocalDate.parse(timestampDate, formatter);

                    if(currTime.isEqual(testDate)) {
                        String query = (String) data.child("query").getValue();

                        Long calorie_db = (Long) data.child("calories").getValue();
                        if (calorie_db == null) {
                            calorie_db = last_calories;
                        }

                        int calories = calorie_db.intValue();

                        String label = timestampArr[3] + ":" + timestampArr[4] + "    " + query; // 5 spaces


                        String[] nutrientLabels = {"fat", "sat_fat", "cholesterol", "sodium",
                        "carbs", "fiber", "sugar", "protein", "potassium"};

                        double[] nutrientList = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};

                        if (data.child("potassium").getValue() == null) { // need to check last item due to async
                            nutrientList = lastNutrientList;
                        }
                        else{
                            for(int i=0; i < nutrientList.length; i++){
                                if (data.child( nutrientLabels[i] ).getValue() instanceof Long) {
                                    Long nutrientLong = (Long) data.child( nutrientLabels[i] ).getValue() ;
                                    nutrientList[i] = nutrientLong.doubleValue();
                                }
                                else {
                                    nutrientList[i] =(double) data.child( nutrientLabels[i] ).getValue();
                                }

                            }
                        }

//                        Double sat_fat = (Double) data.child("sat_fat").getValue();
//                        Double cholesterol = (Double) data.child("cholesterol").getValue();
//                        Double sodium = (Double) data.child("sodium").getValue();
//                        Double carbs = (Double) data.child("carbs").getValue();
//                        Double fiber = (Double) data.child("fiber").getValue();
//                        Double sugar = (Double) data.child("sugar").getValue();
//                        Double protein = (Double) data.child("protein").getValue();
//                        Double potassium = (Double) data.child("potassium").getValue();

                        Food food = new Food(query, calories, nutrientList[0], nutrientList[1],
                                nutrientList[2],nutrientList[3],nutrientList[4],nutrientList[5],
                                nutrientList[6],nutrientList[7],nutrientList[8]);
                        map.put(label, food);
                        listData.add(label);
                    }
                }
                listDataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    public void writeFood(Food food) {
        String timestamp = dateFormat.format(new Date());
        myRef.child(username).child(timestamp).child("query").setValue(food.query);
        myRef.child(username).child(timestamp).child("calories").setValue(food.calories);

        myRef.child(username).child(timestamp).child("fat").setValue(food.fat);
        myRef.child(username).child(timestamp).child("sat_fat").setValue(food.sat_fat);
        myRef.child(username).child(timestamp).child("cholesterol").setValue(food.cholesterol);
        myRef.child(username).child(timestamp).child("sodium").setValue(food.sodium);
        myRef.child(username).child(timestamp).child("carbs").setValue(food.carbs);
        myRef.child(username).child(timestamp).child("fiber").setValue(food.fiber);
        myRef.child(username).child(timestamp).child("sugar").setValue(food.sugar);
        myRef.child(username).child(timestamp).child("protein").setValue(food.protein);
        myRef.child(username).child(timestamp).child("potassium").setValue(food.potassium);
    }

    // When user click list item, this method will be invoked.
    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        // Get the list data adapter.
        ListAdapter listAdapter = listView.getAdapter();
        // Get user selected item object.
        Object selectItemObj = listAdapter.getItem(position);
        String itemText = (String)selectItemObj;

        // Create an AlertDialog to show.
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        Food food = map.get(itemText);

        String msg = "Nutrition Information for " + food.query + "\n\n";
        msg += "Calories: " + food.calories + "\n\n";

        msg += "Total Fat: " + food.fat + "g" + "\n";
        msg += "Saturated Fat: " + food.sat_fat + "g" + "\n\n";

        msg += "Cholesterol: " + food.cholesterol + "mg" + "\n\n";
        msg += "Sodium: " + food.sodium + "mg" + "\n\n";

        msg += "Total Carbohydrates: " + food.carbs + "g" + "\n";
        msg += "Dietary Fiber: " + food.fiber + "g" + "\n";
        msg += "Sugar: " + food.sugar + "g" + "\n\n";

        msg += "Protein: " + food.protein + "g" + "\n\n";
        msg += "Potassium: " + food.potassium + "mg";

        alertDialog.setMessage(msg);

        alertDialog.show();
    }

    public void onClickBtn(View v) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            EditText edit = (EditText)findViewById(R.id.textEdit);
            String query = edit.getText().toString();
            //query = "1 cup sugar, 2 cups lettuce";
            Food newFood = addNewRecord(query);
            // add to firebase storage
            if (newFood != null) {
                writeFood(newFood);
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setMessage("Invalid query, please try again!");
                alertDialog.show();
            }

        }
    }

    protected Food addNewRecord(String query){
        // make post request upon adding new record, return new item
        // iterate through all foods in list, add up for food item to return
        try {
            URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            // comma separated query
            byte[] out = String.format("{\"query\":\"%s\"}",query).getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.setRequestProperty("x-app-id", "ab98843e");
            http.setRequestProperty("x-app-key", "6f401d3282325c9fc04c352acc04741a");
            http.setRequestProperty("x-remote-user-id", "0");
            http.connect();
            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((http.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            String response = sb.toString();

//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setMessage(response);
//            alertDialog.show();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray foods = jsonObject.getJSONArray("foods");

            int totalCalories = 0;
            double[] nutrientTotalList = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            String[] nutrientLabels = {"nf_total_fat", "nf_saturated_fat", "nf_cholesterol",
            "nf_sodium", "nf_total_carbohydrate", "nf_dietary_fiber", "nf_sugars",
            "nf_protein", "nf_potassium"};

            for(int i = 0; i < foods.length(); i++) {
                JSONObject food = foods.getJSONObject(i);
                totalCalories = totalCalories + food.getInt("nf_calories");

                for(int j=0; j < nutrientTotalList.length; j++){
                    nutrientTotalList[j] = nutrientTotalList[j] + food.getDouble( nutrientLabels[j] );
                }

            }

            // food= query + calories
            Food newFood = new Food(query, totalCalories, nutrientTotalList[0], nutrientTotalList[1],
                    nutrientTotalList[2],nutrientTotalList[3],nutrientTotalList[4],
                    nutrientTotalList[5],nutrientTotalList[6],nutrientTotalList[7],
                    nutrientTotalList[8]);

//            String message = String.format("Added %s to firebase storage. Meal contains %s calories.",
//                    query, Integer.toString(totalCalories));
//
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setMessage(message);
//            alertDialog.show();

            last_calories = new Long(newFood.calories);
            lastNutrientList = nutrientTotalList;

            return newFood;

        } catch (IOException | JSONException e) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setMessage("IO/JSON Exception");
//            alertDialog.show();
            Log.w("============IO ERROR", query);

        }
            return null;
    }

}