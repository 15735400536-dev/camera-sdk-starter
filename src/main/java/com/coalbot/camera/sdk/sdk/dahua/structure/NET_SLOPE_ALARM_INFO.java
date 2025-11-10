package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 均值斜率超限报警信息
*/
public class NET_SLOPE_ALARM_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 报警前量化均值斜率
    */
    public float            fSlopeValue;
    /**
     * 报警时量化均值斜率
    */
    public float            fAlarmValue;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_SLOPE_ALARM_INFO() {
    }
}

