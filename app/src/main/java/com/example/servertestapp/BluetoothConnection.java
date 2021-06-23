package com.example.servertestapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import cz.msebera.android.httpclient.Header;

public class BluetoothConnection extends AppCompatActivity{
    private BackPressCloseHandler backPressCloseHandler;
    private BluetoothSPP bt;
    private Data data;
    private Button btnChoiceAD, btnOn, btnOff;
    private ArrayList<String> ADListArr = new ArrayList<>();
    private ListView ADList;
    private HttpConnectionManager hcManager;
    private ArrayAdapter adapter;
    private TextView mTimeTextView;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect);

        // 객체 생성 후 미리 선언한 변수에 넣음
        bt = new BluetoothSPP(this); //Initializing
        backPressCloseHandler = new BackPressCloseHandler(this);
        data.getInstance();


        hcManager = HttpConnectionManager.getInstance();

        setID();

        Intent intent = getIntent();
        String adMCIDName = intent.getStringExtra("AD");
        Log.d("adMCIDname 값은?", adMCIDName);

        data = Data.getInstance();

        showADListInfo(adMCIDName);
        setChoiceVar();
        setOn();
        setOff();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#4ea1d3"));
        }

        mTimeTextView = (TextView) findViewById(R.id.timeView);


        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가라면
            // 사용불가라고 토스트 띄워줌
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            // 화면 종료
            finish();
        }

        // 데이터를 받았는지 감지하는 리스너
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            //데이터 수신되면
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(BluetoothConnection.this, message, Toast.LENGTH_SHORT).show(); // 토스트로 데이터 띄움
            }
        });
        // 블루투스가 잘 연결이 되었는지 감지하는 리스너
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                //Toast.makeText(getApplicationContext(), "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
        // 연결하는 기능 버튼 가져와서 이용하기
        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        // 버튼 클릭하면
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) { // 현재 버튼의 상태에 따라 연결이 되어있으면 끊고, 반대면 연결
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

    }

    void setID(){
        ADList = (ListView)findViewById(R.id.ADlist);
        btnChoiceAD = (Button)findViewById(R.id.btnChoice);
        btnOn = (Button)findViewById(R.id.btnOn);
        btnOff = (Button)findViewById(R.id.btnOff);
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



    private void dataSetting(){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, ADListArr);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter();
        for(int i = 0; i < data.getADNum(); i++){
            myListViewAdapter.addItem(ADListArr.get(i));
        }
        ADList.setAdapter(myListViewAdapter);
        ADList.setAdapter(adapter);
    }

    // 앱 중단시 (액티비티 나가거나, 특정 사유로 중단시)
    public void onDestroy() {
        super.onDestroy();
        Log.i("파괴됨", "파괴됨");
        bt.stopService(); //블루투스 중지
    }

    // 앱이 시작하면
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { // 앱의 상태를 보고 블루투스 사용 가능하면
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 새로운 액티비티 띄워줌, 거기에 현재 가능한 블루투스 정보 intent로 넘겨
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) { // 블루투스 사용 불가
                // setupService() 실행하도록
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기끼리
                // 셋팅 후 연결되면 setup()으로
                setup();
            }
        }
    }
    // 블루투스 사용 - 데이터 전송
    public void setup() {
        Button btnSend = findViewById(R.id.btnChoice); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String AduSendData = data.getPositionText();
                Log.d("AduSendData 값 : ", AduSendData);
                bt.send(AduSendData, true);
                data.setPointOn(true);
                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });
    }

    public void setOn(){
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataOn = "ON";
                Log.d("dataOn 값 : ", dataOn);
                bt.send(dataOn, true);
                isRunning= true;
            }
        });
    }

    public void setOff(){
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataOff = "OFF";
                Log.d("dataOff 값 : ", dataOff);
                bt.send(dataOff, true);
                data.setPointOff(true);
                isRunning= false;
            }
        });
    }

    /*public void pointOn(){
            timeThread = new Thread(new timeThread());
            timeThread.start();
    }*/

    /*public void pointOff(){
        isRunning= !isRunning;
        if(test2){
            timeThread.interrupt();
        }
        test2 = false;
        Log.d("test2값 : ", String.valueOf(test2));
    }*/


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 아까 응답의 코드에 따라 연결 가능한 디바이스와 연결 시도 후 ok 뜨면 데이터 전송
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) { // 연결시도
            if (resultCode == Activity.RESULT_OK) // 연결됨
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) { // 연결 가능
            if (resultCode == Activity.RESULT_OK) { // 연결됨
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else { // 사용불가
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            data.setTime(result);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d%02d%02d", hour, min, sec);
            mTimeTextView.setText(result2);
            if(isRunning == false){
                String time = data.getTime();
                String adText = data.getPositionText();
                Log.d("time 값 확인 : ", time);
                Log.d("adText 값 확인 : ", adText);

                try {
                    hcManager.insertADtime(adText, time, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(BluetoothConnection.this);

                                dlg.setMessage("광고 시간 업로드 완료");
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
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                mTimeTextView.setText("");
                                mTimeTextView.setText("000000");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }

        }
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
