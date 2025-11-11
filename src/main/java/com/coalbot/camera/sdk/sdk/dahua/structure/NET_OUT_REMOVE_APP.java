package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_RemoveApp 接口输出参数
*/
public class NET_OUT_REMOVE_APP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_REMOVE_APP() {
        this.dwSize = this.size();
    }
}

