package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 录像信息更新到time出参 {@link NetSDKLib#CLIENT_OperateCourseRecordManager}
 *
 * @author ： 47040
 * @since ： Created in 2020/9/28 19:40
 */
public class NET_OUT_COURSERECORD_UPDATE_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;

    public NET_OUT_COURSERECORD_UPDATE_INFO() {
        dwSize = this.size();
    }
}

