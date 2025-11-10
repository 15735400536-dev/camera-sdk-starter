package com.coalbot.camera.sdk.sdk.dahua.structure;



/**
 * @author 47081
 * @version 1.0
 * @description 热度图订阅接口出参
 * @date 2020/9/21
 */
public class NET_OUT_ATTACH_VIDEOSTAT_HEATMAP extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;

    public NET_OUT_ATTACH_VIDEOSTAT_HEATMAP() {
        this.dwSize = size();
    }
}

