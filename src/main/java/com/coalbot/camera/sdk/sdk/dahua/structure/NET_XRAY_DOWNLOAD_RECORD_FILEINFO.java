package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 文件信息
*/
public class NET_XRAY_DOWNLOAD_RECORD_FILEINFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 路径
    */
    public byte[]           szPath = new byte[128];
    /**
     * 大小，单位字节
    */
    public long             nSize;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_XRAY_DOWNLOAD_RECORD_FILEINFO() {
    }
}

