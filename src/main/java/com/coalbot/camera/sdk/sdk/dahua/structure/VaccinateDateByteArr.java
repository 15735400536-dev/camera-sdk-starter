package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description  历史接种日期字符串对应字节数组
 * @date 2021/08/15
 */
public class VaccinateDateByteArr extends NetSDKLibStructure.SdkStructure {
	/**
	 *  历史接种日期字符串对应字节数组
	 */
    public byte[]           vaccinateDateByteArr = new byte[32];
}

