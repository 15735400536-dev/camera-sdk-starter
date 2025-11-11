package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 暂停录像二次分析任务输出参数
*/
public class NET_OUT_SECONDARY_ANALYSE_PAUSETASK extends NetSDKLibStructure.SdkStructure
{
    /**
     * 赋值为结构体大小
    */
    public int              dwSize;

    public NET_OUT_SECONDARY_ANALYSE_PAUSETASK() {
        this.dwSize = this.size();
    }
}

