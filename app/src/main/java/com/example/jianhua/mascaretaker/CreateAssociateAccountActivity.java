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

/**
 * Created by Chai on 2018/10/14.
 */

public class CreateAssociateAccountActivity extends AppCompatActivity {

    //    private EditText mNameView;
//    private EditText mGenderView;
//    private EditText mAgeView;
//    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private static final String TAG = "CreateAssociateAccount";

    private FirebaseAuth mAuth;

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
            }
        });
    }

    private void registerAssociateUser() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

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
                                // TODO: SHOW NEXT PAGE
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

}

