package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_GetStateManager 接口入参
 * @date 2023/05/11 14:18:32
 */
public class NET_IN_GET_STATEMANAGER_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 状态枚举 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_STATEMANAGER_STATE}
	 */
    public int              emState;

	public NET_IN_GET_STATEMANAGER_INFO() {
		this.dwSize = this.size();
	}
}

