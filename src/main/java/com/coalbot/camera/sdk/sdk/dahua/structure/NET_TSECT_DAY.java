package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 拆分自{@link MONITORWALL_COLLECTION_SCHEDULE}
 *
 * @author ： 47040
 * @since ： Created in 2020/10/19 9:43
 */
public class NET_TSECT_DAY extends NetSDKLibStructure.SdkStructure {
    /**
     * 时间段结构
     */
    public NetSDKLibStructure.NET_TSECT[] stuSchedule = new NetSDKLibStructure.NET_TSECT[NetSDKLibStructure.NET_TSCHE_SEC_NUM];

    public NET_TSECT_DAY() {
        for (int i = 0; i < stuSchedule.length; i++) {
            stuSchedule[i] = new NetSDKLibStructure.NET_TSECT();
        }
    }
}

