package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 获取热图数据接口出参
 * @date 2020/9/21
 */
public class NET_OUT_GET_VIDEOSTAT_HEATMAP extends NetSDKLibStructure.SdkStructure {
    /**
     * 此结构体大小
     */
    public int              dwSize;
    /**
     * 查询的Token
     */
    public int              nToken;

    public NET_OUT_GET_VIDEOSTAT_HEATMAP(){
        this.dwSize=size();
    }
}

