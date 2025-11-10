package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 合成通道各个窗口的信息
*/
public class CFG_COMPOSE_CHANNEL_WINDOW extends NetSDKLibStructure.SdkStructure
{
    /**
     * 视频通道号
    */
    public int              nChannel;
    /**
     * 窗口在屏幕中的位置，使用8192坐标系，当 Split等于PIPn模式时，该字段为必填字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.CFG_RECT}
    */
    public NetSDKLibStructure.CFG_RECT stuPosition = new NetSDKLibStructure.CFG_RECT();
    /**
     * 窗口类型。当合成通道为画中画模式时，该字段用来表明画面的z轴顺序。主画面取值为Main%d，辅画面取值为Extra%d，%d为数字，从1开始
    */
    public byte[]           szType = new byte[16];
    /**
     * 码流类型, -1:未知, 0:主码流, 1:辅码流
    */
    public int              nStreamType;
    /**
     * 预留字节
    */
    public byte[]           szReserved = new byte[1024];

    public CFG_COMPOSE_CHANNEL_WINDOW() {
    }
}

