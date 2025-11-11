package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * 通知导出包裹信息
*/
public class NET_NOTIFY_PACKAGE_MANUAL_EXPORT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 包裹列表,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_XRAY_PKG_INFO}
    */
    public Pointer          pstuList;
    /**
     * 包裹列表个数
    */
    public int              nListNum;
    /**
     * 预留字段
    */
    public byte[]           szResvered = new byte[1020];

    public NET_NOTIFY_PACKAGE_MANUAL_EXPORT() {
    }
}

