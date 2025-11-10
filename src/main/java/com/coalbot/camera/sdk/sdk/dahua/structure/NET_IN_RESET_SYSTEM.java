package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 恢复出厂设置入参
*/
public class NET_IN_RESET_SYSTEM extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_RESET_SYSTEM() {
        this.dwSize = this.size();
    }
}

