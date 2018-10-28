package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class MainActivity extends AppCompatActivity {
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

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
