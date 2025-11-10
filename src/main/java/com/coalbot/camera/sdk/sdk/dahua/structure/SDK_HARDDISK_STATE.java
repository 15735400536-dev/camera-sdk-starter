package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 47081
 * @version 1.0
 * @description 设备硬盘信息
 * @date 2021/1/27
 */
public class SDK_HARDDISK_STATE extends NetSDKLibStructure.SdkStructure {
  /** 个数 */
    public int              dwDiskNum;
  /** 硬盘或分区信息 */
    public NetSDKLibStructure.NET_DEV_DISKSTATE[] stDisks = (NetSDKLibStructure.NET_DEV_DISKSTATE[]) new NetSDKLibStructure.NET_DEV_DISKSTATE().toArray(256);
}

