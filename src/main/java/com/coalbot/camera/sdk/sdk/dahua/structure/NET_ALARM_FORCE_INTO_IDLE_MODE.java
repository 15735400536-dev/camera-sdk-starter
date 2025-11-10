package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 强制进入休眠模式通知 (对应 DH_ALARM_FORCE_INTO_IDLE_MODE)
*/
public class NET_ALARM_FORCE_INTO_IDLE_MODE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 事件动作, 0:Pulse
    */
    public int              nAction;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuUTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_ALARM_FORCE_INTO_IDLE_MODE() {
    }
}

