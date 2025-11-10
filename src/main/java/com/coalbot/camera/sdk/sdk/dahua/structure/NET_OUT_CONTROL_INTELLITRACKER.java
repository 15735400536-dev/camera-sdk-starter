package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 智能球机控制输出参数
*/
public class NET_OUT_CONTROL_INTELLITRACKER extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_CONTROL_INTELLITRACKER() {
        this.dwSize = this.size();
    }
}

