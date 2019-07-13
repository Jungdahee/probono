package com.example.medicinealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReciver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String getYourString = intent.getExtras().getString("state");

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        Intent control = new Intent(context,AlarmControl.class);

       if(getYourString.equals("alarm on"))
                this.context.startActivity(control.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

           serviceIntent.putExtra("state",getYourString);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            this.context.startForegroundService(serviceIntent);
        }else {
            this.context.startService(serviceIntent);

        }
    }
}
