package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 291189
 * @description 云台三维定位, 对应NET_EXTPTZ_MOVE_DIRECTLY枚举
 * @origin autoTool
 * @date 2023/03/07 16:38:49
 */
public class NET_IN_MOVE_DIRECTLY_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;
    /**
     * 屏幕坐标
     */
    public NetSDKLibStructure.PTZ_SPEED_UNIT stuScreen = new NetSDKLibStructure.PTZ_SPEED_UNIT();
    /**
     * 云台运行速度
     */
    public NetSDKLibStructure.PTZ_SPEED_UNIT stuSpeed = new NetSDKLibStructure.PTZ_SPEED_UNIT();

    public NET_IN_MOVE_DIRECTLY_INFO() {
        this.dwSize = this.size();
    }
}

