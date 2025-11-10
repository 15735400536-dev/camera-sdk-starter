package com.coalbot.camera.sdk.sdk.dahua.structure;




/** 
* @author 291189
* @description  card操作能力集 
* @origin autoTool
* @date 2023/02/10 14:34:21
*/
public class NET_ACCESS_CARD_CAPS extends NetSDKLibStructure.SdkStructure {
/** 
每次下发的最大数量
*/
    public			int            nMaxInsertRate;
/** 
卡片数量上限
*/
    public			int            nMaxCards;
/** 
保留字节
*/
    public			byte[]         byReserved = new byte[512];

public			NET_ACCESS_CARD_CAPS(){
}
}

