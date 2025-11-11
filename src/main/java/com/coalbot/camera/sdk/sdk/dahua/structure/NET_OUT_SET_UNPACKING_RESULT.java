package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SetUnpackingResult 接口输出参数
*/
public class NET_OUT_SET_UNPACKING_RESULT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_SET_UNPACKING_RESULT() {
        this.dwSize = this.size();
    }
}

