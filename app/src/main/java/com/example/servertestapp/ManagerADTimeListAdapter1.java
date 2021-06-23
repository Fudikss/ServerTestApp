package com.example.servertestapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ManagerADTimeListAdapter1 extends BaseAdapter {
    private ArrayList<MyData> TimeitemList = new ArrayList<>();
    private TextView number_text;

    private int position;
    private ViewGroup parent;

    @Override
    public int getCount() {
        return TimeitemList.size();
    }

    @Override
    public MyData getItem(int position) {
        return TimeitemList.get(position);
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

        number_text = (TextView)convertView.findViewById(R.id.number_text);
        //각 리스트에 렌더링시킬 아이템을 받아와야 함
        MyData myData = getItem(position);

        //각 위젯에 세팅된 아이템들을 렌더링

        number_text.setText(myData.getADTime());
        return convertView;
    }


    public void addTimeItem(String TimeName){
        Log.d("addTimeItem 변수값 : ", TimeName);
        MyData mydata = new MyData();
        mydata.setADTime(TimeName);
        TimeitemList.add(mydata);
    }
}
