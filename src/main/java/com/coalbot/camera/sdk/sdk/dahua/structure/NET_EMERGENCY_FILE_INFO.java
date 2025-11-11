package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * 文件信息
*/
public class NET_EMERGENCY_FILE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 原始文件创建时文件的唯一标示
    */
    public byte[]           szUniqueID = new byte[128];
    /**
     * 原始文件创建设备的序列号ID
    */
    public byte[]           szOriginalDeviceID = new byte[128];
    /**
     * 文件的真实类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_MOBILE_ENFORCE_FORMAT}
    */
    public int              emFormat;
    /**
     * 文件后缀类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_MOBILE_ENFORCE_FILE_TYPE}
    */
    public int              emFileType;
    /**
     * 文件名称
    */
    public byte[]           szFileName = new byte[260];
    /**
     * 文件路径。
    */
    public byte[]           szFilePath = new byte[260];
    /**
     * 文件开始时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stStartTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 文件结束时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stEndTime = new NetSDKLibStructure.NET_TIME();
    /**
     * 文件长度
    */
    public int              nLength;
    /**
     * UTC开始时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStartTimeRealUTC = new NetSDKLibStructure.NET_TIME();
    /**
     * UTC结束时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuEndTimeRealUTC = new NetSDKLibStructure.NET_TIME();
    /**
     * 录像标志数组,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_MOBILE_ENFORCE_FLAGS}
    */
    public int[]            emFlags = new int[64];
    /**
     * 文件信息扩展字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EMERGENCY_FILE_INFO_EXTERN}
    */
    public Pointer          pstuFileInfoExtern;
    /**
     * 文件备注信息
    */
    public byte[]           szRemark = new byte[64];
    /**
     * 保留字节
    */
    public byte[]           byReserved = new byte[128-NetSDKLibStructure.POINTERSIZE];

    public NET_EMERGENCY_FILE_INFO() {
    }
}

