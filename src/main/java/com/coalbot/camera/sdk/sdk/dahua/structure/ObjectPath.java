package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 图路径对象
 * @date 2021/02/23
 */
public class ObjectPath extends NetSDKLibStructure.SdkStructure {
	/**
	 *  路径字节数组
	 */
    public byte[]           objectPath = new byte[NetSDKLibStructure.MAX_PATH];
}

