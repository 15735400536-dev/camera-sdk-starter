package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 用户的开门时间段
 * @date 2021/2/8
 */
public class USER_TIME_SECTION extends NetSDKLibStructure.SdkStructure {
    public byte[]           userTimeSections = new byte[20];
}

