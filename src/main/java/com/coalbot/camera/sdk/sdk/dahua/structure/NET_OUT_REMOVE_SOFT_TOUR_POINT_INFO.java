package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 421657
 * @description 接口 CLIENT_RemoveTourPoint 的输出参数
 * @origin autoTool
 * @date 2023/10/16 19:46:41
 */
public class NET_OUT_REMOVE_SOFT_TOUR_POINT_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * / 此结构体大小,必须赋值
     */
    public int              dwSize;

    public NET_OUT_REMOVE_SOFT_TOUR_POINT_INFO() {
        this.dwSize = this.size();
    }
}

