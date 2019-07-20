package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class AlarmControl extends AppCompatActivity {
    private Button alarmCancle;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmcontrl);






        final Calendar calendar = Calendar.getInstance();

        final Intent intentAlarmReceiver = new Intent(this, AlarmReceiver.class);
        final Intent intentAlarmSet = new Intent(this, AlarmSetActivity.class);

        int id = intentAlarmSet.getIntExtra(Long.toString(calendar.getTimeInMillis()),1);
        pendingIntent = PendingIntent.getBroadcast(AlarmControl.this,id,intentAlarmSet,
                PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        alarmCancle = (Button)findViewById(R.id.alarmCancel);
        alarmCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmManager.cancel(pendingIntent);
                intentAlarmReceiver.putExtra("state","alarm off");

                sendBroadcast(intentAlarmReceiver);
                finish();


            }
        });



    }
}
