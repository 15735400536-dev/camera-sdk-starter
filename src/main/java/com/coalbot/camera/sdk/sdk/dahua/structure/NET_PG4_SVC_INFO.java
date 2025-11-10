package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 对应CLIENT_StartSearchDevicesEx接口TTLV回调
*/
public class NET_PG4_SVC_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              nEz;
    public int              nEzs;
    public int              nPg3s;
    /**
     * 扩展字段
    */
    public byte[]           szReserved = new byte[2044];

    public NET_PG4_SVC_INFO() {
    }
}

