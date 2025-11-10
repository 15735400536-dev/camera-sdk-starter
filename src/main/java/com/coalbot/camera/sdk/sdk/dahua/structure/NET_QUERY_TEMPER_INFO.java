package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 温度信息
*/
public class NET_QUERY_TEMPER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 平均温度
    */
    public float            fTemperAve;
    /**
     * 最高的温度
    */
    public float            fTemperMax;
    /**
     * 最低的温度
    */
    public float            fTemperMin;
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[128];

    public NET_QUERY_TEMPER_INFO() {
    }
}

