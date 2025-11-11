package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 可视对讲设备
 * @date 2023/05/11 14:09:05
 */
public class NET_LAMP_EXTERNAL_DEVICE_ALARM_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 可视对讲设备ID
	 */
    public byte[]           szId = new byte[32];
	/**
	 * 保留字节
	 */
    public byte[]           szReserved = new byte[256];

	public NET_LAMP_EXTERNAL_DEVICE_ALARM_INFO() {
	}
}

