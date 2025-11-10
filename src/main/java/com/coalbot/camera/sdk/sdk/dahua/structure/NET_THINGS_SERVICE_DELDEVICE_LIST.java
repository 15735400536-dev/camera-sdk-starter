package com.coalbot.camera.sdk.sdk.dahua.structure;




/**
 * @author 260611
 * @description 子设备ID
 * @date 2022/04/20 10:50:23
 */
public class NET_THINGS_SERVICE_DELDEVICE_LIST extends NetSDKLibStructure.SdkStructure {
    /**
     * 子设备ID
     */
    public byte[]           szDevieID = new byte[64];
    /**
     * 保留字节
     */
    public byte[]           szReserve = new byte[512];
}

