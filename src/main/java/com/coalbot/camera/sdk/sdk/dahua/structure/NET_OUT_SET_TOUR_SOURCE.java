package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SetTourSource 接口输出参数(设置窗口轮巡显示源)
*/
public class NET_OUT_SET_TOUR_SOURCE extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_SET_TOUR_SOURCE() {
        this.dwSize = this.size();
    }
}

