package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 添加电视墙输出参数
*/
public class NET_OUT_MONITORWALL_ADD extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 电视墙ID
    */
    public int              nMonitorWallID;

    public NET_OUT_MONITORWALL_ADD() {
        this.dwSize = this.size();
    }
}

