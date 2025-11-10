package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_GetDeviceEthBandInfo 接口入参
 * @date 2022/05/30 14:45:46
 */
public class NET_IN_GET_DEVICE_ETH_BAND_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;

	public NET_IN_GET_DEVICE_ETH_BAND_INFO() {
		this.dwSize = this.size();
	}
}

