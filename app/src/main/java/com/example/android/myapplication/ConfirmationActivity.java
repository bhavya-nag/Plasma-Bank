package com.example.android.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
    }

    EditText Username, Code;

    public void confirm(View view) {
        Username = (EditText) findViewById(R.id.username2);
        Code = (EditText) findViewById(R.id.code);
        Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);

            Amplify.Auth.confirmSignUp(
                    Username.getText().toString(),
                    Code.getText().toString(),
                    result ->{ Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    if(result.isSignUpComplete()){ String Email= getIntent().getStringExtra("Email");
                   // intent.putExtra("Email", Email);
                        startActivity(intent);}
                    },
                    error ->{ Log.e("AuthQuickstart", error.toString());
                         runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Sign up not successful. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                    });


    });

    }
}