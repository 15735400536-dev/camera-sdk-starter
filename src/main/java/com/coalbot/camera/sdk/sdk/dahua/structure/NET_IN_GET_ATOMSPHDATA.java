package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_GetAtomsphData接口入参
*/
public class NET_IN_GET_ATOMSPHDATA extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;

    public NET_IN_GET_ATOMSPHDATA() {
        this.dwSize = this.size();
    }
}

