package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

public class NET_OUT_STARTSERACH_DEVICE extends NetSDKLibStructure.SdkStructure {
	/**
	 *  结构体大小
	 */
    public 	int             dwSize;

	public NET_OUT_STARTSERACH_DEVICE()
	    {
	     this.dwSize = this.size();
	    }// 此结构体大小
}

