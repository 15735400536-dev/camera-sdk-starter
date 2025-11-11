package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 事件类型 DH_ALARM_OPEN_CLOSE_DOOR_DETECTION (反复开关门事件)对应的数据块描述信息
*/
public class NET_ALARM_OPEN_CLOSE_DOOR_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
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
     * 事件编号，用来唯一标志一个事件
    */
    public int              nEventID;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 智能事件所属大类
    */
    public byte[]           szClass = new byte[16];
    /**
     * 相对事件时间戳,单位毫秒
    */
    public double           dbPTS;
    /**
     * 智能事件规则编号
    */
    public int              nRuleId;
    /**
     * GroupID事件组ID
    */
    public int              nGroupID;
    /**
     * CountInGroup一个事件组内的抓拍张数
    */
    public int              nCountInGroup;
    /**
     * IndexInGroup一个事件组内的抓拍序号
    */
    public int              nIndexInGroup;
    /**
     * 事件触发的预置点号，从1开始没有该字段，表示预置点未知
    */
    public int              nPresetID;
    /**
     * 物体信息列表个数
    */
    public int              nObjectsNum;
    /**
     * 全景广角图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
    */
    public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX();
    /**
     * 物体信息列表,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[64];
    /**
     * 规则区域,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_POINT_EX}
    */
    public NET_POINT_EX[]   stuDetectRegion = new NET_POINT_EX[20];
    /**
     * 规则区域个数
    */
    public int              nDetectRegionNum;
    /**
     * 反复开关门的次数
    */
    public int              nOpenCloseCount;
    /**
     * 预留字节
    */
    public byte[]           szReserverd = new byte[1020];

    public NET_ALARM_OPEN_CLOSE_DOOR_DETECTION_INFO() {
        for(int i = 0; i < stuObjects.length; i++){
            stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
        }
        for(int i = 0; i < stuDetectRegion.length; i++){
            stuDetectRegion[i] = new NET_POINT_EX();
        }
    }
}

