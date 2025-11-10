package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 订阅气象信息输出参数
*/
public class NET_OUT_WEATHER_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_WEATHER_INFO() {
        this.dwSize = this.size();
    }
}

