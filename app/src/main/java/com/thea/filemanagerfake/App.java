package com.thea.filemanagerfake;

import android.app.Application;
import android.content.Context;


/**
 * Created by Thea on 10/18/2016.
 */

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
