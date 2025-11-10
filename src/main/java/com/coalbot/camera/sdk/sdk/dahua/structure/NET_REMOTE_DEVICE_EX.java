package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 远程设备信息扩展
*/
public class NET_REMOTE_DEVICE_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 密码
    */
    public byte[]           szPwdEx2 = new byte[128];
    /**
     * 是否使用szPwdEx2密码
    */
    public int              bUsePwdEx2;
    /**
     * IP
    */
    public byte[]           szIpEx = new byte[64];
    /**
     * 是否使用szIpEx IP
    */
    public int              bUseIpEx;
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[952];

    public NET_REMOTE_DEVICE_EX() {
    }
}

