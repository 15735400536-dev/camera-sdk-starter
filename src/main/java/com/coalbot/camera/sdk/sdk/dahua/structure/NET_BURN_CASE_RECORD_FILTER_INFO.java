package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 刻录案件录像专用查询条件
*/
public class NET_BURN_CASE_RECORD_FILTER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 事件类型条件
    */
    public byte[]           szCaseID = new byte[32];
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[1024];

    public NET_BURN_CASE_RECORD_FILTER_INFO() {
    }
}

