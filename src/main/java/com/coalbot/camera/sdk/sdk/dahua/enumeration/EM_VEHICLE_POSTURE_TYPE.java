package com.coalbot.camera.sdk.sdk.dahua.enumeration;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 车辆姿势
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 20:21
 */
public class EM_VEHICLE_POSTURE_TYPE extends NetSDKLibStructure.SdkStructure {
    /**
     *  未知
     */
    public static final int   EM_VEHICLE_POSTURE_TYPE_UNKNOWN = 0;
    /**
     *  车头
     */
    public static final int   EM_VEHICLE_POSTURE_TYPE_VEHICLE_HEAD = 1;
    /**
     *  车侧
     */
    public static final int   EM_VEHICLE_POSTURE_TYPE_VEHICLE_SIDE = 2;
    /**
     *  车尾
     */
    public static final int   EM_VEHICLE_POSTURE_TYPE_VEHICLE_TAIL = 3;
}

