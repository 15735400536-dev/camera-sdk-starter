package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 开包包裹图片信息
*/
public class NET_XRAY_UNPACKING_IMAGE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 在二进制数据块中的偏移
    */
    public int              nOffset;
    /**
     * 图片大小,单位字节
    */
    public int              nLength;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[8];

    public NET_XRAY_UNPACKING_IMAGE_INFO() {
    }
}

