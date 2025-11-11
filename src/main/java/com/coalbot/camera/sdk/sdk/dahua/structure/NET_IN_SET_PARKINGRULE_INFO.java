package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @version 1.0
 * @description 设置停车规则接口，CLIENT_SetParkingRule 入参
 * @date 2021/05/28
 */
public class NET_IN_SET_PARKINGRULE_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 停车时长阈值，单位秒
	 */
    public int              nParkingTimeThreshold;

	public NET_IN_SET_PARKINGRULE_INFO() {
		this.dwSize = this.size();
	}
}

