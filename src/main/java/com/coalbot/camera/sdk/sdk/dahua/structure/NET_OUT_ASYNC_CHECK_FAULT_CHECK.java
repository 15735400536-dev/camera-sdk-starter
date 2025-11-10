package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_AsyncCheckFaultCheck 接口出参
*/
public class NET_OUT_ASYNC_CHECK_FAULT_CHECK extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ASYNC_CHECK_FAULT_CHECK() {
        this.dwSize = this.size();
    }
}

