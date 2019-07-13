package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlarmControl extends AppCompatActivity {
    Button alarmCancle;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmcontrl);

        final Intent intent = new Intent(this, AlarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmControl.this,0,intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmCancle = (Button)findViewById(R.id.alarmCancel);
        alarmCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alarmManager.cancel(pendingIntent);
                intent.putExtra("state","alarm off");

                sendBroadcast(intent);


            }
        });



    }
}
