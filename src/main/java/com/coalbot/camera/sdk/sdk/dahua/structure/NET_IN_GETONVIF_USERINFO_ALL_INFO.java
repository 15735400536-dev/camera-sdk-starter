package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 获取所有 Onvif 用户信息，CLIENT_GetOnvifUserInfoAll 入参
*/
public class NET_IN_GETONVIF_USERINFO_ALL_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_GETONVIF_USERINFO_ALL_INFO() {
        this.dwSize = this.size();
    }
}

