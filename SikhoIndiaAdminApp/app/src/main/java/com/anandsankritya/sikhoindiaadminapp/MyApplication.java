package com.anandsankritya.sikhoindiaadminapp;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);  // init some SDK, MyApplication is the Context
    }
}
