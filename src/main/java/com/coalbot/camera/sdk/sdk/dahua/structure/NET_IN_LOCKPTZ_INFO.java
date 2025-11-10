package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
/**
 * CLIENT_LockPtz 接口输入参数
*/
public class NET_IN_LOCKPTZ_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 通道号
    */
    public int              nChannel;
    /**
     * 锁定/解锁 true-锁定，false-解锁
    */
    public int              bLock;

    public NET_IN_LOCKPTZ_INFO() {
        this.dwSize = this.size();
    }
}

