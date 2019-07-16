package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;


public class AlarmSetActivity extends AppCompatActivity {
    private Button btnTimeSet; // 시간 설정 버튼
    private Button btnAlarmSet; // 최종알람 설정 버튼
    private TextView medicineName; // 알림명
    private TextView memo; // 복용방법 및 기타메모
    private TextView timeCount; // 알람 갯수를 표시하는 뷰

    private AlarmManager alarmManager;
    PendingIntent pendingIntent;


    private ListView timeListView = null;
    private ArrayList<AlarmListItem> data = new ArrayList<>();
    private AlarmListAdapter adapter;

    private SQLiteDatabase db;  //DB 객체
    private DBHelper helper;  //DB 연동을 위한 DB helper

    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); // DB에 알람 생성시각 표시형식



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        medicineName = (TextView)findViewById(R.id.medicine_name); // 알림 이름
        memo = (TextView)findViewById(R.id.medicine_memo); // 알림 복용방법 및 메모내용
        timeCount = (TextView)findViewById(R.id.time_cnt); // 알림별 설정한 알림시각 갯수
        btnTimeSet = (Button)findViewById(R.id.btn_set_time); // 시간 등록 버튼
        btnAlarmSet = (Button)findViewById(R.id.btn_set_alarm); //  알람 등록 버튼


        final Intent intent = new Intent(this,AlarmReciver.class);

        btnTimeSet.setOnClickListener(new View.OnClickListener() { // 알림별 시간 등록 추가
            @Override
            public void onClick(View v) { // 시간 등록 버튼 이벤트

                if(medicineName.getText().toString().equals(""))  //  알림이름 설정을 안할 경우
                {
                    Toast.makeText(getApplicationContext(),"약 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                setAlarm(); // 요일선택(day_timepicker) 혹은 기간선택(dur_timepicker) 다이얼로그 띄우고 시간 설정하기


            }
        });

        btnAlarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //알람 등록 버튼 이벤트
                        final Calendar calendar = Calendar.getInstance();
                        Date date = new Date();
                        String nowDate = format1.format(date); // 현재 시각 저장, 현재 시각 형식 yyyy-mm-dd hh:mm:ss

                        //DB에 알람 정보 삽입
                        db.execSQL("INSERT INTO schedule (name,memo,createDate) VALUES " +
                                "(\""+medicineName.getText().toString()+"\""+", \""+memo.getText().toString()+"\",\""+nowDate+"\");");

                        Toast.makeText(getApplicationContext(),"등록완료",Toast.LENGTH_SHORT).show();

                        int count =adapter.getCount(); //현재 등록한 시간등록 갯수


                        for(int position = 0 ;position<count;position++) // 시간등록 갯수 만큼 DB에 정보 삽입
                        {
                            AlarmListItem item = (AlarmListItem)adapter.getItem(position);
                            String sql = "SELECT * FROM schedule WHERE createDate =\""+nowDate+"\";";
                            Cursor cursor = db.rawQuery(sql,null);
                            cursor.moveToNext();
                            int id = cursor.getInt(0);
                            if(item == null)
                                break;
                            else if(item.getType() == 0 ) {
                                db.execSQL("INSERT INTO setlistTime (_id,name,alarmTime,alarmDate,type,checked) VALUES " +
                                        "(" + id + ",\"" + medicineName.getText().toString() + "\"" + ", \"" + item.getTime() + "\",\"" + item.getSet_week() + "\"," + "\"DAY\" " + ",\"" + "true" + "\");");
                                calendar.set(Calendar.HOUR_OF_DAY,item.getHour());
                                calendar.set(Calendar.MINUTE,item.getMinute());
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);

                                intent.putExtra("state","alarm on");


                                pendingIntent = PendingIntent.getBroadcast(AlarmSetActivity.this,0,intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                                        pendingIntent);

                            }
                            else {
                                db.execSQL("INSERT INTO setlistTime (_id,name,alarmTime,alarmDate,type,checked) VALUES " +
                                        "(" + id + ",\"" + medicineName.getText().toString() + "\"" + ", \"" + item.getTime() + "\",\"" + item.getRepeat() + "\"," + "\"DUR\" " + ",\"" + "true" + "\");");

                                calendar.set(Calendar.HOUR_OF_DAY,item.getHour());
                                calendar.set(Calendar.MINUTE,item.getMinute());
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);


                                intent.putExtra("state","alarm on");


                                pendingIntent = PendingIntent.getBroadcast(AlarmSetActivity.this,0,intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                                        pendingIntent);

                            }
                        }



            }
        });


        helper = new DBHelper(this); // 로컬 DB와 연동
        db = helper.getWritableDatabase(); // DB에 쓰기 권한 획득
        db = helper.getReadableDatabase(); // DB에 읽기 권한 획득
        timeListView = (ListView)findViewById(R.id.set_listview); // 시간 등록 리스트 뷰
        adapter = new AlarmListAdapter(data); // 어답터 객체 생성
        timeListView.setAdapter(adapter); //리스트와 어답터 연결



    }

    protected void setAlarm() {   // 요일선택(day_timepicker) 혹은 기간선택(dur_timepicker) 다이얼로그 띄우고 시간 설정하기
        TimePickerDialog_Day timePickerFragment = new TimePickerDialog_Day();
        timePickerFragment.setDialogListner(new MyDialogListner() {
            @Override
            public void onPositiveClicked(String time,String inform, String type) { // 다이얼로그 확인 버튼 클릭 이벤트
                if(type.equals("DAY")) // 요일선택이면 시간과 inform(선택한 요일) 추가
                    adapter.addItem_day(time,inform);
                else                   // 기간선택이면 시간과 inform(반복 기간) 추가
                    adapter.addItem_dur(time,inform);

                adapter.notifyDataSetChanged(); // 어답터 변경을 알려 리스트 뷰 재설정
                int times = Integer.valueOf(timeCount.getText().toString().replaceAll("[^0-9]","")); //  시간설정 갯수
                timeCount.setText("시간 선택("+ (++times)+")"); //시간 설정 갯수 수정
            }

            @Override
            public void onNegativeClicked() {
                // 다이얼로그 취소 버튼 선택시 사용할 내용 정의
            }
        });

        timePickerFragment.show(getSupportFragmentManager(), "spinnerTimePicker");
    }


    public void selectWeek(View v){ // 요일 선택 다이얼로그에서 선택한 요일 색상 변경
        Button btn = (Button)v;
        if(btn.isSelected()) {

            btn.setTextColor(Color.parseColor("#000000"));
            btn.setSelected(false);

        }
        else
        {

            btn.setTextColor(Color.parseColor("#E91E63"));
            btn.setSelected(true);
        }



    }


    public AlarmManager getAlarmManager() {
        return alarmManager;
    }
}
