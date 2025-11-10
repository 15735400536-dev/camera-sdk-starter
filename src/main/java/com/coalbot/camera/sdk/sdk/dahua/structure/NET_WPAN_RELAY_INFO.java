package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/** 
* @author 291189
* @description  中继状态 
* @date 2022/08/31 14:44:16
*/
public class NET_WPAN_RELAY_INFO extends NetSDKLibStructure.SdkStructure {
/** 
是否启用中继转发 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_RELAY_TYPE}
*/
    public			int            emEnable;
/** 
使用中继地址
*/
    public			int            nRelayIndex;
/** 
预留字段
*/
    public			byte[]         byReserved = new byte[32];

public			NET_WPAN_RELAY_INFO(){
}
}

