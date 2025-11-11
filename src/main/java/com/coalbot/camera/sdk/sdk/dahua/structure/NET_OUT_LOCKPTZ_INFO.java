package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_LockPtz 接口输出参数
*/
public class NET_OUT_LOCKPTZ_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_LOCKPTZ_INFO() {
        this.dwSize = this.size();
    }
}

