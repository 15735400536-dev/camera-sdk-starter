package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_AttachDockEvents 接口输出参数
*/
public class NET_OUT_ATTACH_DOCK_EVENTS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_DOCK_EVENTS() {
        this.dwSize = this.size();
    }
}

