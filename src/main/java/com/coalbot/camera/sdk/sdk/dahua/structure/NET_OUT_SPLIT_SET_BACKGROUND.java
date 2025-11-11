package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 设置视频输出背景图输出参数
*/
public class NET_OUT_SPLIT_SET_BACKGROUND extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_OUT_SPLIT_SET_BACKGROUND() {
        this.dwSize = this.size();
    }
}

