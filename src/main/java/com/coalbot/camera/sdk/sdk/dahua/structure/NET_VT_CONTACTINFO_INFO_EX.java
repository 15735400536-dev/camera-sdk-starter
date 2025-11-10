package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;


/**
 * @author 421657
 * @description 通讯录格式扩展
 * @origin autoTool
 * @date 2023/10/26 09:34:52
 */
public class NET_VT_CONTACTINFO_INFO_EX extends NetSDKLibStructure.SdkStructure {
    /**
     * / 是否使用设备所处位置扩展字段
     */
    public int              bUseNickNameEx;
    /**
     * / 设备所处位置扩展
     */
    public byte[]           szNickNameEx = new byte[256];
    /**
     * / 保留字段
     */
    public byte[]           szResvered = new byte[2044];

    public NET_VT_CONTACTINFO_INFO_EX() {
    }
}

