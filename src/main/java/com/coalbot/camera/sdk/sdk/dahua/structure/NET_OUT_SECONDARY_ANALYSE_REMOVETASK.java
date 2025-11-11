package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 删除录像二次分析任务输出参数
*/
public class NET_OUT_SECONDARY_ANALYSE_REMOVETASK extends NetSDKLibStructure.SdkStructure
{
    /**
     * 赋值为结构体大小
    */
    public int              dwSize;

    public NET_OUT_SECONDARY_ANALYSE_REMOVETASK() {
        this.dwSize = this.size();
    }
}

