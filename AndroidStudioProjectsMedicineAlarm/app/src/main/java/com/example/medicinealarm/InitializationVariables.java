package com.example.medicinealarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class InitializationVariables  {
    private static int alarmCount;
    private static SQLiteDatabase db;  //DB 객체
    private static DBHelper helper;  //DB 연동을 위한 DB helper
    public  InitializationVariables() { }

    public  InitializationVariables(Context context)
    {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase(); // DB에 읽기 권한 획득
        String sql = "SELECT  * FROM sqlite_sequence WHERE name = \"setlistTime\";";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToNext();
        alarmCount = cursor.getInt(1);
    }



    public static int getAlarmCount() {
        return alarmCount++;
    }

    public  void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
    }
}
