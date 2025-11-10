package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 码流分辨率限制扩展
*/
public class NET_ENCODING_RESOLUTION_LIMIT_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 分辨率个数
    */
    public int              nResolutionNum;
    /**
     * 分辨率,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_CAPTURE_SIZE_EX1}
    */
    public int[]            emResolution = new int[10];
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_ENCODING_RESOLUTION_LIMIT_EX() {
    }
}

