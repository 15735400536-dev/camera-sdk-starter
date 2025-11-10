package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/** 
* @author 291189
* @description  X光机物品 
* @date 2022/12/01 16:27:10
*/
public class NET_XRAY_OBJECT_INFO extends NetSDKLibStructure.SdkStructure {
/** 
物体类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_INSIDE_OBJECT_TYPE}
*/
    public			int            emType;
/** 
物体检测使能
*/
    public			int            bEnable;
/** 
物体检测阈值 0~100
*/
    public			int            nDetectThreshold;
/** 
解析和下发自定义物体类型时使用
*/
    public			byte[]         szType = new byte[32];
/** 
预留
*/
    public			byte[]         szReserved = new byte[224];

public			NET_XRAY_OBJECT_INFO(){
}
}

