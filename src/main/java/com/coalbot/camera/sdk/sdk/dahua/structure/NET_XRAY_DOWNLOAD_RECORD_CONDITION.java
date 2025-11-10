package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 查询条件
*/
public class NET_XRAY_DOWNLOAD_RECORD_CONDITION extends NetSDKLibStructure.SdkStructure
{
    /**
     * 开始时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStartTime = new NET_TIME();
    /**
     * 结束时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NET_TIME stuEndTime = new NET_TIME();
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1020];

    public NET_XRAY_DOWNLOAD_RECORD_CONDITION() {
    }
}

