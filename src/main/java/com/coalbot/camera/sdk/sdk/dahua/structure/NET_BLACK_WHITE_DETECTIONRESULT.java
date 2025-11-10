package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  对应检测类型(NET_DIAGNOSIS_BLACK_WHITE) 黑白图像检测结果 
* @date 2022/08/03 14:32:07
*/
public class NET_BLACK_WHITE_DETECTIONRESULT extends NetSDKLibStructure.SdkStructure {
/** 
此结构体大小
*/
    public			int            dwSize;
/** 
检测结果量化值
*/
    public			int            nValue;
/** 
检测结果状态 {@link NetSDKLib.NET_STATE_TYPE}
*/
    public			int            emState;
/** 
状态持续时间
*/
    public			int            nDuration;
/** 
异常检测结果图片地址
*/
    public			byte[]         szPicUrl = new byte[256];

public NET_BLACK_WHITE_DETECTIONRESULT(){
		this.dwSize=this.size();
}
}

