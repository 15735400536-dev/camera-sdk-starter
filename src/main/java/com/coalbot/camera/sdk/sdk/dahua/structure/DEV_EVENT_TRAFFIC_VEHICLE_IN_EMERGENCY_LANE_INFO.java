package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * @author 260611
 * @description 事件类型 EVENT_IVS_TRAFFIC_VEHICLE_IN_EMERGENCY_LANE (占用应急车道事件)对应的数据块描述信息
 * @date 2022/11/29 15:05:17
 */
public class DEV_EVENT_TRAFFIC_VEHICLE_IN_EMERGENCY_LANE_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 通道号
     */
    public int              nChannelID;
    /**
     * 0:脉冲事件
     */
    public int              nAction;
    /**
     * 事件名称
     */
    public byte[]           szName = new byte[128];
    /**
     * 时间戳(单位是毫秒)
     */
    public int              PTS;
    /**
     * 事件发生的时间
     */
    public NET_TIME_EX      UTC = new NET_TIME_EX();
    /**
     * 事件ID
     */
    public int              nEventID;
    /**
     * 对应车道号
     */
    public int              nLane;
    /**
     * 检测到的物体
     */
    public NetSDKLib.DH_MSG_OBJECT stuObject = new NetSDKLib.DH_MSG_OBJECT();
    /**
     * 字节对齐
     */
    public byte[]           byReserved1 = new byte[4];
    /**
     * 车身信息
     */
    public NetSDKLib.DH_MSG_OBJECT stuVehicle = new NetSDKLib.DH_MSG_OBJECT();
    /**
     * 交通车辆信息
     */
    public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar = new DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO();
    /**
     * 字节对齐
     */
    public byte[]           byReserved2 = new byte[4];
    /**
     * 非机动车对象
     */
    public NetSDKLibStructure.VA_OBJECT_NONMOTOR stuNonMotor = new NetSDKLibStructure.VA_OBJECT_NONMOTOR();
    /**
     * 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
     */
    public int              nSequence;
    /**
     * 事件对应文件信息
     */
    public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo = new NetSDKLibStructure.NET_EVENT_FILE_INFO();
    /**
     * 公共信息
     */
    public NetSDKLibStructure.EVENT_COMM_INFO stuCommInfo = new NetSDKLibStructure.EVENT_COMM_INFO();
    /**
     * 图片信息数组,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IMAGE_INFO_EX3}
    */
    public Pointer          pstuImageInfo;
    /**
     * 图片信息个数
    */
    public int              nImageInfoNum;
    /**
     * 全景图,参见结构体定义 {@link NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
    */
    public Pointer          pstuSceneImage;
    /**
     * 预留字段
     */
    public byte[]           byReserved = new byte[1020-2*NetSDKLibStructure.POINTERSIZE];

    public DEV_EVENT_TRAFFIC_VEHICLE_IN_EMERGENCY_LANE_INFO() {
    }
}

