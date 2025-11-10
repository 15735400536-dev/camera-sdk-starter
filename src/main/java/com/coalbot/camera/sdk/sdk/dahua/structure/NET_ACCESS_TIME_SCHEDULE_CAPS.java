package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 时间计划相关能力
*/
public class NET_ACCESS_TIME_SCHEDULE_CAPS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 每次插入时间计划最大数目
    */
    public int              nMaxInsertRate;
    /**
     * 设备支持最大时间计划数量
    */
    public int              nMaxTimeScheduleCounts;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[512];

    public NET_ACCESS_TIME_SCHEDULE_CAPS() {
    }
}

