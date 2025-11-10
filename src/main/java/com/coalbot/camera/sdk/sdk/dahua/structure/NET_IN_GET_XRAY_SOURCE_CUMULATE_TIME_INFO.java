package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetXRaySourceCumulateTime 接口输入参数
*/
public class NET_IN_GET_XRAY_SOURCE_CUMULATE_TIME_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 通道号
    */
    public int              nChannel;

    public NET_IN_GET_XRAY_SOURCE_CUMULATE_TIME_INFO() {
        this.dwSize = this.size();
    }
}

