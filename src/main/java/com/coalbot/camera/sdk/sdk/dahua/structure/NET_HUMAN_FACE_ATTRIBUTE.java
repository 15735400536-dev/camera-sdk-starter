package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 查询到的目标属性信息
*/
public class NET_HUMAN_FACE_ATTRIBUTE extends NetSDKLibStructure.SdkStructure
{
    /**
     * 性别,参见枚举定义 {@link NetSDKLibStructure.EM_SEX_TYPE}
    */
    public int              emSex;
    /**
     * 年龄
    */
    public int              nAge;
    /**
     * 口罩状态,参见枚举定义 {@link NetSDKLibStructure.EM_MASK_STATE_TYPE}
    */
    public int              emMask;
    /**
     * 胡子状态,参见枚举定义 {@link NetSDKLibStructure.EM_BEARD_STATE_TYPE}
    */
    public int              emBeard;
    /**
     * 是否戴眼镜,参见枚举定义 {@link NetSDKLibStructure.EM_HAS_GLASS}
    */
    public int              emHasGlass;
    /**
     * 表情,参见枚举定义 {@link NetSDKLibStructure.EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE}
    */
    public int              emEmotion;
    /**
     * 保留字段
    */
    public byte[]           byReserved = new byte[1024];

    public NET_HUMAN_FACE_ATTRIBUTE() {
    }
}

