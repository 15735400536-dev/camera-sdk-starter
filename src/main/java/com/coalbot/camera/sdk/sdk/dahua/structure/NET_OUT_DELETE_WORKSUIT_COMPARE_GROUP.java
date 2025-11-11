package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_DeleteWorkSuitCompareGroup 接口输出参数
 * @date 2022/10/08 17:14:26
 */
public class NET_OUT_DELETE_WORKSUIT_COMPARE_GROUP extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;

	public NET_OUT_DELETE_WORKSUIT_COMPARE_GROUP() {
		this.dwSize = this.size();
	}
}

