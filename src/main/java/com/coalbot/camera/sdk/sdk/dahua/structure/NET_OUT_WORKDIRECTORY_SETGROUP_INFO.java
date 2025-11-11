package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 设置工作目录组名 出参
*/
public class NET_OUT_WORKDIRECTORY_SETGROUP_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_WORKDIRECTORY_SETGROUP_INFO() {
        this.dwSize = this.size();
    }
}

