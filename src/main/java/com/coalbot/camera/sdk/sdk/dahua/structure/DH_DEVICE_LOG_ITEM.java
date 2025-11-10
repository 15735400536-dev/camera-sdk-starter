package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 日志信息,对应接口CLIENT_QueryDeviceLog接口
 * @author 47081
 */
public class DH_DEVICE_LOG_ITEM extends NetSDKLibStructure.SdkStructure {
    /**
     * 日志类型
     */
    public int              nLogType;
    /**
     * 日期
     */
    public DHDEVTIME        stuOperateTime;
    /**
     * 操作者
     */
    public byte[]           szOperator = new byte[16];
    public byte[]           bReserved = new byte[3];
    /**
     * union结构类型对应NET_STU_LOG结构体,
     * 0:szLogContext；
     * 1:stuOldLog,在NET_STU_LOG结构体内
     */
    public byte             bUnionType;                           
    //public NET_STU_LOG         stuLog;
   /**
    * 日志备注信息
    */
    public byte[]           szLogContext = new byte[64];
    public byte[]           reserved = new byte[16];
}

