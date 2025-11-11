package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CIENT_SetSplitWindowsInfo接口输出参数
*/
public class NET_OUT_SPLIT_SET_WINDOWS_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_SPLIT_SET_WINDOWS_INFO() {
        this.dwSize = this.size();
    }
}

