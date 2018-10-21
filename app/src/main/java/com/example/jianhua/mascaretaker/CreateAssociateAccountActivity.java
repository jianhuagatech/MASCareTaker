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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Chai on 2018/10/14.
 */
class Associate {

    public String username;
    public String email;
    public String usertype;
    public String masterAccountName;
    public ArrayList<String> foodlist = new ArrayList<>();


    public Associate() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Associate( String email, String usertype, String masterAccountName, ArrayList<String> foodlist) {
        this.email = email;
        this.usertype = usertype;
        this.masterAccountName = masterAccountName;
        this.foodlist = foodlist;

    }
    public String getName() {
        return this.email;
    }
}

public class CreateAssociateAccountActivity extends AppCompatActivity {

    //    private EditText mNameView;
//    private EditText mGenderView;
//    private EditText mAgeView;
//    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private static final String TAG = "CreateAssociateAccount";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_associate_account);


        mAuth = FirebaseAuth.getInstance();
        // TODO: forget about the UI
//        mNameView = (EditText) findViewById(R.id.associate_name);
//        mGenderView = (EditText) findViewById(R.id.associate_gender);
//        mAgeView = (EditText) findViewById(R.id.associate_age);
//        mUsernameView = (EditText) findViewById(R.id.associate_username);
        mEmailView = (EditText) findViewById(R.id.associate_email);
        mPasswordView = (EditText) findViewById(R.id.associate_password);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button mCreateButton = (Button) findViewById(R.id.create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: CHECK VALID INPUT
//                String name = mNameView.getText().toString();
//                String gender = mGenderView.getText().toString();
//                String age = mAgeView.getText().toString();
//                String username = mUsernameView.getText().toString();
                registerAssociateUser();


                // TODO: Delete (for demo, just switch to daily record activity)
                //get the master email
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    //The key argument here must match that used in the other activity
                    String masteraccount = extras.getString("masterAccount");
                    Log.d("create-ac-to-careafter",masteraccount);

                    Intent myIntent = new Intent(CreateAssociateAccountActivity.this, CareTakerAfterLogin.class);
                    myIntent.putExtra("masterAccount", masteraccount);
                    startActivity(myIntent);

                }
            }
        });
    }

    private void registerAssociateUser() {
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //Todo : move this part inside them Auth.createUserWithEmailAndPassword(email, password)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
            String masteraccount = extras.getString("masterAccount");
            String temp_usr_id = extras.getString("userId");
            if(temp_usr_id == null || temp_usr_id.length() == 0)
                temp_usr_id = "0";

            Log.d("what's wrong", temp_usr_id);
            int int_userid = Integer.parseInt(temp_usr_id);

            Log.d("create-associate-ac", masteraccount);

            writeAssociate(email, "associate", masteraccount, new ArrayList<String>());


            Intent careTakerAfterLoginIntent = new Intent(CreateAssociateAccountActivity.this, CareTakerAfterLogin.class);
            Bundle extras1 = new Bundle();
            extras1.putString("masterAccount", masteraccount);
            extras1.putString("userId", String.valueOf(int_userid));
            careTakerAfterLoginIntent.putExtras(extras1);
            Log.d("create-associate-ac!!", masteraccount);
            startActivity(careTakerAfterLoginIntent);
        }
        //////////
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CreateAssociateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createAssociateUser:success");
                                Toast.makeText(CreateAssociateAccountActivity.this, "Authentication succeeded.",
                                        Toast.LENGTH_SHORT).show();
                                // TODO: THE AUTHENTICATION HAS SOMETHING WORNG HERE! Firebase cannot store the AUTHENTICATION data
                                //store to the database

                                //get the master email



                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createAssociateUser:failure", task.getException());
                                Toast.makeText(CreateAssociateAccountActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    // TODO: simply copy and paste from CareTakerLoginActivity, should move to Utils
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private void writeAssociate(String email, String usertype, String masterAccountName, ArrayList<String> foodlist) {
        Associate associateUser = new Associate(email, usertype, masterAccountName, foodlist);
        String[] parts = email.split("@");
        mDatabase.child("associate-account").child(parts[0]).setValue(associateUser);
    }

}

