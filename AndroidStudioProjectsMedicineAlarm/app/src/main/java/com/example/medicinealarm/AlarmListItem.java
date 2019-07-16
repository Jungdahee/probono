package com.example.medicinealarm;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class AlarmListItem {
    private int type;

    private  int id;
    private String title;
    private String time;
    private String date;
    private String set_week;
    private String repeat;
    private String memo;
    private int hour;
    private int minute;

    private CheckBox mcheckbox;
    private boolean checked;



    public View.OnClickListener onClickListener;



    public AlarmListItem()
    {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSet_week() {
        return set_week;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setSet_week(String set_week) {
        this.set_week = set_week;
    }


    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        String temp = this.time;
        String result;
        String array[] =temp.split(" ");
        if(array[0].equals("오전")) {
            result =array[1].substring(0,array[1].length()-1);
            return Integer.parseInt(result);
        }
        else{

            result =array[1].substring(0,array[1].length()-1);
            return Integer.parseInt(result)+12;

        }
    }

    public int getMinute() {

        String temp = this.time;
        String result;
        String array[] =temp.split(" ");
        result = array[2].substring(0,array[2].length()-1);

        return Integer.parseInt(result);
    }
}
