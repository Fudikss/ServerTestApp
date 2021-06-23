package com.example.servertestapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyADshowTime extends AppCompatActivity {
    private Data data;
    private HttpConnectionManager hcManager;
    private ListView showList;
    private ArrayList<String> ADshowTimeList = new ArrayList<>();
    private ArrayAdapter adapter;
    private ManagerADTimeListAdapter ManagerADTimeListAdapter;
    private Button printTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myadshow);

        hcManager = HttpConnectionManager.getInstance();
        data = Data.getInstance();
        setID();
        final String myMCID = data.getMCID();
        MyADTime(myMCID);
        setChoiceVar();
        setShowTime();

        /*printTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printAD = data.getPositionText();
                Log.d("pointAD의 값 : ", printAD);
                try {
                    hcManager.showTime(printAD, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                    JSONObject jsonObj = response.getJSONObject("time");
                                    String test = jsonObj.getString("time");
                                    Log.d("test 값 :", test);

                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
                            }catch (JSONException e){e.printStackTrace();}
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }catch(JSONException e){e.printStackTrace();}
                catch(UnsupportedEncodingException e){e.printStackTrace();}
            }
        });*/
    }

    void setID(){
        showList = (ListView)findViewById(R.id.showList);
        printTime = (Button)findViewById(R.id.printTime);
    }

    /**
     * 선택된 광고에 대한 광고 시간 출력 메소드
     */
    void setShowTime(){
        printTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String printAD = data.getPositionText();
                Log.d("pointAD의 값 : ", printAD);
                try {
                    hcManager.showTime(printAD, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            try {
                                data.setADNum(response.length());
                                String test = null;
                                for(int i = 0; i<response.length(); i++){
                                    JSONObject jsonObj = response.getJSONObject(i);
                                    Log.d("jsonObj 데이터 :", jsonObj.toString());
                                    test = jsonObj.getString("time");
                                }

                                Log.d("test 값 :", test);

                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
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
        });
    }


    /**
     * 광고 리스트 출력
     * @param myMCID
     */
    public void MyADTime(String myMCID){
        try{
            hcManager.showMyADTime(myMCID, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try{
                        data.setADNum(response.length());
                        for(int i = 0; i<response.length(); i++){
                            JSONObject jsonObj = response.getJSONObject(i);
                            Log.d("jsonObj 데이터 :", jsonObj.toString());
                            ADshowTimeList.add(jsonObj.getString("AD"));
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

    void DataSetting(){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, ADshowTimeList);
        ManagerADTimeListAdapter = new ManagerADTimeListAdapter();
        for(int i = 0; i < data.getADNum(); i++){
            ManagerADTimeListAdapter.addItem(ADshowTimeList.get(i));
        }
        showList.setAdapter(ManagerADTimeListAdapter);
        showList.setAdapter(adapter);
    }

    /**
     * 선택된 position값 data에 초기화
     */
    void setChoiceVar(){
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getPositionText = (String)parent.getAdapter().getItem(position);
                Log.d("getPositionText 값 :", getPositionText);
                Log.d("posiNum 값 :", String.valueOf(position));
                data.setPositionText(getPositionText);
            }
        });
    }
}
