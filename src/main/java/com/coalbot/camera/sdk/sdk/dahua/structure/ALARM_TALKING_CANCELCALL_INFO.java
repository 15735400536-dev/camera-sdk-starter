package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 设备呼叫中取消呼叫事件(对应事件 DH_ALARM_TALKING_CANCELCALL)
*/
public class ALARM_TALKING_CANCELCALL_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 事件发生的时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX stuTime = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 呼叫ID
    */
    public byte[]           szCallID = new byte[32];

    public ALARM_TALKING_CANCELCALL_INFO() {
    }
}

