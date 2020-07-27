package com.example.android.myapplication;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryFilter;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBEntry;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.mobileconnectors.dynamodbv2.document.internal.Key;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAccess {
    private static final String COGNITO_POOL_ID = "ap-south-1:3de59513-0e70-476f-894c-c4f96712df28";
    private static final Regions MY_REGION = Regions.AP_SOUTH_1;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;
    private Context context;
    private final String DYNAMODB_TABLE = "Donors";
    CognitoCredentialsProvider credentialsProvider;


    public DatabaseAccess() {

        System.out.println("Database ACcess");
        this.context =context;
        credentialsProvider = new CognitoCredentialsProvider (COGNITO_POOL_ID, MY_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(MY_REGION));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
        System.out.println("got it");

    }

    public void create(Document memo) {
        if (!memo.containsKey("creationDate")) {

            memo.put("creationDate", new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
        }
        dbTable.putItem(memo);
    }

    public Document getItem (String username){
        Document result = dbTable.getItem(new Primitive("Username"), new Primitive(username));
        return result;
    }

    public List<Document> getAllItems(String BloodGroup, String PinCode ) {

        Expression exp = new Expression().withExpressionAttibuteNames("#isDonor", "isDonor")
                .withExpressionAttibuteNames("#BloodGroup", "Blood Group")
                .withExpressionAttibuteNames("#PinCode", "Pin Code")
                .withExpressionAttibuteValues(":isDonor", new DynamoDBEntry() {
                    @Override
                    public AttributeValue convertToAttributeValue() {
                        return new AttributeValue().withS("Y");
                    }
                });

        if (BloodGroup.length() != 0) {
            exp.addExpressionAttributeValues(":BloodGroup", new DynamoDBEntry() {
                @Override
                public AttributeValue convertToAttributeValue() {
                    return new AttributeValue().withS(BloodGroup);
                }
            });

        }

        if (PinCode.length() != 0) {
            exp.addExpressionAttributeValues(":PinCode", new DynamoDBEntry() {
                @Override
                public AttributeValue convertToAttributeValue() {
                    return new AttributeValue().withS(PinCode);
                }
            });
        }

        Search items = dbTable.scan(exp);
        return items.getAllResults();
    }

}

