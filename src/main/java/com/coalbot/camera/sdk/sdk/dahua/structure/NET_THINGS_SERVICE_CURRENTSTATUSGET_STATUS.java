package com.coalbot.camera.sdk.sdk.dahua.structure;




/**
 * @author 260611
 * @description 设备状态数据
 * @date 2022/04/20 10:50:24
 */
public class NET_THINGS_SERVICE_CURRENTSTATUSGET_STATUS extends NetSDKLibStructure.SdkStructure {
    /**
     * 设备ID
     */
    public byte[]           szDeviceID = new byte[64];
    /**
     * 报警状态 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_THINGS_CURRENTSTATUSGET_STATUS}
     */
    public int              emStatus;
    /**
     * 保留字节
     */
    public byte[]           szReserve = new byte[512];
}

