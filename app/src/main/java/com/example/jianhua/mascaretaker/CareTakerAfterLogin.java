package com.example.jianhua.mascaretaker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Map;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.*;
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
import java.util.ArrayList;


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
        //TODO show associate account
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //get all assocaite account
        mDatabase.child("associate-account").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                ((TextView)findViewById(R.id.ass_account_name)).setText(map.keySet().toString());
                Log.d("here====>",map.keySet().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        Button addNewAssociate = (Button) findViewById(R.id.add_new_associate_account_button);
        addNewAssociate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO


                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String masteraccount = extras.getString("masterAccount");
                    String userId = extras.getString("userId");
                    if(userId == null || userId.length() == 0)
                        userId = "0";

                    //The key argument here must match that used in the other activity

                    int int_userId = Integer.parseInt(userId);
                    userId = String.valueOf(++int_userId);
                    Intent careTakerAfterLoginIntent = new Intent(CareTakerAfterLogin.this, CreateAssociateAccountActivity.class);

                    Bundle extras2 = new Bundle();
                    extras2.putString("masterAccount", masteraccount);
                    extras2.putString("userId", userId);
                    careTakerAfterLoginIntent.putExtras(extras2);
                    Log.d("create-associate-ac!!", masteraccount);

                    startActivity(careTakerAfterLoginIntent);
                }

            }


        });

    }



}

