package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型 DH_ALARM_ASGCLIMBOVER (闸机翻越报警事件) 对应的数据块描述信息
*/
public class ALARM_ASGCLIMBOVER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 0:脉冲 1:开始 2:停止
    */
    public int              nAction;
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 事件发生的时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX UTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件公共扩展字段结构体,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_INFO_EXTEND}
    */
    public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
    /**
     * 预留字节
    */
    public byte[]           byReserved = new byte[1020];

    public ALARM_ASGCLIMBOVER_INFO() {
    }
}

