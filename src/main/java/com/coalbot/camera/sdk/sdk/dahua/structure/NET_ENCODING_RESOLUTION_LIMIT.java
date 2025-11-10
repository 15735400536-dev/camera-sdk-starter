package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 码流分辨率限制
*/
public class NET_ENCODING_RESOLUTION_LIMIT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 分辨率1,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_CAPTURE_SIZE_EX}
    */
    public int              emResolution1;
    /**
     * 分辨率2,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_CAPTURE_SIZE_EX}
    */
    public int              emResolution2;
    /**
     * 保留
    */
    public byte[]           szReserved = new byte[1024];

    public NET_ENCODING_RESOLUTION_LIMIT() {
    }
}

