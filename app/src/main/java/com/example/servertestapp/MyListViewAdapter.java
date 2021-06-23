package com.example.servertestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {
    /*아이템 XML파일을 담는 파일*/
    private ArrayList<MyData> ADList = new ArrayList<>();
    private TextView ADName_text;

    @Override
    public int getCount() {
        return ADList.size();
    }

    @Override
    public MyData getItem(int position) {
        return ADList.get(position);
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
            convertView = inflater.inflate(R.layout.list_view_item_layout, parent, false);
        }
        ADName_text = (TextView)convertView.findViewById(R.id.ADname_text);
        //각 리스트에 렌더링시킬 아이템을 받아와야 함
        MyData myData = getItem(position);

        //각 위젯에 세팅된 아이템들을 렌더링
        ADName_text.setText(myData.getADName());
        return convertView;

    }

    /**
     * 아이템(목차)에 데이터를 추가하는 메소드
     * @param
     */
    public void addItem(String adName){
        MyData myDate = new MyData();
        myDate.setADName(adName);
        ADList.add(myDate);
    }
}
