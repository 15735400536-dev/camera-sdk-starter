package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 火警事件 (DH_ALARM_FIRE_DETECTION)
 * @date 2020/12/21
 */



public class ALARM_FIRE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 通道号
	 * */
    public int              nChannelID;
	/**
	 * 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束
	 * */
    public int              nAction;
	/**
	 * 事件发生的时间
	 * */
    public NetSDKLibStructure.NET_TIME_EX UTC;
    /**
     * 事件公共扩展字段结构体
     */
    public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
	/**
	 * 保留字节 
	 * */
    public byte[]           byReserved = new byte[1024];
}

