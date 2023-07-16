package com.example.courseshare;

import android.app.Application;

import com.example.courseshare.utilities.SignalGenerator;

public class App extends Application
{
    public void onCreate()
    {
        super.onCreate();
        SignalGenerator.init(this);
    }
}
