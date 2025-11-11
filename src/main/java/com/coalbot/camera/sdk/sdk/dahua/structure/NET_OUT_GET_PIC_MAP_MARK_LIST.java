package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_GetPicMapMarkList 接口输出参数
*/
public class NET_OUT_GET_PIC_MAP_MARK_LIST extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 地图已标记信息个数
    */
    public int              nMarkInfosCount;
    /**
     * 地图已标记信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MAP_PIC_MARK_INFOS}
    */
    public NET_MAP_PIC_MARK_INFOS[] stuMarkInfos = new NET_MAP_PIC_MARK_INFOS[1024];

    public NET_OUT_GET_PIC_MAP_MARK_LIST() {
        this.dwSize = this.size();
        for(int i = 0; i < stuMarkInfos.length; i++){
            stuMarkInfos[i] = new NET_MAP_PIC_MARK_INFOS();
        }
    }
}

