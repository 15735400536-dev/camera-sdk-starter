package com.coalbot.camera.sdk.sdk.dahua.structure;

import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 人体历史库以图搜图主动上报结果信息
*/
public class NET_RESULT_OF_VEHICLEHISTORY_BYPIC extends NetSDKLibStructure.SdkStructure
{
    /**
     * 小图ID
    */
    public int              nSmallPicID;
    /**
     * 目标图ID
    */
    public int              nPictureID;
    /**
     * 候选人员信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_VEHICLEHISTORY_CANDIDATE}
    */
    public NET_VEHICLEHISTORY_CANDIDATE[] stuCandidateInfo = new NET_VEHICLEHISTORY_CANDIDATE[50];
    /**
     * 候选人员数量
    */
    public int              nCandidateNum;
    /**
     * 预留字节数
    */
    public byte[]           szReserved = new byte[1028];

    public NET_RESULT_OF_VEHICLEHISTORY_BYPIC() {
        for(int i = 0; i < stuCandidateInfo.length; i++){
            stuCandidateInfo[i] = new NET_VEHICLEHISTORY_CANDIDATE();
        }
    }
}

