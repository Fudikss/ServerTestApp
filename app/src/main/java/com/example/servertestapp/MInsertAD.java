package com.example.servertestapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MInsertAD extends AppCompatActivity {
    private EditText InsertEditView;
    private Button btnInsertAD;
    private HttpConnectionManager hcManager;
    private Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minsert_ad);

        data = Data.getInstance();
        setID();
        onClickInsertAD();
    }

    void setID(){
        InsertEditView = (EditText) findViewById(R.id.InsertText);
        btnInsertAD = (Button)findViewById(R.id.InsertBtn);
    }

    void onClickInsertAD(){
        btnInsertAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myInsertAD = InsertEditView.getText().toString();
                final String MBusinessNum = "321321321";
                final String MCID = data.getMCID();

                Log.d("myInsertAD : ", myInsertAD);
                Log.d("MCID : ", MCID);
                try{
                    hcManager.getInstance().insertAD(myInsertAD, MBusinessNum ,MCID, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            AlertDialog.Builder dlg = new AlertDialog.Builder(MInsertAD.this);

                            dlg.setMessage("광고 업로드 완료");
                            Toast.makeText(getApplicationContext(), "광고 업로드 완료", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e){e.printStackTrace();}
                catch (UnsupportedEncodingException e){e.printStackTrace();}
            }
        });
    }
}
