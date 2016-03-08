package com.whatsoft.contactbook.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.whatsoft.contactbook.database.DatabaseHelper;

/**
 * Created by mb on 3/4/16
 */
public class MyApplication extends Application {
    private DatabaseHelper databaseHelper;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        databaseHelper = new DatabaseHelper(this);
        instance = this;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
