 package com.coalbot.camera.sdk.sdk.dahua.structure;


 import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

 public class NET_CFG_VSP_LXSJ_NOPLATE extends NetSDKLibStructure.SdkStructure {
/** 使能*/
    public			int            bEnable;
/** 无牌车内容*/
    public			byte[]         szText = new byte[128];
/** 预留*/
    public			byte[]         byReserved = new byte[380];
}

