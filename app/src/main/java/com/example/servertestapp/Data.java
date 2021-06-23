package com.example.servertestapp;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class Data {

    private int Grade;
    private String CID, MCID;
    private String CPassword, MPassword;
    private String CName, MName;
    private String CPhoneNum, MPhoneNum;
    private String positionText;
    private int positionNum;
    private BluetoothSPP bt;
    private boolean pointOn;
    private boolean pointOff;
    private String time;

    private int ADNum;

    private Data() { }
    private static Data instance;

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public int getGrade() { return Grade; }
    public void setGrade(int grade) { Grade = grade; }

    public String getCID() { return CID; }
    public void setCID(String CID){ this.CID = CID; }

    public String getCName() { return CName; }
    public void setCName(String CName) { this.CName = CName; }

    public String getCPhoneNum() { return CPhoneNum; }
    public void setCPhoneNum(String CPhoneNum) {
        this.CPhoneNum = CPhoneNum;
    }



    public String getMCID() { return MCID; }
    public void setMCID(String MID){ this.MCID= MID; }

    public int getADNum() { return ADNum; }
    public void setADNum(int ADNum) { this.ADNum= ADNum; }

    public String getPositionText(){return positionText; }
    public void setPositionText(String positionText){ this.positionText = positionText;}

    public int getPositionNum(){return positionNum;}
    public void setPositionNum(int posiNum){this.positionNum = posiNum;}

    public BluetoothSPP getBT(){return bt;}
    public void setBT(BluetoothSPP bt){ this.bt = bt; }

    public boolean getPointOn(){return pointOn;}
    public void setPointOn(boolean pointOn){this.pointOn = pointOn;}

    public boolean getPointOff(){return pointOff;}
    public void setPointOff(boolean pointOff){this.pointOff = pointOff;}

    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}
}
