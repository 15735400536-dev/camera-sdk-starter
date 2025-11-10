package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.NativeLong;

/**
 * @author 251823
 * @description
 * @date 2022/06/17 11:14:34
 */
public class RECT extends NetSDKLibStructure.SdkStructure {
    public NativeLong       left;
    public NativeLong       top;
    public NativeLong       right;
    public NativeLong       bottom;

	public RECT() {
	}
}

