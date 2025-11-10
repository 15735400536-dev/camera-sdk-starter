package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author 251589
 * @version V1.0
 * @Description:
 * @date 2020/12/9 19:43
 */
public class NET_OUT_GETFACEEIGEN_INFO extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;
    public int              nInFaceEigenLen;                      // 用户申请的人脸特征值数据大小
    public int              nOutFaceEigenLen;                     // 人脸特征值数据实际的大小
    public Pointer          pszFaceEigen = new Memory(8*1024);    // 人脸特征值数据, 由用户申请释放, 每张照片最大为8K

    public NET_OUT_GETFACEEIGEN_INFO() {
        dwSize = this.size();
    }
}

