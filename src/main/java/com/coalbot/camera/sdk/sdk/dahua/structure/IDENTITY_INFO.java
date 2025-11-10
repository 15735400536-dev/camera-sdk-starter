package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 291189
 * @version 1.0
 * @description  身份信息
 * @date 2021/9/9
 */

public class IDENTITY_INFO extends NetSDKLibStructure.SdkStructure {
 /** 法定姓名*/
    public			byte[]         szCitizenName = new byte[128];
 /** 性别 ,参考枚举{@link NetSDKLibStructure.EM_SEX_TYPE } */
    public			int            emSex;
 /** 证件号18位*/
    public			byte[]         szCitizenIDNo = new byte[20];
 /** 年龄*/
    public			int            nAge;
    public byte[]           szReserved = new byte[512];           // 预留字节
}

