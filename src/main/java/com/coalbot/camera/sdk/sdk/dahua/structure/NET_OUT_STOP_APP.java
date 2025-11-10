package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_StopApp 接口输出参数
*/
public class NET_OUT_STOP_APP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_STOP_APP() {
        this.dwSize = this.size();
    }
}

