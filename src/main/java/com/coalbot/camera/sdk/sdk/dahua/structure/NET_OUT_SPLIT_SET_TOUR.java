package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 窗口轮巡控制输出参数, 对应NET_SPLIT_OPERATE_SET_TOUR
*/
public class NET_OUT_SPLIT_SET_TOUR extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_SPLIT_SET_TOUR() {
        this.dwSize = this.size();
    }
}

