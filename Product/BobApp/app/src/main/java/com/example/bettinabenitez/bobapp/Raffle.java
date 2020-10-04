package com.example.bettinabenitez.bobapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Raffle extends Fragment implements View.OnClickListener {

    Button grandButton, regularButton;
    int access = 1;
    int gclick = 0;
    int rclick = 0;
    public String url;
    TextView grandWinner, regularWinner;

    public Raffle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_raffle, container, false);

        grandButton = v.findViewById(R.id.grandButton);
        grandWinner = v.findViewById(R.id.grandWinner);
        regularButton = v.findViewById(R.id.regularButton);
        regularWinner = v.findViewById(R.id.regularWinner);
        grandButton.setOnClickListener(this);
        regularButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grandButton:
                if (access == 1) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                connects to the url
                                URL url = new URL("http://10.0.2.2:8080/raffle");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
                                conn.setRequestMethod("GET");
                                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);


                                //Deserialisation
                                InputStream inputStream = conn.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                                StringBuilder sb = new StringBuilder();
                                String line = bufferedReader.readLine();
                                JSONArray jsonArray = new JSONArray(line);
                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("RaffleWinner", jsonArray);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray performerArray = jsonObject.getJSONArray("RaffleWinner");
                                String gWinner = performerArray.getString(0);
                                String regwinner = performerArray.getString(1);


                                grandWinner.setText(gWinner);
                                regularWinner.setText(regwinner);

                                if (line != null) {
                                    sb.append(line+"\n");
                                    Log.e("ERR", line);
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

                break;
        }

    }
}
