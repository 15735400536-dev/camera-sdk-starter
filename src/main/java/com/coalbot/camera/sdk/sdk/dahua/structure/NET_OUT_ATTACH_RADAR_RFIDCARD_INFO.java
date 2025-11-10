package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author ： 260611
 * @description ： CLIENT_AttachRadarRFIDCardInfo接口出参
 * @since ： Created in 2022/02/11 10:22
 */

public class NET_OUT_ATTACH_RADAR_RFIDCARD_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;

    public NET_OUT_ATTACH_RADAR_RFIDCARD_INFO() {
        this.dwSize = this.size();
    }
}

