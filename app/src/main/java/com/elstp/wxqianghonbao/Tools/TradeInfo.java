package com.elstp.wxqianghonbao.Tools;


import com.elstp.wxqianghonbao.BuildConfig;

public class TradeInfo {
    public int notifyTimes;
    public String tradeAmount;
    public String tradeName;
    public String tradeNo;
    public Long tradeTime;
    public Long tradeTm;

    public TradeInfo() {
        init();
    }

    public void init() {
        Long valueOf = Long.valueOf(0);
        this.tradeTm = valueOf;
        this.tradeTime = valueOf;
        String str = BuildConfig.FLAVOR;
        this.tradeNo = str;
        this.tradeAmount = str;
        this.notifyTimes = 0;
        this.tradeName = str;
    }
}