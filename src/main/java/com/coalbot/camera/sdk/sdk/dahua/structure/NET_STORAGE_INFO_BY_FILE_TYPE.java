package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 工作目录信息
*/
public class NET_STORAGE_INFO_BY_FILE_TYPE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 图片区是否为活动存储块
    */
    public int              bImageIsCurrent;
    /**
     * 图片区该存储块是否损坏
    */
    public int              bImageIsError;
    /**
     * 录像区是否为活动存储块
    */
    public int              bVideoIsCurrent;
    /**
     * 录像区该存储块是否损坏
    */
    public int              bVideoIsError;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_STORAGE_INFO_BY_FILE_TYPE() {
    }
}

