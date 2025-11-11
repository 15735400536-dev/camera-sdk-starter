package com.coalbot.camera.sdk.sdk.dahua.enumeration;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 车辆状态
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 20:00
 */
public class EM_CITYMOTOR_STATUS extends NetSDKLibStructure.SdkStructure {
    /**
     *  未知
     */
    public static final int   EM_CITYMOTOR_STATUS_UNKNOWN = 0;
    /**
     *  驶入
     */
    public static final int   EM_CITYMOTOR_STATUS_DRIVE_IN = 1;
    /**
     *  驶离
     */
    public static final int   EM_CITYMOTOR_STATUS_DRIVE_OUT = 2;
}

