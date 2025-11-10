package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachDockInfo 接口输出参数
*/
public class NET_OUT_ATTACH_DOCK_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_DOCK_INFO() {
        this.dwSize = this.size();
    }
}

