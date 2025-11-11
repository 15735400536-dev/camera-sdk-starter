package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  CLIENT_GetDynamicLockRandomPublicKey 接口出参 
* @date 2022/06/23 10:09:19
*/
public class NET_OUT_GET_DYNAMIC_LOCK_RANDOM_PUBLICKEY_INFO extends NetSDKLibStructure.SdkStructure {
/** 
结构体大小
*/
    public			int            dwSize;
/** 
动态密码锁错误码 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_DYNAMIC_LOCK_ERRORCODE}
*/
    public			int            emErrorCode;
/** 
锁随机公钥
*/
    public			byte[]         szRandomPublicKey = new byte[512];

public NET_OUT_GET_DYNAMIC_LOCK_RANDOM_PUBLICKEY_INFO(){
		this.dwSize=this.size();
}
}

