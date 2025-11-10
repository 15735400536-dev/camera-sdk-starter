package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * OSD使能状态及文本信息
*/
public class NET_TITLE_OSD_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 叠加内容
    */
    public byte[]           szText = new byte[1024];
    /**
     * 是否使能osd显示
    */
    public int              nEnable;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1020];

    public NET_TITLE_OSD_INFO() {
    }
}

