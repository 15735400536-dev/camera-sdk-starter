package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 结束查询课程 入参 {@link NetSDKLib#CLIENT_QueryCourseClose}
 *
 * @author ： 47040
 * @since ： Created in 2020/9/17 21:27
 */
public class NET_IN_QUERY_COURSE_CLOSE extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;
    /**
     * 课程数量
     */
    public int              nFindID;

    public NET_IN_QUERY_COURSE_CLOSE() {
        dwSize = this.size();
    }
}

