package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_AttachXRayPackageManualExport 接口出参
*/
public class NET_OUT_ATTACH_XRAY_PACKAGE_MANUAL_EXPORT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ATTACH_XRAY_PACKAGE_MANUAL_EXPORT() {
        this.dwSize = this.size();
    }
}

