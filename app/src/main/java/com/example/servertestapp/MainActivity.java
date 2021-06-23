package com.example.servertestapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private Button btnMakeAccount, btnLogin, buttonFindAccount;
    private EditText CID, CPassword;
    private BackPressCloseHandler backPressCloseHandler;
    private HttpConnectionManager hcManager;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = Data.getInstance();
        CID = (EditText)findViewById(R.id.CID);
        CPassword = (EditText)findViewById(R.id.CPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnMakeAccount = (Button) findViewById(R.id.buttonMakeAccount);
        buttonFindAccount = (Button)findViewById(R.id.buttonFindAccount);

        backPressCloseHandler = new BackPressCloseHandler(this); //2번 클릭 시 종료
        onClickMakeAccount();
        onClickLogin();
        onClickFindAccount();
    }

    void onClickMakeAccount(){
        btnMakeAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAccount.class);
                startActivity(intent);
            }
        });
    }

    void onClickLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myID = CID.getText().toString();
                final String myPassword = CPassword.getText().toString();

                try{
                    hcManager.getInstance().login(myID, myPassword, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                int success = response.getInt("success");
                                Log.d("success", ""+success);

                                if(success == 1){
                                    int myGrade = response.getInt("Grade");
                                    data.setGrade(myGrade);
                                    Log.d("mygrade", "" +myGrade);


                                    if(Data.getInstance().getGrade() == 0){
                                        //다음 화면 만들고 넘어가게 만들어야함
                                        data.setCID(myID);
                                        data.setCName(response.getString("CName"));

                                        Intent intent = new Intent(getApplicationContext(), BluetoothConnection.class);
                                        intent.putExtra("AD", "AD");
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(getApplicationContext(), "일반사용자 로그인 성공", Toast.LENGTH_SHORT).show();
                                    }else if(Data.getInstance().getGrade() == 1){

                                        data.setMCID(myID);
                                        data.setCName(response.getString("CName"));
                                        //사업자 화면으로 넘어가게 만들어야함
                                        Intent intent = new Intent(getApplicationContext(), ManagerMenu.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(getApplicationContext(), "사업자 로그인 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Fail", "Oops, response fail");
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
                catch (JSONException e) { e.printStackTrace(); }


            }
        });
    }

    void onClickFindAccount(){
        buttonFindAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindAccount.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() { backPressCloseHandler.onBackPressed(); }
}
