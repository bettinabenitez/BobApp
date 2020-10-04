package com.example.bettinabenitez.bobapp;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
public class Schedule extends Fragment implements View.OnClickListener {

    Button update;
    int access = 1;
    public String url;
    TextView performerNext, performerNow;
    int click = 0;

    public Schedule() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        update = (Button) v.findViewById(R.id.update);
        performerNow = (TextView) v.findViewById(R.id.PerformerNow);
        performerNext = (TextView) v.findViewById(R.id.PerformerNext);
        update.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) { //when button is clicked
        switch (v.getId()) {
            case R.id.update:
                if (access == 1) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                connects to the url
                                URL url = new URL("http://10.0.2.2:8080/schedule");
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
                                    jsonObject.put("Performer_Name", jsonArray);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray performerArray = jsonObject.getJSONArray("Performer_Name");
                                String performingNow = performerArray.getString(0);
                                String performingNext = performerArray.getString(1);

                                //String json = jsonObject.getString("Performer_Name");

                                Log.e("ERR", performingNow);
                                performerNow.setText(performingNow);
                                performerNext.setText(performingNext);

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
