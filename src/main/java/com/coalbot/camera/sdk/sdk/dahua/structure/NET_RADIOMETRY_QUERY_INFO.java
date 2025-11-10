package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 测温查询结果信息
*/
public class NET_RADIOMETRY_QUERY_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 记录时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuTime = new NET_TIME();
    /**
     * 预置点编号
    */
    public int              nPresetId;
    /**
     * 规则编号
    */
    public int              nRuleId;
    /**
     * 查询类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_RADIOMETRY_QUERY_TYPE}
    */
    public int              emQueryType;
    /**
     * 查询项名称
    */
    public byte[]           szName = new byte[64];
    /**
     * 查询测温点坐标,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_POINT}
    */
    public NetSDKLibStructure.NET_POINT[] stuCoordinate = new NetSDKLibStructure.NET_POINT[8];
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 温度单位,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TEMPERATURE_UNIT}
    */
    public int              emTemperatureUnit;
    /**
     * 温度信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_QUERY_TEMPER_INFO}
    */
    public NET_QUERY_TEMPER_INFO stuQueryTemperInfo = new NET_QUERY_TEMPER_INFO();
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[256];

    public NET_RADIOMETRY_QUERY_INFO() {
        for(int i = 0; i < stuCoordinate.length; i++){
            stuCoordinate[i] = new NetSDKLibStructure.NET_POINT();
        }
    }
}

