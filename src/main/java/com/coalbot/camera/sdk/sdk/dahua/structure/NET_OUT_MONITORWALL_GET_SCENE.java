package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_MonitorWallGetScene接口输出参数(获取电视墙场景)
*/
public class NET_OUT_MONITORWALL_GET_SCENE extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 当前预案名称, 可以为空
    */
    public byte[]           szName = new byte[128];
    /**
     * 电视墙场景,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MONITORWALL_SCENE}
    */
    public NET_MONITORWALL_SCENE stuScene = new NET_MONITORWALL_SCENE();

    public NET_OUT_MONITORWALL_GET_SCENE() {
        this.dwSize = this.size();
    }
}

