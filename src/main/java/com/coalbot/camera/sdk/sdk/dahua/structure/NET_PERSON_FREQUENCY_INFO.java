package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 291189
 * @version 1.0
 * @description  频次报警信息
 * @date 2021/7/21 11:18
 */
public class NET_PERSON_FREQUENCY_INFO extends NetSDKLibStructure.SdkStructure {
     /**
      * ,参考枚举{@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_FREQUENCY_ALARM_TYPE}
      */
    public    int           emAlarmType;                          // 报警类型
    public    int           nTimes;                               // 频次
    public    byte[]        szReserved = new byte[128];           // 保留字节
}

