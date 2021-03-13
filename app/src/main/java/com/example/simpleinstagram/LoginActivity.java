package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check if a user is already login, if they are go to main activity
        // (don't need to login again)
        if (ParseUser.getCurrentUser() != null) {
            gotoMainActivity();
        }

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login!");
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick SignUp");
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                signUpUser(username, password);
            }
        });

    }

    // TODO check if user already exists
    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to create a user: " + username);
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                // if null is not empty, there is an error
                if(e != null) {
                    Toast.makeText(LoginActivity.this, "Sign up failed: " + e, Toast.LENGTH_SHORT).show();
                    return;
                }
                // else continue
                    gotoMainActivity();
                    Toast.makeText(LoginActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        // navigate to main activity if the user has signed in properly
        // use LogInInBackground to make a new thread - does not run in a new UI
        // this takes in a user, pass, and callback for the login state since we
        // are making a network request (run the operation in the bg)
        Log.i(TAG, "Attempting to login: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            // handle the login
            @Override
            public void done(ParseUser user, ParseException e) {
                // if there was an exception throw e (empty if there is not)
                if(e != null){
                    Log.i(TAG, "Issue with login.", e);
                    Toast.makeText(LoginActivity.this, "Issue with login", Toast.LENGTH_SHORT).show();
                    // go back
                    return;
                }
                // else login was successful
                else {
                    gotoMainActivity();
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoMainActivity() {
        // this is referring to the activity and the activity is an instance of a context
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // go from login to main
        startActivity(intent);
        // prevent user from coming back through the back stack
        finish();
    }
}