package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description CLIENT_StartThingsHistoryData 输出参数
 * @date 2022/04/20 10:16:56
 */
public class NET_OUT_THINGS_START_HISTORYDATA extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小, 调用者必须初始化该字段
     */
    public int              dwSize;
    /**
     * 符合查询条件的总数
     */
    public int              dwCount;

    public NET_OUT_THINGS_START_HISTORYDATA() {
        this.dwSize = this.size();
    }
}

