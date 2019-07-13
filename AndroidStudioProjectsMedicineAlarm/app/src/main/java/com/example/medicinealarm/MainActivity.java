package com.example.medicinealarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button[] mainMenu = new Button[4];
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                intent = new Intent(getApplicationContext(), AlramCheckActivity.class);
                startActivity(intent);
            }
        });


    }
}
