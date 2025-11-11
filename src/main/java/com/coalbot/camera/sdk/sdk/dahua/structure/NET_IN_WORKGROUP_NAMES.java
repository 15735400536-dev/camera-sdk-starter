package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_QueryDevInfo , NET_QUERY_WORKGROUP_NAMES 命令输入参数
*/
public class NET_IN_WORKGROUP_NAMES extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_WORKGROUP_NAMES() {
        this.dwSize = this.size();
    }
}

