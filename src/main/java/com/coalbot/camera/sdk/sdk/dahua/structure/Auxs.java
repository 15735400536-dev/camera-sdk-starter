package com.coalbot.camera.sdk.sdk.dahua.structure;/**
 * @author 47081
 * @descriptio
 * @date 2020/11/9
 * @version 1.0
 */



import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

import static com.coalbot.camera.sdk.sdk.dahua.constant.SDKStructureFieldLenth.CFG_COMMON_STRING_32;

/**
 * @author 47081
 * @version 1.0
 * @description
 * @date 2020/11/9
 */
public class Auxs extends NetSDKLibStructure.SdkStructure {
    public byte[]           auxs = new byte[CFG_COMMON_STRING_32];
}

