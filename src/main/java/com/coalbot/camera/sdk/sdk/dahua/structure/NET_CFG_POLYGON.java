package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 区域顶点信息
*/
public class NET_CFG_POLYGON extends NetSDKLibStructure.SdkStructure
{
    /**
     * X坐标
    */
    public int              nX;
    /**
     * Y坐标
    */
    public int              nY;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[32];

    public NET_CFG_POLYGON() {
    }
}

