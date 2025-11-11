package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 获取工作目录实例 出参
*/
public class NET_OUT_WORKDIRECTORY_GETGROUP_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 工作目录组名
    */
    public byte[]           szGroupName = new byte[256];

    public NET_OUT_WORKDIRECTORY_GETGROUP_INFO() {
        this.dwSize = this.size();
    }
}

