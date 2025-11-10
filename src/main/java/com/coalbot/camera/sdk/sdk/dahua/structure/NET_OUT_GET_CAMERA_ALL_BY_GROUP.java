package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
/**
 * CLIENT_MatrixGetCameraAllByGroup 接口输出参数
*/
public class NET_OUT_GET_CAMERA_ALL_BY_GROUP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 设备组最大个数, 最多为1024
    */
    public int              nMaxCameraGroup;
    /**
     * 设备实际组个数
    */
    public int              nCameraGroup;
    /**
     * 设备组信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CAMERA_GROUP_INFO}
    */
    public Pointer          pstCameraGroupInfo;

    public NET_OUT_GET_CAMERA_ALL_BY_GROUP() {
        this.dwSize = this.size();
    }
}

