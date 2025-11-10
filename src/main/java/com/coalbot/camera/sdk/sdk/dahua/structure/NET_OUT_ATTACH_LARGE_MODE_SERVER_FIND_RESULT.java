package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachLargeModeServerFindResult 输出参数
*/
public class NET_OUT_ATTACH_LARGE_MODE_SERVER_FIND_RESULT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ATTACH_LARGE_MODE_SERVER_FIND_RESULT() {
        this.dwSize = this.size();
    }
}

