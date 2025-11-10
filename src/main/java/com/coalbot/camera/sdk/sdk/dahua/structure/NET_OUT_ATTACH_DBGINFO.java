package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 订阅日志回调出参
*/
public class NET_OUT_ATTACH_DBGINFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_ATTACH_DBGINFO() {
        this.dwSize = this.size();
    }
}

