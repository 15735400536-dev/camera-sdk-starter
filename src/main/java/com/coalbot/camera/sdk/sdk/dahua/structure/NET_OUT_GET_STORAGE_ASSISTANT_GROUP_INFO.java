package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_GetStorageAssistantGroupInfos 接口输出参数
*/
public class NET_OUT_GET_STORAGE_ASSISTANT_GROUP_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 盘组的个数
    */
    public int              nGroupNums;
    /**
     * 盘组信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_GROUP_INFO}
    */
    public NET_GROUP_INFO[] stuGroupInfo = new NET_GROUP_INFO[128];

    public NET_OUT_GET_STORAGE_ASSISTANT_GROUP_INFO() {
        this.dwSize = this.size();
        for(int i = 0; i < stuGroupInfo.length; i++){
            stuGroupInfo[i] = new NET_GROUP_INFO();
        }
    }
}

