package com.example.bettinabenitez.bobapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Context;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voting extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Button vote;
    TextView message;
    String GmailAddress, BandVote, VocalVote;
    int count = 0;
    User user = new User();

    public Voting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_voting, container, false);

        vote = (Button) v.findViewById(R.id.button3);
        message = v.findViewById(R.id.errormessage);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinnerBand);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter;
        //R.layout.spinner_bands is the layout I created see PATH: res > layout > spinner_vocals
        adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Bands, R.layout.spinner_bands);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinnerVocal);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Vocals, R.layout.spinner_vocals);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        vote.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button3) {
            setVote();

            if (count == 0) {
                message.setText("You've placed your vote");
                count = 1;
            }
            else if (count == 1) {
                message.setText("You've already placed your vote");
            }

        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);
        Log.e("ParentId", Integer.toString(parent.getId()));
        switch (parent.getId()){
            case 2131624104:
                user.setVocalPerformer(parent.getItemAtPosition(pos).toString());
                Log.e("BAND", user.getVocalPerformer());
                Log.e("email", user.getEmailAddress());
                break;

            case 2131624108:
                user.setBandPerformer(parent.getItemAtPosition(pos).toString());
                Log.e("BAND", user.getBandPerformer());
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setVote() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    GmailAddress = user.getEmailAddress();
                    BandVote = user.getBandPerformer();
                    VocalVote = user.getVocalPerformer();


                    //CONNECTING TO URL --> send/retrieve JSON data
                    URL url = new URL("http://10.0.2.2:8080/voting"); //10.0.2.2 => localhost
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
                    jsonParam.put("BandVote", BandVote);
                    jsonParam.put("VocalVote", VocalVote);

                    //Sending JSON Objects to HTTP SERIALISATION
                    DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                    outputStream.writeBytes(jsonParam.toString());

                    outputStream.flush();
                    outputStream.close();

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
