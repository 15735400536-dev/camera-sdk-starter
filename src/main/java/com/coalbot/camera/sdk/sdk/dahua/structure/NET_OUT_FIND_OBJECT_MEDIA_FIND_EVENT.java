package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_FindObjectMediaFindEvent 接口输出参数
*/
public class NET_OUT_FIND_OBJECT_MEDIA_FIND_EVENT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 查找到的记录总数
    */
    public int              nTotalCount;
    /**
     * 目标事件列表最大个数
    */
    public int              nMaxCount;
    /**
     * 目标事件列表实际返回个数
    */
    public int              nRetCount;
    /**
     * 目标事件列表, 内存由用户申请释放, 大小为sizeof(NET_OBJECT_MEDIA_FILE_INFOS)*nMaxCount,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OBJECT_EVENTS_INFO}
    */
    public Pointer          pstuObjectEvents;

    public NET_OUT_FIND_OBJECT_MEDIA_FIND_EVENT() {
        this.dwSize = this.size();
    }
}

