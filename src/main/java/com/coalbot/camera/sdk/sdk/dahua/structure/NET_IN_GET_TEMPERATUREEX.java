package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_FaceBoard_GetTemperatureEx的入参
 * @date 2022/05/12 16:28:47
 */
public class NET_IN_GET_TEMPERATUREEX extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 温度类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_TEMPERATUREEX_TYPE}
	 */
    public int              emTemperatureType;

	public NET_IN_GET_TEMPERATUREEX() {
		this.dwSize = this.size();
	}
}

