package com.coalbot.camera.sdk.sdk.dahua.structure;



/**
 * @author 260611
 * @description 删除标定点信息出参
 * @date 2023/05/24 10:24:52
 */
public class NET_OUT_DELETE_CALIBRATEINFO_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;

	public NET_OUT_DELETE_CALIBRATEINFO_INFO() {
		this.dwSize = this.size();
	}
}

