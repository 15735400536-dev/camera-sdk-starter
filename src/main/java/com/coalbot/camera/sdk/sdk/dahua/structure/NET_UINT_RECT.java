package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * 区域；各边距按整长8192的比例
*/
public class NET_UINT_RECT extends NetSDKLibStructure.SdkStructure
{
    public int              nLeft;
    public int              nTop;
    public int              nRight;
    public int              nBottom;

    public NET_UINT_RECT() {
    }
}

