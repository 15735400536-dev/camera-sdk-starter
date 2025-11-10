package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 多通道产品的无感唤醒功能
*/
public class NET_SLEEPING_CONTROL_IMPERCEPTIBLE_WAKEUP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 无感唤醒功能是否开启
    */
    public int              bEnable;
    /**
     * 保留字节
    */
    public byte[]           szResvered = new byte[1020];

    public NET_SLEEPING_CONTROL_IMPERCEPTIBLE_WAKEUP() {
    }
}

