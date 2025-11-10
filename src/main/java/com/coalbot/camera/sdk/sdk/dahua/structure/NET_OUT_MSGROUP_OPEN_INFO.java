package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 打开主从跟踪组出参
 * @date 2022/09/14 13:52:58
 */
public class NET_OUT_MSGROUP_OPEN_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 主从跟踪组句柄
	 */
    public int              dwToken;

	public NET_OUT_MSGROUP_OPEN_INFO() {
		this.dwSize = this.size();
	}
}

