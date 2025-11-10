package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 大致日出/日落时间
*/
public class NET_SUN_TIME extends NetSDKLibStructure.SdkStructure
{
    /**
     * 时
    */
    public int              nHour;
    /**
     * 分
    */
    public int              nMinute;
    /**
     * 秒
    */
    public int              nSecond;

    public NET_SUN_TIME() {
    }
}

