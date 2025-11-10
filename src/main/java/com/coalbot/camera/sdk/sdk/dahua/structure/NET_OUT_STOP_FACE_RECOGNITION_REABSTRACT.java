package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_StopFaceRecognitionReAbstract 接口输出参数
*/
public class NET_OUT_STOP_FACE_RECOGNITION_REABSTRACT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_STOP_FACE_RECOGNITION_REABSTRACT() {
        this.dwSize = this.size();
    }
}

