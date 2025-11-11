package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型EVENT_IVS_GENERAL_ATTITUDE_DETECTION (姿态检测事件)对应的数据块描述信息
*/
public class DEV_EVENT_GENERAL_ATTITUDE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
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
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 时间戳(单位是毫秒)
    */
    public double           dbPTS;
    /**
     * 事件发生的时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 检测到的物体,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_GENERAL_ATTITUDE_DETECTION_OBJECT}
    */
    public NET_GENERAL_ATTITUDE_DETECTION_OBJECT stuObject = new NET_GENERAL_ATTITUDE_DETECTION_OBJECT();
    /**
     * 全景广角图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_SCENE_IMAGE_INFO}
    */
    public NET_SCENE_IMAGE_INFO stuSceneImage = new NET_SCENE_IMAGE_INFO();
    /**
     * 保留字节,留待扩展
    */
    public byte[]           szReserved = new byte[1024];

    public DEV_EVENT_GENERAL_ATTITUDE_DETECTION_INFO() {
    }
}

