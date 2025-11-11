package com.coalbot.camera.sdk.sdk.dahua.structure;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.sun.jna.Pointer;
/**
 * CLIENT_FaceRecognitionReAbstract 接口输入参数
*/
public class NET_IN_FACE_RECOGNITION_REABSTRACT_INFO extends NetSDKLibStructure.SdkStructure
{
    /**
     * 此结构体大小
    */
    public int              dwSize;
    /**
     * 重新建模的人员个数
    */
    public int              nPersonNum;
    /**
     * 重新建模的人员信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_REABSTRACT_PERSON_INFO}
    */
    public Pointer          pstReAbstractPersonInfo;

    public NET_IN_FACE_RECOGNITION_REABSTRACT_INFO() {
        this.dwSize = this.size();
    }
}

