package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_AttachRadarAISInfo接口入参
*/
public class NET_IN_ATTACH_RADAR_AIS_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 结构体大小
    */
    public int              dwSize;
    /**
     * 雷达AIS信息回调,参见回调函数定义 {@link NetSDKLib.fRadarAISInfoCallBack}
    */
    public NetSDKLib.fRadarAISInfoCallBack cbAISInfo;
    /**
     * 用户数据
    */
    public Pointer          dwUser;

    public NET_IN_ATTACH_RADAR_AIS_INFO() {
        this.dwSize = this.size();
    }
}

