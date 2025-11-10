package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 主驾驶人脸图片信息
*/
public class NET_DRIVER_FACE_IMAGE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 图片文件大小，单位:字节
    */
    public int              nLength;
    /**
     * 图片偏移字节数
    */
    public int              nOffset;
    /**
     * 预留字段
    */
    public byte[]           szReserved = new byte[256];

    public NET_DRIVER_FACE_IMAGE_INFO() {
    }
}

