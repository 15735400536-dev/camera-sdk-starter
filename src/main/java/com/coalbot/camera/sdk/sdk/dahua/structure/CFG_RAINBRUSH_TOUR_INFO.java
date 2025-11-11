package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 291189
 * @version 1.0
 * @description //雨刷巡航模式配置
 * @date 2021/10/29 14:31
 */
public class CFG_RAINBRUSH_TOUR_INFO extends NetSDKLibStructure.SdkStructure {
    public int              nTimes;                               // 雨刷执行次数：单位次
    public int              nPeriod;                              // 雨刷巡航周期：单位秒
}

