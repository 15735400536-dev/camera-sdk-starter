package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 目标识别联动报警通道配置, 对应枚举 NET_EM_CFG_FACE_RECOGNITION_ALARM
*/
public class NET_FACE_RECOGNITION_ALARM_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 目标识别联动报警通道个数
    */
    public int              nFaceReconChannelNum;
    /**
     * 目标识别联动报警通道信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_FACE_RECOGNITION_ALARM_CHANNEL}
    */
    public NET_FACE_RECOGNITION_ALARM_CHANNEL[] stuFaceReconChannel = new NET_FACE_RECOGNITION_ALARM_CHANNEL[50];

    public NET_FACE_RECOGNITION_ALARM_INFO() {
        this.dwSize = this.size();
        for(int i = 0; i < stuFaceReconChannel.length; i++){
            stuFaceReconChannel[i] = new NET_FACE_RECOGNITION_ALARM_CHANNEL();
        }
    }
}

