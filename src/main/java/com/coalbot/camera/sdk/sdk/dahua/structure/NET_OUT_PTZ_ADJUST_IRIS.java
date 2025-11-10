package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_PTZAdjustIris 接口输出参数
*/
public class NET_OUT_PTZ_ADJUST_IRIS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_PTZ_ADJUST_IRIS() {
        this.dwSize = this.size();
    }
}

