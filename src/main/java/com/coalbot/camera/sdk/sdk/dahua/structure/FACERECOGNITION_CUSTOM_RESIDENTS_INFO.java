package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 居民信息
*/
public class FACERECOGNITION_CUSTOM_RESIDENTS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 绑定房屋信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.FACERECOGNITION_CUSTOM_HOUSE_INFO}
    */
    public FACERECOGNITION_CUSTOM_HOUSE_INFO[] stuHouse = new FACERECOGNITION_CUSTOM_HOUSE_INFO[8];
    /**
     * 绑定房屋信息个数
    */
    public int              nHouse;
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[2044];

    public FACERECOGNITION_CUSTOM_RESIDENTS_INFO() {
        for(int i = 0; i < stuHouse.length; i++){
            stuHouse[i] = new FACERECOGNITION_CUSTOM_HOUSE_INFO();
        }
    }
}

