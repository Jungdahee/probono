package com.example.medicinealarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

public class VoiceRerordDialog extends Dialog {

    private Button btnSave;
    private Button btnCancel;

    private  View.OnClickListener mSaveListener;
    private  View.OnClickListener mCancelListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.record_dialog);

        btnSave = (Button)findViewById(R.id.btn_record_save);
        btnCancel = (Button)findViewById(R.id.btn_record_cancel);

        btnSave.setOnClickListener(mSaveListener);
        btnCancel.setOnClickListener(mCancelListener);


    }

    public VoiceRerordDialog(@NonNull Context context, View.OnClickListener saveListener, View.OnClickListener cancelListener)
    {
        super(context);

        this.mSaveListener = saveListener;
        this.mCancelListener = cancelListener;

    }


}
