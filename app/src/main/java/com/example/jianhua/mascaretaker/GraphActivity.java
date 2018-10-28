package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        Map<Date, Integer> hashMap = (HashMap<Date, Integer>)intent.getSerializableExtra("dateMap");

        TreeMap<Date, Integer> map = new TreeMap<Date, Integer>(hashMap);
        Log.w("MAP", map.toString());

        // min, max
        Long minLong = getIntent().getLongExtra("minDateLong", 0);
        Long maxLong = getIntent().getLongExtra("maxDateLong", 0);

        Date minDate = Date.from(Instant.ofEpochSecond(minLong));
        Date maxDate = Date.from(Instant.ofEpochSecond(maxLong));

        DataPoint[] pointList = new DataPoint[map.size()];

        int count = 0;
        for (Map.Entry<Date, Integer> entry : map.entrySet()) {
            Date date = entry.getKey();
            Integer calories = entry.getValue();

            DataPoint newPoint = new DataPoint(date, calories);
            pointList[count] = newPoint;
            count++;
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointList);

        Log.w("series", series.toString());

        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Dates");
        gridLabel.setVerticalAxisTitle("Nutrient (calories)"); // can pass in as intent which nutrient

        gridLabel.setPadding(50); // IMPORTANT to not cut off x axis

        gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        gridLabel.setNumHorizontalLabels(2);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(minDate.getTime());
        graph.getViewport().setMaxX(maxDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        String title = getIntent().getExtras().getString("title");
        graph.setTitle(title);

        graph.addSeries(series);
    }
}
