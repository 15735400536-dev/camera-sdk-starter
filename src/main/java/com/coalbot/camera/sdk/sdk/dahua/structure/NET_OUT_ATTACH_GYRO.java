package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  CLIENT_AttachGyro接口出参 
* @date 2022/10/26 16:10:20
*/
public class NET_OUT_ATTACH_GYRO extends NetSDKLibStructure.SdkStructure {
/** 
此结构体大小,必须赋值
*/
    public			int            dwSize;

public NET_OUT_ATTACH_GYRO(){
		this.dwSize=this.size();
}
}

