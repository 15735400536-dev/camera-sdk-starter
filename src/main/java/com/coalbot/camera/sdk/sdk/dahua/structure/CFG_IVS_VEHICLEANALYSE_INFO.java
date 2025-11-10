package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 事件类型EVENT_IVS_VEHICLEANALYSE(车辆特征检测分析)对应规则配置
*/
public class CFG_IVS_VEHICLEANALYSE_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 规则名称,不同规则不能重名
    */
    public byte[]           szRuleName = new byte[128];
    /**
     * 规则使能
    */
    public byte             bRuleEnable;
    /**
     * 保留字段
    */
    public byte[]           bReserved = new byte[3];
    /**
     * 相应物体类型个数
    */
    public int              nObjectTypeNum;
    /**
     * 相应物体类型列表
    */
    public BYTE_ARRAY_128[] szObjectTypes = new BYTE_ARRAY_128[16];
    /**
     * 云台预置点编号	0~65535
    */
    public int              nPtzPresetId;
    /**
     * 报警联动,参见结构体定义 {@link NetSDKLibStructure.CFG_ALARM_MSG_HANDLE}
    */
    public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler = new NetSDKLibStructure.CFG_ALARM_MSG_HANDLE();
    /**
     * 事件响应时间段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.CFG_TIME_SECTION}
    */
    public CFG_TIME_SECTION_ARRAY_10[] stuTimeSection = new CFG_TIME_SECTION_ARRAY_10[7];
    /**
     * 警戒区顶点数
    */
    public int              nDetectRegionPoint;
    /**
     * 警戒区,参见结构体定义 {@link NetSDKLibStructure.CFG_POLYGON}
    */
    public NetSDKLibStructure.CFG_POLYGON[] stuDetectRegion = new NetSDKLibStructure.CFG_POLYGON[20];
    /**
     * 规则特定的尺寸过滤器是否有效
    */
    public int              bSizeFileter;
    /**
     * 规则特定的尺寸过滤器,参见结构体定义 {@link NetSDKLibStructure.CFG_SIZEFILTER_INFO}
    */
    public NetSDKLibStructure.CFG_SIZEFILTER_INFO stuSizeFileter = new NetSDKLibStructure.CFG_SIZEFILTER_INFO();

    public CFG_IVS_VEHICLEANALYSE_INFO() {
        for(int i = 0; i < szObjectTypes.length; i++){
            szObjectTypes[i] = new BYTE_ARRAY_128();
        }
        for(int i = 0; i < stuTimeSection.length; i++){
            stuTimeSection[i] = new CFG_TIME_SECTION_ARRAY_10();
        }
        for(int i = 0; i < stuDetectRegion.length; i++){
            stuDetectRegion[i] = new NetSDKLibStructure.CFG_POLYGON();
        }
    }
}

