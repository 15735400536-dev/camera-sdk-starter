package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 外部防拆配置
*/
public class NET_ACCESSORY_TAMPER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 外部防拆配置使能是否存在
    */
    public int              abEnable;
    /**
     * 外部防拆配置使能
    */
    public int              bEnable;
    /**
     * 传感器类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SENSOR_TYPE}
    */
    public int              emType;
    /**
     * 外部防拆状态, 0:未知, 1:正常, 2:告警
    */
    public int              nStatus;
    /**
     * 预留字段
    */
    public byte[]           szReserved = new byte[64];

    public NET_ACCESSORY_TAMPER_INFO() {
    }
}

