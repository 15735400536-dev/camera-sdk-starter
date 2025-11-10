package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetDevCaps NET_MEDIAMANAGER_CAPS 命令入参
*/
public class NET_IN_MEDIAMANAGER_GETCAPS extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 需要获取的能力集类型,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_MEDIA_CAP_TYPE}
    */
    public int              emType;

    public NET_IN_MEDIAMANAGER_GETCAPS() {
        this.dwSize = this.size();
    }
}

