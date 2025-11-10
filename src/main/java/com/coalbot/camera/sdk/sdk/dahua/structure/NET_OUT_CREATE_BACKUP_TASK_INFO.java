package com.coalbot.camera.sdk.sdk.dahua.structure;

/**
 * CLIENT_CreateBackupTask接口输出参数
*/
public class NET_OUT_CREATE_BACKUP_TASK_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小,必须赋值
    */
    public int              dwSize;
    /**
     * 创建的实例ID
    */
    public int              nInstanceID;

    public NET_OUT_CREATE_BACKUP_TASK_INFO() {
        this.dwSize = this.size();
    }
}

