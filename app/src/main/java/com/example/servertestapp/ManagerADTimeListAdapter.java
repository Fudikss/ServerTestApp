package com.example.servertestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ManagerADTimeListAdapter extends BaseAdapter {
    private ArrayList<MyData> MyADshowTime = new ArrayList<>();
    private ArrayList<MyData> TimeitemList = new ArrayList<>();
    private TextView showTimeAD_text;
    //private TextView number_text;

    @Override
    public int getCount() {
        return MyADshowTime.size();
    }

    @Override
    public MyData getItem(int position) {
        return MyADshowTime.get(position);
    }



    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        ViewGroup viewGroup = parent;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_ad_show_time, parent, false);
        }
        showTimeAD_text = (TextView)convertView.findViewById(R.id.showTimeAD_text);
        //number_text = (TextView)convertView.findViewById(R.id.number_text);
        //각 리스트에 렌더링시킬 아이템을 받아와야 함
        MyData myData = getItem(position);

        //각 위젯에 세팅된 아이템들을 렌더링
        showTimeAD_text.setText(myData.getADName());
        //number_text.setText(myData.getADTime());
        return convertView;
    }

    public void addItem(String adName){
        MyData myDate = new MyData();
        myDate.setADName(adName);
        MyADshowTime.add(myDate);
    }

    /*public void addTimeItem(String TimeName){
        Log.d("TimeName 값 : ", TimeName);
        MyData mydata = new MyData();
        mydata.setADTime(TimeName);
        TimeitemList.add(mydata);
    }*/
}
