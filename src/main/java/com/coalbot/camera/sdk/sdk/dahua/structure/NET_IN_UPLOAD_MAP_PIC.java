package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_UploadMapPic 接口输入参数
*/
public class NET_IN_UPLOAD_MAP_PIC extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 图片描述信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MAP_PIC_INFO}
    */
    public NET_MAP_PIC_INFO stuPicInfo = new NET_MAP_PIC_INFO();
    /**
     * 上传的图片二进制数据长度
    */
    public int              nBinaryDataLen;
    /**
     * 上传的图片二进制数据，由用户申请释放内存，内存大小为nBinaryDataLen
    */
    public Pointer          pszBinaryData;

    public NET_IN_UPLOAD_MAP_PIC() {
        this.dwSize = this.size();
    }
}

