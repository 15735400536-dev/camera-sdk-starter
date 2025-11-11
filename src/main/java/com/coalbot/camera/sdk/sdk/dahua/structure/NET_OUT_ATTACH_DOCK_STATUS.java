package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachDockStatus 接口输出参数
*/
public class NET_OUT_ATTACH_DOCK_STATUS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_DOCK_STATUS() {
        this.dwSize = this.size();
    }
}

