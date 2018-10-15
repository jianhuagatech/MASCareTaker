package com.example.jianhua.mascaretaker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;


// https://www.dev2qa.com/android-listactivity-example/
public class DailyRecordActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_record);

        // Create a list data which will be displayed in inner ListView.
        List<String> listData = new ArrayList<String>();

        // just add query name!
        listData.add("Apple");
        listData.add("Orange");
        listData.add("Yogurt");
        listData.add("1 cup sugar, 2 pieces of bread");
        listData.add("Chicken sandwich");
        listData.add("Tomato soup");

        // Create the ArrayAdapter use the item row layout and the list data.
        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this, R.layout.activity_daily_record_row, R.id.listRowTextView, listData);

        // Set this adapter to inner ListView object.
        this.setListAdapter(listDataAdapter);



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
        alertDialog.setMessage(itemText);
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

            for(int i = 0; i < foods.length(); i++) {
                JSONObject food = foods.getJSONObject(i);
                totalCalories = totalCalories + food.getInt("nf_calories");
            }

            // food= query + calories
            Food newFood = new Food(query, totalCalories);

            String message = String.format("Added %s to firebase storage. Meal contains %s calories.",
                    query, Integer.toString(totalCalories));

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage(message);
            alertDialog.show();

            return newFood;

        } catch (IOException | JSONException e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("IO/JSON Exception");
            alertDialog.show();
        }
            return new Food();
    }

}