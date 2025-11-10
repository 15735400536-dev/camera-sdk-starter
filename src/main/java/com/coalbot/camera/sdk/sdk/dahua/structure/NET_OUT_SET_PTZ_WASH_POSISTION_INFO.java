package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author ： 260611
 * @description ： 标定球机和蓄水池污点位置，出参
 * @since ： Created in 2021/11/29 10:47
 */

public class NET_OUT_SET_PTZ_WASH_POSISTION_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;

    public NET_OUT_SET_PTZ_WASH_POSISTION_INFO() {
        this.dwSize = this.size();
    }
}

