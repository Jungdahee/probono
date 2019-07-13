package com.example.medicinealarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >=26) {
            String channelID ="default";
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Channel human readalbe title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, channelID)
                    .setContentTitle("알람시작")
                    .setContentText("알람음이 재생됩니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1,notification);



        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String getState = intent.getExtras().getString("state");

        assert  getState != null;
        switch (getState){

            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;



        }


        if(!this.isRunning && startId == 1){

            mediaPlayer = MediaPlayer.create(this,R.raw.test);
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;

        }

        else  if(this.isRunning && startId == 0) {

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.isRunning = false;
            this.startId =0;

        }

        else if(this.isRunning && startId == 1){

            this.isRunning = true;
            this.startId =1;
        }

        else {

        }
        return START_NOT_STICKY;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("onDestrory() 실행","서비스 종료");
    }
}
