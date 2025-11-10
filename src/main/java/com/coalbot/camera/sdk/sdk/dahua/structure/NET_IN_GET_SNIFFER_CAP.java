package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetSnifferCaps 接口入参
*/
public class NET_IN_GET_SNIFFER_CAP extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_GET_SNIFFER_CAP() {
        this.dwSize = this.size();
    }
}

