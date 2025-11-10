package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * 异物物体 ForeignMatterObjects
*/
public class FOREIGN_MATTER_OBJECT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 物体ID, 每个ID表示一个唯一的物体
    */
    public int              dwObjectID;
    /**
     * 物体动作类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_OBJECT_ACTION}
    */
    public int              emAction;
    /**
     * 跟踪物体包围盒,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RECT}
    */
    public NET_RECT         stuBoundingBox = new NET_RECT();
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[256];

    public FOREIGN_MATTER_OBJECT() {
    }
}

