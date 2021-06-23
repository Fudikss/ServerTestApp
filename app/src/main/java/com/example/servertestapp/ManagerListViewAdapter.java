package com.example.servertestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ManagerListViewAdapter extends BaseAdapter{
    private ArrayList<MyData> MyDeleteADList = new ArrayList<>();
    private TextView DeleteAD_text;

    @Override
    public int getCount() {
        return MyDeleteADList.size();
    }

    @Override
    public MyData getItem(int position) {
        return MyDeleteADList.get(position);
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
            convertView = inflater.inflate(R.layout.list_view_item_deletead, parent, false);
        }
        DeleteAD_text = (TextView)convertView.findViewById(R.id.DeleteAD_text);
        //각 리스트에 렌더링시킬 아이템을 받아와야 함
        MyData myData = getItem(position);

        //각 위젯에 세팅된 아이템들을 렌더링
        DeleteAD_text.setText(myData.getADName());
        return convertView;
    }

    public void addItem(String adName){
        clearAllitems();
        MyData myDate = new MyData();
        myDate.setADName(adName);
        MyDeleteADList.add(myDate);
    }

    public void clearAllitems(){
        MyDeleteADList.clear();
    }

    /*public void upDateItemList(ArrayList<MyData> listItems){
        this.MyDeleteADList = listItems;
        notifyDataSetChanged();
    }*/
}
