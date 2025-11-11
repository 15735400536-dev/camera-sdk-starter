package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_StopApp 接口输出参数
*/
public class NET_IN_STOP_APP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * APP的ID
    */
    public int              nAppID;
    /**
     * APP的名称
    */
    public byte[]           szAppName = new byte[128];

    public NET_IN_STOP_APP() {
        this.dwSize = this.size();
    }
}

