package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * className：NET_USER_PICTURE_DATA
 * description：
 * author：251589
 * createTime：2020/12/22 21:56
 *
 * @version v1.0
 */
public class NET_USER_PICTURE_DATA extends NetSDKLibStructure.SdkStructure {
    public String           pszPictureData;                       // 图片数据，最大 200K
    public int              nPictureLen;                          // pszPictureData 长度
    public byte[]           byReserved = new byte[1020];          // 预留字段
}

