package com.coalbot.camera.sdk.sdk.dahua.structure;




/** 
设备网络信息
*/
public class NET_DEV_NETWORK_INFO extends NetSDKLibStructure.SdkStructure {
/** 
/< 设备IP
*/
    public			byte[]         szDevIP = new byte[64];
/** 
/< 设备端口号
*/
    public			int            nDevPort;
/** 
/< 保留字节
*/
    public			byte[]         szReserved = new byte[516];
}

