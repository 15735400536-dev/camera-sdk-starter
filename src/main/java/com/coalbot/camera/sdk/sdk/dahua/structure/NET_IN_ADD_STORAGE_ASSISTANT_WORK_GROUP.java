package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AddStorageAssistantWorkGroup 接口输入参数
*/
public class NET_IN_ADD_STORAGE_ASSISTANT_WORK_GROUP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 需要添加的盘组名称
    */
    public byte[]           szWorkGroup = new byte[32];

    public NET_IN_ADD_STORAGE_ASSISTANT_WORK_GROUP() {
        this.dwSize = this.size();
    }
}

