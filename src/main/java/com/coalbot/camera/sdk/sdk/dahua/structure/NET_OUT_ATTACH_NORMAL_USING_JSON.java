package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachNormalUsingJson 接口输出参数
*/
public class NET_OUT_ATTACH_NORMAL_USING_JSON extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_NORMAL_USING_JSON() {
        this.dwSize = this.size();
    }
}

