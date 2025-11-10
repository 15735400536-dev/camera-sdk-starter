package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetLaserDistMeasureCaps 接口输入参数
*/
public class NET_IN_GET_LASER_DIST_MEASURE_CAPS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 通道号,从0开始
    */
    public int              nChannel;

    public NET_IN_GET_LASER_DIST_MEASURE_CAPS_INFO() {
        this.dwSize = this.size();
    }
}

