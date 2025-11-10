package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description  网络模式字符串对应字节数组
 * @date 2021/09/17
 */
public class SupportedModeByteArr extends NetSDKLibStructure.SdkStructure {
	/**
	 *  网络模式字符串对应字节数组
	 */
    public byte[]           supportedModeByteArr = new byte[NetSDKLibStructure.NET_MAX_MODE_LEN];
}

