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

        Button mMangeSeniorsButton = (Button) findViewById(R.id.manage_associate_account_button);
        mMangeSeniorsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String masteraccount = extras.getString("masterAccount");
                    //The key argument here must match that used in the other activity
                    Log.d("masterAccount-manage",masteraccount);
                    Intent careTakerAfterLoginIntent = new Intent(CareTakerMainActivity.this, CareTakerAfterLogin.class);
                    careTakerAfterLoginIntent.putExtra("masterAccount",masteraccount);
                    startActivity(careTakerAfterLoginIntent);
                }


                //setContentView(R.layout.activity_care_taker_after_login);
            }
        });

        Button mCreateAssociateAcountButton = (Button)findViewById(R.id.create_associate_account_button);
        mCreateAssociateAcountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String masteraccount = extras.getString("masterAccount");
                    Log.d("masterAccount-create",masteraccount);
                    //The key argument here must match that used in the other activity
                    Intent createAssociateIntent = new Intent(CareTakerMainActivity.this, CreateAssociateAccountActivity.class);
                    createAssociateIntent.putExtra("masterAccount",masteraccount);
                    startActivity(createAssociateIntent);
                }



            }
        });


    }
}
