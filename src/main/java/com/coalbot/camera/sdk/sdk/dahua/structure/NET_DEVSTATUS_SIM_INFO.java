package com.coalbot.camera.sdk.sdk.dahua.structure;




/** 
* @author 291189
* @description   sim卡状态信息 
* @date 2022/08/31 14:35:19
*/
public class NET_DEVSTATUS_SIM_INFO extends NetSDKLibStructure.SdkStructure {
/** 
SIM卡状态 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_SIM_STATE}
*/
    public			int            emStatus;
/** 
SIM卡编号
*/
    public			byte           byIndex;
/** 
预留字段
*/
    public			byte[]         byReserved = new byte[31];

public			NET_DEVSTATUS_SIM_INFO(){
}
}

