package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 烟火检测查询结果 ( CLIENT_FindFileEx + DH_FILE_QUERY_SMOKE_FIRE )
*/
public class MEDIAFILE_SMOKE_FIRE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 开始时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuBeginTime = new NET_TIME();
    /**
     * 结束时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NET_TIME stuEndTime = new NET_TIME();
    /**
     * 文件路径
    */
    public byte[]           szGlobalSceneFilePath = new byte[260];
    /**
     * 文件长度
    */
    public int              nGlobalSceneFileSize;
    /**
     * 规则
    */
    public byte[]           szRule = new byte[64];
    /**
     * 包围盒，8192坐标系,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RECT}
    */
    public NET_RECT         stuBoundingBox = new NET_RECT();
    /**
     * 小图信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_IMAGE_INFO}
    */
    public NET_EVENT_IMAGE_INFO stuImageInfo = new NET_EVENT_IMAGE_INFO();
    /**
     * 文件类型，1:jpg, 2:dav，默认:jpg
    */
    public int              nFileType;
    /**
     * 为TRUE表示仅stuStartTimeRealUTC和stuEndTimeRealUTC有效(仅使用stuStartTimeRealUTC和stuEndTimeRealUTC), 为FALSE表示仅stuBeginTime和stuEndTime有效(仅使用stuBeginTime和stuEndTime)
    */
    public int              bRealUTC;
    /**
     * UTC开始时间(标准UTC时间), 与stuEndTimeRealUTC配对使用,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuStartTimeRealUTC = new NET_TIME();
    /**
     * UTC结束时间(标准UTC时间), 与stuStartTimeRealUTC配对使用,参见结构体定义 {@link NetSDKLibStructure.NET_TIME}
    */
    public NetSDKLibStructure.NET_TIME stuEndTimeRealUTC = new NET_TIME();

    public MEDIAFILE_SMOKE_FIRE_INFO() {
        this.dwSize = this.size();
    }
}

