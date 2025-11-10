package com.coalbot.camera.sdk.sdk.dahua.structure;




/** 
* @author 291189
* @description  标准黑体源异常报警配置 
* @date 2022/07/23 10:52:36
*/
public class CFG_REGULATOR_DETECT_INFO extends NetSDKLibStructure.SdkStructure {
/** 
使能开关
*/
    public			int            bEnable;
/** 
灵敏度, 1-100
*/
    public			int            nSensitivity;
/** 
报警联动
*/
    public NetSDKLib.CFG_ALARM_MSG_HANDLE stuEventHandler = new NetSDKLib.CFG_ALARM_MSG_HANDLE();

public CFG_REGULATOR_DETECT_INFO(){
}
}

