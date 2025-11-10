package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_AttachResultOfFindHistoryByPic接口输出参数
*/
public class NET_OUT_ATTACH_RESULT_FINDHISTORY_BYPIC extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;

    public NET_OUT_ATTACH_RESULT_FINDHISTORY_BYPIC() {
        this.dwSize = this.size();
    }
}

