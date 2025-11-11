package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 蓝牙相关能力
*/
public class NET_ACCESS_BT_CARD_CAPS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 每次最大插入量
    */
    public int              nMaxInsertRate;
    /**
     * 最大存储的卡数量
    */
    public int              nMaxCards;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[512];

    public NET_ACCESS_BT_CARD_CAPS() {
    }
}

