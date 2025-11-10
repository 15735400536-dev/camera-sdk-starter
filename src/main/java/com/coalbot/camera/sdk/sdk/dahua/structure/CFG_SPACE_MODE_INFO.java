package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 车位信息
 * @date 2022/07/22 14:43:48
 */
public class CFG_SPACE_MODE_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 车位编号
	 */
    public byte[]           szParkNo = new byte[32];
	/**
	 * 车位中心点坐标
	 */
    public NetSDKLibStructure.CFG_POLYGON stuCoordinate = new NetSDKLibStructure.CFG_POLYGON();
	/**
	 * 车位类型: 0:未知, 1:小车车位, 2:大车车位, 3:危化品车位, 4:货车车位, 5:小客车车位, 6:大客车车位
	 */
    public int              nSpaceType;
	/**
	 * 保留字节
	 */
    public byte[]           szReserved = new byte[60];

	public CFG_SPACE_MODE_INFO() {
	}
}

