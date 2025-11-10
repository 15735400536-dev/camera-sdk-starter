package com.coalbot.camera.sdk.sdk.dahua.structure;



public class NET_IN_SCADA_GET_ATTRIBUTE_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     *  结构体大小
     */
    public int              dwSize;
    /**
     *  获取条件
     */
    public NET_GET_CONDITION_INFO stuCondition;

    public NET_IN_SCADA_GET_ATTRIBUTE_INFO(){
        this.dwSize = this.size();
    }
}

