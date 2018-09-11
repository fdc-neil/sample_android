package com.example.fdc_neil.machevaratalkneil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.refactor.lib.colordialog.PromptDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PromptDialog(MainActivity.this)
                .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText(R.string.congrats)
                .setContentText(R.string.welcome_mainActivity)
                .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        promptDialog.dismiss();
                    }
                }).show();
    }
}
