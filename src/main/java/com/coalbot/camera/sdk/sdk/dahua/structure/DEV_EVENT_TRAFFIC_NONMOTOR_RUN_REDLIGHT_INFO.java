package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * @author 251823
 * @description 事件类型 EVENT_IVS_TRAFFIC_NONMOTOR_RUN_REDLIGHT (非机动车闯红灯事件)对应的数据块描述信息
 * @date 2021/10/23
 */
public class DEV_EVENT_TRAFFIC_NONMOTOR_RUN_REDLIGHT_INFO extends NetSDKLibStructure.SdkStructure {
	/**
     *  通道号
     */
    public int              nChannelID;
    /**
     *  0:脉冲事件
     */
    public int              nAction;
    /**
     *  事件名称
     */
    public byte[]           szName = new byte[128];
    /**
     *  时间戳(单位是毫秒)
     */
    public double           PTS;
    /**
     *  事件发生的时间
     */
    public NET_TIME_EX      UTC;
    /**
     *  事件ID
     */
    public int              nEventID;
    /**
     *  视频帧序号
     */
    public int              nSequence;
    /**
     *  车道号
     */
    public int              nLane;
    /**
     *  非机动车
     */
    public NetSDKLibStructure.VA_OBJECT_NONMOTOR stuNonMotor;
    /**
     *  公共信息
     */
    public NetSDKLibStructure.EVENT_COMM_INFO stCommInfo;
    /**
     *  事件对应文件信息
     */
    public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;
    /**
     *  对应图片的分辨率
     */
    public NetSDKLibStructure.NET_RESOLUTION_INFO stuResolution;
    /**
     * 交通车辆信息,参见结构体定义 {@link NetSDKLib.DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO}
    */
    public Pointer          pstuTrafficCar;
    /**
     *  预留字段 
     */
    public byte[]           byReserved = new byte[1016-NetSDKLibStructure.POINTERSIZE];
    /**
     * 扩展协议字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_INFO_EXTEND}
    */
    public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
}

