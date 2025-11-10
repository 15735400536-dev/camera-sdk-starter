package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 通用名字字符串字节数组对象
 * @date 2021/01/13
 */
public class MaxNameByteArrInfo extends NetSDKLibStructure.SdkStructure {
	/**
	 * 二维数组内字符串对应字节数组
	 */
    public byte[]           name = new byte[NetSDKLibStructure.MAX_NAME_LEN];
}

