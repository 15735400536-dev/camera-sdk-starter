package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 向客户端发送录像文件回调信息
*/
public class NET_RECORDMANAGER_NOTIFY_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 码流类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_STREAM_TYPE}
    */
    public int              emStreamType;
    /**
     * 是否在录像
    */
    public int              bState;
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[128];

    public NET_RECORDMANAGER_NOTIFY_INFO() {
    }
}

