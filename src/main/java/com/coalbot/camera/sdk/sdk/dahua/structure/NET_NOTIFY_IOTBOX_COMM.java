package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 回调的实时数据
*/
public class NET_NOTIFY_IOTBOX_COMM extends NetSDKLibStructure.SdkStructure
{
    /**
     * 传感器的信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IOTBOX_DEVICE_INFO}
    */
    public NET_IOTBOX_DEVICE_INFO[] stuDeviceInfo = new NET_IOTBOX_DEVICE_INFO[16];
    /**
     * 传感器的信息个数
    */
    public int              nDeviceInfoNum;
    /**
     * 保留字段
    */
    public byte[]           szResvered = new byte[1028];

    public NET_NOTIFY_IOTBOX_COMM() {
        for(int i = 0; i < stuDeviceInfo.length; i++){
            stuDeviceInfo[i] = new NET_IOTBOX_DEVICE_INFO();
        }
    }
}

