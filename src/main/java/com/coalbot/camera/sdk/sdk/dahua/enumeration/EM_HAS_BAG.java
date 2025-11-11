package com.coalbot.camera.sdk.sdk.dahua.enumeration;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 是否戴包(包括背包或拎包)
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 19:35
 */
public class EM_HAS_BAG extends NetSDKLibStructure.SdkStructure {
    /**
     *  未知
     */
    public static final int   EM_HAS_BAG_UNKNOWN = 0;
    /**
     *  不带包
     */
    public static final int   EM_HAS_BAG_NO = 1;
    /**
     *  带包
     */
    public static final int   EM_HAS_BAG_YES = 2;
}

