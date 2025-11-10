package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  用户数据 
* @date 2023/02/06 14:57:49
*/
public class NET_OPEN_INTELLI_USER_DATA_INFO extends NetSDKLibStructure.SdkStructure {
/** 
自定义报警id
*/
    public			int            nAlarmId;
/** 
保留字节
*/
    public			byte[]         szReserved = new byte[512];

public NET_OPEN_INTELLI_USER_DATA_INFO(){
}
}

