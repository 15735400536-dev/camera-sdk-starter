package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 联动报警输出通道信息
*/
public class NET_ALARM_OUT_CHANNEL_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 比对报警规则,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_ALARM_RULE_TYPE}
    */
    public int              emAlarmRuleType;
    /**
     * 报警输出延时, 单位秒, 范围1-300
    */
    public int              nAlarmOutLatch;
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[128];

    public NET_ALARM_OUT_CHANNEL_INFO() {
    }
}

