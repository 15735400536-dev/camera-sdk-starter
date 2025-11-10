package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachRadarAISInfo接口出参
*/
public class NET_OUT_ATTACH_RADAR_AIS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_RADAR_AIS_INFO() {
        this.dwSize = this.size();
    }
}

