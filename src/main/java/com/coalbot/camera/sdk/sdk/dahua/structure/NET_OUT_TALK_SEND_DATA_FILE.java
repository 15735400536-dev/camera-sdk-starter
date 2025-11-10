package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 251823
 * @version 1.0
 * @description CLIENT_TalkSendDataByFile接口 出参
 * @date 2021/12/29
 */
public class NET_OUT_TALK_SEND_DATA_FILE extends NetSDKLibStructure.SdkStructure {
	/**
     *  结构体大小
     */
    public int              dwSize;

    public NET_OUT_TALK_SEND_DATA_FILE(){
        this.dwSize = this.size();
    }
}

