package com.example.android.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

interface AsyncResponse {
    void processFinish(List<Document> doc);
}

class query extends AsyncTask<String, Void, List<Document>> {
    AsyncResponse delegate = null;

    @Override
    protected List<Document> doInBackground(String... strings) {
        List<Document> doc = null;

      //  try {
            DatabaseAccess acc = new DatabaseAccess();
            doc = acc.getAllItems(strings[0], strings[1]);
            System.out.println(doc.toString());
       // } catch (Exception e) {
          //  Log.i("query", e.getCause().toString());
        //}

        return doc;
    }

    @Override
    protected void onPostExecute(List<Document> documents) {
        super.onPostExecute(documents);
        Log.i("query", "completed");
        try {
            delegate.processFinish(documents);
        } catch (Exception e) {
            Log.i("onpostExecute", e.toString());
        }
    }
}



public class DonorsActivity extends AppCompatActivity implements AsyncResponse {
    EditText BloodGroup, PinCode; Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors);
        Log.i("donors", "started");

    }

    public void search(View view){
        System.out.println("clicked");
        BloodGroup= (EditText) findViewById(R.id.searchBloodEditText);
        PinCode=(EditText) findViewById(R.id.searchPinEditText);
        intent= new Intent(getApplicationContext(), ResultsList.class);

      //  try{
            query q= new query(); q.delegate=this;
            Log.i("donors", "do this");
            q.execute(BloodGroup.getText().toString(), PinCode.getText().toString());

      //  }
        // catch(Exception e){
        //Log.i("search", e.getCause().toString());
        //}
    }


    @Override
    public void processFinish(List<Document> doc) {
        try {
            System.out.println("done");
            if(doc==null){
                intent.putExtra("DocList", new String[0]);
                intent.putExtra("Count", 0);
                startActivity(intent); return;
            }
            ArrayList<String> arr= new ArrayList<>();
            ArrayList<String> device= new ArrayList<>();
            for(Document d:doc) {

                AttributeValue BG = d.get("Blood Group").convertToAttributeValue();
                AttributeValue PC = d.get("Pin Code").convertToAttributeValue();
                AttributeValue Add = d.get("Address").convertToAttributeValue();
                AttributeValue Contact = d.get("Contact").convertToAttributeValue();
                AttributeValue donor = d.get("isDonor").convertToAttributeValue();
                AttributeValue Name= d.get("Name").convertToAttributeValue();
                AttributeValue DeviceToken= d.get("Device Token").convertToAttributeValue();
                if (BG!=null && PC!=null && donor!=null &&
                BG.getS().equals(BloodGroup.getText().toString()) && PC.getS().equals(PinCode.getText().toString()) && donor.getS().equals("Y")) {
                   arr.add(BG.getS()+" : "+Name.getS()+"\n"+Add.getS()+"\n"+Contact.getS());
                   device.add(DeviceToken.getS());
                }
            }
            String[] str= new String[arr.size()]; int i=0;
            String[] dev= new String[arr.size()];
            for(String s: arr){
                str[i]=s; i++; dev[i]=device.get(i);
            }
            intent.putExtra("DocList", str);
            intent.putExtra("Devices", dev);
            startActivity(intent);
        }
        catch(Exception e){
            Log.i("Error", "processFinish:"+e);
        }
    }
}
