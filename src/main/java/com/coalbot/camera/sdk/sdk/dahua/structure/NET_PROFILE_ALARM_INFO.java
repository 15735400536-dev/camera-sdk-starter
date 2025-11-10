package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 报警类型的附带信息
*/
public class NET_PROFILE_ALARM_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 低电压附带信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_LOWERPOWER_INFO}
    */
    public NET_LOWERPOWER_INFO stuLowerPower = new NET_LOWERPOWER_INFO();
    /**
     * 报警输入附带信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_ALARMIN_INFO}
    */
    public NET_ALARMIN_INFO stuAlarmIn = new NET_ALARMIN_INFO();
    /**
     * 传感器类型报警的数值与emSenseMethod关联,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_SENSE_VALUE_INFO}
    */
    public NET_SENSE_VALUE_INFO stuSenseValue = new NET_SENSE_VALUE_INFO();
    /**
     * 上报的云附带信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CLOUD_INFO}
    */
    public NET_CLOUD_INFO   stuCloudInfo = new NET_CLOUD_INFO();
    /**
     * 预留
    */
    public byte[]           szReserved = new byte[64];

    public NET_PROFILE_ALARM_INFO() {
    }
}

