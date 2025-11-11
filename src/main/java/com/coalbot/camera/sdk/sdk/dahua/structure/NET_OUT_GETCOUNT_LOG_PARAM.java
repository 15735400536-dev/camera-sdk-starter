package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description CLIENT_QueryDevLogCount获取日志条数输出参数
 * @date 2023/05/05 11:12:30
 */
public class NET_OUT_GETCOUNT_LOG_PARAM extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;
    /**
     * 日志数(设备返回)
     */
    public int              nLogCount;

    public NET_OUT_GETCOUNT_LOG_PARAM() {
        this.dwSize = this.size();
    }
}

