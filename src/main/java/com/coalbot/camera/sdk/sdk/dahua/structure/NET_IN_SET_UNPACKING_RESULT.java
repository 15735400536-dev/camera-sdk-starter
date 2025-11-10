package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_SetUnpackingResult 接口输入参数
*/
public class NET_IN_SET_UNPACKING_RESULT extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 开包结果,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_XRARY_UNPACKING_INFO}
    */
    public NET_XRARY_UNPACKING_INFO stuUnpackingResult = new NET_XRARY_UNPACKING_INFO();

    public NET_IN_SET_UNPACKING_RESULT() {
        this.dwSize = this.size();
    }
}

