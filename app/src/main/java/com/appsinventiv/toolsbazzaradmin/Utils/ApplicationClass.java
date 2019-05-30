package com.appsinventiv.toolsbazzaradmin.Utils;

import android.app.Application;

/**
 * Created by AliAh on 11/04/2018.
 */

public class ApplicationClass extends Application {
    private static  ApplicationClass instance;

    public static ApplicationClass getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    @Override
    public void onTerminate() {
        CommonUtils.showToast("Terminated");
        super.onTerminate();

    }
}
