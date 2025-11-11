package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachIotboxComm 接口出参
*/
public class NET_OUT_ATTACH_IOTBOX_COMM_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ATTACH_IOTBOX_COMM_EX() {
        this.dwSize = this.size();
    }
}

