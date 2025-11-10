package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_RadiometryAttachTemper 出参
*/
public class NET_OUT_RADIOMETRY_ATTACH_TEMPER extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_RADIOMETRY_ATTACH_TEMPER() {
        this.dwSize = this.size();
    }
}

