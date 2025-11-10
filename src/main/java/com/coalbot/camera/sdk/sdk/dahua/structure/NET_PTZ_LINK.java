package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 云台联动项
*/
public class NET_PTZ_LINK extends NetSDKLibStructure.SdkStructure
{
    /**
     * 云台联动类型,参见枚举定义 {@link NetSDKLibStructure.CFG_LINK_TYPE}
    */
    public int              emType;
    /**
     * 联动参数1
    */
    public int              nParam1;
    /**
     * 联动参数2
    */
    public int              nParam2;
    /**
     * 联动参数3
    */
    public int              nParam3;
    /**
     * 所联动云台通道
    */
    public int              nChannelID;

    public NET_PTZ_LINK() {
    }
}

