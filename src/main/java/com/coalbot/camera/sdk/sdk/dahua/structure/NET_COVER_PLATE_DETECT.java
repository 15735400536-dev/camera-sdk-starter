package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  盖板检测结果 
* @date 2022/06/28 19:44:56
*/
public class NET_COVER_PLATE_DETECT extends NetSDKLibStructure.SdkStructure {
/** 
盖板状态 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_COVER_PLATE_STATE}
*/
    public			int            emCoverPlateState;
/** 
包围盒
*/
    public NET_RECT         stuBoundingBox = new NET_RECT();

public			NET_COVER_PLATE_DETECT(){
}
}

