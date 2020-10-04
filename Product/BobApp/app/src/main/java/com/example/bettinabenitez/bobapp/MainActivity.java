package com.example.bettinabenitez.bobapp;

import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.support.annotation.NonNull;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button update;
    int access = 1;
    public String url;
    TextView performerNext, performerNow;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.schedule:
                    Schedule fragment = new Schedule();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home, fragment, "FragmentName");
                    fragmentTransaction.commit();
                    return true;
                case R.id.raffle:
                    Raffle fragment2 = new Raffle();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.home, fragment2, "FragmentName");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.voting:
                    Voting fragment3 = new Voting();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.home, fragment3, "FragmentName");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.live_stream:
                    LiveStream fragment4 = new LiveStream();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.home, fragment4, "FragmentName");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }

    };

    Button click;
    public static TextView data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nv);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Schedule fragment = new Schedule();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home, fragment, "FragmentName");
        fragmentTransaction.commit();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


}
