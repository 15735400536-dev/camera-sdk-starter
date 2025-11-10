package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 上报的云附带信息
*/
public class NET_CLOUD_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 报警时，云存储使用的视频联动通道号，可视对讲默认使用0通道
    */
    public int              nVideoLinkChannel;
    /**
     * 预留
    */
    public byte[]           szReserved = new byte[60];

    public NET_CLOUD_INFO() {
    }
}

