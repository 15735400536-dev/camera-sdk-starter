package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * 事件类型 DH_ALARM_DOOR_STATE_DETECTION (开关门检测事件)对应的数据块描述信息
*/
public class NET_ALARM_DOOR_STATE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 事件类型 0:脉冲,1:开始, 2:停止
    */
    public int              nAction;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 智能事件所属大类
    */
    public byte[]           szClass = new byte[16];
    /**
     * 智能事件规则编号
    */
    public int              nRuleID;
    /**
     * GroupID事件组ID，同一物体抓拍过程内GroupID相同
    */
    public int              nGroupID;
    /**
     * CountInGroup一个事件组内的抓拍张数
    */
    public int              nCountInGroup;
    /**
     * IndexInGroup一个事件组内的抓拍序号，从1开始
    */
    public int              nIndexInGroup;
    /**
     * 相对事件时间戳,(单位是毫秒)
    */
    public double           dbPTS;
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 事件触发的预置点号，从1开始没有该字段，表示预置点未知
    */
    public int              nPresetID;
    /**
     * 检测区个数
    */
    public int              nDetectRegionNum;
    /**
     * 检测区,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_POINT}
    */
    public NetSDKLibStructure.NET_POINT[] stuDetectRegion = new NetSDKLibStructure.NET_POINT[20];
    /**
     * 全景广角图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO}
    */
    public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO();
    /**
     * 报警类型 门状态异常或开门异常
    */
    public byte[]           szAlarmType = new byte[32];
    /**
     * 门状态异常报警,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_DOOR_STATE_ABNORMAL_INFO}
    */
    public NET_DOOR_STATE_ABNORMAL_INFO stuDoorStateAbnormal = new NET_DOOR_STATE_ABNORMAL_INFO();
    /**
     * 开门状态异常报警,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OPEN_DOOR_ABNORMAL_INFO}
    */
    public NET_OPEN_DOOR_ABNORMAL_INFO stuOpenDoorAbnormal = new NET_OPEN_DOOR_ABNORMAL_INFO();
    /**
     * 检测目标的物体信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public Pointer          pstuObjects;
    /**
     * 检测目标的物体信息个数
    */
    public int              nObjectsNum;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1020-NetSDKLibStructure.POINTERSIZE];

    public NET_ALARM_DOOR_STATE_DETECTION_INFO() {
        for(int i = 0; i < stuDetectRegion.length; i++){
            stuDetectRegion[i] = new NetSDKLibStructure.NET_POINT();
        }
    }
}

