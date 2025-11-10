package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author ： 47040
 * @since ： Created in 2020/9/28 18:55
 */
public class NET_RECORD_INFO_ARRAY extends NetSDKLibStructure.SdkStructure {
    public NET_RECORD_INFO[] stuRecordInfo_2 = new NET_RECORD_INFO[16];

    public NET_RECORD_INFO_ARRAY() {
        for (int i = 0; i < stuRecordInfo_2.length; i++) {
            stuRecordInfo_2[i] = new NET_RECORD_INFO();
        }
    }
}

