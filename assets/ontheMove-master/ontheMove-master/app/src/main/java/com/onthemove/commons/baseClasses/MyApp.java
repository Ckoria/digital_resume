package com.onthemove.commons.baseClasses;

import android.app.Application;

public class MyApp extends Application {
    private static MyApp myApp;

    public static MyApp getMyApp() {
        return myApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }
}
