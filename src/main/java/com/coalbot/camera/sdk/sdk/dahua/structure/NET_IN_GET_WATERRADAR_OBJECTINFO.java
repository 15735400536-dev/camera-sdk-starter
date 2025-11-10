package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetWaterRadarObjectInfo 输入参数
*/
public class NET_IN_GET_WATERRADAR_OBJECTINFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_GET_WATERRADAR_OBJECTINFO() {
        this.dwSize = this.size();
    }
}

