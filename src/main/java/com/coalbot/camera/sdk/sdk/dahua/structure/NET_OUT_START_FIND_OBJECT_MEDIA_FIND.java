package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_StartFindObjectMediaFind 接口输出参数
*/
public class NET_OUT_START_FIND_OBJECT_MEDIA_FIND extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_START_FIND_OBJECT_MEDIA_FIND() {
        this.dwSize = this.size();
    }
}

