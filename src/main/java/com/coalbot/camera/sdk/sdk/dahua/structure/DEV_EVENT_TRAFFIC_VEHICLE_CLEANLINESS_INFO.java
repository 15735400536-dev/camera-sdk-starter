package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author ： 260611
 * @description ： 事件类型 EVENT_IVS_TRAFFIC_VEHICLE_CLEANLINESS (交通车辆清洁度检测事件检测)对应的数据块描述信息
 * @since ： Created in 2022/01/18 14:34
 */

public class DEV_EVENT_TRAFFIC_VEHICLE_CLEANLINESS_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 通道号
     */
    public int              nChannelID;
    /**
     * 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
     */
    public int              nAction;
    /**
     * 事件名称
     */
    public byte[]           szName = new byte[128];
    /**
     * 时间戳(单位:毫秒)
     */
    public double           PTS;
    /**
     * 事件发生的时间
     */
    public NET_TIME_EX      UTC = new NET_TIME_EX();
    /**
     * 事件ID
     */
    public int              nEventID;
    /**
     * 智能事件规则编号
     */
    public int              nRuleId;
    /**
     * 事件对应文件信息
     */
    public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo = new NetSDKLibStructure.NET_EVENT_FILE_INFO();
    /**
     * 检测到的车牌信息
     */
    public NetSDKLibStructure.NET_MSG_OBJECT stuObject = new NetSDKLibStructure.NET_MSG_OBJECT();
    /**
     * 检测到的车辆信息
     */
    public NetSDKLibStructure.NET_MSG_OBJECT stuVehicle = new NetSDKLibStructure.NET_MSG_OBJECT();
    /**
     * 触发类型
     */
    public int              emTriggerType;
    /**
     * 公共信息
     */
    public NetSDKLibStructure.EVENT_COMM_INFO stuCommInfo = new NetSDKLibStructure.EVENT_COMM_INFO();
    /**
     * 清洁度阀值
     */
    public int              nCleanValue;
    /**
     * 保留字节
     */
    public byte[]           bReserved = new byte[1024];
}

