package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 车流量统计结果信息
*/
public class NET_VEHICLE_FLOW_STAT_INFO extends NetSDKLibStructure.SdkStructure
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
     * 总车辆
    */
    public int              nTotal;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1020];

    public NET_VEHICLE_FLOW_STAT_INFO() {
    }
}

