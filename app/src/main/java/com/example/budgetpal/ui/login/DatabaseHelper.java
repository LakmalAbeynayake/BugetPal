package com.example.budgetpal.ui.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PIN = "pin";
    private static final String COLUMN_LOGIN_METHOD = "login_method";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_PIN + " TEXT," +
                COLUMN_LOGIN_METHOD + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }

    // CRUD Operations
    // Add new user
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PIN, user.getPin());
        values.put(COLUMN_LOGIN_METHOD, user.getLoginMethod());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1; // Returns true if insertion was successful, false otherwise
    }

    // Check if user exists
    public boolean checkUserExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
    // Get details of the last user added
    // Get details of the last user added
    public User getLastUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User lastUser = null;

        String query = "SELECT * FROM " + TABLE_USERS +
                " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int pinIndex = cursor.getColumnIndex(COLUMN_PIN);
            int loginMethodIndex = cursor.getColumnIndex(COLUMN_LOGIN_METHOD);

            if (idIndex != -1 && nameIndex != -1 && pinIndex != -1 && loginMethodIndex != -1) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String pin = cursor.getString(pinIndex);
                String loginMethod = cursor.getString(loginMethodIndex);

                lastUser = new User(id, name, pin, loginMethod);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return lastUser;
    }
}
