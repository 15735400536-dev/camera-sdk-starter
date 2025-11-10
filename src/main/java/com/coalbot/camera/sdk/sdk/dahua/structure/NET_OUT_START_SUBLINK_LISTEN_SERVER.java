package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/** 
CLIENT_StartSubLinkListenServer 接口输出参数
*/
public class NET_OUT_START_SUBLINK_LISTEN_SERVER extends NetSDKLibStructure.SdkStructure {
/** 
/<  结构体大小
*/
    public			int            dwSize;

public NET_OUT_START_SUBLINK_LISTEN_SERVER(){
    this.dwSize=this.size();
}
}

