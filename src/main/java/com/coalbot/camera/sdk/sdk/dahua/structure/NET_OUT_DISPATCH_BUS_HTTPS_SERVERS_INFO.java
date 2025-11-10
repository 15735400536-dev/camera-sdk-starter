package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 下发https服务器地址，供设备以https方式上报公交线路、报站信息 CLIENT_DispatchBusHttpsServers 接口输出参数
*/
public class NET_OUT_DISPATCH_BUS_HTTPS_SERVERS_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_DISPATCH_BUS_HTTPS_SERVERS_INFO() {
        this.dwSize = this.size();
    }
}

