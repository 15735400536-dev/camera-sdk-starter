package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 区域状态
*/
public class NET_PARKING_SPACE_AREA_STATUS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 区域名称
    */
    public byte[]           szName = new byte[32];
    /**
     * 该区域的总车位个数
    */
    public int              nCount;
    /**
     * 车位类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_PARKINGSPACE_TYPE}
    */
    public int              emSpaceType;
    /**
     * 该区域剩余可用的车位个数
    */
    public int              nRemainCnt;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[252];

    public NET_PARKING_SPACE_AREA_STATUS() {
    }
}

