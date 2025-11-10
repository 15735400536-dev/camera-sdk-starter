package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_AttachIotboxComm 接口入参
*/
public class NET_IN_ATTACH_IOTBOX_COMM_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 用户自定义参数
    */
    public Pointer          dwUser;
    /**
     * 回调函数,参见回调函数定义 {@link NetSDKLib.fNotifyIotboxRealdataEx}
    */
    public NetSDKLib.fNotifyIotboxRealdataEx cbNotifyIotboxRealdataEx;

    public NET_IN_ATTACH_IOTBOX_COMM_EX() {
        this.dwSize = this.size();
    }
}

