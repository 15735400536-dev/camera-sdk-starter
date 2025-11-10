package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_ControlMiscPower 输出参数
*/
public class NET_OUT_CONTROL_MISC_POWER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_CONTROL_MISC_POWER_INFO() {
        this.dwSize = this.size();
    }
}

