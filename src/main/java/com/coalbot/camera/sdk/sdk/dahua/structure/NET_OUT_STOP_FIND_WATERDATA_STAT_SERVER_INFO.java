package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  CLIENT_StopFindWaterDataStatServer 输出参数 
* @date 2022/08/23 10:34:25
*/
public class NET_OUT_STOP_FIND_WATERDATA_STAT_SERVER_INFO extends NetSDKLibStructure.SdkStructure {
/** 
此结构体大小,必须赋值
*/
    public			int            dwSize;

public NET_OUT_STOP_FIND_WATERDATA_STAT_SERVER_INFO(){
		this.dwSize=this.size();
}
}

