package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
/**
 * CLIENT_AttachIotboxComm 接口入参
*/
public class NET_IN_ATTACH_IOTBOX_COMM extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 回调函数,参见回调函数定义 {@link NetSDKLib.fNotifyIotboxRealdata}
    */
    public NetSDKLib.fNotifyIotboxRealdata cbNotifyIotboxRealdata;
    /**
     * 用户自定义参数
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_IOTBOX_COMM() {
        this.dwSize = this.size();
    }
}

