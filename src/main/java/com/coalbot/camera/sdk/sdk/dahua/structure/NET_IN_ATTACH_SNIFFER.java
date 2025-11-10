package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_AttachSniffer 接口入参
*/
public class NET_IN_ATTACH_SNIFFER extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 回调函数,参见回调函数定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.fAttachSniffer}
    */
    public NetSDKLib.fAttachSniffer cbSniffer;
    /**
     * 用户信息
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_SNIFFER() {
        this.dwSize = this.size();
    }
}

