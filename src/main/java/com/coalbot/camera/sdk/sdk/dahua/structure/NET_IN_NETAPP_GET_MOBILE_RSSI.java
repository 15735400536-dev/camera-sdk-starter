package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * EM_RPC_NETAPP_TYPE_GET_MOBILE_RSSI 入参
*/
public class NET_IN_NETAPP_GET_MOBILE_RSSI extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 无线模块名称,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_WIRELESS_MODE}
    */
    public int              emName;

    public NET_IN_NETAPP_GET_MOBILE_RSSI() {
        this.dwSize = this.size();
    }
}

