package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
public class NET_TSECT_ARRAY_6 extends NetSDKLibStructure.SdkStructure {
    /**
     * 参见结构体定义 {@link NetSDKLibStructure.NET_TSECT}
    */
    public NetSDKLibStructure.NET_TSECT[] obj_6 = new NetSDKLibStructure.NET_TSECT[6];

    public NET_TSECT_ARRAY_6() {
        for(int i = 0; i < obj_6.length; i++){
            obj_6[i] = new NetSDKLibStructure.NET_TSECT();
        }
    }
}

