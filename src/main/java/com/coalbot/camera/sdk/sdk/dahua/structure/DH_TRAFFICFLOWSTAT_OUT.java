package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/** 
* @author 291189
* @description  接口(CLIENT_DoFindFluxStat)输入参数 
* @date 2022/05/07 10:04:12
*/
public class DH_TRAFFICFLOWSTAT_OUT extends NetSDKLibStructure.SdkStructure {
/** 
此结构体大小
*/
    public			int            dwSize;
/** 
统计信息个数
*/
    public			int            nStatInfo;
/** 
统计信息指针,由用户申请内存，大小为sizeof(DH_TRAFFICFLOWSTAT)*nStatInfo
*/
    public Pointer          pStatInfo;

public DH_TRAFFICFLOWSTAT_OUT(){
		this.dwSize=this.size();
}
}

