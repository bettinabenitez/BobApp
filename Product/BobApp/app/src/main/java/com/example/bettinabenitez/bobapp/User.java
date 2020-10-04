package com.example.bettinabenitez.bobapp;

import android.util.Log;
/**
 * Created by bettinabenitez on 19/02/2018.
 */

public class User {

    private static String emailAddress;
    private String bandPerformer;
    private String vocalPerformer;


    public static String getEmailAddress() {
        return emailAddress;
    }

    public String getBandPerformer() {
        return bandPerformer;
    }

    public String getVocalPerformer() { return vocalPerformer; }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setBandPerformer(String bandPerformer) {
        this.bandPerformer = bandPerformer;
    }

    public void setVocalPerformer(String vocalPerformer) {
        this.vocalPerformer = vocalPerformer;
    }
}
