package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 开启/关闭指定通道录像出参 {@link NetSDKLib#CLIENT_SetCourseRecordState}
 *
 * @author ： 47040
 * @since ： Created in 2020/9/28 16:16
 */
public class NET_OUT_SET_COURSE_RECORD_STATE extends NetSDKLibStructure.SdkStructure {
    /**
     * 该结构体大小
     */
    public int              dwSize;

    public NET_OUT_SET_COURSE_RECORD_STATE() {
        dwSize = this.size();
    }
}

