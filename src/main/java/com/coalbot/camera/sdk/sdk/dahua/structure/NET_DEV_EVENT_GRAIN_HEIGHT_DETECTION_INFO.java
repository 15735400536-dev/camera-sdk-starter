package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型 EVENT_IVS_GRAIN_HEIGHT_DETECTION(动粮检测事件)对应的数据块描述信息
*/
public class NET_DEV_EVENT_GRAIN_HEIGHT_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
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
     * 事件发生的时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
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
     * 全景广角图,协议中仅IndexInData有效,参见结构体定义 {@link NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
    */
    public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX();
    /**
     * 物体信息列表,参见结构体定义 {@link NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[32];
    /**
     * 物体信息列表实际数量
    */
    public int              nObjectsCount;
    /**
     * 当前报警对应数值，如高度
    */
    public float            fValue;
    /**
     * 上报事件类型，1：定时上报，2：数值告警上报
    */
    public int              nEventType;
    /**
     * 预留字节
    */
    public byte[]           szReserverd = new byte[1020];

    public NET_DEV_EVENT_GRAIN_HEIGHT_DETECTION_INFO() {
        for(int i = 0; i < stuObjects.length; i++){
            stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
        }
    }
}

