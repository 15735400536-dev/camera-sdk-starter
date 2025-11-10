package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_DevStorageFormatPartitionAsyn 接口输出参数
*/
public class NET_OUT_DEVSTORAGE_FORMAT_PARTITION_ASYN extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_DEVSTORAGE_FORMAT_PARTITION_ASYN() {
        this.dwSize = this.size();
    }
}

