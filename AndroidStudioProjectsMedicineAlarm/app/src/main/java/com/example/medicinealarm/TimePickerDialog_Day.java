package com.example.medicinealarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimePickerDialog_Day extends DialogFragment {
    private Button btnDurMode;
    private Button btnConfirm;
    private Button btnCancel;
    private TimePicker timePicker;
    private final Calendar calendar = Calendar.getInstance();
    private String date;
    private String selected_week="";

    private  Button[] btnweek = new Button[7];
    private MyDialogListner dialogListner;



    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.day_timepicker,null);

        timePicker = (TimePicker)v.findViewById(R.id.alarm_timepicker);

        btnweek[0]  = v.findViewById(R.id.btn_sun);
        btnweek[1]  = v.findViewById(R.id.btn_mon);
        btnweek[2]  = v.findViewById(R.id.btn_tue);
        btnweek[3]  = v.findViewById(R.id.btn_wed);
        btnweek[4]  = v.findViewById(R.id.btn_thu);
        btnweek[5]  = v.findViewById(R.id.btn_fri);
        btnweek[6]  = v.findViewById(R.id.btn_sat);


        btnDurMode = (Button)v.findViewById(R.id.btn_dur_mode);
        btnDurMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                TimePickerDialog_Dur during = new TimePickerDialog_Dur();
                during.show(getFragmentManager(),"during");
                during.setDialogListner(dialogListner);

            }
        });


        btnConfirm = (Button)v.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int i=0; i<btnweek.length;i++)
                {
                    if(btnweek[i].isSelected())
                        selected_week+=btnweek[i].getText().toString()+"/";
                }


                if(selected_week.equals(""))
                {
                    Toast.makeText(getContext(),"원하시는 요일을 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                selected_week = selected_week.substring(0,selected_week.length()-1);
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();


                if (hour ==12)
                {
                    date = "오후 "+Integer.valueOf(hour) + "시 " + Integer.valueOf(min) + "분";

                }
                else if(hour >12 && hour <24)
                {
                    date = "오후 "+Integer.valueOf(hour-12) + "시 " + Integer.valueOf(min) + "분";

                }
                else {
                    date = "오전 "+Integer.valueOf(hour) + "시 " + Integer.valueOf(min) + "분";
                }

                dialogListner.onPositiveClicked(date,selected_week,"DAY");
                Toast.makeText(getContext(),"시간등록완료",Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });

        btnCancel = (Button)v.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        builder.setView(v);
        return builder.create();
    }


    public void setDialogListner(MyDialogListner dialogListner){
        this.dialogListner = dialogListner;
    }

    public MyDialogListner getDialogListner() {
        return dialogListner;
    }
}
