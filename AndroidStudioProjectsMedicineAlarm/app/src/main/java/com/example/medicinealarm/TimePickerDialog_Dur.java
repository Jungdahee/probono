package com.example.medicinealarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimePickerDialog_Dur extends DialogFragment {
    private Button btnDurMode;
    private Button btnConfirm;
    private Button btnCancel;
    private TimePicker timePicker;
    private EditText set_repeat;
    private final Calendar calendar = Calendar.getInstance();

    private String date;
    private String repeat;

    private MyDialogListner dialogListner;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dur_timepicker,null);

        timePicker = (TimePicker)v.findViewById(R.id.alarm_timepicker);
        set_repeat = v.findViewById(R.id.edit_dur_setday);

        btnDurMode = (Button)v.findViewById(R.id.btn_day_mode);
        btnDurMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                TimePickerDialog_Day day = new TimePickerDialog_Day();
                day.show(getFragmentManager(),"day");
                Log.d("test","실행");

            }
        });


        btnConfirm = (Button)v.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                repeat = set_repeat.getText().toString();

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



                dialogListner.onPositiveClicked(date,repeat,"DUR");
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
