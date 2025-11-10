package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_GetCollectDevicesInfoByTsfId 接口输入参数
*/
public class NET_IN_GET_COLLECT_DEVICES_INFO_BY_TSFID_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 执法人id
    */
    public byte[]           szTipStaffId = new byte[32];

    public NET_IN_GET_COLLECT_DEVICES_INFO_BY_TSFID_INFO() {
        this.dwSize = this.size();
    }
}

