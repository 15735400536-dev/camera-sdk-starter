package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 录播主机删除模式出参
 *
 * @author ： 47040
 * @since ： Created in 2020/9/27 17:04
 */
public class NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_DELETE extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;
    /**
     * 删除的模式个数
     */
    public int              nReturnNum;
    /**
     * 返回码: 1 成功, 2 失败s
     */
    public int[]            nReturnCode = new int[NetSDKLibStructure.NET_MAX_MODE_NUMBER];

    public NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_DELETE() {
        dwSize = this.size();
    }
}

