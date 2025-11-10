package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 智能球控制接口参数, 场景结构信息
*/
public class NETDEV_INTELLI_SCENE_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 场景编号
    */
    public int              nScene;

    public NETDEV_INTELLI_SCENE_INFO() {
        this.dwSize = this.size();
    }
}

