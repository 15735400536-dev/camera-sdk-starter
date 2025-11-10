package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * 电视墙场景
*/
public class NET_MONITORWALL_SCENE extends NetSDKLibStructure.SdkStructure
{
    public int              dwSize;
    /**
     * 当前预案名称
    */
    public byte[]           szName = new byte[128];
    /**
     * 控制ID
    */
    public byte[]           szControlID = new byte[128];
    /**
     * 电视墙配置,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_MONITORWALL}
    */
    public NetSDKLibStructure.NET_MONITORWALL stuMonitorWall = new NetSDKLibStructure.NET_MONITORWALL();
    /**
     * 拼接屏场景信息数组, 用户分配内存,大小为sizeof(DH_SPLIT_SCENE)*nMaxSplitSceneCount,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_SPLIT_SCENE}
    */
    public Pointer          pstuSplitScene;
    /**
     * 拼接屏场景数组大小, 用户填写
    */
    public int              nMaxSplitSceneCount;
    /**
     * 返回的拼接屏场景数量
    */
    public int              nRetSplitSceneCount;

    public NET_MONITORWALL_SCENE() {
        this.dwSize = this.size();
    }
}

