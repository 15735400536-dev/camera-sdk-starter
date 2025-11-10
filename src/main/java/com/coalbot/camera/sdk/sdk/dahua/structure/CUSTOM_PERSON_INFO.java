package com.coalbot.camera.sdk.sdk.dahua.structure;



import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * className：CUSTOM_PERSON_INFO
 * description：
 * author：251589
 * createTime：2020/12/28 11:08
 *
 * @version v1.0
 */
public class CUSTOM_PERSON_INFO extends NetSDKLibStructure.SdkStructure {
    public byte[]           szPersonInfo = new byte[NetSDKLibStructure.DH_MAX_PERSON_INFO_LEN]; //人员扩展信息
    public byte[]           byReserved = new byte[124];           // 保留字节
}

