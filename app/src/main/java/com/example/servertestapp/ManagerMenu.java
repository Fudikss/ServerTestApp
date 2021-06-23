package com.example.servertestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ManagerMenu extends AppCompatActivity {
    private Button btnAdUpload, btnADelete, btnAdExposureTime;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_menu);

        setID();

        backPressCloseHandler = new BackPressCloseHandler(this);
        onClickAdUpload();
        onClickAdDelete();
        onClickAdExposureTime();
    }

    /**
     * 아이디값 세팅 메소드
     */
    void setID(){
        btnAdUpload = (Button)findViewById(R.id.adUpload);
        btnADelete = (Button)findViewById(R.id.adDelete);
        btnAdExposureTime = (Button)findViewById(R.id.adExposureTime);
    }

    /**
     * 버튼 클릭 시 광고 업로드 화면으로 이동함
     */
    void onClickAdUpload(){
        btnAdUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MInsertAD.class);
                startActivity(intent);

            }
        });
    }

    /**
     * 버튼 클릭 시 광고 삭제 화면으로 이동함
     */
    void onClickAdDelete(){
        btnADelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeleteAD.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 버튼 클릭 시 광고 노출 시간 화면으로 이동함
     */
    void onClickAdExposureTime(){
        btnAdExposureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyADshowTime.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
