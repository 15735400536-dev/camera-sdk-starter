package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_MatrixGetCameraAllByGroup 接口输入参数
*/
public class NET_IN_GET_CAMERA_ALL_BY_GROUP extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 输入通道类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_CAMERA_CHANNEL_TYPE}
    */
    public int              emChannelType;

    public NET_IN_GET_CAMERA_ALL_BY_GROUP() {
        this.dwSize = this.size();
    }
}

