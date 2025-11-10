package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 重复分析信息
*/
public class NET_LOCAL_ANALYSE_TASK_REPEAT_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 目标类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_ANALYSE_OBJECT_TYPE}
    */
    public int              emObjectType;
    /**
     * 重复开始时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NET_TIME stuStartTime = new NET_TIME();
    /**
     * 重复结束时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NET_TIME stuEndTime = new NET_TIME();
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[512];

    public NET_LOCAL_ANALYSE_TASK_REPEAT_INFO() {
    }
}

