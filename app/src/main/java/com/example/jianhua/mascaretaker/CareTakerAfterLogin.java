package com.example.jianhua.mascaretaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Yuhao Lan
 */

public class CareTakerAfterLogin extends AppCompatActivity {


    private static final String TAG = "CareTakerAfterLogin";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_after_login);

        Button addNewAssociate = (Button) findViewById(R.id.add_new_associate_account_button);
        addNewAssociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Intent careTakerAfterLoginIntent = new Intent(CareTakerAfterLogin.this, CreateAssociateAccountActivity.class);
                startActivity(careTakerAfterLoginIntent);
                //setContentView(R.layout.activity_care_taker_after_login);
            }
        });

    }



}

