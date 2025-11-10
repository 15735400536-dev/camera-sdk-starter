package com.coalbot.camera.sdk.sdk.dahua.structure;



/**
 * @author 251823
 * @description 区域或车位更新信息
 * @date 2022/05/11 13:27:51
 */
public class NET_UPDATE_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 更新类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_UPDATE_TYPE}
	 */
    public int              emType;
	/**
	 * 更新的区域或车位名称
	 */
    public byte[]           szName = new byte[32];
	/**
	 * 更新前的车位类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SPACE_TYPE}
	 */
    public int              emBeforeUpdateType;
	/**
	 * 更新后的车位类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SPACE_TYPE}
	 */
    public int              emAfterUpdateType;
	/**
	 * 保留字节
	 */
    public byte[]           szResvered = new byte[256];

	public NET_UPDATE_INFO() {
	}
}

