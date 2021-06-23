package com.example.servertestapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DeleteAD extends AppCompatActivity {
    private ArrayList<String> ADdeleteList = new ArrayList<>();
    private ListView deleteList;
    private HttpConnectionManager hcManager;
    private Data data;
    private Button btnDelete;
    private ArrayAdapter adapter;
    private ManagerListViewAdapter ManagerListViewAdapter;
    private int posiNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_ad);

        hcManager = HttpConnectionManager.getInstance();
        data = Data.getInstance();
        setID();
        final String myMCID = data.getMCID();
        Log.d("String myMCID : ", myMCID);
        showMyADList(myMCID);
        deleteBtn();
        setDeleteVar();
    }

    void setID(){
        deleteList = (ListView)findViewById(R.id.deleteList);
        btnDelete = (Button)findViewById(R.id.btndelete);
    }

    public void showMyADList(final String myMCID){
        try{
            hcManager.showMyDeleteADList(myMCID, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try{
                        data.setADNum(response.length());
                        for(int i = 0; i<response.length(); i++){
                            JSONObject jsonObj = response.getJSONObject(i);
                            Log.d("jsonObj 데이터 :", jsonObj.toString());
                            ADdeleteList.add(jsonObj.getString("AD"));
                        }
                        DataSetting();
                    } catch (JSONException e) { e.printStackTrace(); }
                    super.onSuccess(statusCode, headers, response);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } catch (JSONException e) { e.printStackTrace(); }
        catch (UnsupportedEncodingException e) { e.printStackTrace();}
    }

    public void deleteBtn(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MCID = data.getMCID();
                String positionText = data.getPositionText();
                Log.d("MCID 값 : ", MCID);
                Log.d("positionText 값 : ", positionText);
                try {
                    hcManager.mDeleteAdChoice( positionText, MCID, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                Log.i("성공", "성공");
                                int count, checked;
                                count = adapter.getCount();

                                if(count > 0){
                                    checked = deleteList.getCheckedItemPosition();

                                    if( checked > -1 && checked < count){
                                        ADdeleteList.remove(checked);
                                        deleteList.clearChoices();
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                            } catch (Exception e) { e.printStackTrace(); }
                            super.onSuccess(statusCode, headers, response);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } catch (JSONException e) { e.printStackTrace(); }
                catch (UnsupportedEncodingException e) { e.printStackTrace(); }

            }
        });
    }

    void setDeleteVar(){
        deleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posiNum = deleteList.getCheckedItemPosition();
                String getPositionText = (String)parent.getAdapter().getItem(position);
                Log.d("getPositionText 값 :", getPositionText);
                Log.d("posiNum 값 :", String.valueOf(position));
                data.setPositionText(getPositionText);
                data.setPositionNum(posiNum);
            }
        });
    }

    void DataSetting(){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, ADdeleteList);
        ManagerListViewAdapter = new ManagerListViewAdapter();
        for(int i = 0; i < data.getADNum(); i++){
            ManagerListViewAdapter.addItem(ADdeleteList.get(i));
        }
        deleteList.setAdapter(ManagerListViewAdapter);
        deleteList.setAdapter(adapter);
    }
}
