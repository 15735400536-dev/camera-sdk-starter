package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * 事件类型 EVENT_IVS_POWERLINE_FOREIGN_DETECITON 对应数据
*/
public class DEV_EVENT_POWERLINE_FOREIGN_DETEC_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 事件动作 1-开始. 2-结束
    */
    public int              nAction;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 时间戳(单位是毫秒)
    */
    public int              PTS;
    /**
     * 事件发生的时间,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX UTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 智能事件所属大类,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_CLASS_TYPE}
    */
    public int              emClassType;
    /**
     * 智能事件规则编号, 缺省为0
    */
    public int              nRuleID;
    /**
     * 事件触发次数
    */
    public int              nCount;
    /**
     * 事件触发的预置点号, 缺省为0
    */
    public int              nPresetID;
    /**
     * 规则检测区域顶点数
    */
    public int              nDetectRegionNum;
    /**
     * 规则检测区域,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_POINT}
    */
    public NetSDKLibStructure.NET_POINT[] stuDetectRegion = new NetSDKLibStructure.NET_POINT[20];
    /**
     * 异物物体实际个数
    */
    public int              nForeignMatterNum;
    /**
     * 异物物体信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.FOREIGN_MATTER_OBJECT}
    */
    public Pointer          pForeignMatter;
    /**
     * 字节对齐
    */
    public Pointer          pReserved;
    /**
     * 保留
    */
    public byte[]           byReserved = new byte[1024];

    public DEV_EVENT_POWERLINE_FOREIGN_DETEC_INFO() {
        for(int i = 0; i < stuDetectRegion.length; i++){
            stuDetectRegion[i] = new NetSDKLibStructure.NET_POINT();
        }
    }
}

