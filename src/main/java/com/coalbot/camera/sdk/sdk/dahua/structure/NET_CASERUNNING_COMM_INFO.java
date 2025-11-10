package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 动环（串口）状态
 * @date 2023/05/11 16:03:12
 */
public class NET_CASERUNNING_COMM_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 动环设备序列号
	 */
    public byte[]           szPortSN = new byte[32];
	/**
	 * 设备序列号
	 */
    public byte[]           szSn = new byte[32];
	/**
	 * 保留字节
	 */
    public byte[]           szReserved = new byte[256];

	public NET_CASERUNNING_COMM_INFO() {
	}
}

