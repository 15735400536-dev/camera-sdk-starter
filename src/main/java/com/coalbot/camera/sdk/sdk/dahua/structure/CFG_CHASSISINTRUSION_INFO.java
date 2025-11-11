package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @description 机箱入侵报警(防拆报警)配置
 * @date 2023/03/15 21:57:49
 */
public class CFG_CHASSISINTRUSION_INFO extends NetSDKLibStructure.SdkStructure {
	/**
	 * 使能开关
	 */
    public int              bEnable;
	/**
	 * 报警联动
	 */
    public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler = new NetSDKLibStructure.CFG_ALARM_MSG_HANDLE();

	public CFG_CHASSISINTRUSION_INFO() {
	}
}

