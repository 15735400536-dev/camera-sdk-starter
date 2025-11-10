package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
/**
 * CLIENT_AttachNormalUsingJson 接口输入参数
*/
public class NET_IN_ATTACH_NORMAL_USING_JSON extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 订阅类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SUPPORT_ATTACH_TYPE}
    */
    public int              emAttachType;
    /**
     * 订阅参数，见EM_SUPPORT_ATTACH_TYPE枚举说明
    */
    public Pointer          pstuAttachParam;

    public NET_IN_ATTACH_NORMAL_USING_JSON() {
        this.dwSize = this.size();
    }
}

