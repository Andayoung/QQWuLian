package com.hk.zhouyuyin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class DBsqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBsqliteHelper";
    private static final String DATABASE_NAME = "binder.db";
    private static final int DATABASE_VERSION = 1;

    public DBsqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS binderz" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, tinyid INTEGER, nickname VARCHAR, bindertype INTEGER, headurl VARCHAR)");

    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
