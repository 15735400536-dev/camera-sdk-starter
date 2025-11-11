package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型 EVENT_IVS_TICKET_EVADE_DETECTION (逃票检测事件)对应的数据块描述信息
*/
public class DEV_EVENT_TICKET_EVADE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 0:脉冲 1:开始 2:停止
    */
    public int              nAction;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 时间戳(单位是毫秒)
    */
    public double           PTS;
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX UTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 智能事件所属大类,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_CLASS_TYPE}
    */
    public int              emClassType;
    /**
     * 智能事件规则编号，用于标示哪个规则触发的事件
    */
    public int              nRuleID;
    /**
     * 事件组ID，同一物体抓拍过程内GroupID相同
    */
    public int              nGroupID;
    /**
     * 一个事件组内的抓拍张数
    */
    public int              nCountInGroup;
    /**
     * 一个事件组内的抓拍序号
    */
    public int              nIndexInGroup;
    /**
     * 事件触发的预置点号，从1开始, 没有该字段，表示预置点未知
    */
    public int              nPresetID;
    /**
     * 检测到的多个物体,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[128];
    /**
     * 检测到的物体个数
    */
    public int              nObjectNum;
    /**
     * 实际检测到区域个数
    */
    public int              nDetectRegionNum;
    /**
     * 检测区域,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_POINT}
    */
    public NetSDKLibStructure.NET_POINT[] stuDetectRegion = new NetSDKLibStructure.NET_POINT[20];
    /**
     * 保留字节
    */
    public byte[]           byReversed = new byte[1024];

    public DEV_EVENT_TICKET_EVADE_DETECTION_INFO() {
        for(int i = 0; i < stuObjects.length; i++){
            stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
        }
        for(int i = 0; i < stuDetectRegion.length; i++){
            stuDetectRegion[i] = new NetSDKLibStructure.NET_POINT();
        }
    }
}

