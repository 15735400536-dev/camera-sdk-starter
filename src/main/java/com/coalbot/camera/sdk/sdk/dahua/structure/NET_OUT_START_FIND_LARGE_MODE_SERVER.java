package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * CLIENT_StartFindLargeModeServer 接口输出参数
*/
public class NET_OUT_START_FIND_LARGE_MODE_SERVER extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 取到的查询令牌
    */
    public int              nToken;

    public NET_OUT_START_FIND_LARGE_MODE_SERVER() {
        this.dwSize = this.size();
    }
}

