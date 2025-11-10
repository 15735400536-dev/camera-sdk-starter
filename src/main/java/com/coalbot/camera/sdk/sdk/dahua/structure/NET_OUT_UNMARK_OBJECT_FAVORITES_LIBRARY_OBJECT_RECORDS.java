package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_UnmarkObjectFavoritesLibraryObjectRecords 接口输出参数
*/
public class NET_OUT_UNMARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_UNMARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS() {
        this.dwSize = this.size();
    }
}

