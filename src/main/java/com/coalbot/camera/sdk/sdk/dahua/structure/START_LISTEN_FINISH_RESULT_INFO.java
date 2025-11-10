package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @Author 251589
 * @Description：
 * @Date 2020/11/27 12:08
 */
public class START_LISTEN_FINISH_RESULT_INFO extends NetSDKLibStructure.SdkStructure {
    public int              dwEventResult;                        // 事件订阅返回结果 NET_NOERROR：成功  非0：失败，值为错误码，详见_EC(x)
    public byte[]           byReserved = new byte[508];
}

