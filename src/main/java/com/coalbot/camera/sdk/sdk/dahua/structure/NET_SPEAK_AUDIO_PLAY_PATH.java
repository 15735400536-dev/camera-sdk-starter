package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 音频文件路径
*/
public class NET_SPEAK_AUDIO_PLAY_PATH extends NetSDKLibStructure.SdkStructure
{
    /**
     * 文件路径
    */
    public byte[]           szPath = new byte[256];
    /**
     * 是否支持上传
    */
    public int              bSupportUpload;
    /**
     * 支持最大上传个数
    */
    public int              nMaxFileUploadNum;
    /**
     * 最大上传文件大小，单位：字节
    */
    public int              nMaxUploadFileSize;
    /**
     * 预留
    */
    public byte[]           byReserved = new byte[756];

    public NET_SPEAK_AUDIO_PLAY_PATH() {
    }
}

