package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 接口 CLIENT_GetSubModuleInfo 输入参数
*/
public class NET_IN_GET_SUBMODULES_INFO extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_GET_SUBMODULES_INFO() {
        this.dwSize = this.size();
    }
}

