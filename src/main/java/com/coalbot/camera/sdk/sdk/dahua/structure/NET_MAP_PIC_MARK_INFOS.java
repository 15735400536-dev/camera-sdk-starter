package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 地图已标记信息
*/
public class NET_MAP_PIC_MARK_INFOS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 地图位置点信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_POINT_EX}
    */
    public NET_POINT_EX     stuPosition = new NET_POINT_EX();
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[128];

    public NET_MAP_PIC_MARK_INFOS() {
    }
}

