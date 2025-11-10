package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * NET_VOICE_BROADCAST_INFO 语音播放内容信息
*/
public class NET_VOICE_BROADCAST_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 播放内容,Type是"Text"时表示播放内容,Type是"File"时表示播放的文件名
    */
    public byte[]           szContent = new byte[128];
    /**
     * 播放的内容类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_VOICE_BROADCAST_TYPE}
    */
    public int              emType;
    /**
     * 保留字节
    */
    public byte[]           szResvered = new byte[204];

    public NET_VOICE_BROADCAST_INFO() {
    }
}

