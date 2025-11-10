package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 人员重建信息
*/
public class NET_REABSTRACT_PERSON_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 人员唯一标识符
    */
    public byte[]           szUID = new byte[32];
    public byte[]           bReserved = new byte[1024];

    public NET_REABSTRACT_PERSON_INFO() {
    }
}

