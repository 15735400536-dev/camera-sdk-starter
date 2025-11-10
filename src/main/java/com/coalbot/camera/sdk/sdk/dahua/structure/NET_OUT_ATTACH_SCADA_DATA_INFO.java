package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description CLIENT_AttachSCADAData 接口出参
 * @date 2022/12/13 10:20:50
 */
public class NET_OUT_ATTACH_SCADA_DATA_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 此结构体大小,必须赋值
     */
    public int              dwSize;

    public NET_OUT_ATTACH_SCADA_DATA_INFO() {
        this.dwSize = this.size();
    }
}

