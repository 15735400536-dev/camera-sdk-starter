package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_StartRemoteUploadFile 接口输出参数(上传文件到前端设备)
*/
public class NET_OUT_REMOTE_UPLOAD_FILE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_REMOTE_UPLOAD_FILE() {
        this.dwSize = this.size();
    }
}

