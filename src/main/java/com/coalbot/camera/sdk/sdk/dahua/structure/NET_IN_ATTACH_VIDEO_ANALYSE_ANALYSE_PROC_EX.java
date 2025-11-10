package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.sun.jna.Pointer;
/**
 * CLIENT_AttachVideoAnalyseAnalyseProc 输入参数
*/
public class NET_IN_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 通道号
    */
    public int              nChannelId;
    /**
     * 回调函数,参见回调函数定义 {@link NetSDKLib.fVideoAnalyseAnalyseProcEx}
    */
    public NetSDKLib.fVideoAnalyseAnalyseProcEx cbVideoAnalyseAnalyseProcEx;
    /**
     * 用户信息
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX() {
        this.dwSize = this.size();
    }
}

