package com.example.servertestapp;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class HttpConnectionManager {
    //내 컴퓨터 IPv4 주소::172.30.1.25
    public static final String baseURL = "http://172.30.1.25:3333";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private  static HttpConnectionManager instance;

    public static HttpConnectionManager getInstance(){
        if(instance == null){
            instance = new HttpConnectionManager();
        }
        return  instance;
    }

    /**
     * 로그인을 시도하는 메소드
     * @Param CID, CPassword, responseHandler
     * @throws//JSONException
     */
    public void login(String CID, String CPassword, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException {
        String url = baseURL + "/login";
        JSONObject jsonParams = new JSONObject();

        jsonParams.put("CID", CID);
        jsonParams.put("CPassword", CPassword);
        ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(null, url, entity, "application/json", responseHandler);
    }

    /**
     * 일반 사용자가 회원가입을 시도하는 메소드
     * @Param
     * 비밀번호 체크 추가해야함!!
     */

    public void makeAccount(String CID, String CPassword, String CName, String CPhoneNum, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/makeAccount";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CID", CID);
            jsonParams.put("CPassword", CPassword);
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            //StringEntity entity = new StringEntity(jsonParams.toString());
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 사업자가 회원가입을 시도하는 메소드
     * @Param
     * 사업자 번호 추가해야함!!!!
     * 비밀번호 체크 추가해야함!!!
     */
    public void makeMAccount(String MID, String MPassword, String MName, String MPhoneNum, String MBusinessNum, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/makeManageraccount";
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("MID", MID);
            jsonParams.put("MPassword", MPassword);
            jsonParams.put("MName", MName);
            jsonParams.put("MPhoneNum", MPhoneNum);
            jsonParams.put("MBusinessNum", MBusinessNum);
            //StringEntity entity = new StringEntity(jsonParams.toString());
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 광고 리스트를 보여주는 메소드
     * @Param
     *
     *
     */
    public void showADList(String AD, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/showADList";

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("AD", AD);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 광고주가 INSERT할 광고 메소드
     */
    public void insertAD(String AD, String MBusinessNum ,String MCID, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/insertAdText";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("AD", AD);
            jsonParams.put("MBusinessNum", MBusinessNum);
            jsonParams.put("MCID", MCID);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 광고주가 자신이 업로드한 광고 리스트를 출력하는 메소드
     */
    public void showMyDeleteADList(String MCID, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/DeleteAdList";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("MCID", MCID);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 광고주가 삭제할 광고를 없애는 메소드
     */

    public void mDeleteAdChoice(String AD, String MCID, JsonHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/mDeleteAdChoice";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("AD", AD);
            jsonParams.put("MCID", MCID);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * ID찾기를 누를 시, DB 내에 있는 CName, CPhoneNum 값을 비교하여 조회를 한다
     * 일치하면 DB 내에 있는 CID값을 response로 넣어 클라이언트로 다시 던져준다. (반드시 키 값을 통일화시킬 것)
     * @param CName
     * @param CPhoneNum
     * @param responseHandler
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void findID(String CName, String CPhoneNum, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/findid";
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }catch(Exception e){e.printStackTrace();}
    }

    /**
     * PW찾기를 누를 시, DB 내에 있는 CID, CName ,CPhoneNum 값을 조회한다.
     * 일치하면 DB 내에 있는 PW 값을 response로 넣어 클라이언트로 다시 던져준다. (이후 재설정으로 바꿔야 함)
     * @param CID
     * @param CName
     * @param CPhoneNum
     * @param responseHandler
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void findPW(String CID, String CName, String CPhoneNum, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/findPW";
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CID", CID);
            jsonParams.put("CName", CName);
            jsonParams.put("CPhoneNum", CPhoneNum);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        }
        catch(Exception e){e.printStackTrace();}
    }
    /**
     * 광고주의 광고가 출력된 시간을 INSERT하는 메소드
     */
    public void insertADtime(String AD, String time, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/insertADtime";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("AD", AD);
            jsonParams.put("time", time);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 광고주가 자신의 광고 출력 광고를 먼저 확인하는 메소드
     *
     */
    public void showMyADTime(String MCID, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/showMyADTime";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("MCID", MCID);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 광고주가 자신의 광고 시간을 받아오는 메소드
     */
    public void showTime(String AD, JsonHttpResponseHandler responseHandler)throws JSONException, UnsupportedEncodingException{
        String url = baseURL + "/showTime";

        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("AD", AD);
            ByteArrayEntity entity = new ByteArrayEntity(jsonParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, url, entity, "application/json", responseHandler);
        } catch (Exception e) { e.printStackTrace(); }
    }
}


