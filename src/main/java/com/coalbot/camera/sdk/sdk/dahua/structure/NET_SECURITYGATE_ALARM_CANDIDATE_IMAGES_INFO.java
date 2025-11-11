package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 注册库人员图片信息
*/
public class NET_SECURITYGATE_ALARM_CANDIDATE_IMAGES_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 在二进制数据块中的偏移
    */
    public int              nOffSet;
    /**
     * 图片大小,单位字节
    */
    public int              nLength;
    /**
     * 预留字节
    */
    public byte[]           byReserved = new byte[24];

    public NET_SECURITYGATE_ALARM_CANDIDATE_IMAGES_INFO() {
    }
}

