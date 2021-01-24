package com.elstp.wxqianghonbao.Tools;


import com.elstp.wxqianghonbao.BuildConfig;

import java.io.File;

public class GroupInfo {
    public String channelId;
    public int coolDown;
    public String groupName;
    public Long qrTime;
    public String qrcode;
    public String qrcodeImg;
    public File qrcodeImgFile = null;

    public GroupInfo() {
        String str = BuildConfig.FLAVOR;
        this.groupName = str;
        this.qrcodeImg = str;
        this.qrcode = str;
        this.qrTime = Long.valueOf(0);
        this.channelId = str;
        this.coolDown = 0;
    }
}