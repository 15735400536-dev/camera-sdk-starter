package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_GetASGState 接口输入参数
*/
public class NET_IN_ACCESS_GET_ASG_STATE extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_ACCESS_GET_ASG_STATE() {
        this.dwSize = this.size();
    }
}

