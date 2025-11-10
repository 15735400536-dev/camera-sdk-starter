package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_GetDeviceInfoEx 接口输入参数
 * @date 2023/04/19 15:36:29
 */
public class NET_IN_GET_DEVICE_INFO_EX extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 设备个数
	 */
    public int              nCount;
	/**
	 * 设备信息列表
	 */	
    public NetSDKLibStructure.DEVICE_ID[]      szDeviceIDs = (NetSDKLibStructure.DEVICE_ID[])new NetSDKLibStructure.DEVICE_ID().toArray(32); // 设备列表

	public NET_IN_GET_DEVICE_INFO_EX() {
		for (int i = 0; i < szDeviceIDs.length; i++) {
			szDeviceIDs[0] = new NetSDKLibStructure.DEVICE_ID();
		}
		this.dwSize = this.size();
	}
}

