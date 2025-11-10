package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/** 
* @author 291189
* @description  读卡器配置 
* @date 2022/08/31 14:44:17
*/
public class NET_WPAN_CARD_READER_INFO extends NetSDKLibStructure.SdkStructure {
/** 
读卡器使能
*/
    public			int            bEnable;
/** 
发卡时是否加密卡片，1：加密(软加密)；2：不加密
*/
    public			byte           byEncryption;
/** 
预留字段
*/
    public			byte[]         byReserved = new byte[31];

public			NET_WPAN_CARD_READER_INFO(){
}
}

