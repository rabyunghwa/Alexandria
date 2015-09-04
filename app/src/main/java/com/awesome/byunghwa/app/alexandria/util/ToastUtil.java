package com.awesome.byunghwa.app.alexandria.util;

import android.content.Context;
import android.widget.Toast;

import com.awesome.byunghwa.app.alexandria.MyApplication;

/**
 * Created by ByungHwa on 7/8/2015.
 */
public class ToastUtil {

    static Context context;

    public static void showToast(String toastContent) {
        context = MyApplication.applicationContext;
        if (toastContent != null) {
            Toast.makeText(context, toastContent, Toast.LENGTH_SHORT).show();
            LogUtil.log_i("info", "ToastUtil Gets Called");
        }
    }
}
