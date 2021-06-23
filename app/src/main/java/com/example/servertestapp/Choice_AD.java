package com.example.servertestapp;

import android.content.Intent;
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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import cz.msebera.android.httpclient.Header;

public class Choice_AD extends AppCompatActivity {
    private Button btnChoiceAD;
    private ArrayList<String> ADListArr = new ArrayList<>();
    private ListView ADList;
    private HttpConnectionManager hcManager;
    private Data data;
    private ArrayAdapter adapter;
    private BluetoothConnection blue;
    private BluetoothSPP bt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_ad);

        hcManager = HttpConnectionManager.getInstance();

        setID();

        Intent intent = getIntent();
        String adMCIDName = intent.getStringExtra("AD");
        Log.d("adMCIDname 값은?", adMCIDName);

        data = Data.getInstance();

        showADListInfo(adMCIDName);
        setChoiceVar();
        sendBtn();
    }


    void setID(){
        ADList = (ListView)findViewById(R.id.ADlist);
        btnChoiceAD = (Button)findViewById(R.id.btnChoice);
    }

    public void showADListInfo(final String adMCIDName) {
        try {
            hcManager.showADList(adMCIDName, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        Log.i("ws", "---->>onSuccess JSONObject:" + response);

                        data.setADNum(response.length());
                        for(int i = 0; i<response.length(); i++){
                            JSONObject jsonObj = response.getJSONObject(i);
                            Log.d("jsonObj 데이터 :", jsonObj.toString());
                            ADListArr.add(jsonObj.getString("AD"));
                        }
                        dataSetting();
                    }catch (JSONException e){e.printStackTrace();}
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }catch(JSONException e){e.printStackTrace();}
        catch(UnsupportedEncodingException e){e.printStackTrace();}
    }

    void setChoiceVar(){
        ADList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getPositionText = (String)parent.getAdapter().getItem(position);
                Log.d("getPositionText 값 :", getPositionText);
                Log.d("posiNum 값 :", String.valueOf(position));
                data.setPositionText(getPositionText);
            }
        });
    }

    public void sendBtn(){
        btnChoiceAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AduSendData = data.getPositionText();
                Log.d("AduSendData 값 : ", AduSendData);
                bt = data.getBT();
                bt.send(AduSendData, true);
            }
        });
    }

    private void dataSetting(){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, ADListArr);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter();
        for(int i = 0; i < data.getADNum(); i++){
            myListViewAdapter.addItem(ADListArr.get(i));
        }
        ADList.setAdapter(myListViewAdapter);
        ADList.setAdapter(adapter);
    }
}

