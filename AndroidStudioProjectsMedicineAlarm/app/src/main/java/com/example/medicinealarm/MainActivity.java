package com.example.medicinealarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button[] mainMenu = new Button[4];
    Intent intent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializationVariables initializationVariables = new InitializationVariables(this);
        int writePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int recordPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);

        if(writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || recordPermission != PackageManager.PERMISSION_GRANTED)
                showPermissionDialog();






        mainMenu[2] = (Button)findViewById(R.id.btn_alarm_set);


        mainMenu[2].setOnClickListener(new View.OnClickListener() { // 알람 설정 액티비티 켜기
            @Override
            public void onClick(View v) {
                 intent = new Intent(getApplicationContext(), AlarmSetActivity.class);
                 startActivity(intent);
            }
        });


        mainMenu[3] = (Button)findViewById(R.id.btn_alarm_check);
        mainMenu[3].setOnClickListener(new View.OnClickListener() {  // 알람 확인 액티비티 켜기
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AlarmCheckActivity.class);
                startActivity(intent);
            }
        });

    }


    private void showPermissionDialog() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한이 정상적으로 승인되었습니다!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"권한 승인 없이 정상적으로 앱이 작동하지 않습니다. 다시 실행하여 주십시요.",Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity.this, "Permission Denied. " , Toast.LENGTH_SHORT).show();
                finish();
            }
        };


        new TedPermission(this).setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
        new TedPermission(this).setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();
        new TedPermission(this).setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.RECORD_AUDIO).check();

    }



}
