package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_GetPeripheralChipVersion 接口输出参数
*/
public class NET_OUT_GET_PERIPHERAL_CHIP_VERSION extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 从片版本信息个数
    */
    public int              nVersionCount;
    /**
     * 从片版本信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_PERIPHERAL_CHIP_VERSION}
    */
    public NET_PERIPHERAL_CHIP_VERSION[] stuVersion = new NET_PERIPHERAL_CHIP_VERSION[32];

    public NET_OUT_GET_PERIPHERAL_CHIP_VERSION() {
        this.dwSize = this.size();
        for(int i = 0; i < stuVersion.length; i++){
            stuVersion[i] = new NET_PERIPHERAL_CHIP_VERSION();
        }
    }
}

