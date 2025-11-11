package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

/**
 * @author 260611
 * @description 订阅可视域输入参数
 * @origin autoTool
 * @date 2023/05/30 10:04:52
 */
public class NET_IN_VIEW_RANGE_STATE extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;
	/**
	 * 云台通道
	 */
    public int              nChannel;
	/**
	 * 状态回调函数
	 */
    public NetSDKLib.fViewRangeStateCallBack cbViewRange;
	/**
	 * 用户数据
	 */
    public Pointer          dwUser;

	public NET_IN_VIEW_RANGE_STATE() {
		this.dwSize = this.size();
	}
}

