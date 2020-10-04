package com.example.bettinabenitez.bobapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.Writer;
import java.io.DataOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileWriter;

import org.json.JSONObject;
import org.json.JSONException;

import static junit.framework.Assert.assertEquals;


public class SignUpActivity extends AppCompatActivity {

    EditText email, firstName, lastName, password, userType;
    String FirstName, GmailAddress;
    String LastName;
    String Password, Email, UserType;
    TextView admin;
    String ipAddress = "10.70.0.180:8080" ;

    Button button4;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = (EditText) findViewById(R.id.emails);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        password = (EditText) findViewById(R.id.password);
        userType = (EditText) findViewById(R.id.usertype);
        admin = (TextView) findViewById(R.id.textView10);

        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPost();
                if (isConnectedToServer() == false) {
                    admin.setText("ERROR");
                }
                email.setText("");
                firstName.setText("");
                lastName.setText("");
                password.setText("");
                userType.setText("");
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                
            }
        });

    }

    public boolean isConnectedToServer() {
        try{
            URL url = new URL("http://" + ipAddress + "/postdata");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailAddress = email.getText().toString();
                    FirstName = firstName.getText().toString();
                    LastName = lastName.getText().toString();
                    Password = password.getText().toString();
                    UserType = userType.getText().toString();



                    URL url = new URL("http://10.0.2.2:8080/postdata");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("GmailAddress", GmailAddress);
                    jsonParam.put("FirstName", FirstName);
                    jsonParam.put("LastName", LastName);
                    jsonParam.put("Password", Password);
                    jsonParam.put("UserType", UserType);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


}


