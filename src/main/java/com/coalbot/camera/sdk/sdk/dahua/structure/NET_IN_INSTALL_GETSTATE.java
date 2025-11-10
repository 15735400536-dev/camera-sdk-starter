package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_UpgraderInstall接口的 EM_UPGRADE_INSTALL_GETSTATE命令入参
*/
public class NET_IN_INSTALL_GETSTATE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_IN_INSTALL_GETSTATE() {
        this.dwSize = this.size();
    }
}

