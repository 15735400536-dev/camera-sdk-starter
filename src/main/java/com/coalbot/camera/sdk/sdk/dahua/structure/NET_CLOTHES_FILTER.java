package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 工作服过滤信息
*/
public class NET_CLOTHES_FILTER extends NetSDKLibStructure.SdkStructure
{
    /**
     * 衣服颜色,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_CLOTHES_COLOR}
    */
    public int[]            emColors = new int[16];
    /**
     * emColors 数量
    */
    public int              nColorsCount;
    /**
     * 衣服状态,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_WORKCLOTHES_STATE}
    */
    public int[]            emStates = new int[8];
    /**
     * emStates 个数
    */
    public int              nStatesCount;
    /**
     * 预留字段
    */
    public byte[]           byReserved = new byte[512];

    public NET_CLOTHES_FILTER() {
    }
}

