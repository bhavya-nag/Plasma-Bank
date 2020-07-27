package com.example.android.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amplifyframework.core.Amplify;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import static com.amplifyframework.core.Amplify.Analytics;

public class ResultsList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        try {
            setData();
        } catch (Exception e) {
            Log.i("setdata", e.toString());
        }
    }

    ListView listView;

    public void setData() {
        TextView showingText = (TextView) findViewById(R.id.showingTextView);
        listView = (ListView) findViewById(R.id.myList);

        Intent intent = getIntent();
        String[] doc = intent.getStringArrayExtra("DocList");
        String[] dev = intent.getStringArrayExtra("Devices");
        showingText.setText("Showing " + doc.length + " donors");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doc);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendMessage(dev[i], Amplify.Auth.getCurrentUser().getUsername());

            }
        });



    }

}
