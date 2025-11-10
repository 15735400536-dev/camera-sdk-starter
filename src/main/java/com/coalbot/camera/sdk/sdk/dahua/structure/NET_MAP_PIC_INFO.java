package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 图片描述信息
*/
public class NET_MAP_PIC_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 图片格式: png, jpg, bmp
    */
    public byte[]           szFormat = new byte[16];
    /**
     * 图片宽度
    */
    public int              nWidth;
    /**
     * 图片高度
    */
    public int              nHeight;
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[512];

    public NET_MAP_PIC_INFO() {
    }
}

