package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_RadiometryGetTemper 接口出参
*/
public class NET_OUT_RADIOMETRY_GET_TEMPER_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 获取测温点的参数值,参见结构体定义 NET_RADIOMETRY_TEMP_INFO
    */
    public NET_RADIOMETRY_TEMP_INFO stTempInfo = new NET_RADIOMETRY_TEMP_INFO();

    public NET_OUT_RADIOMETRY_GET_TEMPER_INFO() {
        this.dwSize = this.size();
    }
}

