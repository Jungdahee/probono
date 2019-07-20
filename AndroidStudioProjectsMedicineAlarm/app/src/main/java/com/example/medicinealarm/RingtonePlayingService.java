package com.example.medicinealarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;

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


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >=26) {  //알람 발생 및 알림 띄우기
            String channelID ="default";
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Medicine channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


            Intent intent2 = new Intent(this,AlarmControl.class);

            PendingIntent pending= PendingIntent.getActivity(this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(this, channelID)
                    .setContentTitle("알람시작")
                    .setContentText("알람음이 재생됩니다.")
                    .setSmallIcon(R.drawable.medicine)
                    .setContentIntent(pending)
                    .setAutoCancel(true)
                    .build();

            startForeground(1,notification);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.notify(1,notification);




        }

        String getState = intent.getExtras().getString("state");
        String voiceCheck = intent.getExtras().getString("voice");
        assert  getState != null;
        switch (getState){

            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                stopForeground(true);
                break;
            default:
                startId = 0;
                break;



        }

        File file= Environment.getExternalStorageDirectory();
        String path=file.getAbsolutePath()+"/"+"recoder.mp3";

        if(!this.isRunning && startId == 1) {

            if(voiceCheck.equals("voice unchecked")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.defaultringtone);
            }
            else {
                Uri recordFile = Uri.parse(path);
                mediaPlayer = MediaPlayer.create(this,recordFile);

            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;
        }

        else if(this.isRunning && startId == 0) {

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.isRunning = false;
            this.startId = 0;
        }

        else if(!this.isRunning && startId == 0) {

            this.isRunning = false;
            this.startId = 0;

        }

        else if(this.isRunning && startId == 1){

            this.isRunning = true;
            this.startId = 1;
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
