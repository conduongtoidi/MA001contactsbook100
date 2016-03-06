package com.whatsoft.contactbook.base;

import com.whatsoft.contactbook.database.DatabaseHelper;

public class BaseTable {

    protected DatabaseHelper mDatabaseHelper;

    public BaseTable(DatabaseHelper dh) {
        mDatabaseHelper = dh;
    }

}
