package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 低电压附带信息
*/
public class NET_LOWERPOWER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 当前电量
    */
    public int              nPercent;
    /**
     * 预留
    */
    public byte[]           szReserved = new byte[128];

    public NET_LOWERPOWER_INFO() {
    }
}

