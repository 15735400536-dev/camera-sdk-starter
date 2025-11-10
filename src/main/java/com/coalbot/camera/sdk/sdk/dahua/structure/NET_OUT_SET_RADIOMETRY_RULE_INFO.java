package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SetRadiometryRule 接口出参
*/
public class NET_OUT_SET_RADIOMETRY_RULE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_SET_RADIOMETRY_RULE_INFO() {
        this.dwSize = this.size();
    }
}

