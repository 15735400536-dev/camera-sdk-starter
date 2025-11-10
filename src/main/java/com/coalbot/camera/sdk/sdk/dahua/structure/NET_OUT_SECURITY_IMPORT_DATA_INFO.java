package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SecurityImportDataEx接口出参
*/
public class NET_OUT_SECURITY_IMPORT_DATA_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_SECURITY_IMPORT_DATA_INFO() {
        this.dwSize = this.size();
    }
}

