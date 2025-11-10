package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_DockEventsReply 接口输出参数
*/
public class NET_OUT_DOCK_EVENTS_REPLY extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_DOCK_EVENTS_REPLY() {
        this.dwSize = this.size();
    }
}

