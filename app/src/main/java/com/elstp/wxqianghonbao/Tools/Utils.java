package com.elstp.wxqianghonbao.Tools;

import android.Manifest;
import android.widget.Toast;


import com.elstp.wxqianghonbao.MainActivity;

import java.util.Collection;

public class Utils {

/** 常用权限（可能不全）  **/
// SMS（短信）
//    SEND_SMS
//    RECEIVE_SMS
//    READ_SMS
//    RECEIVE_WAP_PUSH
//    RECEIVE_MMS

//    STORAGE（存储卡-读写权限）
//    READ_EXTERNAL_STORAGE
//    WRITE_EXTERNAL_STORAGE

//    CONTACTS（联系人）
//    READ_CONTACTS
//    WRITE_CONTACTS
//    GET_ACCOUNTS

//    PHONE（手机）
//    READ_PHONE_STATE
//    CALL_PHONE
//    READ_CALL_LOG
//    WRITE_CALL_LOG
//    ADD_VOICEMAIL
//    USE_SIP
//    PROCESS_OUTGOING_CALLS

//    CALENDAR（日历）
//    READ_CALENDAR
//    WRITE_CALENDAR

//    CAMERA（相机）
//    CAMERA

//    LOCATION（位置）
//    ACCESS_FINE_LOCATION
//    ACCESS_COARSE_LOCATION

//    SENSORS（传感器）
//    BODY_SENSORS

//    MICROPHONE（麦克风）
//    RECORD_AUDIO

    /**
     *  读写权限  自己可以添加需要判断的权限
     */
    /**
     *  读写权限  自己可以添加需要判断的权限
     */
    public static String[]permissionsREAD={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };



    //集合是否是空的
    public static boolean isEmptyArray(Collection list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmptyArray(T[] list) {
        return list == null || list.length == 0;
    }
}
