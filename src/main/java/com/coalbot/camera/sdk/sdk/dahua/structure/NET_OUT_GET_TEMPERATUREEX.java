package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description CLIENT_FaceBoard_GetTemperatureEx的出参
 * @date 2022/05/12 16:32:08
 */
public class NET_OUT_GET_TEMPERATUREEX extends NetSDKLibStructure.SdkStructure {
	/**
	 * 结构体大小
	 */
    public int              dwSize;
	/**
	 * 返回的有效温度监测点的个数,num>1时,对应emTemperatureType为ALL
	 */
    public int              nRetMonitorPointNum;
	/**
	 * 监测点温度
	 */
    public NET_TEMPERATUREEX_VALUE[] stuTemperatureEx = new NET_TEMPERATUREEX_VALUE[NetSDKLibStructure.MAX_TEMPERATUREEX_POINT_NUM];

	public NET_OUT_GET_TEMPERATUREEX() {
		for (int i = 0; i < stuTemperatureEx.length; i++) {
			stuTemperatureEx[i] = new NET_TEMPERATUREEX_VALUE();
		}
		this.dwSize = this.size();
	}
}

