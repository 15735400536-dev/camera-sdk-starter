package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetSoftwareVersion 入参
*/
public class NET_IN_GET_SOFTWAREVERSION_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_GET_SOFTWAREVERSION_INFO() {
        this.dwSize = this.size();
    }
}

