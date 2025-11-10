package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 设备类型信息
*/
public class CFG_DEVICE_CLASS_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 设备类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_DEVICE_TYPE}
    */
    public int              emDeviceType;

    public CFG_DEVICE_CLASS_INFO() {
        this.dwSize = this.size();
    }
}

