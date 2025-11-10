package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * @author 291189
 * @version 1.0
 * @description CLIENT_DoFindVehicleRegisterDB 接口输出参数
 * @date 2022/10/22 10:52
 */
public class NET_OUT_DO_FIND_VEHICLE_REG_DB extends NetSDKLibStructure.SdkStructure {
    public int              dwSize;                               // 结构体大小
    public 	int             nCarCandidateNum;                     // 候选车辆数量
    public 	NetSDKLibStructure.NET_CAR_CANDIDATE_INFO[] stuCarCandidate = new NetSDKLibStructure.NET_CAR_CANDIDATE_INFO[128]; // 候选车辆数据
    public	int              nFound;                               // 查询到的条数

    public NET_OUT_DO_FIND_VEHICLE_REG_DB(){

        for (int i=0;i<stuCarCandidate.length;i++){
            stuCarCandidate[i]=new NetSDKLibStructure.NET_CAR_CANDIDATE_INFO();
        }

        dwSize=this.size();
    }
}

