package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_UpgraderInstall接口的 EM_UPGRADE_INSTALL_CANCEL命令出参
*/
public class NET_OUT_INSTALL_CANCEL extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_INSTALL_CANCEL() {
        this.dwSize = this.size();
    }
}

