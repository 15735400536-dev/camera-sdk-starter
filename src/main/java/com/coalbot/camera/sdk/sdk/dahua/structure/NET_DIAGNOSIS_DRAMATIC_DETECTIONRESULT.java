package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
* @author 291189
* @description  对应检测类型(NET_DIAGNOSIS_DRAMATIC_CHANGE) 场景剧变检测结果 
* @date 2022/08/03 14:36:15
*/
public class NET_DIAGNOSIS_DRAMATIC_DETECTIONRESULT extends NetSDKLibStructure.SdkStructure {
/** 
此结构体大小
*/
    public			int            dwSize;
/** 
检测结果量化值
*/
    public			int            nValue;
/** 
检测结果状态 {@link NetSDKLibStructure.NET_STATE_TYPE}
*/
    public			int            emState;
/** 
状态持续时间
*/
    public			int            nDuration;
/**
 * 异常检测结果图片地址
 */
    public byte[]           szPicUrl = new byte[256];

public NET_DIAGNOSIS_DRAMATIC_DETECTIONRESULT(){
		this.dwSize=this.size();
}
}

