package com.example.medicinealarm;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AlarmCheckActivity extends AppCompatActivity {

    private ListView checkListView = null;
    private ArrayList<AlarmListItem> oData = new ArrayList<>();
    private AlarmListAdapter adapter;
    SQLiteDatabase db;
    DBHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        helper = new DBHelper(this);

        db = helper.getReadableDatabase();
        db = helper.getWritableDatabase();

        checkListView = (ListView)findViewById(R.id.listView);
        adapter = new AlarmListAdapter(oData);

        String sql = "SELECT * FROM schedule;";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            final String name = cursor.getString(1);
            String memo = cursor.getString(2);

            adapter.addItem_check_main(id,name,memo);

        }

        checkListView.setAdapter(adapter);


    }

    public AlarmListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AlarmListAdapter adapter) {
        this.adapter = adapter;
    }
}







