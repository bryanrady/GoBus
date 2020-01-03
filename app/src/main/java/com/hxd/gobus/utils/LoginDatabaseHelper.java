package com.hxd.gobus.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangqingbin on 2018/6/13.
 */

public class LoginDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    public static String DBNAME = "user_login.db";
    private static LoginDatabaseHelper instance;

    public static LoginDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LoginDatabaseHelper(context);
        }
        return instance;
    }


    private LoginDatabaseHelper(Context context) {
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username varchar(20),password varchar(20),userinfo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
}