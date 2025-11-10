package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

public class CFG_TIME_SECTION_ARRAY_10 extends NetSDKLibStructure.SdkStructure {
    /**
     * 参见结构体定义 {@link NetSDKLibStructure.CFG_TIME_SECTION}
    */
    public NetSDKLibStructure.CFG_TIME_SECTION[] obj_10 = new NetSDKLibStructure.CFG_TIME_SECTION[10];

    public CFG_TIME_SECTION_ARRAY_10() {
        for(int i = 0; i < obj_10.length; i++){
            obj_10[i] = new NetSDKLibStructure.CFG_TIME_SECTION();
        }
    }
}

