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

public class AlramCheckActivity extends AppCompatActivity {

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
        /*
       int count =  adapter.getCount();

        for(int position = 0 ;position<count;position++)
        {
            final AlarmListItem item = (AlarmListItem)adapter.getItem(position);
             final int itemId = item.getId();

             item.onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();
                    String sql = "SELECT * FROM setlistTime WHERE _id ="+itemId+" AND name = \""+item.getTitle()+"\";";
                    Cursor cursor = db.rawQuery(sql,null);

                    while(cursor.moveToNext()){
                        int id = cursor.getInt(0);
                        final String name = cursor.getString(1);
                        String alarmTime = cursor.getString(2);
                        String alarmDate = cursor.getString(3);
                        String type = cursor.getString(4);
                        String checked = cursor.getString(5);

                        AlarmListItem item = new AlarmListItem();
                        item.setId(id);
                        item.setTitle(name);
                        item.setTime(alarmTime);
                        item.setDate(alarmDate);
                        if(type.equals("DAY"))
                            item.setType(0);
                        else
                            item.setType(1);

                        adapter.button_click(id,name,item);
                        adapter.notifyDataSetChanged();
                    }


                }
            };


        }

 */


        checkListView.setAdapter(adapter);


    }

    public ArrayList<AlarmListItem> getoData() {
        return oData;
    }


    public  void order_checked(ArrayList<AlarmListItem> tagetArray) {

    Collections.sort(tagetArray, new Comparator<AlarmListItem>() {
        @Override
        public int compare(AlarmListItem o1, AlarmListItem o2) {
            if(o1.isChecked() == true && o2.isChecked() == false)
                return -1;
            else if(o1.isChecked()==false && o2.isChecked() == true)
                return 1;
            else
                return 0;
        }
    });

    }




    public  void order_time(ArrayList<AlarmListItem> tagetArray) {

        int count =0;
        AlarmListItem temp = new AlarmListItem();
        List<AlarmListItem> copy_Array;
        List<AlarmListItem> copy_Array2;
        ArrayList<AlarmListItem> temp_Array = new ArrayList<>();
        while(true)
        {
            temp=tagetArray.get(count++);
            if(temp.isChecked() == false)
                break;

        }
      copy_Array = tagetArray.subList(0,count);
      copy_Array2 = tagetArray.subList(count,tagetArray.size());
      Collections.sort(copy_Array, new Comparator<AlarmListItem>() {
          @Override
          public int compare(AlarmListItem o1, AlarmListItem o2) {
              String[] clock1 = o1.getDate().split(" ");
              String[] clock2 = o2.getDate().split(" ");
              if (clock1[0].equals("오후") && clock2[0].equals("오전"))
                  return 1;

              else if (clock1[0].equals("오전") && clock2[0].equals("오후"))
                  return -1;
              else {
                  if (clock1[1].compareTo(clock2[1]) < 0)
                      return -1;
                  else if (clock1[1].compareTo(clock2[1]) > 0)
                      return 1;
                  else {
                      if (clock1[2].compareTo(clock2[2]) < 0)
                          return -1;

                      else if (clock1[2].compareTo(clock2[2]) > 0)
                          return 1;
                      else
                          return 0;
                  }
              }
          }
      });

      temp_Array.addAll(copy_Array);
      temp_Array.addAll(copy_Array2);
      tagetArray = temp_Array;

    }



}







