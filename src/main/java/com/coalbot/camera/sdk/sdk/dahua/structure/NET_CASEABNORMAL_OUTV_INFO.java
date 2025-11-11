package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 输出电压状态
 * @date 2023/05/11 16:00:20
 */
public class NET_CASEABNORMAL_OUTV_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * DC12v1状态，00异常01正常
	 */
    public byte[]           szDc12v1 = new byte[32];
	/**
	 * DC12v2状态，00异常01正常
	 */
    public byte[]           szDc12v2 = new byte[32];
	/**
	 * AC24V1状态，00异常01正常
	 */
    public byte[]           szAc24v1 = new byte[32];
	/**
	 * AC24V2状态，00异常01正常
	 */
    public byte[]           szAc24v2 = new byte[32];
	/**
	 * AC220V1状态，00异常01正常
	 */
    public byte[]           szAc220v1 = new byte[32];
	/**
	 * AC220V2状态，00异常01正常
	 */
    public byte[]           szAc220v2 = new byte[32];
	/**
	 * AC220V3状态，00异常01正常
	 */
    public byte[]           szAc220v3 = new byte[32];
	/**
	 * 保留字节
	 */
    public byte[]           szReserved = new byte[256];

	public NET_CASEABNORMAL_OUTV_INFO() {
	}
}

