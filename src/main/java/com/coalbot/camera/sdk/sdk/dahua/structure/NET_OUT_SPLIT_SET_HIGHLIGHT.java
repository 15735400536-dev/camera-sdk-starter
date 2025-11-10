package com.coalbot.camera.sdk.sdk.dahua.structure;




/**
 * @author 260611
 * @description 设置源边框高亮使能开关输出参数
 * @date 2022/06/22 09:56:20
 */
public class NET_OUT_SPLIT_SET_HIGHLIGHT extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;

    public NET_OUT_SPLIT_SET_HIGHLIGHT() {
        this.dwSize = this.size();
    }
}

