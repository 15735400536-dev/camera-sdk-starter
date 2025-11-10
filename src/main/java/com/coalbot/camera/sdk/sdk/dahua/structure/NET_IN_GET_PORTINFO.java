package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetStoragePortInfo 入参
*/
public class NET_IN_GET_PORTINFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_GET_PORTINFO() {
        this.dwSize = this.size();
    }
}

