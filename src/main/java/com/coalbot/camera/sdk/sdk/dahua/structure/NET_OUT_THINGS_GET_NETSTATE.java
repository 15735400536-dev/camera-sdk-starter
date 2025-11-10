package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description CLIENT_GetThingsNetState接口输出参数
 * @date 2022/04/20 10:16:56
 */
public class NET_OUT_THINGS_GET_NETSTATE extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小, 调用者必须初始化该字段
     */
    public int              dwSize;
    /**
     * 设备连接状态 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_THINGS_CONNECT_STATE}
     */
    public int              emConnectState;

    public NET_OUT_THINGS_GET_NETSTATE() {
        this.dwSize = this.size();
    }
}

