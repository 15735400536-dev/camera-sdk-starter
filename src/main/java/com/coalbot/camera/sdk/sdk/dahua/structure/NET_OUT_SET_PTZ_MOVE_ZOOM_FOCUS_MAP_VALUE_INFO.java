package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_SetPtzMoveZoomFocusMapValue 接口输出参数
*/
public class NET_OUT_SET_PTZ_MOVE_ZOOM_FOCUS_MAP_VALUE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_SET_PTZ_MOVE_ZOOM_FOCUS_MAP_VALUE_INFO() {
        this.dwSize = this.size();
    }
}

