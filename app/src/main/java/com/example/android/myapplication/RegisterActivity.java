package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    EditText Username, Password, Email;

    public void newUser(View view) {


        Username = (EditText) findViewById(R.id.username1);
        Password = (EditText) findViewById(R.id.password1);
        Email = (EditText) findViewById(R.id.email);

        Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
        intent.putExtra("Email", Email.getText().toString());

             Amplify.Auth.signUp(
                    Username.getText().toString(),
                    Password.getText().toString(),
                    AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), Email.getText().toString()).build(),
                    result -> startActivity(intent),
                    error -> {
                        Log.e("AuthQuickStart", "Sign up failed", error);

                        final String msg = "Sign up failed! Password should be atleast 6 characters long containing and must a lowercase letter and a number";
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Username.setText(""); Password.setText(""); Email.setText("");
                    }
                    );

    }
}
