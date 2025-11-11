package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SetPersonInfoInputResult 输出参数
 */
public class NET_OUT_PERSON_INFO_INPUT_RESULT extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;

	public NET_OUT_PERSON_INFO_INPUT_RESULT() {
		this.dwSize = this.size();
	}
}

