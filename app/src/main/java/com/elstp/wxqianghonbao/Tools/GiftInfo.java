package com.elstp.wxqianghonbao.Tools;


import com.elstp.wxqianghonbao.BuildConfig;

public class GiftInfo {
    public String creatorName;
    public String receiveAmount;
    public String receiveName;

    public GiftInfo() {
        init();
    }

    public void init() {
        String str = BuildConfig.FLAVOR;
        this.receiveAmount = str;
        this.creatorName = str;
        this.receiveName = str;
    }
}