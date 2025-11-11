package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_RemoteRemoveFiles 接口输出参数
*/
public class NET_OUT_REMOTE_REMOVE_FILES extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_REMOTE_REMOVE_FILES() {
        this.dwSize = this.size();
    }
}

