package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_ASGManagerClearStatistics 出参
*/
public class NET_OUT_ASGMANAGER_CLEAR_STATISTICS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ASGMANAGER_CLEAR_STATISTICS() {
        this.dwSize = this.size();
    }
}

