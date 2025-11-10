package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 检测器配置信息
 * @date 2021/09/28
 */
public class NET_DETECTOR_CONFIG_INFO extends NetSDKLibStructure.SdkStructure {
	/**
     *  检测器id
     */
    public int              nDetectorId;
    /**
     *  是否启用
     */
    public int              bEnable;
    /**
     *  预留字节
     */
    public byte[]           szReserved = new byte[32];
}

