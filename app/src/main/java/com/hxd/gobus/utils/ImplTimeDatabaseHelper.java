package com.hxd.gobus.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 存取接口时间刷新
 * Created by wangqingbin on 2018/6/22.
 */

public class ImplTimeDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public ImplTimeDatabaseHelper(Context context, String name) {
        this(context,name,null,VERSION);
    }

    public ImplTimeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ImplTimeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table impltime(id integer PRIMARY KEY AUTOINCREMENT NOT NULL," + "refreshtime long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists impltime");
        onCreate(db);
    }
}
