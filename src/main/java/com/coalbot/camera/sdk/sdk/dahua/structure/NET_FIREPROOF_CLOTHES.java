package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 防火衣相关属性状态信息
 * @date 2022/11/01 19:47:04
 */
public class NET_FIREPROOF_CLOTHES extends NetSDKLibStructure.SdkStructure {
	/**
	 * 是否穿着防火衣 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_FIREPROOF_CLOTHES_STATE}
	 */
    public int              emHasFireProofClothes;
	/**
	 * 防火衣颜色 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_CLOTHES_COLOR}
	 */
    public int              emFireProofClothesColor;
	/**
	 * 预留字节
	 */
    public byte[]           szReserved = new byte[128];

	public NET_FIREPROOF_CLOTHES() {
	}
}

