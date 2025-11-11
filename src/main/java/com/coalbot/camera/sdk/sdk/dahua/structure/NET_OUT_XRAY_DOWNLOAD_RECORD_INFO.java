package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetXRayDownloadRecord 接口输出参数
*/
public class NET_OUT_XRAY_DOWNLOAD_RECORD_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 文件信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_XRAY_DOWNLOAD_RECORD_FILEINFO}
    */
    public NET_XRAY_DOWNLOAD_RECORD_FILEINFO stuFileInfo = new NET_XRAY_DOWNLOAD_RECORD_FILEINFO();

    public NET_OUT_XRAY_DOWNLOAD_RECORD_INFO() {
        this.dwSize = this.size();
    }
}

