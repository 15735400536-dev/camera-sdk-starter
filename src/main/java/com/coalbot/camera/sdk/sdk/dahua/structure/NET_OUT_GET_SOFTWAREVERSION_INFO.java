package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_GetSoftwareVersion 出参
*/
public class NET_OUT_GET_SOFTWAREVERSION_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 软件版本
    */
    public byte[]           szVersion = new byte[64];
    /**
     * 日期,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuBuildDate = new NET_TIME();
    /**
     * web软件信息
    */
    public byte[]           szWebVersion = new byte[16];
    /**
     * 安全基线版本
    */
    public byte[]           szSecurityVersion = new byte[64];
    /**
     * 返回的外设数量
    */
    public int              nPeripheralNum;
    /**
     * 设备的外设软件版本,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_PERIPHERAL_VERSIONS}
    */
    public NET_PERIPHERAL_VERSIONS[] stuPeripheralVersions = new NET_PERIPHERAL_VERSIONS[32];
    /**
     * 算法训练对外代号
    */
    public byte[]           szAlgorithmTrainingVersion = new byte[64];
    /**
     * 设备mobileWeb上云匹配版本号
    */
    public byte[]           szMobileWebVersion = new byte[128];

    public NET_OUT_GET_SOFTWAREVERSION_INFO() {
        this.dwSize = this.size();
        for(int i = 0; i < stuPeripheralVersions.length; i++){
            stuPeripheralVersions[i] = new NET_PERIPHERAL_VERSIONS();
        }
    }
}

