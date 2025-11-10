package com.coalbot.camera.sdk.sdk.dahua.structure;



import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.NativeLong;

/** 
* @author 251823
* @description 区域；各边距按整长8192的比例
* @date 2022/07/21 15:15:27
*/
public class DH_RECT_REGION extends NetSDKLibStructure.SdkStructure {
    public NativeLong       left;
    public NativeLong       top;
    public NativeLong       right;
    public NativeLong       bottom;
}

