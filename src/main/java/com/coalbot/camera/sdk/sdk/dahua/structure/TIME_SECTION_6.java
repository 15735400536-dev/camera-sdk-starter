package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 6个NET_TSECT时间段结构体
 * @date 2022/10/14 13:53:01
 */
public class TIME_SECTION_6 extends NetSDKLibStructure.SdkStructure {
    public NetSDKLibStructure.NET_TSECT[] timeSection = new NetSDKLibStructure.NET_TSECT[NetSDKLibStructure.NET_N_REC_TSECT];
}

