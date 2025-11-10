package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 人员过滤信息
 * @date 2021/02/22
 */
public class ANATOMY_TEMP_DETECT_PERSON_FILTER extends NetSDKLibStructure.SdkStructure {
	 /**
     *  姓名 
     */
    public byte[]           szName = new byte[64];
    /**
     *  保留字节
     */
    public byte[]           byReserved = new byte[1024];
}

