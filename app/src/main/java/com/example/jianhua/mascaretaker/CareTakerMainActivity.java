package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CareTakerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_main);

        Button mCreateAssociateAcountButton = (Button)findViewById(R.id.create_associate_account_button);
        mCreateAssociateAcountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAssociateIntent = new Intent(CareTakerMainActivity.this, CreateAssociateAccountActivity.class);
                startActivity(createAssociateIntent);

            }
        });
    }
}
