package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_WirelessUnbind 接口输入参数
*/
public class NET_IN_WIRELESS_UNBIND extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小，必须赋值
    */
    public int              dwSize;
    /**
     * SIM卡的解绑码
    */
    public byte[]           szPassword = new byte[64];

    public NET_IN_WIRELESS_UNBIND() {
        this.dwSize = this.size();
    }
}

