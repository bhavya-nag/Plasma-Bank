package com.example.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

    }
    EditText Username, Password;

    public void SignIn(View view){

        Username=(EditText) findViewById(R.id.username1);
        Password=(EditText) findViewById(R.id.password);
        Intent intent = new Intent(getApplicationContext(), DonorsActivity.class);


            Amplify.Auth.signIn(
                    Username.getText().toString(),
                    Password.getText().toString(),
                    result ->{
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    if(result.isSignInComplete()) startActivity(intent);
                    },
                    error ->{ Log.e("AuthQuickstart", error.toString());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }); }
            );
    }

    public void SignUp(View view){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
