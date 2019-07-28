package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AlarmSetActivity extends AppCompatActivity {
    private Button btnTimeSet; // 시간 설정 버튼
    private Button btnAlarmSet; // 최종알람 설정 버튼
    private Button btnRecord; //음성등록 버튼
    private TextView medicineName; // 알림명
    private TextView memo; // 복용방법 및 기타메모
    private TextView timeCount; // 알람 갯수를 표시하는 뷰


    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    private ListView timeListView = null;
    private ArrayList<AlarmListItem> data = new ArrayList<>();
    private AlarmListAdapter adapter;

    private SQLiteDatabase db;  //DB 객체
    private DBHelper helper;  //DB 연동을 위한 DB helper

    private boolean voiceCheck = false;


    VoiceRerordDialog voiceRerordDialog;
    MediaRecorder recorder = new MediaRecorder();
    Intent intentAlarmReceiver;
    Intent intentAlarmControl;
    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); // DB에 알람 생성시각 표시형식


    private  View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "등록버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();
            recorder.stop();
            recorder.release();
            voiceRerordDialog.dismiss();

            voiceCheck = true;
        }
    };

    private  View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "취소버튼이 눌렸습니다.",Toast.LENGTH_SHORT).show();

            recorder.release();
            voiceRerordDialog.dismiss();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);

        intentAlarmReceiver = new Intent(this, AlarmReceiver.class);
        intentAlarmControl = new Intent(this,AlarmControl.class);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        medicineName = (TextView)findViewById(R.id.medicine_name); // 알림 이름
        memo = (TextView)findViewById(R.id.medicine_memo); // 알림 복용방법 및 메모내용
        timeCount = (TextView)findViewById(R.id.time_cnt); // 알림별 설정한 알림시각 갯수
        btnRecord = (Button)findViewById(R.id.btn_record);
        btnTimeSet = (Button)findViewById(R.id.btn_set_time); // 시간 등록 버튼
        btnAlarmSet = (Button)findViewById(R.id.btn_set_alarm); //  알람 등록 버튼

        voiceRerordDialog = new VoiceRerordDialog(this,saveListener,cancelListener);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"녹음 시작", Toast.LENGTH_LONG).show();
                try{
                    File file= Environment.getExternalStorageDirectory();
                    //갤럭시 S4기준으로 /storage/emulated/0/의 경로를 갖고 시작한다.
                    String path=file.getAbsolutePath()+"/"+"recoder.mp3";

                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    //첫번째로 어떤 것으로 녹음할것인가를 설정한다. 마이크로 녹음을 할것이기에 MIC로 설정한다.
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    //이것은 파일타입을 설정한다. 녹음파일의경우 3gp로해야 용량도 작고 효율적인 녹음기를 개발할 수있다.
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    //이것은 코덱을 설정하는 것이라고 생각하면된다.
                    recorder.setOutputFile(path);
                    //저장될 파일을 저장한뒤
                    recorder.prepare();
                    recorder.start();
                    //시작하면된다.
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }


                voiceRerordDialog.show();
            }
        });

        btnTimeSet.setOnClickListener(new View.OnClickListener() { // 알림별 시간 등록 추가
            @Override
            public void onClick(View v) { // 시간 등록 버튼 이벤트

                if(medicineName.getText().toString().equals(""))  //  알림이름 설정을 안할 경우
                {
                    Toast.makeText(getApplicationContext(),"약 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                setDate(); // 요일선택(day_timepicker) 혹은 기간선택(dur_timepicker) 다이얼로그 띄우고 시간 설정하기


            }
        });

        btnAlarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //알람 등록 버튼 이벤트
                setAlarm();
            }
        });


        helper = new DBHelper(this); // 로컬 DB와 연동
        db = helper.getWritableDatabase(); // DB에 쓰기 권한 획득
        db = helper.getReadableDatabase(); // DB에 읽기 권한 획득
        timeListView = (ListView)findViewById(R.id.set_listview); // 시간 등록 리스트 뷰
        adapter = new AlarmListAdapter(data); // 어답터 객체 생성
        timeListView.setAdapter(adapter); //리스트와 어답터 연결



    }

    protected void setDate() {   // 요일선택(day_timepicker) 혹은 기간선택(dur_timepicker) 다이얼로그 띄우고 시간 설정하기
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

    public void setAlarm(){

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

            calendar.set(Calendar.HOUR_OF_DAY,item.getHour());
            calendar.set(Calendar.MINUTE,item.getMinute());
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            String sql = "SELECT * FROM schedule WHERE createDate =\""+nowDate+"\";";
            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToNext();
            int id = cursor.getInt(0);
            if(item == null)
                break;
            else if(item.getType() == 0 ) {
                db.execSQL("INSERT INTO setlistTime (_id,name,alarmTime,alarmDate,type,checked) VALUES " +
                        "(" + id + ",\"" + medicineName.getText().toString() + "\"" + ", \"" + item.getTime() + "\",\"" + item.getSet_week() + "\"," + "\"DAY\" " + ",\"" + "true" + "\");");

            }
            else {
                db.execSQL("INSERT INTO setlistTime (_id,name,alarmTime,alarmDate,type,checked) VALUES " +
                        "(" + id + ",\"" + medicineName.getText().toString() + "\"" + ", \"" + item.getTime() + "\",\"" + item.getRepeat() + "\"," + "\"DUR\" " + ",\"" + "true" + "\");");

            }
            int setListid = InitializationVariables.getAlarmCount();
            intentAlarmControl.putExtra(Long.toString(calendar.getTimeInMillis()),setListid);
            intentAlarmReceiver.putExtra("state","alarm on");

            if(voiceCheck == true)
                intentAlarmReceiver.putExtra("voice", "voice checked");
            else
                intentAlarmReceiver.putExtra("voice", "voice unchecked");

            pendingIntent = PendingIntent.getBroadcast(AlarmSetActivity.this,setListid,intentAlarmReceiver,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    pendingIntent);
        }

    }

}
