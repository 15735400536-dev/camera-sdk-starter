package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_WirelessUnbind 接口出参
*/
public class NET_OUT_WIRELESS_UNBIND extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_WIRELESS_UNBIND() {
        this.dwSize = this.size();
    }
}

