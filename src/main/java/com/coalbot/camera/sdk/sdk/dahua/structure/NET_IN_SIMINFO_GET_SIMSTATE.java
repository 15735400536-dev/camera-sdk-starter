package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 获取SIM卡的状态类型入参
*/
public class NET_IN_SIMINFO_GET_SIMSTATE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 赋值为结构体大小
    */
    public int              dwSize;
    /**
     * 无线模块名称,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_WIRELESS_MODE}
    */
    public int              emMode;

    public NET_IN_SIMINFO_GET_SIMSTATE() {
        this.dwSize = this.size();
    }
}

