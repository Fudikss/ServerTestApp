package com.example.servertestapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MakeAccount extends AppCompatActivity {
    private HttpConnectionManager hcManager;
    private EditText CID, CPassword, checkCPassword, CName, CPhoneNum_1, CPhoneNum_2, CPhoneNum_3;
    private EditText MID, MPassword, checkMPassword, MName, MPhoneNum_1, MPhoneNum_2, MPhoneNum_3;
    private EditText HCPassword, checkHCPassword;
    private EditText mBusinessNum;
    private Button btnMakeClient, btnMakeManager;
    private TabHost tabHost;
    private TabHost.TabSpec ts1, ts2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_account);
        setTabHost();
        setID();
        onClickMakeClient();
        onClickMakeManger();
    }

    /**
     * 아이디 세팅
     */
    void setID(){
        btnMakeClient = (Button)findViewById(R.id.completeClient);
        btnMakeManager = (Button)findViewById(R.id.completeHairDresser);
    }

    /**
     * 버튼 클릭 시 일반사용자 회원가입
     */
    private void onClickMakeClient() {
        btnMakeClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CID = (EditText) findViewById(R.id.makeCName);
                CPassword = (EditText) findViewById(R.id.makeCPassword);
                CName = (EditText) findViewById(R.id.makeCName);
                CPhoneNum_1 = (EditText) findViewById(R.id.makeCPhoneNum_1);
                CPhoneNum_2 = (EditText) findViewById(R.id.makeCPhoneNum_2);
                CPhoneNum_3 = (EditText) findViewById(R.id.makeCPhoneNum_3);

                final String myID = CID.getText().toString();
                final String myPassword = CPassword.getText().toString();
                final String myName = CName.getText().toString();
                final String myPhoneNum = CPhoneNum_1.getText().toString() + '-' + CPhoneNum_2.getText().toString() + '-' + CPhoneNum_3.getText().toString();
                //final String checkPassword = checkCPassword.getText().toString();

                Log.d("myID", "" + myID.length());
                Log.d("myPassword", "" + myPassword.length());
                Log.d("myName", "" + myName.length());
                Log.d("myPhoneNum", "" + myPhoneNum.length());

                hcManager = HttpConnectionManager.getInstance();

                try {
                    hcManager.makeAccount(myID, myPassword, myName, myPhoneNum, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String resMsg = response.getString("result");
                                if (resMsg != null) {
                                    Toast.makeText(getApplicationContext(), resMsg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else
                                    Toast.makeText(getApplicationContext(), resMsg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 버튼 클릭 시 사업자 회원가입
     */
    public void onClickMakeManger(){
        btnMakeManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MID = (EditText)findViewById(R.id.makeMID);
                MName = (EditText)findViewById(R.id.makeMName);
                MPassword = (EditText)findViewById(R.id.makeMPassword);
                MPhoneNum_1 = (EditText)findViewById(R.id.makeMPhoneNum_1);
                MPhoneNum_2 = (EditText)findViewById(R.id.makeMPhoneNum_2);
                MPhoneNum_3 = (EditText)findViewById(R.id.makeMPhoneNum_3);
                mBusinessNum = (EditText)findViewById(R.id.makeLicense);

                final String myID = MID.getText().toString();
                final String myPassword = MPassword.getText().toString();
                final String myName = MName.getText().toString();
                final String myPhoneNum = MPhoneNum_1.getText().toString() + '-' + MPhoneNum_2.getText().toString() + '-' + MPhoneNum_3.getText().toString();
                final String myBusinessNum = mBusinessNum.getText().toString();

                Log.d("myID", "" + myID.length());
                Log.d("myPassword", "" + myPassword.length());
                Log.d("myName", "" + myName.length());
                Log.d("myPhoneNum", "" + myPhoneNum.length());
                Log.d("myBusinessNum", "" + myBusinessNum.length());

                hcManager = HttpConnectionManager.getInstance();

                try {
                    hcManager.makeMAccount(myID, myPassword, myName, myPhoneNum, myBusinessNum, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String resMsg = response.getString("result");
                                if (resMsg != null) {
                                    Toast.makeText(getApplicationContext(), resMsg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else
                                    Toast.makeText(getApplicationContext(), resMsg, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setTabHost(){
        tabHost = (TabHost) findViewById(R.id.selectClient);
        tabHost.setup();
        // First tab
        ts1 = tabHost.newTabSpec("Tab1");
        ts1.setContent(R.id.makeClient);
        ts1.setIndicator("일반회원");
        tabHost.addTab(ts1);
        // Second tab
        ts2 = tabHost.newTabSpec("Tab2");
        ts2.setContent(R.id.makeManger);
        ts2.setIndicator("사업자");
        tabHost.addTab(ts2);
    }
}
