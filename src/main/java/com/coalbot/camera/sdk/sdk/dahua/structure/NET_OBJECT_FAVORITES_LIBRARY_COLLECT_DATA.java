package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 收藏的目标事件数据
*/
public class NET_OBJECT_FAVORITES_LIBRARY_COLLECT_DATA extends NetSDKLibStructure.SdkStructure
{
    /**
     * 候选人来自哪个数据库,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_PEOPLE_GROUP_TYPE}
    */
    public int              emRange;
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 目标事件时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 全景广角图,参见结构体定义 {@link NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
    */
    public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX();
    /**
     * 目标信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OBJECT_FAVORITES_LIBRARY_COLLECT_DATA_OBJECT}
    */
    public NET_OBJECT_FAVORITES_LIBRARY_COLLECT_DATA_OBJECT stuObject = new NET_OBJECT_FAVORITES_LIBRARY_COLLECT_DATA_OBJECT();
    /**
     * 人体信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_HISTORY_HUMAN_INFO}
    */
    public NET_HISTORY_HUMAN_INFO stuHuman = new NET_HISTORY_HUMAN_INFO();
    /**
     * 机动车信息, 机动车以图搜图时返回的机动车属性,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_HISTORY_TRAFFIC_CAR_INFO}
    */
    public NET_HISTORY_TRAFFIC_CAR_INFO stuTrafficCar = new NET_HISTORY_TRAFFIC_CAR_INFO();
    /**
     * 保留字段
    */
    public byte[]           szReserved = new byte[1024];

    public NET_OBJECT_FAVORITES_LIBRARY_COLLECT_DATA() {
    }
}

