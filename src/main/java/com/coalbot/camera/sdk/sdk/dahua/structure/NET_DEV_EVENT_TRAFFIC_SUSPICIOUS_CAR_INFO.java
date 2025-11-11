package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型 EVENT_IVS_TRAFFIC_SUSPICIOUS_CAR(嫌疑车辆事件)对应的数据块描述信息
*/
public class NET_DEV_EVENT_TRAFFIC_SUSPICIOUS_CAR_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 0:脉冲,1:开始, 2:停止
    */
    public int              nAction;
    /**
     * 扩展协议字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_INFO_EXTEND}
    */
    public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 对齐字节
    */
    public byte[]           szReservedUTC = new byte[4];
    /**
     * 检测到的车辆信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public NetSDKLibStructure.NET_MSG_OBJECT stuVehicle = new NetSDKLibStructure.NET_MSG_OBJECT();
    /**
     * 禁止名单信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TRAFFIC_LIST_RECORD}
    */
    public NetSDKLibStructure.NET_TRAFFIC_LIST_RECORD stuCarInfo = new NetSDKLibStructure.NET_TRAFFIC_LIST_RECORD();
    /**
     * 交通事件公共信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.EVENT_COMM_INFO}
    */
    public NetSDKLibStructure.EVENT_COMM_INFO stuCommInfo = new NetSDKLibStructure.EVENT_COMM_INFO();
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_DEV_EVENT_TRAFFIC_SUSPICIOUS_CAR_INFO() {
    }
}

