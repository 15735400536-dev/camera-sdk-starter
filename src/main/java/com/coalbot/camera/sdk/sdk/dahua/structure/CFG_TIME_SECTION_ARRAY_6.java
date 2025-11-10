package com.coalbot.camera.sdk.sdk.dahua.structure;


public class CFG_TIME_SECTION_ARRAY_6 extends NetSDKLibStructure.SdkStructure {
    /**
     * 参见结构体定义 {@link NetSDKLib.CFG_TIME_SECTION}
    */
    public NetSDKLib.CFG_TIME_SECTION[] obj_6 = new NetSDKLib.CFG_TIME_SECTION[6];

    public CFG_TIME_SECTION_ARRAY_6() {
        for(int i = 0; i < obj_6.length; i++){
            obj_6[i] = new NetSDKLib.CFG_TIME_SECTION();
        }
    }
}

