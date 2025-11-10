package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 标定线段相关能力
*/
public class CFG_VEHICLES_CALIBRATION extends NetSDKLibStructure.SdkStructure
{
    /**
     * 水平线段数量
    */
    public int              nHorizontalLines;
    /**
     * 垂直线段数量
    */
    public int              nVerticalLines;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public CFG_VEHICLES_CALIBRATION() {
    }
}

