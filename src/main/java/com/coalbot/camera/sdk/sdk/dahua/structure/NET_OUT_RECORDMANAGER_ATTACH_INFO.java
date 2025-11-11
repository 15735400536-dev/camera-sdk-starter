package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachRecordManagerState 出参
*/
public class NET_OUT_RECORDMANAGER_ATTACH_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_RECORDMANAGER_ATTACH_INFO() {
        this.dwSize = this.size();
    }
}

