package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 音频格式
*/
public class NET_SPEAK_AUDIO_FORMAT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 音频编码格式,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SPEAK_AUDIO_TYPE}
    */
    public int              emFormat;
    /**
     * 预留
    */
    public byte[]           byReserved = new byte[1020];

    public NET_SPEAK_AUDIO_FORMAT() {
    }
}

