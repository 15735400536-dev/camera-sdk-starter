package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 抬头检测周期上报的回调
 * @date 2022/10/28 11:33:47
 */
public class NET_VIDEOSTAT_SUMMARY_HEAD_LIFT_DETECTION extends NetSDKLibStructure.SdkStructure {
	/**
	 * 通道号
	 */
    public int              nChannelID;
	/**
	 * 预置点编号
	 */
    public int              nPtzPresetId;
	/**
	 * 时间
	 */
    public NET_TIME_EX      UTC = new NET_TIME_EX();
	/**
	 * 检测到的抬头人数
	 */
    public int              nFaceCount;
	/**
	 * 检测到的总人数
	 */
    public int              nHumanCount;
	/**
	 * 保留字节
	 */
    public byte[]           szReserved = new byte[1024];

	public NET_VIDEOSTAT_SUMMARY_HEAD_LIFT_DETECTION() {
	}
}

