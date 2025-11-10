package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 *
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 19:35
 */
public class NET_POINT_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     *  主相机标定点
     */
    public NetSDKLibStructure.DH_POINT stuMasterPoint = new NetSDKLibStructure.DH_POINT();
    /**
     *  从相机(球机)标定点
     */
    public NetSDKLibStructure.DH_POINT stuSlavePoint = new NetSDKLibStructure.DH_POINT();
    /**
     *  保留字段
     */
    public byte             byReserved[] = new byte[256];
}

