package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_ModifyFloorHeight 接口输出参数
*/
public class NET_OUT_MODIFY_FLOOR_HEIGHT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_MODIFY_FLOOR_HEIGHT() {
        this.dwSize = this.size();
    }
}

