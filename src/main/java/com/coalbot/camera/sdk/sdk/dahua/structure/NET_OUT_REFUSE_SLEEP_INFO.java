package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_RefuseLowPowerDevSleep 接口输出参数
*/
public class NET_OUT_REFUSE_SLEEP_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_REFUSE_SLEEP_INFO() {
        this.dwSize = this.size();
    }
}

