package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
/**
 * 订阅气象信息输入参数
*/
public class NET_IN_WEATHER_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 云台通道
    */
    public int              nChannel;
    /**
     * 回调函数,参见回调函数定义 {@link NetSDKLib.fWeatherInfoCallBack}
    */
    public NetSDKLib.fWeatherInfoCallBack cbWeatherInfo;
    /**
     * 用户数据
    */
    public Pointer          dwUser;

    public NET_IN_WEATHER_INFO() {
        this.dwSize = this.size();
    }
}

