package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 工作组包含的工作目录名称
*/
public class NET_DIRECTORIES_LIST_NAME extends NetSDKLibStructure.SdkStructure
{
    /**
     * 名称
    */
    public byte[]           szName = new byte[256];

    public NET_DIRECTORIES_LIST_NAME() {
    }
}

