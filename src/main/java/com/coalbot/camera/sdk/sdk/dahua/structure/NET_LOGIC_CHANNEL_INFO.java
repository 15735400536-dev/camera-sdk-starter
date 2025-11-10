package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_LOGIC_CHANNEL;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 通道信息
 *
 * @author ： 47040
 * @since ： Created in 2020/9/18 9:42
 */
public class NET_LOGIC_CHANNEL_INFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 教室ID号
     */
    public int              nRoomID;
    /**
     * 逻辑通道号 {@link NET_EM_LOGIC_CHANNEL}
     */
    public int              emLogicChannel;
    /**
     * 保留字节
     */
    public byte[]           byReserved = new byte[32];
}

