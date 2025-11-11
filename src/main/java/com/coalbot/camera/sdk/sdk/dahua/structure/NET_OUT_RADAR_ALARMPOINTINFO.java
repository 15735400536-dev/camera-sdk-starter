package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 260611
 * @description 订阅雷达的报警点信息出参(对应接口 CLIENT_AttachRadarAlarmPointInfo)
 * @date 2022/08/04 10:13:33
 */
public class NET_OUT_RADAR_ALARMPOINTINFO extends NetSDKLibStructure.SdkStructure {
    /**
     * 结构体大小
     */
    public int              dwSize;

    public NET_OUT_RADAR_ALARMPOINTINFO() {
        this.dwSize = this.size();
    }
}

