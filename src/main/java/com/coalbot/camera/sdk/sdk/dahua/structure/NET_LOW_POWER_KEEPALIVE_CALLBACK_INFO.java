package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * 低功耗设备保活状态回调数据
*/
public class NET_LOW_POWER_KEEPALIVE_CALLBACK_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 是否收到设备保活包
    */
    public int              bKeepAlive;
    /**
     * 用户数据
    */
    public Pointer          dwUserData;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_LOW_POWER_KEEPALIVE_CALLBACK_INFO() {
    }
}

