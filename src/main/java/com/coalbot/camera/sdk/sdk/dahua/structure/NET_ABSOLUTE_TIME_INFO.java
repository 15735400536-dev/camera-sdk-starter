package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 绝对时间信息
*/
public class NET_ABSOLUTE_TIME_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 开始时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStartTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 结束时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStopTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 绝对时间使能
    */
    public int              bEnable;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[28];

    public NET_ABSOLUTE_TIME_INFO() {
    }
}

