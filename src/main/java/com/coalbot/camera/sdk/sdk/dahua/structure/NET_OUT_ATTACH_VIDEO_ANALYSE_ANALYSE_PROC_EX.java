package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_AttachVideoAnalyseAnalyseProc 输出参数
*/
public class NET_OUT_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;

    public NET_OUT_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX() {
        this.dwSize = this.size();
    }
}

