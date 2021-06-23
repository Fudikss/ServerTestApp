package com.example.servertestapp;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    public BackPressCloseHandler(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed(){
        //버튼을 누를 시, 토스트 메시지를 띄움
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        //빠르게 2번 누르면 액티비티가 종료됨
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(){
        toast = Toast.makeText(activity, " \"뒤로\"버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT);
        toast.show();
    }
}
