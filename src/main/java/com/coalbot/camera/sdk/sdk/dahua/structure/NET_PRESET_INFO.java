package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * 预置点主动巡视具体信息
*/
public class NET_PRESET_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 预置点配置列表，内存由用户申请释放,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_PRESET_CONFIG_LIST_INFO}
    */
    public Pointer          pstList;
    /**
     * 预置点配置内存申请个数
    */
    public int              nMaxListNum;
    /**
     * 预置点配置有效返回个数，获取配置时作为出参
    */
    public int              nRetListNum;
    /**
     * 预置点到位超时时间(单位：秒)，取值范围5-300
    */
    public int              nArriveTimeout;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[252-NetSDKLibStructure.POINTERSIZE];

    public NET_PRESET_INFO() {
    }
}

