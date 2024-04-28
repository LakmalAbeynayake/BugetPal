package com.example.budgetpal.ui.login;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.budgetpal.ui.BankAccount;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PIN = "pin";
    private static final String COLUMN_LOGIN_METHOD = "login_method";

    /*Accounts*/
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String COLUMN_ACCOUNT_ID = "account_id";
    private static final String COLUMN_ACCOUNT_NAME = "account_name";
    private static final String COLUMN_ADDED_ON = "added_on";
    private static final String COLUMN_LAST_UPDATED_ON = "last_updated_on";

    private static final String TABLE_ACCOUNT_CONTENT = "account_content";
    private static final String COLUMN_CONTENT_ID = "id";
    private static final String COLUMN_ACCOUNT_CONTENT = "sms_content";
    private static final String COLUMN_SMS_TYPE = "sms_type";
    private static final String COLUMN_AMOUNT = "amount";

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

        // Create Accounts Table
        String createAccountsTable = "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACCOUNT_NAME + " TEXT, " +
                COLUMN_ADDED_ON + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_LAST_UPDATED_ON + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        // Create Account Content Table
        String createAccountContentTable = "CREATE TABLE " + TABLE_ACCOUNT_CONTENT + "(" +
                COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACCOUNT_ID + " INTEGER, " +
                COLUMN_ACCOUNT_CONTENT + " TEXT, " +
                COLUMN_SMS_TYPE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                "FOREIGN KEY(" + COLUMN_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNTS + "(" + COLUMN_ACCOUNT_ID + "))";

        db.execSQL(createAccountsTable);
        db.execSQL(createAccountContentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
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
        //emptyUserTable();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
    public void emptyUserTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_USERS, null, null);
        } catch (SQLException e) {

        } finally {
            db.close();
        }
    }

    // Get details of the last user added
    // Get details of the last user added
    public User getLastUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User lastUser = null;

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_ID + " = 1";

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

    public long insertAccount(String accountName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_NAME, accountName);
        return db.insert(TABLE_ACCOUNTS, null, values);
    }

    public long insertAccountContent(long accountId, String smsContent, String smsType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_ID, accountId);
        values.put(COLUMN_ACCOUNT_CONTENT, smsContent);
        values.put(COLUMN_SMS_TYPE, smsType);
        values.put(COLUMN_AMOUNT, amount);
        return db.insert(TABLE_ACCOUNT_CONTENT, null, values);
    }

    public List<BankAccount> getAllAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ACCOUNTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int accountIdIndex = cursor.getColumnIndex(COLUMN_ACCOUNT_ID);
                int accountNameIndex = cursor.getColumnIndex(COLUMN_ACCOUNT_NAME);
                int addedOnIndex = cursor.getColumnIndex(COLUMN_ADDED_ON);
                int lastUpdatedOnIndex = cursor.getColumnIndex(COLUMN_LAST_UPDATED_ON);

                int accountId = cursor.getInt(accountIdIndex);
                String accountName = cursor.getString(accountNameIndex);
                String addedOn = cursor.getString(addedOnIndex);
                String lastUpdatedOn = cursor.getString(lastUpdatedOnIndex);

                BankAccount account = new BankAccount(accountId, accountName, addedOn, lastUpdatedOn);
                accounts.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accounts;
    }
}
