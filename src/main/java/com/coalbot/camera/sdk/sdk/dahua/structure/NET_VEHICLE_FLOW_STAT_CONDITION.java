package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 查询条件
*/
public class NET_VEHICLE_FLOW_STAT_CONDITION extends NetSDKLibStructure.SdkStructure
{
    /**
     * 开始时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStartTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 结束时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuEndTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 查询要求返回的报表统计信息粒度, "Hour"按小时, "Day"按天, "Month"按月, "Year"按年
    */
    public byte[]           szGranularity = new byte[8];
    /**
     * 选择查询的通道
    */
    public int[]            nChannels = new int[1024];
    /**
     * 查询通道有效个数
    */
    public int              nChannelsNum;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1020];

    public NET_VEHICLE_FLOW_STAT_CONDITION() {
    }
}

