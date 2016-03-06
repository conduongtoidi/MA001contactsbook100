package com.whatsoft.contactbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whatsoft.contactbook.utils.Log;

/**
 * Created by mb on 3/5/16
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ContactsBook";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(DATABASE_NAME);
        Log.d(String.valueOf(DATABASE_VERSION));

        tableContact = new TableContact(this);
    }

    TableContact tableContact;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableContact.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TableContact.SQL_DROP_TABLE);
        onCreate(db);
    }

    public TableContact getTableContact() {
        return tableContact;
    }
}
