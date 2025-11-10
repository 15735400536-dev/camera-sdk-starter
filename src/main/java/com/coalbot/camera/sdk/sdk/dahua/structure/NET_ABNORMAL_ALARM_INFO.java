package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 设备异常报警状态灯色控制
*/
public class NET_ABNORMAL_ALARM_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 视频遮挡状态灯色,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_PARKINGSPACELIGHT_INFO}
    */
    public NetSDKLib.NET_PARKINGSPACELIGHT_INFO stuVideoBlind = new NetSDKLib.NET_PARKINGSPACELIGHT_INFO();
    /**
     * 烟雾火焰状态灯色,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_PARKINGSPACELIGHT_INFO}
    */
    public NetSDKLib.NET_PARKINGSPACELIGHT_INFO stuSmokeFire = new NetSDKLib.NET_PARKINGSPACELIGHT_INFO();
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[256];

    public NET_ABNORMAL_ALARM_INFO() {
    }
}

