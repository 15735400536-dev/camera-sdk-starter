package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 电视墙预案名称 拆分自{@link NET_IN_MONITORWALL_GET_COLL_SCHD}
 *
 * @author ： 47040
 * @since ： Created in 2020/10/19 9:28
 */
public class NET_MONITORWALL_COLLECTION_NAME extends NetSDKLibStructure.SdkStructure {
    /**
     * 电视墙预案名称
     */
    public byte[]           collectionName = new byte[NetSDKLibStructure.NET_DEVICE_NAME_LEN];
}

