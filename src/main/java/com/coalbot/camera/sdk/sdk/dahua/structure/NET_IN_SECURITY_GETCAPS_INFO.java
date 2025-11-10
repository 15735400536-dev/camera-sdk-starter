package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_SecurityGetCaps接口入参
*/
public class NET_IN_SECURITY_GETCAPS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 是否下发通道号
    */
    public int              bChannel;

    public NET_IN_SECURITY_GETCAPS_INFO() {
        this.dwSize = this.size();
    }
}

