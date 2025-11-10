package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 智能物体全局唯一物体标识
*/
public class NET_MSG_OBJECT_OBJECTUUID_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 物体全局唯一性标识
    */
    public byte[]           szUUID = new byte[48];
    /**
     * 扩展字节
    */
    public byte[]           szReserved = new byte[208];

    public NET_MSG_OBJECT_OBJECTUUID_INFO() {
    }
}

