package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_AttachDockStatus 接口输入参数
*/
public class NET_IN_ATTACH_DOCK_STATUS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[4];
    /**
     * 任务状态回调函数,参见回调函数定义 {@link NetSDKLib.fUAVDockStatus}
    */
    public NetSDKLib.fUAVDockStatus cbNotify;
    /**
     * 用户信息
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_DOCK_STATUS() {
        this.dwSize = this.size();
    }
}

