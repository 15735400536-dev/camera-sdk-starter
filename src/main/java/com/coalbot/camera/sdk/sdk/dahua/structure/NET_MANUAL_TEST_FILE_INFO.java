package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * NET_MANUAL_TEST_FILE_INFO 文件信息
*/
public class NET_MANUAL_TEST_FILE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 图片数量
    */
    public int              nPictureCount;
    /**
     * 视频数量
    */
    public int              nVideoCount;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[32];

    public NET_MANUAL_TEST_FILE_INFO() {
    }
}

