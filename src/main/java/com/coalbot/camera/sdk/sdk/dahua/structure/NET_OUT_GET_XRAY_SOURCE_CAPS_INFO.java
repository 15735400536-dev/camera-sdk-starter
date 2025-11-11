package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_GetXRaySourceCaps 接口输出参数
*/
public class NET_OUT_GET_XRAY_SOURCE_CAPS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * X射线源能力集信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_XRAY_SOURCE_CAPS}
    */
    public NET_XRAY_SOURCE_CAPS stuCaps = new NET_XRAY_SOURCE_CAPS();

    public NET_OUT_GET_XRAY_SOURCE_CAPS_INFO() {
        this.dwSize = this.size();
    }
}

