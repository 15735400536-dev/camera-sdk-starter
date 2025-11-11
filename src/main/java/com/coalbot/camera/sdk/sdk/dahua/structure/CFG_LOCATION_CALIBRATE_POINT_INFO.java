package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 标定点信息 
 * @date 2021/02/02
 */
public class CFG_LOCATION_CALIBRATE_POINT_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 标定点ID
	 */
    public int              nID;
	/**
	 * 标定点名称
	 */
    public byte[]           szName = new byte[NetSDKLibStructure.CFG_COMMON_STRING_128];
	/**
	 * 使能
	 */
    public int              bEnable;
	/**
	 * 经度
	 */
    public int              nLongitude;
	/**
	 * 纬度
	 */
    public int              nLatitude;
	/**
	 * 海拔
	 */
    public double           fAltitude;
	/**
	 * 标定点基本信息
	 */
    public CFG_CALIBRATE_INFO stuCalibrateInfo;
	/**
	 * 字节对齐 
	 */
    public byte[]           byReserved1 = new byte[4];
}

