package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型 EVENT_IVS_CHANNEL_ABNORMAL（IVS设备前端通道状态异常事件）对应的数据块描述信息
*/
public class NET_DEV_EVENT_IVS_CHANNEL_ABNORMAL_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 0：脉冲，1：开始，2：停止
    */
    public int              nAction;
    /**
     * 扩展协议字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_INFO_EXTEND}
    */
    public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
    /**
     * 事件发生的时间,参见结构体定义 {@link NetSDKLib.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件编号
    */
    public int              nEventID;
    /**
     * 相对事件时间戳，单位毫秒
    */
    public double           dbPTS;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 分类
    */
    public byte[]           szClass = new byte[16];
    /**
     * 异常状态，"Normal": 正常 "Offline": 离线 "Disconnection": 断流
    */
    public byte[]           szStatus = new byte[32];
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_DEV_EVENT_IVS_CHANNEL_ABNORMAL_INFO() {
    }
}

