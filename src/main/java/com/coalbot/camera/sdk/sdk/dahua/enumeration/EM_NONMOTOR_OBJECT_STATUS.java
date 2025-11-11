package com.coalbot.camera.sdk.sdk.dahua.enumeration;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 事件/物体状态
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 20:21
 */
public class EM_NONMOTOR_OBJECT_STATUS extends NetSDKLibStructure.SdkStructure {
    /**
     *  未识别
     */
    public static final int   EM_NONMOTOR_OBJECT_STATUS_UNKNOWN = 0;
    /**
     *  否
     */
    public static final int   EM_NONMOTOR_OBJECT_STATUS_NO = 1;
    /**
     *  是
     */
    public static final int   EM_NONMOTOR_OBJECT_STATUS_YES = 2;
}

