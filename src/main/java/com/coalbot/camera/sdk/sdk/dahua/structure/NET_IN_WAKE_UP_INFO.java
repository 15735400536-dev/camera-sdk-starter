package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_DoWakeUpLowPowerDevcie 接口输入参数
*/
public class NET_IN_WAKE_UP_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_WAKE_UP_INFO() {
        this.dwSize = this.size();
    }
}

