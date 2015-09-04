package com.awesome.byunghwa.app.alexandria;

import android.app.Application;

/**
 * Created by ByungHwa on 7/8/2015.
 */
public class MyApplication extends Application {

    public static Application applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }
}
