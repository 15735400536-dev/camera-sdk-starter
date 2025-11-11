package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * EM_RPC_NETAPP_TYPE_GET_NET_RESOURCE_STAT 入参
*/
public class NET_IN_NETAPP_GET_NET_RESOURCE_STAT extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_NETAPP_GET_NET_RESOURCE_STAT() {
        this.dwSize = this.size();
    }
}

