package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 车牌
 * @date 2021/2/22
 */
public class PlateNumber extends NetSDKLibStructure.SdkStructure {
    public byte[]           plateNumber = new byte[32];
}

