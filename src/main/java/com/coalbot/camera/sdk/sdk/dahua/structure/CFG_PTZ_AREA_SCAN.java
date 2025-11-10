package com.coalbot.camera.sdk.sdk.dahua.structure;/**
 * @author 47081
 * @descriptio
 * @date 2020/11/9
 * @version 1.0
 */


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 云台-区域扫描能力集
 * @date 2020/11/9
 */
public class CFG_PTZ_AREA_SCAN extends NetSDKLibStructure.SdkStructure {
    /**
     * 是否支持区域扫描
     */
    public int              bIsSupportAutoAreaScan;
    /**
     * 区域扫描的个数
     */
    public int              wScanNum;
}

