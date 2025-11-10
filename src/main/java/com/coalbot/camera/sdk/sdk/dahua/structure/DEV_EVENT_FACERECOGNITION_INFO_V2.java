package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * 事件类型EVENT_IVS_FACERECOGNITION(目标识别)对应的数据块描述信息
*/
public class DEV_EVENT_FACERECOGNITION_INFO_V2 extends NetSDKLibStructure.SdkStructure
{
    /**
     * 通道号
    */
    public int              nChannelID;
    /**
     * 事件名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 事件ID
    */
    public int              nEventID;
    /**
     * 事件发生的时间,参见结构体定义 {@link NetSDKLibStructure.NET_TIME_EX}
    */
    public NetSDKLibStructure.NET_TIME_EX UTC = new NetSDKLibStructure.NET_TIME_EX();
    /**
     * 当前人脸匹配到的候选对象数量
    */
    public int              nCandidateNum;
    /**
     * 当前人脸匹配到的候选对象信息,参见结构体定义 {@link NetSDKLibStructure.CANDIDATE_INFOEX}
    */
    public Pointer          pstuCandidates;
    /**
     * 检测到的物体,参见结构体定义 {@link NetSDKLibStructure.NET_MSG_OBJECT}
    */
    public NetSDKLibStructure.NET_MSG_OBJECT stuObject = new NetSDKLibStructure.NET_MSG_OBJECT();
    /**
     * 保留字节
    */
    public byte[]           szReserved = new byte[10240];

    public DEV_EVENT_FACERECOGNITION_INFO_V2() {
    }
}

