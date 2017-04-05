package com.lrd.loadingforresultview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnLoadingResultListener {

    private LoadingForResultView lv;
    private Button btn_loading;
    private Button btn_success;
    private Button btn_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (LoadingForResultView) findViewById(R.id.lv);
        lv.setRingStrokeWidth(10);
        lv.setCheckMarkWidth(10);
        lv.setErrorStrokeWidth(10);
        lv.setLoadingColor(Color.parseColor("#2673FF"));
        lv.setSuccessColor(Color.parseColor("#8ada55"));
        lv.setErrorColor(Color.RED);
        lv.setOnLoadingResultListener(this);
        btn_loading= (Button) findViewById(R.id.btn_loading);
        btn_success= (Button) findViewById(R.id.btn_success);
        btn_error= (Button) findViewById(R.id.btn_error);
        btn_loading.setOnClickListener(this);
        btn_success.setOnClickListener(this);
        btn_error.setOnClickListener(this);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loading:
                lv.start();
                break;
            case R.id.btn_success:
                lv.showSuccessResult();
                break;
            case R.id.btn_error:
                lv.showErrorResult();
                break;
        }
    }

    @Override
    public void onFillRingStart() {

    }

    @Override
    public void onFillRingEnd() {

    }

    @Override
    public void onResultShowStart() {

    }

    @Override
    public void onResultShowEnd() {

    }
}
