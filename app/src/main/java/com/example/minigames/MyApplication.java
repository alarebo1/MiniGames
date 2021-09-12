package com.example.minigames;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context sContext;
    private static MyApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return sContext;
    }
}
