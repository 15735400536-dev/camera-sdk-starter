package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_VOLUME_TYPE;

/**
 * CLIENT_QueryDevInfo , NET_QUERY_DEV_STORAGE_INFOS接口输入参数
 * @author 29779
 */
public class NET_IN_STORAGE_DEV_INFOS extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;
	/**
	 * 要获取的卷类型
	 * {@link NET_VOLUME_TYPE }
	 */
    public int              emVolumeType;

	public NET_IN_STORAGE_DEV_INFOS() {
		this.dwSize = this.size();
	}
}

