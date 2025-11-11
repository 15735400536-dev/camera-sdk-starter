package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_StartRemoteSpeakPlay 接口输出参数
*/
public class NET_OUT_START_REMOTE_SPEAK_PLAY extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 开始放音状态,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_REMOTE_SPEAK_PLAY_STATE}
    */
    public int              emStatus;

    public NET_OUT_START_REMOTE_SPEAK_PLAY() {
        this.dwSize = this.size();
    }
}

