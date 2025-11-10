package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description CLIENT_GetThingsDevList接口输入参数
 * @date 2022/04/20 10:16:56
 */
public class NET_IN_THINGS_GET_DEVLIST extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小, 调用者必须初始化该字段
     */
    public int              dwSize;

    public NET_IN_THINGS_GET_DEVLIST() {
        this.dwSize = this.size();
    }
}

