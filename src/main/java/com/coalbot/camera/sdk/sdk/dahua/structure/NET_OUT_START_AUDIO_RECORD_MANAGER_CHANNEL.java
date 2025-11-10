package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_StartAudioRecordManagerChannel 接口输出参数
*/
public class NET_OUT_START_AUDIO_RECORD_MANAGER_CHANNEL extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_START_AUDIO_RECORD_MANAGER_CHANNEL() {
        this.dwSize = this.size();
    }
}

