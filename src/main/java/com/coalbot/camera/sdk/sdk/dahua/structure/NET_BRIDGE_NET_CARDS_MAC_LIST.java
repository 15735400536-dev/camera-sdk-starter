package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 网卡内部网卡信息
*/
public class NET_BRIDGE_NET_CARDS_MAC_LIST extends NetSDKLibStructure.SdkStructure
{
    /**
     * 网卡名称
    */
    public byte[]           szNetCardName = new byte[32];
    /**
     * 网卡Mac
    */
    public byte[]           szNetCardMac = new byte[18];
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[14];

    public NET_BRIDGE_NET_CARDS_MAC_LIST() {
    }
}

