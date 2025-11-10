package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * @author 251823
 * @version 1.0
 * @description 非机动车的滞留时间信息
 * @date 2022/04/11
 */
public class NET_NONMOTOR_STAY_STAT extends NetSDKLibStructure.SdkStructure {
    /**
     *  非机动车进入区域的时间
     */
    public NET_TIME         stuEnterTime;
    /**
     *  非机动车离开区域的时间
     */
    public NET_TIME         stuExitTime;
    /**
     *  保留字节
     */
    public byte[]           reserved = new byte[128];
}

