package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_QueryDevInfo , NET_QUERY_DEV_STORAGE_INFOS接口输出参数
 * @author 29779
 */
public class NET_OUT_STORAGE_DEV_INFOS extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;
    public int              nDevInfosNum;                         // 获取到设备的存储模块信息列表
    public NET_STORAGE_DEVICE[] stuStoregeDevInfos = new NET_STORAGE_DEVICE[128]; // 设备信息列表,DH_STORAGE_DEVICE的dwsize需赋值

	public NET_OUT_STORAGE_DEV_INFOS() {				
		this.dwSize = this.size();		
		for (int i = 0; i < stuStoregeDevInfos.length; i++) {
			stuStoregeDevInfos[i] = new NET_STORAGE_DEVICE();
		}
	}
}

