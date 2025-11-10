package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 分组人员统计--获取摘要信息 输入参数
*/
public class NET_IN_NUMBERSTATGROUPSUMMARY_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * GroupID
    */
    public byte[]           szGroupID = new byte[64];

    public NET_IN_NUMBERSTATGROUPSUMMARY_INFO() {
        this.dwSize = this.size();
    }
}

