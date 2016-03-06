package com.whatsoft.contactbook.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.whatsoft.contactbook.base.BaseTable;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mb on 3/5/16
 */
public class TableContact extends BaseTable {
    public static String TABLE_NAME = "table_contact";

    public static String KEY_ID = "id";
    public static String KEY_NAME = "name";
    public static String KEY_ADDRESS = "address";
    public static String KEY_RELATIONSHIP = "relationship";
    public static String KEY_ADDRESS_NO = "address_no";
    public static String KEY_HOME_NUMBER = "home_number";
    public static String KEY_NATION = "nation";
    public static String KEY_GENDER = "gender";
    public static String KEY_DOB = "dob";

    public static String KEY_IS_FAVORITE = "favorite";

    public static String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            KEY_ID + " TEXT PRIMARY KEY," +
            KEY_NAME + " TEXT," +
            KEY_ADDRESS + " TEXT," +
            KEY_ADDRESS_NO + " TEXT," +
            KEY_DOB + " TEXT," +
            KEY_GENDER + " TEXT," +
            KEY_HOME_NUMBER + " TEXT," +
            KEY_NATION + " TEXT," +
            KEY_RELATIONSHIP + " TEXT," +
            KEY_IS_FAVORITE + " INTEGER)";

    public TableContact(DatabaseHelper dh) {
        super(dh);
    }

    public void deleteAllRows() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public boolean insertRow(Contact row) {
        if (row == null) {
            return false;
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.insert(TABLE_NAME, null, changeToContent(row));
        return true;
    }

    public boolean insertRows(List<Contact> rows) {
        for (Contact r : rows) {
            insertRow(r);
        }

        return true;
    }

    /**
     * Get one row and convert to model
     *
     * @param id contact id
     * @return contact result
     */
    public Contact getRowById(String id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ?";

        Log.i(selectQuery);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        Contact contact = null;
        if (c != null && c.moveToFirst()) {
            try {
                contact = fetchToObject(c);
            } catch (Exception ex) {
                Log.e(ex);
            }
            c.close();
        }
        return contact;
    }

    /**
     * Get all row and convert to array list model
     *
     * @return contacts list
     */
    public List<Contact> getAllRows() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Log.i(selectQuery);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                contacts.add(fetchToObject(c));
            } while (c.moveToNext());
        }
        c.close();
        return contacts;
    }

    public List<Contact> filterBy(String column, String value) {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + column + " = ?";

        Log.i(selectQuery);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{value});

        // Looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                contacts.add(fetchToObject(c));
            } while (c.moveToNext());
        }
        c.close();
        return contacts;
    }

    /**
     * Update one row
     *
     * @param row contact
     * @return result
     */
    public boolean updateRow(Contact row) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.update(TABLE_NAME, changeToContent(row), KEY_ID + " = ?", new String[]{String.valueOf(row.getId())});
        return true;
    }

    public boolean containts(String id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ?";

        Log.i(selectQuery);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (c != null && c.moveToFirst()) {
            c.close();
            return true;
        }
        return false;
    }

    private ContentValues changeToContent(Contact row) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, row.getId());
        values.put(KEY_NAME, row.getName());
        values.put(KEY_ADDRESS, row.getAddress());
        values.put(KEY_ADDRESS_NO, row.getAddressNo());
        values.put(KEY_GENDER, row.getGender().getValue());
        values.put(KEY_RELATIONSHIP, row.getRelationship());
        values.put(KEY_HOME_NUMBER, row.getHomeNumber());
        values.put(KEY_DOB, row.getDayOfBirth());
        values.put(KEY_NATION, row.getNation());
        values.put(KEY_IS_FAVORITE, row.isFavorite() ? 1 : 0);
        return values;
    }

    private Contact fetchToObject(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
        contact.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        contact.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
        contact.setAddressNo(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_NO)));
        contact.setHomeNumber(cursor.getString(cursor.getColumnIndex(KEY_HOME_NUMBER)));
        contact.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
        contact.setNation(cursor.getString(cursor.getColumnIndex(KEY_NATION)));
        contact.setRelationship(cursor.getString(cursor.getColumnIndex(KEY_RELATIONSHIP)));
        contact.setDayOfBirth(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
        contact.setFavorite(cursor.getInt(cursor.getColumnIndex(KEY_IS_FAVORITE)) == 1);
        return contact;
    }
}
