package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 获取布防模式 输入参数。此时CLIENT_GetAlarmRegionInfo的emType参数为NET_EM_GET_ALARMREGION_INFO_ARMMODE
 * @date 2021/11/08
 */
public class NET_IN_GET_ALARMMODE extends NetSDKLibStructure.SdkStructure {
	/**
     *  结构体大小
     */
    public int              dwSize;

    public NET_IN_GET_ALARMMODE(){
        this.dwSize = this.size();
    }		
}

