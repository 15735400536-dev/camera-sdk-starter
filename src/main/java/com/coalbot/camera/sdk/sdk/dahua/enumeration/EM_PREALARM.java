package com.coalbot.camera.sdk.sdk.dahua.enumeration;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 是否为违规预警图片
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 20:00
 */
public class EM_PREALARM extends NetSDKLibStructure.SdkStructure {
    /**
     * 未知
     */
    public static final int   EM_PREALARM_UNKNOWN = -1;
    /**
     * 机动车违章事件
     */
    public static final int   EM_PREALARM_VIOLATION_EVENT = 0;
    /**
     * 机动车违章预警事件
     */
    public static final int   EM_PREALARM_VIOLATION_WARNING_EVENT = 1;
}

