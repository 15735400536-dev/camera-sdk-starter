package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_RebootDevice 接口输出参数
*/
public class NET_OUT_REBOOT_DEVICE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_REBOOT_DEVICE() {
        this.dwSize = this.size();
    }
}

