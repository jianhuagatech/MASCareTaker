package com.example.jianhua.mascaretaker;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.*;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Yuhao Lan
 */

public class CareTakerAfterLogin extends AppCompatActivity {


    private static final String TAG = "CareTakerAfterLogin";

    private ListView listView;
    private ArrayList<String> seniorNameList = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_after_login);
        //TODO show associate account
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) findViewById(R.id.senior_list);
        final ArrayAdapter mAdapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                seniorNameList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tView = (TextView) view.findViewById(android.R.id.text1);
                tView.setTextColor(Color.parseColor("#FE5E08"));
                tView.setTypeface(Typeface.MONOSPACE);
                tView.setTextSize(24);
                return view;
            }
        };

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        goToSenior(position);
                    }
                }
        );

        //get all assocaite account
        mDatabase.child("associate-account").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                seniorNameList.addAll(map.keySet());
                mAdapter.notifyDataSetChanged();
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

    private void goToSenior(int index) {
        System.out.print(index);
    }

}

