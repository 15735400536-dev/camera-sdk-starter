package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 就座率检测事件
*/
public class NET_VIDEOSTAT_SEATING_RATE_DETECTION extends NetSDKLibStructure.SdkStructure
{
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
     * 事件发生时间，带时区偏差的UTC时间，单位秒,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件编号，用来唯一标志一个事件
    */
    public int              nEventID;
    /**
     * 事件触发的预置点号，从1开始没有该字段，表示预置点未知
    */
    public int              nPresetID;
    /**
     * 总座位数
    */
    public int              nSeatCount;
    /**
     * 检测到的总人数
    */
    public int              nHumanCount;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1020];

    public NET_VIDEOSTAT_SEATING_RATE_DETECTION() {
    }
}

