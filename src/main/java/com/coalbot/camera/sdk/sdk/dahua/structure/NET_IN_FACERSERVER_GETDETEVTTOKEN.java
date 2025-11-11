package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_FaceRServerGetDetectToken 接口输入参数
 * @date 2023/04/20 14:04:21
 */
public class NET_IN_FACERSERVER_GETDETEVTTOKEN extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;

	public NET_IN_FACERSERVER_GETDETEVTTOKEN() {
		this.dwSize = this.size();
	}
}

