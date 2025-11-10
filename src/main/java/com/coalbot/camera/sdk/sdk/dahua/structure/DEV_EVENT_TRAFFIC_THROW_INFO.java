package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * @author ： 260611
 * @description ： 事件类型 EVENT_IVS_TRAFFIC_THROW(交通抛洒物品事件)对应数据块描述信息
 * @since ： Created in 2022/01/18 14:34
 */

public class DEV_EVENT_TRAFFIC_THROW_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 通道号
     */
    public int              nChannelID;
    /**
     * 事件名称
     */
    public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN];
    /**
     * 字节对齐
     */
    public byte[]           bReserved1 = new byte[8];
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
     * 事件对应文件信息
     */
    public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo = new NetSDKLibStructure.NET_EVENT_FILE_INFO();
    /**
     * 对应图片的分辨率
     */
    public NetSDKLibStructure.NET_RESOLUTION_INFO stuResolution = new NetSDKLibStructure.NET_RESOLUTION_INFO();
    /**
     * 抓图标志(按位),0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"
     */
    public int              dwSnapFlagMask;
    /**
     * 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
     */
    public byte             bEventAction;
    /**
     *
     */
    public byte[]           bReserved2 = new byte[2];
    /**
     * 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
     */
    public byte             byImageIndex;
    /**
     * 对应车道号
     */
    public int              nLane;
    /**
     * 检测到的物体
     */
    public NetSDKLibStructure.NET_MSG_OBJECT stuObject = new NetSDKLibStructure.NET_MSG_OBJECT();
    /**
     * 智能事件公共信息
     */
    public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo = new NetSDKLibStructure.EVENT_INTELLI_COMM_INFO();
    /**
     * 交通车辆部分信息
     */
    public NetSDKLibStructure.EVENT_TRAFFIC_CAR_PART_INFO stuTrafficCarPartInfo = new NetSDKLibStructure.EVENT_TRAFFIC_CAR_PART_INFO();
    /**
     * GPS信息 
     */
    public NetSDKLibStructure.NET_GPS_INFO stuGPSInfo = new NetSDKLibStructure.NET_GPS_INFO();
    /**
     * 全景广角图,参见结构体定义 {@link NetSDKLibStructure.SCENE_IMAGE_INFO}
    */
    public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO();
    /**
     * 图片信息数组,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IMAGE_INFO_EX3}
    */
    public Pointer          pstuImageInfo;
    /**
     * 图片信息个数
    */
    public int              nImageInfoNum;
    /**
     * 保留字节
     */
    public byte[]           bReserved = new byte[264-NetSDKLibStructure.POINTERSIZE];
    /**
     * 公共信息
     */
    public NetSDKLibStructure.EVENT_COMM_INFO stCommInfo = new NetSDKLibStructure.EVENT_COMM_INFO();
}

