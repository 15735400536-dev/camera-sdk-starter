package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 工作服信息
*/
public class MEDIAFILE_CLOTHES_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 工作服颜色,参见枚举定义 {@link NetSDKLibStructure.EM_CLOTHES_COLOR}
    */
    public int              emColor;
    /**
     * 工作服状态,参见枚举定义 {@link NetSDKLibStructure.EM_WORKCLOTHES_STATE}
    */
    public int              emState;
    /**
     * 预留字段
    */
    public byte[]           byReserved = new byte[512];

    public MEDIAFILE_CLOTHES_INFO() {
    }
}

