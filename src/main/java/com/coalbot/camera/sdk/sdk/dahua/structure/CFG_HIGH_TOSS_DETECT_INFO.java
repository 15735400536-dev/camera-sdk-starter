package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

import static com.coalbot.camera.sdk.sdk.dahua.constant.SDKStructureFieldLenth.*;

/**
 * @author 47081
 * @version 1.0
 * @description 事件类型 {@link NetSDKLibStructure#EVENT_IVS_HIGH_TOSS_DETECT} (高空抛物检测)对应的规则配置
 * @date 2020/8/24
 */
public class CFG_HIGH_TOSS_DETECT_INFO extends NetSDKLibStructure.SdkStructure {
  /** 规则名称,不同规则不能重名 */
    public byte[]           szRuleName = new byte[NetSDKLibStructure.MAX_NAME_LEN];
  /** 规则使能 */
    public int              bRuleEnable;
  /** 相应物体类型个数 */
    public int              nObjectTypeNum;
  /** 相应物体类型列表 */
    public byte[]           szObjectTypes = new byte[MAX_OBJECT_LIST_SIZE * NetSDKLibStructure.MAX_NAME_LEN];
  /** 云台预置点编号 0~65535 */
    public int              nPtzPresetId;
  /** 报警联动 */
    public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler;
  /** 事件响应时间段 */
    public NetSDKLibStructure.CFG_TIME_SECTION[] stuTimeSection = (NetSDKLibStructure.CFG_TIME_SECTION[])new NetSDKLibStructure.CFG_TIME_SECTION().toArray(WEEK_DAY_NUM * MAX_REC_TSECT_EX);
  /** 检测区顶点数 */
    public int              nDetectRegionPoint;
  /** 检测区 */
    public NetSDKLibStructure.CFG_POLYGON[] stuDetectRegion = (NetSDKLibStructure.CFG_POLYGON[]) new NetSDKLibStructure.CFG_POLYGON().toArray(20);
  /** 规则特定的尺寸过滤器，为提高规则判断精度 */
    public NetSDKLibStructure.CFG_SIZEFILTER_INFO stuSizeFilter;
  /** 尺寸过滤器使能 */
    public int              bSizeFilterEnable;
  /** 灵敏度,越高越容易检测, 取值1-10，默认值5 */
    public int              nSensitivity;
  /** 画轨迹使能 */
    public int              bTrackEnable;
  /** 轨迹点数量，TrackEnable为true时有效（2~10） */
    public int              nTrackPointNum;
  /** 轨迹线粗细等级，TrackEnable为true时有效（1~10） */
    public int              nTrackLineLevel;
  /** 轨迹线颜色 */
    public int              emColor;
  /** 保留字节 */
    public byte[]           byReserved = new byte[3592];
}

