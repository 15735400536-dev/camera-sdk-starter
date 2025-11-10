package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_GetSnifferCaps 接口出参
*/
public class NET_OUT_GET_SNIFFER_CAP extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 是否支持远程流式抓包
    */
    public int              bRemoteCap;

    public NET_OUT_GET_SNIFFER_CAP() {
        this.dwSize = this.size();
    }
}

