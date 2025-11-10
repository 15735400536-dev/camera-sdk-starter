package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 测温温度数据
*/
public class NET_RADIOMETRY_TEMPER_DATA extends NetSDKLibStructure.SdkStructure
{
    /**
     * 测温温度信息个数
    */
    public int              nRadiometryTemperNum;
    /**
     * 测温温度信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RADIOMETRY_QUERY_INFO}
    */
    public NET_RADIOMETRY_QUERY_INFO[] stuRadiometryTemperInfo = new NET_RADIOMETRY_QUERY_INFO[32];
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[512];

    public NET_RADIOMETRY_TEMPER_DATA() {
        for(int i = 0; i < stuRadiometryTemperInfo.length; i++){
            stuRadiometryTemperInfo[i] = new NET_RADIOMETRY_QUERY_INFO();
        }
    }
}

