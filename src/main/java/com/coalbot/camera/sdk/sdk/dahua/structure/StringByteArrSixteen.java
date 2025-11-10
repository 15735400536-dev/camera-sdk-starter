package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 字符串字节数组对象(长度16)
 * @date 2021/01/13
 */
public class StringByteArrSixteen extends NetSDKLibStructure.SdkStructure {
	/**
	 * 二维数组内字符串对应字节数组
	 */
    public byte[]           data = new byte[NetSDKLibStructure.CFG_COMMON_STRING_16];
}

