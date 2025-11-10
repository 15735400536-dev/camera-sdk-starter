package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 楼层号,不要超过999
 * @date 2021/2/8
 */
public class NET_FLOORS_EX extends NetSDKLibStructure.SdkStructure {
    public byte[]           szFloorEx = new byte[8];
}

