package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.fSearchDevicesCBEx;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;

public class NET_IN_STARTSERACH_DEVICE extends NetSDKLibStructure.SdkStructure {
	   /**
	    * 结构体大小
	    */
    public int              dwSize;
	   /**
	    * 发起搜索的本地IP
	    */
    public byte[]           szLocalIp = new byte[64];
	   /**
	    * 设备信息回调函数
	    */
    public fSearchDevicesCBEx cbSearchDevices;
	   /**
	    * 用户自定义数据
	    */
    public Pointer          pUserData;
	   /**
	    * 下发搜索类型(参考EM_SEND_SEARCH_TYPE)
	    */
    public int              emSendType;
    /**
     * TTLV设备信息回调函数,参见回调函数定义 {@link NetSDKLib.fSearchDevicesCBTTLV}
    */
    public NetSDKLib.fSearchDevicesCBTTLV cbSearchDevicesTTLV;
    /**
     * 4代设备信息回调函数,参见回调函数定义 {@link NetSDKLib.fSearchDevicesCB4th}
    */
    public NetSDKLib.fSearchDevicesCB4th cbSearchDevices4th;

	   public NET_IN_STARTSERACH_DEVICE()
	    {
	     this.dwSize = this.size();
	    }// 此结构体大小
}

