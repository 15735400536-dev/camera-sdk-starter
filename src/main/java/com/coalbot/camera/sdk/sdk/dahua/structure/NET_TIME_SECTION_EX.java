package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
public class NET_TIME_SECTION_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 时
    */
    public int              nStartHour;
    /**
     * 分
    */
    public int              nStartMinute;
    /**
     * 秒
    */
    public int              nStartSecond;
    /**
     * 时
    */
    public int              nStopHour;
    /**
     * 分
    */
    public int              nStopMinute;
    /**
     * 秒
    */
    public int              nStopSecond;
    /**
     * 名称 Day，Night，BackLight，Normal，StrongLight，FrontLight，LowLight，Custom
    */
    public byte[]           szName = new byte[22];
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[34];

    public NET_TIME_SECTION_EX() {
    }
}

