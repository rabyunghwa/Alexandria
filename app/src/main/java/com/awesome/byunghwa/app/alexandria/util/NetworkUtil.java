package com.awesome.byunghwa.app.alexandria.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.awesome.byunghwa.app.alexandria.MyApplication;

/**
 * Created by ByungHwa on 7/8/2015.
 */
public class NetworkUtil {

    private static Context context;

    public static boolean isNetworkAvailable() {
        context = MyApplication.applicationContext;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnectedOrConnecting());
    }
}
