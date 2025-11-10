package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description  城市名字符串对应字节数组
 * @date 2021/08/15
 */
public class PassingCityByteArr extends NetSDKLibStructure.SdkStructure {
	/**
	 *  城市名字符串对应字节数组
	 */
    public byte[]           passingCityByteArr = new byte[128];
}

