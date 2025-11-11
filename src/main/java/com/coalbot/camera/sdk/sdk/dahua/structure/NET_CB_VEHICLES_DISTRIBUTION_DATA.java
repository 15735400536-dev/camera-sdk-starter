package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/** 
* @author 291189
* @description  CLIENT_AttachVehiclesDistributionData回调信息 
* @origin autoTool
* @date 2023/07/03 14:46:48
*/
public class NET_CB_VEHICLES_DISTRIBUTION_DATA extends NetSDKLibStructure.SdkStructure {
/** 
服务器订阅ID号
*/
    public			int            nSID;
/** 
通道号
*/
    public			int            nChannel;
/** 
统计区数据个数
*/
    public			int            nVehiclesDataNums;
/** 
统计区数据
*/
    public			NET_VEHICLES_DISTRIBUTION_DATA[] stuVehiclesData = new NET_VEHICLES_DISTRIBUTION_DATA[16];

public			NET_CB_VEHICLES_DISTRIBUTION_DATA(){
		for(int i=0;i<stuVehiclesData.length;i++){
			stuVehiclesData[i]=new NET_VEHICLES_DISTRIBUTION_DATA();
			}
}
}

