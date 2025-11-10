package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_StopFindObjectMediaFind 接口输出参数
*/
public class NET_OUT_STOP_FIND_OBJECT_MEDIA_FIND extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_STOP_FIND_OBJECT_MEDIA_FIND() {
        this.dwSize = this.size();
    }
}

