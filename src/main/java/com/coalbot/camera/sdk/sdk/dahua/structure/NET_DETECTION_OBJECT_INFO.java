package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 隔栏传物事件检测到的目标信息结构体
*/
public class NET_DETECTION_OBJECT_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 目标类型 支持"Human"
    */
    public byte[]           szObjectType = new byte[16];
    /**
     * 目标物体包围盒，矩形类型归一化到[0,8192)区间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RECT_EX}
    */
    public NET_RECT_EX      stuBoundingBox = new NET_RECT_EX();
    /**
     * 目标ID
    */
    public int              nObjectID;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[516];

    public NET_DETECTION_OBJECT_INFO() {
    }
}

