package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 服务器地址参数
*/
public class NET_BUS_HTTPS_ADDRESS_INFO extends NetSDKLibStructure.SdkStructure
{
    public byte[]           szIPAddress = new byte[64];
    public int              nPort;
    public byte[]           byReserved = new byte[956];

    public NET_BUS_HTTPS_ADDRESS_INFO() {
    }
}

