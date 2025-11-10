package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 区域异常防区信息
*/
public class NET_AREA_STATUS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 区域号
    */
    public int              nArea;
    /**
     * 防区个数
    */
    public int              nZoneRet;
    /**
     * 防区异常状态,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_ZONE_STATUS}
    */
    public NET_ZONE_STATUS[] stuZoneStatus = new NET_ZONE_STATUS[72];
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[1024];

    public NET_AREA_STATUS() {
        for(int i = 0; i < stuZoneStatus.length; i++){
            stuZoneStatus[i] = new NET_ZONE_STATUS();
        }
    }
}

