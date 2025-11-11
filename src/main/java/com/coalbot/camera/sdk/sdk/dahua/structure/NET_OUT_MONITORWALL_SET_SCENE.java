package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_MonitorWallSetScene接口输出参数(设置电视墙场景)
*/
public class NET_OUT_MONITORWALL_SET_SCENE extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_MONITORWALL_SET_SCENE() {
        this.dwSize = this.size();
    }
}

