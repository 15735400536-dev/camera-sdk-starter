package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 报警输入附带信息
*/
public class NET_ALARMIN_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 报警输入通道号
    */
    public int              nChannel;
    /**
     * 预留
    */
    public byte[]           szReserved = new byte[128];

    public NET_ALARMIN_INFO() {
    }
}

