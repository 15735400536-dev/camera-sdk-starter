package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetVideoStatServerSummary 接口输出参数
*/
public class NET_OUT_GET_VIDEO_STAT_SERVER_SUMMARY_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 人数统计摘要信息,参见结构体定义 {@link NetSDKLibStructure.NET_VIDEOSTAT_SUMMARY}
    */
    public NetSDKLibStructure.NET_VIDEOSTAT_SUMMARY stuSummaryInfo = new NetSDKLibStructure.NET_VIDEOSTAT_SUMMARY();

    public NET_OUT_GET_VIDEO_STAT_SERVER_SUMMARY_INFO() {
        this.dwSize = this.size();
    }
}

