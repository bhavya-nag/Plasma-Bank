package com.example.android.myapplication;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amplifyframework.analytics.UserProfile;
import com.amplifyframework.core.Amplify;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.core.content.ContextCompat.startActivity;
import static com.amplifyframework.core.Amplify.Analytics;
import static com.amplifyframework.core.Amplify.Auth;

public class ProfileSetupActivity extends AppCompatActivity {

    String Username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Username=Auth.getCurrentUser().getUsername();
        TextView welcome= (TextView) findViewById(R.id.welcomeTextView);
        welcome.setText("Welcome "+Username+"!");
    }

    public void registerUser(View view){
       // String email=getIntent().getStringExtra("Email");
        EditText Name= (EditText) findViewById(R.id.nameEditText);
        EditText Address= (EditText) findViewById(R.id.addressEditText);
        EditText Contact= (EditText) findViewById(R.id.contactEditText);
        EditText BloodGroup= (EditText) findViewById(R.id.bloodEditText);
        EditText PinCode= (EditText) findViewById(R.id.pinEditText);
        boolean isDonor= ((CheckBox)findViewById(R.id.donorCheckBox)).isChecked();

            UserProfile profile = UserProfile.builder()
                    .name(Name.getText().toString())
                 //   .email(email)
                    .build();
        Document doc= new Document();
//try {


       // String userId = Amplify.Auth.getCurrentUser().getUserId();;



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
                doc.put("Device Token", deviceToken);
                System.out.println("Token: "+deviceToken);
            }
        });

        doc.put("Username", Username);
        doc.put("Name", Name.getText().toString());
        doc.put("Address", Address.getText().toString());
        doc.put("Contact", Contact.getText().toString());
        doc.put("Blood Group", BloodGroup.getText().toString());
        doc.put("Pin Code", PinCode.getText().toString());
        if(isDonor) doc.put("isDonor", "Y");
        else doc.put("isDonor", "N");


        try {
            System.out.println("Executing");
            createEntry entry = new createEntry();
            entry.execute(doc);
            if(entry.getStatus()== AsyncTask.Status.FINISHED) {
                Toast.makeText(this, "Profile complete.", Toast.LENGTH_SHORT);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
           startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        catch(Exception e){
            System.out.println("Exception");
        }


    }
}

class createEntry extends AsyncTask<Document, Void, Void> {

    @Override
    protected Void doInBackground(Document... documents) {
        try {
            DatabaseAccess acc = new DatabaseAccess();
            acc.create(documents[0]);
            return null;
        }
        catch(Exception e){
            Log.d("error", "doInBackground: "+e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("done");
    }
}

