package com.example.bettinabenitez.bobapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    public static TextView signup;
    EditText email, password;

    String GmailAddress, Password;
    User user = new User();
    int access = 0;

    private static String url = "http://10.0.2.2:8080/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signup = (TextView) findViewById(R.id.signup);
        loginbtn = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //when button is clicked
                user.setEmailAddress(email.getText().toString());
                getPost(); //send and get json data
                if (access == 1) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void getPost() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailAddress = email.getText().toString();
                    Password = password.getText().toString();

                    //CONNECTING TO URL --> send/retrieve JSON data
                    URL url = new URL("http://10.0.2.2:8080/login"); //10.0.2.2 => localhost
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //POST request method, from python code
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    //Converting Strings into JSON format and adding them to the jsonParam Obeject
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("GmailAddress", GmailAddress);
                    jsonParam.put("Password", Password);

                    //Sending JSON Objects to HTTP SERIALISATION
                    DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                    outputStream.writeBytes(jsonParam.toString());

                    outputStream.flush();
                    outputStream.close();

                    //Retrieves JSON Objects from HTTP DESERIALISATION
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    //Creates a string builder used to convert JSON data to string
                    StringBuilder sb = new StringBuilder();
                    String line;
                    //Reads the JSON data
                    line = bufferedReader.readLine();

                    //checks if no JSON data was send, access is denied
                    if (line == null) {
                        access = 0; // cannot move on to the next activity
                        Log.e("ACCESS", "denied");
                        signup.setText("Incorrect Email and/or Password");
                    }

                    //while line is not = null, add the JSON data to the string builder
                    while (line != null) {
                        sb.append(line+"\n");
                        Log.e("ACCESS:", line); //checks the access through logcat
                        line = bufferedReader.readLine();
                        access = 1; //moves to the next activity
                    }

                    bufferedReader.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
