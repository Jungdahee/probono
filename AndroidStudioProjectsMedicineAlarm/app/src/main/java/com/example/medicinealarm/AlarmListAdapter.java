package com.example.medicinealarm;

import android.content.Context;
import android.database.Cursor;
import android.database.Observable;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class AlarmListAdapter extends BaseAdapter {

    private static final int ITEM_VIEW_TYPE_DAY = 0 ;
    private static final int ITEM_VIEW_TYPE_DUR = 1 ;
    private static final int ITEM_VIEW_TYPE_CHECK_MAIN = 2;
    private static final int ITEM_VIEW_TYPE_MAX = 3 ;


    LayoutInflater inflater = null;
    private ArrayList<AlarmListItem> alarm_Data = new ArrayList<AlarmListItem>();
    private TextView[] selected_week = new TextView[7];
    private String set_week;

    SQLiteDatabase db;
    DBHelper helper;




    public AlarmListAdapter(ArrayList<AlarmListItem>  setData) {

        alarm_Data = setData;
    }


    public ArrayList<AlarmListItem> getAlarm_Data() {
        return alarm_Data;
    }


    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX ;
    }

    @Override
    public int getItemViewType(int position) {
        return alarm_Data.get(position).getType();
    }

    @Override
    public int getCount() {
        return alarm_Data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        if (convertView == null)
        {
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            final AlarmListItem alarmListItem = alarm_Data.get(position);

            switch (viewType){
                case ITEM_VIEW_TYPE_DAY :
                    convertView = inflater.inflate(R.layout.setlistview_day, parent, false);
                    TextView oTextDate = (TextView) convertView.findViewById(R.id.texttime);
                    oTextDate.setText(alarmListItem.getTime());

                    selected_week[0] = convertView.findViewById(R.id.sunday);
                    selected_week[1] = convertView.findViewById(R.id.monday);
                    selected_week[2] = convertView.findViewById(R.id.tuesday);
                    selected_week[3] = convertView.findViewById(R.id.wednesday);
                    selected_week[4] = convertView.findViewById(R.id.thursday);
                    selected_week[5] = convertView.findViewById(R.id.friday);
                    selected_week[6] = convertView.findViewById(R.id.saturday);



                    set_week = alarmListItem.getSet_week();
                    String week[] =set_week.split("/");
                    for(int i=0; i<selected_week.length ; i++)
                    {
                        for(int j=0; j<week.length; j++)
                        {
                            if(selected_week[i].getText().toString().equals(week[j]))
                                selected_week[i].setTextColor(Color.parseColor("#E91E63"));
                        }

                    }
                    break;

                case  ITEM_VIEW_TYPE_DUR:
                    convertView = inflater.inflate(R.layout.setlistview_dur, parent, false);

                    TextView TextDate = (TextView) convertView.findViewById(R.id.texttime);
                    TextDate.setText(alarmListItem.getTime());


                    TextView edit = convertView.findViewById(R.id.set_repeat);
                    edit.setText(alarmListItem.getRepeat()+" 일 마다 알림");
                    break;

                case ITEM_VIEW_TYPE_CHECK_MAIN:
                    convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);

                    helper = new DBHelper(convertView.getContext());
                    db = helper.getReadableDatabase();
                    db = helper.getWritableDatabase();

                    final TextView title = (TextView) convertView.findViewById(R.id.alarm_set_title);
                    title.setText(alarmListItem.getTitle());
                    TextView memo =(TextView)convertView.findViewById(R.id.alarm_set_memo);
                    memo.setText(alarmListItem.getMemo());
                   final Switch mswitch = (Switch)convertView.findViewById(R.id.alarm_detail);
                    mswitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("click","메인 클릭 테스트");


                            String sql = "SELECT * FROM setlistTime WHERE _id =" + alarmListItem.getId() + " AND name = \"" + title.getText() + "\";";
                            Cursor cursor = db.rawQuery(sql, null);

                            int id;
                            String name;
                            String alarmTime;
                            String alarmDate;
                            String type;
                            String checked;


                            while (cursor.moveToNext()) {
                                 id = cursor.getInt(1);
                                 name = cursor.getString(2);
                                alarmTime = cursor.getString(3);
                                alarmDate = cursor.getString(4);
                                type = cursor.getString(5);
                                checked = cursor.getString(6);

                                AlarmListItem item = new AlarmListItem();
                                item.setId(id);
                                item.setTitle(name);
                                item.setTime(alarmTime);
                                item.setDate(alarmDate);
                                if (type.equals("DAY")) {
                                    item.setType(ITEM_VIEW_TYPE_DAY);
                                    item.setSet_week(alarmDate);
                                } else {
                                    item.setType(ITEM_VIEW_TYPE_DUR);
                                    item.setRepeat(alarmDate);
                                }


                                if(mswitch.isChecked()) {

                                    button_click(id, name, item);
                                    notifyDataSetChanged();
                                }


                                else {

                                    ArrayList<AlarmListItem> tempCopy = alarm_Data;

                                    for(int i=0 ; i<alarm_Data.size() ; i++)
                                    {

                                        for(int j=0 ; j<tempCopy.size();j++) {
                                            AlarmListItem target = tempCopy.get(j);
                                            if (target.getId() == id && target.getType() != 2)
                                                tempCopy.remove(target);
                                        }

                                    }
                                    alarm_Data = tempCopy;
                                    notifyDataSetChanged();


                                }






                            }

                        }

                    });


            }

        }




        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return alarm_Data.get(position);
    }

    public  void addItem_day(String time, String set_week)
    {
        AlarmListItem item = new AlarmListItem();

        item.setType(ITEM_VIEW_TYPE_DAY);
        item.setTime(time);
        item.setSet_week(set_week);

        alarm_Data.add(item);

    }

    public  void addItem_dur(String time, String repeat)
    {
        AlarmListItem item = new AlarmListItem();

        item.setType(ITEM_VIEW_TYPE_DUR);
        item.setTime(time);
        item.setRepeat(repeat);

        alarm_Data.add(item);

    }

    public  void addItem_check_main(int id,String name, String memo)
    {
        AlarmListItem item = new AlarmListItem();

        item.setId(id);
        item.setType(ITEM_VIEW_TYPE_CHECK_MAIN);
        item.setTitle(name);
        item.setMemo(memo);

        alarm_Data.add(item);

    }


    public void button_click(int id,String name, AlarmListItem timeSet)
    {

        int check;
        for (check =0; check<alarm_Data.size();check++)
        {
                AlarmListItem item = alarm_Data.get(check);
                if (item.getId() == id && item.getTitle().equals(name) && item.getType()==2) {
                    check++;
                    break;
                }
        }
        alarm_Data.add(check,timeSet);

        /*
        List<AlarmListItem>  tempList = new ArrayList<>(alarm_Data.subList(0,check));
        tempList.add(timeSet);
        tempList.addAll(alarm_Data.subList(check,alarm_Data.size()));

        alarm_Data.clear();

        alarm_Data.addAll(tempList);

        */

    }




}
