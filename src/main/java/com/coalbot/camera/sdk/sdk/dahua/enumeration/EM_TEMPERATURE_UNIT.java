package com.coalbot.camera.sdk.sdk.dahua.enumeration;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 温度单位
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 20:21
 */
public class EM_TEMPERATURE_UNIT extends NetSDKLibStructure.SdkStructure {
    /**
     *  未知
     */
    public static final int   EM_TEMPERATURE_UNKNOWN = -1;
    /**
     *  摄氏度
     */
    public static final int   EM_TEMPERATURE_CENTIGRADE = 0;
    /**
     *  华氏度
     */
    public static final int   EM_TEMPERATURE_FAHRENHEIT = 1;
    /**
     *  开尔文
     */
    public static final int   EM_TEMPERATURE_KELVIN = 2;
}

