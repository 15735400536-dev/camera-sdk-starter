package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_GetSnifferInfo 接口输入参数
*/
public class NET_IN_GET_SNIFFER_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    public Pointer          pszNetInterface;

    public NET_IN_GET_SNIFFER_INFO() {
        this.dwSize = this.size();
    }
}

