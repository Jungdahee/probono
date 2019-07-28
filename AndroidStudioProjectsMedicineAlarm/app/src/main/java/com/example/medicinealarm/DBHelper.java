package com.example.medicinealarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Schedules.db";
    private static final  int DATABASE_VERSION = 1;

    public DBHelper() { super(null,DATABASE_NAME, null, DATABASE_VERSION); }
    public DBHelper(Context context) { super(context,DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성
        db.execSQL("CREATE TABLE schedule (_id INTEGER PRIMARY KEY"
                +" AUTOINCREMENT,name TEXT,memo TEXT,createDate TEXT);");

        db.execSQL("CREATE TABLE setlistTime (_index INTEGER PRIMARY KEY AUTOINCREMENT,_id INTEGER ,name TEXT ,"
                +"alarmTime TEXT, alarmDate  TEXT, type TEXT,checked TEXT," +
                "FOREIGN KEY (_id, name) REFERENCES schedule(_id,name));");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS schedule");
        onCreate(db);
    }
}
