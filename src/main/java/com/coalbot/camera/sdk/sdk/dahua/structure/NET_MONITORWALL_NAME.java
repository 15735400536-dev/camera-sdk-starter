package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 电视墙名称 拆分自{@link NET_IN_MONITORWALL_GET_ENABLE}
 *
 * @author ： 47040
 * @since ： Created in 2020/10/19 11:08
 */
public class NET_MONITORWALL_NAME extends NetSDKLibStructure.SdkStructure {
    /**
     * 电视墙名称
     */
    public byte[]           szName = new byte[NetSDKLibStructure.NET_COMMON_STRING_128];
}

