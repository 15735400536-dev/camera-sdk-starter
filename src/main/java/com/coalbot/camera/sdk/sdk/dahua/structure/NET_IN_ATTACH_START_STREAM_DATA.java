package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_AttachStartStreamData 接口输入参数
*/
public class NET_IN_ATTACH_START_STREAM_DATA extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 回调函数,参见回调函数定义 {@link NetSDKLib.fStartStreamDataCallBack}
    */
    public NetSDKLib.fStartStreamDataCallBack cbStartStreamData;
    /**
     * 用户信息
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_START_STREAM_DATA() {
        this.dwSize = this.size();
    }
}

