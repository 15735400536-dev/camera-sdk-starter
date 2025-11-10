package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 获取录播主机通道输入媒体介质入参 ，对应接口{@link NetSDKLibStructure#CLIENT_GetInputChannelMedia}
 *
 * @author ： 47040
 * @since ： Created in 2020/9/28 16:00
 */
public class NET_IN_GET_INPUT_CHANNEL_MEDIA extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;
    /**
     * 通道数量
     */
    public int              nChannelNum;
    /**
     * 通道号
     */
    public int[]            nChannel = new int[NetSDKLibStructure.MAX_PREVIEW_CHANNEL_NUM];

    public NET_IN_GET_INPUT_CHANNEL_MEDIA() {
        dwSize = this.size();
    }
}

