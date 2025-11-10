package com.coalbot.camera.sdk.sdk.dahua.structure;



/**
 * @author 260611
 * @description CLIENT_GetBindMode 接口出参
 * @origin autoTool
 * @date 2023/09/15 10:59:48
 */
public class NET_OUT_GET_BIND_MODE_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * / 此结构体大小,必须赋值
	 */
    public int              dwSize;
	/**
	 * / 绑定模式 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_BIND_MODE}
	 */
    public int              emBindMode;

	public NET_OUT_GET_BIND_MODE_INFO() {
		this.dwSize = this.size();
	}
}

