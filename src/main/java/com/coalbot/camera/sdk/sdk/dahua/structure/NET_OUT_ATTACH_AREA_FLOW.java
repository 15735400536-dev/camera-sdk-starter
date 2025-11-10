package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_AttachAreaFlow 输出参数
*/
public class NET_OUT_ATTACH_AREA_FLOW extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ATTACH_AREA_FLOW() {
        this.dwSize = this.size();
    }
}

