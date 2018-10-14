package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button careTakerButton = (Button) findViewById(R.id.careTakerButton);
        careTakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, CareTakerLoginActivity.class);
                startActivity(registerIntent);
            }
        });
        final Button seniorButton = (Button) findViewById(R.id.seniorButton);
        seniorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, SeniorLoginActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
