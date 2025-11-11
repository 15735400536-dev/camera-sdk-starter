package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件类型EVENT_IVS_WRITE_ON_THE_BOARD_DETECTION(板书检测事件)对应的数据块描述信息
*/
public class DEV_EVENT_WRITE_ON_THE_BOARD_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 1:开始 2:停止
    */
    public int              nAction;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 智能事件所属大类,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_CLASS_TYPE}
    */
    public int              emClassType;
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX UTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 时间戳(单位是毫秒)
    */
    public double           PTS;
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 场景预置点号
    */
    public int              nPresetID;
    /**
     * 预留字段
    */
    public byte[]           byReserved = new byte[1024];

    public DEV_EVENT_WRITE_ON_THE_BOARD_DETECTION_INFO() {
    }
}

