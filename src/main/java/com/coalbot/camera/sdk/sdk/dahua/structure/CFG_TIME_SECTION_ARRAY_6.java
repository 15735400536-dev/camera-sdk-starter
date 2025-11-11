package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

public class CFG_TIME_SECTION_ARRAY_6 extends NetSDKLibStructure.SdkStructure {
    /**
     * 参见结构体定义 {@link NetSDKLibStructure.CFG_TIME_SECTION}
    */
    public NetSDKLibStructure.CFG_TIME_SECTION[] obj_6 = new NetSDKLibStructure.CFG_TIME_SECTION[6];

    public CFG_TIME_SECTION_ARRAY_6() {
        for(int i = 0; i < obj_6.length; i++){
            obj_6[i] = new NetSDKLibStructure.CFG_TIME_SECTION();
        }
    }
}

