package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 添加电视墙输入参数
*/
public class NET_IN_MONITORWALL_ADD extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 电视墙信息,参见结构体定义 {@link NetSDKLibStructure.NET_MONITORWALL}
    */
    public NetSDKLibStructure.NET_MONITORWALL stuMonitorWall = new NetSDKLibStructure.NET_MONITORWALL();

    public NET_IN_MONITORWALL_ADD() {
        this.dwSize = this.size();
    }
}

