package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 获取录像状态 出参 {@link NetSDKLib#CLIENT_GetRecordState}
 *
 * @author ： 47040
 * @since ： Created in 2020/10/13 20:32
 */
public class NET_OUT_GET_RECORD_STATE extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;
    /**
     * 是否在录像 0 否 1 是
     */
    public int              bState;

    public NET_OUT_GET_RECORD_STATE() {
        dwSize = this.size();
    }
}

