package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 日期
*/
public class CFG_DATA_TIME extends NetSDKLibStructure.SdkStructure
{
    /**
     * 年
    */
    public int              dwYear;
    /**
     * 月
    */
    public int              dwMonth;
    /**
     * 日
    */
    public int              dwDay;
    /**
     * 时
    */
    public int              dwHour;
    /**
     * 分
    */
    public int              dwMinute;
    /**
     * 秒
    */
    public int              dwSecond;
    /**
     * 保留字段
    */
    public int[]            dwReserved = new int[2];

    public CFG_DATA_TIME() {
    }
}

