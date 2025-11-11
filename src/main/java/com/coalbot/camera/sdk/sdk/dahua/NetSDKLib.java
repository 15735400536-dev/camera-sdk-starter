package com.coalbot.camera.sdk.sdk.dahua;

import com.coalbot.camera.sdk.enums.CameraBrand;
import com.coalbot.camera.sdk.sdk.dahua.enumeration.*;
import com.coalbot.camera.sdk.sdk.dahua.structure.*;
import com.coalbot.camera.sdk.util.CameraSdkUtils;
import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * NetSDK JNA接口封装
 */
public interface NetSDKLib extends Library {

    NetSDKLib NETSDK_INSTANCE = Native.load(CameraSdkUtils.getSdkPath(CameraBrand.Dahua, "dhnetsdk"), NetSDKLib.class);

    NetSDKLib CONFIG_INSTANCE = Native.load(CameraSdkUtils.getSdkPath(CameraBrand.Dahua, "dhconfigsdk"), NetSDKLib.class);

    //NetSDKLib CONFIG_JNI = (NetSDKLib)Native.loadLibrary(util.getLoadLibrary("JNI1.dll"), INetSDK.class);
    class LLong extends IntegerType {
        private static final long serialVersionUID = 1L;

        /** Size of a native long, in bytes. */
        public static int size;
        static {
            size = Native.LONG_SIZE;
            if (Utils.getOsPrefix().equalsIgnoreCase("linux-amd64")
                    || Utils.getOsPrefix().equalsIgnoreCase("win32-amd64")
                    || Utils.getOsPrefix().equalsIgnoreCase("mac-64")) {
                size = 8;
            } else if (Utils.getOsPrefix().equalsIgnoreCase("linux-i386")
                    || Utils.getOsPrefix().equalsIgnoreCase("win32-x86")) {
                size = 4;
            }
        }

        /** Create a zero-valued LLong. */
        public LLong() {
            this(0);
        }

        /** Create a LLong with the given value. */
        public LLong(long value) {
            super(size, value);
        }
    }

    /***********************************************************************
     ** 回调
     ***********************************************************************/
    //JNA Callback方法定义,断线回调
    public interface fDisConnect extends Callback {
        public void invoke(LLong lLoginID,String pchDVRIP,int nDVRPort,Pointer dwUser);
    }

    // 网络连接恢复回调函数原形
    public interface fHaveReConnect extends Callback {
        public void invoke(LLong lLoginID,String pchDVRIP,int nDVRPort,Pointer dwUser);
    }

    // 消息回调函数原形(pBuf内存由SDK内部申请释放)
    public interface fMessCallBack extends Callback {
        public boolean invoke(int lCommand,LLong lLoginID,Pointer pStuEvent,int dwBufLen,String strDeviceIP,NativeLong nDevicePort,Pointer dwUser);
    }

    // 消息回调函数原形(pBuf内存由SDK内部申请释放)
    // 新增参数说明
    // bAlarmAckFlag : TRUE,该事件为可以进行确认的事件；FALSE,该事件无法进行确认
    // nEventID 用于对 CLIENT_AlarmAck 接口的入参进行赋值,当 bAlarmAckFlag 为 TRUE 时,该数据有效
    // pBuf内存由SDK内部申请释放
    public interface fMessCallBackEx1 extends Callback {
        public boolean invoke(int lCommand,LLong lLoginID,Pointer pStuEvent,int dwBufLen,String strDeviceIP,NativeLong nDevicePort,int bAlarmAckFlag,NativeLong nEventID,Pointer dwUser);
    }

    // 订阅人脸回调函数
    public interface fFaceFindState extends Callback {
// pstStates 指向NET_CB_FACE_FIND_STATE的指针
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer pstStates,int nStateNum,Pointer dwUser);
    }

    // 智能分析数据回调;nSequence表示上传的相同图片情况，为0时表示是第一次出现，为2表示最后一次出现或仅出现一次，为1表示此次之后还有
    // int nState = *(int*) reserved 表示当前回调数据的状态, 为0表示当前数据为实时数据，为1表示当前回调数据是离线数据，为2时表示离线数据传送结束
    // pAlarmInfo 对应智能事件信息, pBuffer 对应智能图片信息, dwBufSize 智能图片信息大小
    public interface fAnalyzerDataCallBack extends Callback {
        public int invoke(LLong lAnalyzerHandle,int dwAlarmType,Pointer pAlarmInfo,Pointer pBuffer,int dwBufSize,Pointer dwUser,int nSequence,Pointer reserved) throws UnsupportedEncodingException;
    }

    // 抓图回调函数原形(pBuf内存由SDK内部申请释放)
    // EncodeType 编码类型，10：表示jpeg图片      0：mpeg4    CmdSerial : 操作流水号，同步抓图的情况下用不上
    public interface fSnapRev extends Callback {
        public void invoke(LLong lLoginID,Pointer pBuf,int RevLen,int EncodeType,int CmdSerial,Pointer dwUser);
    }

    // 异步搜索设备回调(pDevNetInfo内存由SDK内部申请释放)
    public interface fSearchDevicesCB extends Callback {
        public void invoke(Pointer pDevNetInfo,Pointer pUserData);
    }

    // 按时间回放进度回调函数原形
    public interface fTimeDownLoadPosCallBack extends Callback {
        public void invoke(LLong lPlayHandle,int dwTotalSize,int dwDownLoadSize,int index,NetSDKLibStructure.NET_RECORDFILE_INFO.ByValue recordfileinfo,Pointer dwUser);
    }

    // 回放数据回调函数原形
    public interface fDataCallBack extends Callback {
        public int invoke(LLong lRealHandle,int dwDataType,Pointer pBuffer,int dwBufSize,Pointer dwUser);
    }

    // 回放进度回调函数原形
    public interface fDownLoadPosCallBack extends Callback {
        public void invoke(LLong lPlayHandle,int dwTotalSize,int dwDownLoadSize,Pointer dwUser);
    }

    // 视频统计摘要信息回调函数原形，lAttachHandle 是 CLIENT_AttachVideoStatSummary 返回值
    public interface fVideoStatSumCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NetSDKLibStructure.NET_VIDEOSTAT_SUMMARY pBuf,int dwBufLen,Pointer dwUser);
    }

    // 回放数据回调函数原形（扩展）
    public interface fDataCallBackEx extends Callback {
        public int invoke(LLong lRealHandle,NetSDKLibStructure.NET_DATA_CALL_BACK_INFO pDataCallBack,Pointer dwUser);
    }

    // 用户自定义的数据回调   lTalkHandle是CLIENT_StartTalkEx的返回值
    // byAudioFlag：   0表示是本地录音库采集的音频数据 ，  1表示收到的设备发过来的音频数据
    public interface pfAudioDataCallBack extends Callback {
        public void invoke(LLong lTalkHandle,Pointer pDataBuf,int dwBufSize,byte byAudioFlag,Pointer dwUser);
    }

    // lHandle是文件传输句柄 ，nTransType是文件传输类型，nState是文件传输状态，
    public interface fTransFileCallBack extends Callback {
        public void invoke(LLong lHandle,int nTransType,int nState,int nSendSize,int nTotalSize,Pointer dwUser);
    }

    // GPS信息订阅回调--扩展
    public interface fGPSRevEx extends Callback {
        public void invoke(LLong lLoginID,NetSDKLibStructure.GPS_Info.ByValue GpsInfo,NetSDKLibStructure.ALARM_STATE_INFO.ByValue stAlarmInfo,Pointer dwUserData,Pointer reserved);
    }

    // GPS信息订阅回调--扩展2
    public interface fGPSRevEx2 extends Callback {
        public void invoke(LLong lLoginID, NetSDKLibStructure.NET_GPS_LOCATION_INFO lpData, Pointer dwUserData, Pointer reserved);
    }

    // 实时预览数据回调函数--扩展(pBuffer内存由SDK内部申请释放)EM_CLASS_CROWD_ABNORMAL
    // lRealHandle实时预览           dwDataType: 0-原始数据   1-帧数据    2-yuv数据   3-pcm音频数据
    // pBuffer对应BYTE*
    // param:当类型为0(原始数据)和2(YUV数据) 时为0。当回调的数据类型为1时param为一个tagVideoFrameParam结构体指针。
    // param:当数据类型是3时,param也是一个tagCBPCMDataParam结构体指针
    public interface fRealDataCallBackEx extends Callback {
        public void invoke(LLong lRealHandle,int dwDataType,Pointer pBuffer,int dwBufSize,int param,Pointer dwUser);
    }

    // 实时预览数据回调函数原形--扩展(pBuffer内存由SDK内部申请释放)
    // 通过 dwDataType 过滤得到对应码流，具体码流类型请参考 EM_REALDATA_FLAG; 转码流时 dwDataType 值请参考 NET_DATA_CALL_BACK_VALUE 说明
    // 当转码流时，param 为具体的转码信息（视频帧、音频帧等信息），对应结构体 NET_STREAMCONVERT_INFO
    public interface fRealDataCallBackEx2 extends Callback {
        void invoke(LLong lRealHandle,int dwDataType,Pointer pBuffer,int dwBufSize,LLong param,Pointer dwUser);
    }

    // 视频预览断开回调函数, (param内存由SDK内部申请释放 )
    // lOperateHandle监控句柄   dwEventType对应EM_REALPLAY_DISCONNECT_EVENT_TYPE   param对应void*,事件参数
    public interface fRealPlayDisConnect extends Callback {
        public void invoke(LLong lOperateHandle,int dwEventType,Pointer param,Pointer dwUser);
    }

    // 订阅过车记录数据回调函数原型     lAttachHandle为CLIENT_ParkingControlAttachRecord返回值
    public interface fParkingControlRecordCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,NetSDKLibStructure.NET_CAR_PASS_ITEM pInfo,int nBufLen,Pointer dwUser);
    }

    // 订阅车位信息回调函数原型
    public interface fParkInfoCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,NetSDKLibStructure.NET_PARK_INFO_ITEM pInfo,int nBufLen,Pointer dwUser);
    }

    // 订阅监测点位信息回调函数原型
    public interface fSCADAAttachInfoCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,NetSDKLibStructure.NET_SCADA_NOTIFY_POINT_INFO_LIST pInfo,int nBufLen,Pointer dwUser);
    }

    // 透明串口回调函数原形(pBuffer内存由SDK内部申请释放))
    public interface fTransComCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lTransComChannel,Pointer pBuffer,int dwBufSize,Pointer dwUser);
    }

    //视频分析进度状态实时回调函数
    public interface fVideoAnalyseState extends Callback {
        public int invoke(LLong lAttachHandle, NetSDKLibStructure.NET_VIDEOANALYSE_STATE pAnalyseStateInfos, Pointer dwUser, Pointer pReserved);
    }

    // 侦听服务器回调函数原形
    public interface fServiceCallBack extends Callback {
        public int invoke(LLong lHandle,String pIp,int wPort,int lCommand,Pointer pParam,int dwParamLen,Pointer dwUserData);
    }

    // 雷达RFID信息回调函数原形
    public interface fRadarRFIDCardInfoCallBack extends Callback {
        public int invoke(LLong lLoginID,LLong lAttachHandle,NET_RADAR_NOTIFY_RFIDCARD_INFO pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    //订阅Bus状态回调函数原型
    public interface fBusStateCallBack extends Callback {
        public void invoke(LLong lAttachHandle,int lCommand,Pointer pBuf,int dwBufLen,Pointer dwUser);
    }

    // GPS温湿度信息订阅回调
    public interface fGPSTempHumidityRev extends Callback {
        public void invoke(LLong lLoginID,NetSDKLibStructure.GPS_TEMP_HUMIDITY_INFO.ByValue GpsTHInfo,Pointer dwUserData);
    }

    // 向设备注册的回调函数原型
    public interface fDeviceStateCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NetSDKLibStructure.NET_CB_ATTACH_DEVICE_STATE pstDeviceState,Pointer dwUser);
    }

    // 注册添加设备的回调函数原型
    public interface fAddDeviceCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NetSDKLibStructure.NET_CB_ATTACH_ADD_DEVICE pstAddDevice,Pointer dwUser);
    }

    // 定义监测点报警信息回调函数原型
    public interface fSCADAAlarmAttachInfoCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NetSDKLibStructure.NET_SCADA_NOTIFY_POINT_ALARM_INFO_LIST pInfo,int nBufLen,Pointer dwUser);
    }

    //视频诊断结果上报回调函数
    public interface fRealVideoDiagnosis extends Callback {
        public int invoke(LLong lDiagnosisHandle,NetSDKLibStructure.NET_REAL_DIAGNOSIS_RESULT pDiagnosisInfo,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

    /// \fn 温度分布数据状态回调函数
    /// \brief
    /// \param  LLONG lAttachHandle [OUT] 订阅句柄, CLIENT_RadiometryAttach 的返回值
    /// \param  NET_RADIOMETRY_DATA pBuf [OUT] 热图数据信息
    /// \param  int nBufLen [OUT] 状态信息长度
    /// \param  LDWORD dwUser 用户数据
    /// \return 无
    public interface fRadiometryAttachCB extends Callback {
        public void invoke(LLong lAttachHandle,NetSDKLibStructure.NET_RADIOMETRY_DATA pBuf,int nBufLen,Pointer dwUser);
    }

    // 刻录设备回调函数原形,lAttachHandle是CLIENT_AttachBurnState返回值, 每次1条,pBuf->dwSize == nBufLen
    public interface fAttachBurnStateCB extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,NetSDKLibStructure.NET_CB_BURNSTATE pBuf,int nBufLen,Pointer dwUser);
    }

    // 刻录设备回调扩展函数原形
    public interface fAttachBurnStateCBEx extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,NetSDKLibStructure.NET_OUT_BURN_GET_STATE pBuf,int nBufLen,Pointer dwUser);
    }

    //刻录设备回调函数,lUploadHandle是CLIENT_StartUploadFileBurned返回值
    //typedef void (CALLBACK *fBurnFileCallBack) (LLONG lLoginID, LLONG lUploadHandle, int nTotalSize, int nSendSize, LDWORD dwUser);
    public interface fBurnFileCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lUploadHandle,int nTotalSize,int nSendSize,Pointer dwUser);
    }

    // 升级回调
    public interface fUpgradeCallBackEx extends Callback {
        public void invoke(LLong lLoginID,LLong lUpgradechannel,int nTotalSize,int nSendSize,Pointer dwUserData);
    }

    /************************************************************************
     ** 接口
     ***********************************************************************/
    //  JNA直接调用方法定义，cbDisConnect 实际情况并不回调Java代码，仅为定义可以使用如下方式进行定义。 fDisConnect 回调
    public boolean CLIENT_Init(Callback cbDisConnect,Pointer dwUser);

    //  JNA直接调用方法定义，SDK退出清理
    public void CLIENT_Cleanup();

    //  JNA直接调用方法定义，设置断线重连成功回调函数，设置后SDK内部断线自动重连, fHaveReConnect 回调
    public void CLIENT_SetAutoReconnect(Callback cbAutoConnect,Pointer dwUser);

    // 返回函数执行失败代码
    public int CLIENT_GetLastError();

    // 设置连接设备超时时间和尝试次数
    public void CLIENT_SetConnectTime(int nWaitTime,int nTryTimes);

    // 设置登陆网络环境
    public void CLIENT_SetNetworkParam(NetSDKLibStructure.NET_PARAM pNetParam);

    //
    public boolean CLIENT_SetDeviceSearchParam(NetSDKLibStructure.NET_DEVICE_SEARCH_PARAM pstParam);

    // 获取SDK的版本信息
    public int CLIENT_GetSDKVersion();

    //  JNA直接调用方法定义，登陆接口
    public LLong CLIENT_LoginEx(String pchDVRIP,int wDVRPort,String pchUserName,String pchPassword,int nSpecCap,Pointer pCapParam,NetSDKLibStructure.NET_DEVICEINFO lpDeviceInfo,IntByReference error/*= 0*/);

    //  JNA直接调用方法定义，登陆扩展接口///////////////////////////////////////////////////
    //  nSpecCap 对应  EM_LOGIN_SPAC_CAP_TYPE 登陆类型
    public LLong CLIENT_LoginEx2(String pchDVRIP,int wDVRPort,String pchUserName,String pchPassword,int nSpecCap,Pointer pCapParam,NetSDKLibStructure.NET_DEVICEINFO_Ex lpDeviceInfo,IntByReference error/*= 0*/);

    //  JNA直接调用方法定义，向设备注销
    public boolean CLIENT_Logout(LLong lLoginID);

    // 获取配置
    // error 为设备返回的错误码： 0-成功 1-失败 2-数据不合法 3-暂时无法设置 4-没有权限
    public boolean CLIENT_GetNewDevConfig(LLong lLoginID,String szCommand,int nChannelID,byte[] szOutBuffer,int dwOutBufferSize,IntByReference error,int waiitime,Pointer pReserved);

    // 设置配置
    public boolean CLIENT_SetNewDevConfig(LLong lLoginID,String szCommand,int nChannelID,byte[] szInBuffer,int dwInBufferSize,IntByReference error,IntByReference restart,int waittime);

    // 删除配置接口(Json格式)
    public boolean CLIENT_DeleteDevConfig(LLong lLoginID,NetSDKLibStructure.NET_IN_DELETECFG pInParam,NetSDKLibStructure.NET_OUT_DELETECFG pOutParam,int waittime);

    // 获取配置成员名称接口(Json格式)(pInParam, pOutParam内存由用户申请释放)
    public boolean CLIENT_GetMemberNames(LLong lLoginID,NetSDKLibStructure.NET_IN_MEMBERNAME pInParam,NetSDKLibStructure.NET_OUT_MEMBERNAME pOutParam,int waittime);

    // 解析查询到的配置信息
    public boolean CLIENT_ParseData(String szCommand,byte[] szInBuffer,Pointer lpOutBuffer,int dwOutBufferSize,Pointer pReserved);

    // 组成要设置的配置信息
    public boolean CLIENT_PacketData(String szCommand,Pointer lpInBuffer,int dwInBufferSize,byte[] szOutBuffer,int dwOutBufferSize);

    // 设置报警回调函数, fMessCallBack 回调
    public void CLIENT_SetDVRMessCallBack(Callback cbMessage,Pointer dwUser);

    // 设置报警回调函数, fMessCallBackEx1 回调
    public void CLIENT_SetDVRMessCallBackEx1(fMessCallBackEx1 cbMessage,Pointer dwUser);

    // 向设备订阅报警--扩展
    public boolean CLIENT_StartListenEx(LLong lLoginID);

    // 停止订阅报警
    public boolean CLIENT_StopListen(LLong lLoginID);

    /////////////////////////////////目标识别接口/////////////////////////////////////////
    //目标识别数据库信息操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITIONDB类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITIONDB类型的指针
    public boolean CLIENT_OperateFaceRecognitionDB(LLong lLoginID,NetSDKLibStructure.NET_IN_OPERATE_FACERECONGNITIONDB pstInParam,NetSDKLibStructure.NET_OUT_OPERATE_FACERECONGNITIONDB pstOutParam,int nWaitTime);

    // 按条件查询目标识别结果
    // pstInParam指向NET_IN_STARTFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_STARTFIND_FACERECONGNITION类型的指针
    public boolean CLIENT_StartFindFaceRecognition(LLong lLoginID,NetSDKLibStructure.NET_IN_STARTFIND_FACERECONGNITION pstInParam,NetSDKLibStructure.NET_OUT_STARTFIND_FACERECONGNITION pstOutParam,int nWaitTime);

    // 查找目标识别结果:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕(每次最多只能查询20条记录)
    // pstInParam指向NET_IN_DOFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_DOFIND_FACERECONGNITION类型的指针
    public boolean CLIENT_DoFindFaceRecognition(final NetSDKLibStructure.NET_IN_DOFIND_FACERECONGNITION pstInParam,NetSDKLibStructure.NET_OUT_DOFIND_FACERECONGNITION pstOutParam,int nWaitTime);

    //结束查询
    public boolean CLIENT_StopFindFaceRecognition(LLong lFindHandle);

    // 目标检测(输入一张大图,输入大图中被检测出来的人脸图片)
    // pstInParam指向NET_IN_DETECT_FACE类型的指针
    // pstOutParam指向NET_OUT_DETECT_FACE类型的指针
    public boolean CLIENT_DetectFace(LLong lLoginID,NetSDKLibStructure.NET_IN_DETECT_FACE pstInParam,NetSDKLibStructure.NET_OUT_DETECT_FACE pstOutParam,int nWaitTime);

    // 目标识别人员组操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITION_GROUP类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITION_GROUP类型的指针
    public boolean CLIENT_OperateFaceRecognitionGroup(LLong lLoginID,NetSDKLibStructure.NET_IN_OPERATE_FACERECONGNITION_GROUP pstInParam,NetSDKLibStructure.NET_OUT_OPERATE_FACERECONGNITION_GROUP pstOutParam,int nWaitTime);

    // 查询目标识别人员组信息
    // pstInParam指向NET_IN_FIND_GROUP_INFO类型的指针
    // pstOutParam指向NET_OUT_FIND_GROUP_INFO类型的指针
    public boolean CLIENT_FindGroupInfo(LLong LLong,NetSDKLibStructure.NET_IN_FIND_GROUP_INFO pstInParam,NetSDKLibStructure.NET_OUT_FIND_GROUP_INFO pstOutParam,int nWaitTime);

    // 获取布控在视频通道的组信息,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_GetGroupInfoForChannel(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_GROUPINFO_FOR_CHANNEL pstInParam,NetSDKLibStructure.NET_OUT_GET_GROUPINFO_FOR_CHANNEL pstOutParam,int nWaitTime);

    // 布控通道人员组信息
    // pstInParam指向NET_IN_SET_GROUPINFO_FOR_CHANNEL类型的指针
    // pstOutParam指向NET_OUT_SET_GROUPINFO_FOR_CHANNEL类型的指针
    public boolean CLIENT_SetGroupInfoForChannel(LLong lLoginID,NetSDKLibStructure.NET_IN_SET_GROUPINFO_FOR_CHANNEL pstInParam,NetSDKLibStructure.NET_OUT_SET_GROUPINFO_FOR_CHANNEL pstOutParam,int nWaitTime);

    // 以人脸库的角度进行布控, pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_FaceRecognitionPutDisposition(LLong lLoginID,NetSDKLibStructure.NET_IN_FACE_RECOGNITION_PUT_DISPOSITION_INFO pstInParam,NetSDKLibStructure.NET_OUT_FACE_RECOGNITION_PUT_DISPOSITION_INFO pstOutParam,int nWaitTime);

    // 以人脸库的角度进行撤控, pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_FaceRecognitionDelDisposition(LLong lLoginID,NetSDKLibStructure.NET_IN_FACE_RECOGNITION_DEL_DISPOSITION_INFO pstInParam,NetSDKLibStructure.NET_OUT_FACE_RECOGNITION_DEL_DISPOSITION_INFO pstOutParam,int nWaitTime);

    // 订阅人脸查询状态
    // pstInParam指向NET_IN_FACE_FIND_STATE类型的指针
    // pstOutParam指向NET_OUT_FACE_FIND_STATE类型的指针
    public LLong CLIENT_AttachFaceFindState(LLong lLoginID,NetSDKLibStructure.NET_IN_FACE_FIND_STATE pstInParam,NetSDKLibStructure.NET_OUT_FACE_FIND_STATE pstOutParam,int nWaitTime);

    //取消订阅人脸查询状态,lAttachHandle为CLIENT_AttachFaceFindState返回的句柄
    public boolean CLIENT_DetachFaceFindState(LLong lAttachHandle);

    // 文件下载, 只适用于小文件,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_DownloadRemoteFile(LLong lLoginID,NetSDKLibStructure.NET_IN_DOWNLOAD_REMOTE_FILE pInParam,NetSDKLibStructure.NET_OUT_DOWNLOAD_REMOTE_FILE pOutParam,int nWaitTime);

    // 打开日志功能
    // pstLogPrintInfo指向LOG_SET_PRINT_INFO的指针
    public boolean CLIENT_LogOpen(NetSDKLibStructure.LOG_SET_PRINT_INFO pstLogPrintInfo);

    // 关闭日志功能
    public boolean CLIENT_LogClose();

    // 获取符合查询条件的文件总数
    // reserved为void *
    public boolean CLIENT_GetTotalFileCount(LLong lFindHandle,IntByReference pTotalCount,Pointer reserved,int waittime);

    // 设置查询跳转条件
    // reserved为void *
    public boolean CLIENT_SetFindingJumpOption(LLong lFindHandle, NetSDKLibStructure.NET_FINDING_JUMP_OPTION_INFO pOption, Pointer reserved, int waittime);

    // 按查询条件查询文件
    // pQueryCondition为void *, 具体类型根据emType的类型确定,对应 EM_FILE_QUERY_TYPE
    // reserved为void *, 具体类型根据emType的类型确定
    public LLong CLIENT_FindFileEx(LLong lLoginID,int emType,Pointer pQueryCondition,Pointer reserved,int waittime);

    // 查找文件:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕
    // pMediaFileInfo为void *
    // reserved为void *
    public int CLIENT_FindNextFileEx(LLong lFindHandle,int nFilecount,Pointer pMediaFileInfo,int maxlen,Pointer reserved,int waittime);

    // 结束录像文件查找
    public boolean CLIENT_FindCloseEx(LLong lFindHandle);

    // 实时上传智能分析数据－图片(扩展接口,bNeedPicFile表示是否订阅图片文件,Reserved类型为RESERVED_PARA)
    // bNeedPicFile为BOOL类型，取值范围为0或者1, fAnalyzerDataCallBack回调
    public LLong CLIENT_RealLoadPictureEx(LLong lLoginID,int nChannelID,int dwAlarmType,int bNeedPicFile,Callback cbAnalyzerData,Pointer dwUser,Pointer Reserved);

    // 停止上传智能分析数据－图片
    public boolean CLIENT_StopLoadPic(LLong lAnalyzerHandle);

    // 设置抓图回调函数, fSnapRev回调
    public void CLIENT_SetSnapRevCallBack(Callback OnSnapRevMessage,Pointer dwUser);

    // 抓图请求扩展接口
    public boolean CLIENT_SnapPictureEx(LLong lLoginID, NetSDKLibStructure.SNAP_PARAMS stParam, IntByReference reserved);

    // 异步搜索局域网内IPC、NVS等设备, fSearchDevicesCB回调
    public LLong CLIENT_StartSearchDevices(Callback cbSearchDevices,Pointer pUserData,String szLocalIp);

    // 停止异步搜索局域网内IPC、NVS等设备
    public boolean CLIENT_StopSearchDevices(LLong lSearchHandle);

    // 同步跨网段搜索设备IP (pIpSearchInfo内存由用户申请释放)DEVICE_IP_SEARCH_INFO
    // szLocalIp为本地IP，可不做输入, fSearchDevicesCB回调
    // 接口调用1次只发送搜索信令1次
    public boolean CLIENT_SearchDevicesByIPs(Pointer pIpSearchInfo,Callback cbSearchDevices,Pointer dwUserData,String szLocalIp,int dwWaitTime);

    // 开始实时预览
    // rType  : NET_RealPlayType    返回监控句柄
    public LLong CLIENT_RealPlayEx(LLong lLoginID,int nChannelID,Pointer hWnd,int rType);

    // 停止实时预览--扩展     lRealHandle为CLIENT_RealPlayEx的返回值
    public boolean CLIENT_StopRealPlayEx(LLong lRealHandle);

    // 开始实时预览支持设置码流回调接口     rType  : NET_RealPlayType   返回监控句柄
    // cbRealData 对应 fRealDataCallBackEx 回调
    // cbDisconnect 对应 fRealPlayDisConnect 回调
    public LLong CLIENT_StartRealPlay(LLong lLoginID,int nChannelID,Pointer hWnd,int rType,Callback cbRealData,Callback cbDisconnect,Pointer dwUser,int dwWaitTime);

    // 停止实时预览
    public boolean CLIENT_StopRealPlay(LLong lRealHandle);

    // 设置实时预览数据回调函数扩展接口    lRealHandle监控句柄,fRealDataCallBackEx 回调
    public boolean CLIENT_SetRealDataCallBackEx(LLong lRealHandle,Callback cbRealData,Pointer dwUser,int dwFlag);

    // 设置图象流畅性
    // 将要调整图象的等级(0-6),当level为0时，图象最流畅；当level为6时，图象最实时。Level的默认值为3。注意：直接解码下有效
    public boolean CLIENT_AdjustFluency(LLong lRealHandle,int nLevel);

    // 保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值,pchFileName为实时预览保存文件名
    public boolean CLIENT_SaveRealData(LLong lRealHandle,String pchFileName);

    // 结束保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值
    public boolean CLIENT_StopSaveRealData(LLong lRealHandle);

    // 打开声音
    public boolean CLIENT_OpenSound(LLong hPlayHandle);

    // 关闭声音
    public boolean CLIENT_CloseSound();

    // 设置显示源(pInparam, pOutParam内存由用户申请释放)
    public boolean CLIENT_MatrixSetCameras(LLong lLoginID, NetSDKLibStructure.NET_IN_MATRIX_SET_CAMERAS pInParam, NetSDKLibStructure.NET_OUT_MATRIX_SET_CAMERAS pOutParam, int nWaitTime);

    // 获取所有有效显示源
    // pInParam  对应  NET_IN_MATRIX_GET_CAMERAS
    // pOutParam 对应  NET_OUT_MATRIX_GET_CAMERAS
    public boolean CLIENT_MatrixGetCameras(LLong lLoginID, NetSDKLibStructure.NET_IN_MATRIX_GET_CAMERAS pInParam, NetSDKLibStructure.NET_OUT_MATRIX_GET_CAMERAS pOutParam, int nWaitTime);

    // 抓图同步接口,将图片数据直接返回给用户
   // public boolean CLIENT_SnapPictureToFile(LLong lLoginID, NET_IN_SNAP_PIC_TO_FILE_PARAM pInParam, NET_OUT_SNAP_PIC_TO_FILE_PARAM pOutParam, int nWaitTime);
    public boolean CLIENT_SnapPictureToFile(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 查询时间段内的所有录像文件
    // nRecordFileType 录像类型 0:所有录像  1:外部报警  2:动态监测报警  3:所有报警  4:卡号查询   5:组合条件查询   6:录像位置与偏移量长度   8:按卡号查询图片(目前仅HB-U和NVS特殊型号的设备支持)  9:查询图片(目前仅HB-U和NVS特殊型号的设备支持)  10:按字段查询    15:返回网络数据结构(金桥网吧)  16:查询所有透明串数据录像文件
    // nriFileinfo 返回的录像文件信息，是一个 NET_RECORDFILE_INFO 结构数组
    // maxlen 是 nriFileinfo缓冲的最大长度(单位字节，建议在(100~200)*sizeof(NET_RECORDFILE_INFO)之间)
    // filecount返回的文件个数，属于输出参数最大只能查到缓冲满为止的录像记录;
    // bTime 是否按时间查(目前无效)
    public boolean CLIENT_QueryRecordFile(LLong lLoginID,int nChannelId,int nRecordFileType,NET_TIME tmStart,NET_TIME tmEnd,String pchCardid,NetSDKLibStructure.NET_RECORDFILE_INFO[] stFileInfo,int maxlen,IntByReference filecount,int waittime,boolean bTime);

    // NET_RECORDFILE_INFO[] stFileInfo Pointer 版本
    public boolean CLIENT_QueryRecordFile(LLong lLoginID,int nChannelId,int nRecordFileType,NET_TIME tmStart,NET_TIME tmEnd,String pchCardid,Pointer pFileInfo,int maxlen,IntByReference filecount,int waittime,boolean bTime);

    // 查询时间段内是否有录像文件   bResult输出参数，true有录像，false没录像
    public boolean CLIENT_QueryRecordTime(LLong lLoginID,int nChannelId,int nRecordFileType,NET_TIME tmStart,NET_TIME tmEnd,String pchCardid,IntByReference bResult,int waittime);

    // 通过时间下载录像--扩展
    // nRecordFileType 对应 EM_QUERY_RECORD_TYPE
    // cbTimeDownLoadPos 对应 fTimeDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public LLong CLIENT_DownloadByTimeEx(LLong lLoginID,int nChannelId,int nRecordFileType,NET_TIME tmStart,NET_TIME tmEnd,String sSavedFileName,Callback cbTimeDownLoadPos,Pointer dwUserData,Callback fDownLoadDataCallBack,Pointer dwDataUser,Pointer pReserved);

    // 停止录像下载
    public boolean CLIENT_StopDownload(LLong lFileHandle);

    /******************************************************************************
     功能描述	:	通过时间下载录像--扩展,可加载码流转换库
     输入参数	:
     lLoginID:       登录接口返回的句柄
     nChannelId:     视频通道号,从0开始
     nRecordFileType:录像类型 0 所有录像文件
     1 外部报警
     2 动态检测报警
     3 所有报警
     4 卡号查询
     5 组合条件查询
     6 录像位置与偏移量长度
     8 按卡号查询图片(目前仅HB-U和NVS特殊型号的设备支持)
     9 查询图片(目前仅HB-U和NVS特殊型号的设备支持)
     10 按字段查询
     15 返回网络数据结构(金桥网吧)
     16 查询所有透明串数据录像文件
     tmStart:        开始时间
     tmEnd:          结束时间
     sSavedFileName: 保存录像文件名,支持全路径
     cbTimeDownLoadPos: 下载进度回调函数(回调下载进度,下载结果), 对应回调   fTimeDownLoadPosCallBack
     dwUserData:     下载进度回调对应用户数据
     fDownLoadDataCallBack: 录像数据回调函数(回调形式暂不支持转换PS流)，对应回调  fDataCallBack
     dwDataUser:     录像数据回调对应用户数据
     scType:         码流转换类型,0-DAV码流(默认); 1-PS流,3-MP4
     pReserved:      保留参数,后续扩展
     输出参数	：	N/A
     返 回 值	：	LLONG 下载录像句柄
     其他说明	：	特殊接口,SDK默认不支持转PS流,需SDK
     ******************************************************************************/
    public LLong CLIENT_DownloadByTimeEx2(LLong lLoginID,int nChannelId,int nRecordFileType,NET_TIME tmStart,NET_TIME tmEnd,String sSavedFileName,Callback cbTimeDownLoadPos,Pointer dwUserData,Callback fDownLoadDataCallBack,Pointer dwDataUser,int scType,Pointer pReserved);

    /**
     * 下载录像文件--扩展
     * sSavedFileName 不为空, 录像数据写入到该路径对应的文件; fDownLoadDataCallBack不为空, 录像数据通过回调函数返回
     * pReserved 指加密录像的密码（长度不小于 8 的字符串）
     *
     * @param lLoginID 登录句柄
     * @param lpRecordFile 录像信息
     * @param sSavedFileName 本地保存路径 如果要保存到本地则必填
     * @param cbDownLoadPos 下载进度回调 建议使用 可以在下载完成时调结束下载接口
     * @param dwUserData 下载进度回调对应用户数据 不建议使用 直接填 null
     * @param fDownLoadDataCallBack 下载数据回调 不为 null 则录像数据
     * @param dwDataUser 录像数据回调对应用户数据 不建议使用 直接填 null
     * @param pReserved pReserved 指加密录像的密码（长度不小于 8 的字符串） 没有的话填 null
     * @return 录像下载句柄fDownLoadDataCallBack
     */
    public LLong CLIENT_DownloadByRecordFileEx(LLong lLoginID,LPNET_RECORDFILE_INFO lpRecordFile,Pointer sSavedFileName,Callback cbDownLoadPos,Pointer dwUserData,Callback fDownLoadDataCallBack,Pointer dwDataUser,Pointer pReserved);

    // 下载录像文件--扩展 重载接口
    // lpRecordFile 类型 替换为 NET_RECORDFILE_INFO
    public LLong CLIENT_DownloadByRecordFileEx(LLong lLoginID,NetSDKLibStructure.NET_RECORDFILE_INFO lpRecordFile,Pointer sSavedFileName,Callback cbDownLoadPos,Pointer dwUserData,Callback fDownLoadDataCallBack,Pointer dwDataUser,Pointer pReserved);

    // 自适应速度的按文件下载录像, pstInParam 和 pstOutParam 资源由用户申请和释放
    // pstInParam->NET_IN_DOWNLOAD_BYFILE_SELFADAPT
    // pstOutParam->NET_OUT_DOWNLOAD_BYFILE_SELFADAPT
    public LLong CLIENT_DownloadByFileSelfAdapt(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int dwWaitTime);

    // 自适应速度的按时间下载录像
    // pstInParam->NET_IN_ADAPTIVE_DOWNLOAD_BY_TIME
    // pstOutParam->NET_OUT_ADAPTIVE_DOWNLOAD_BY_TIME
    public LLong CLIENT_AdaptiveDownloadByTime(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int dwWaitTime);

    // 私有云台控制扩展接口,支持三维快速定位
    public boolean CLIENT_DHPTZControlEx(LLong lLoginID,int nChannelID,int dwPTZCommand,int lParam1,int lParam2,int lParam3,int dwStop);

    // 云台控制扩展接口,支持三维快速定位,鱼眼
    // dwStop类型为BOOL, 取值0或者1
    // dwPTZCommand取值为NET_EXTPTZ_ControlType中的值或者是NET_PTZ_ControlType中的值
    // NET_IN_PTZBASE_MOVEABSOLUTELY_INFO
    // 精准绝对移动控制命令, param4对应结构 NET_IN_PTZBASE_MOVEABSOLUTELY_INFO（通过 CFG_CAP_CMD_PTZ 命令获取云台能力集( CFG_PTZ_PROTOCOL_CAPS_INFO )，若bSupportReal为TRUE则设备支持该操作）
    public boolean CLIENT_DHPTZControlEx2(LLong lLoginID,int nChannelID,int dwPTZCommand,int lParam1,int lParam2,int lParam3,int dwStop,Pointer param4);

    // 设备控制(param内存由用户申请释放)  emType对应 枚举 CtrlType
    public boolean CLIENT_ControlDevice(LLong lLoginID,int emType,Pointer param,int waittime);

    // 设备控制扩展接口，兼容 CLIENT_ControlDevice (pInBuf, pOutBuf内存由用户申请释放)
    // emType的取值为CtrlType中的值
    public boolean CLIENT_ControlDeviceEx(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    // 查询配置信息(lpOutBuffer内存由用户申请释放)
    public boolean CLIENT_GetDevConfig(LLong lLoginID,int dwCommand,int lChannel,Pointer lpOutBuffer,int dwOutBufferSize,IntByReference lpBytesReturned,int waittime);

    // 设置配置信息(lpInBuffer内存由用户申请释放)
    public boolean CLIENT_SetDevConfig(LLong lLoginID,int dwCommand,int lChannel,Pointer lpInBuffer,int dwInBufferSize,int waittime);

    // 查询设备状态(pBuf内存由用户申请释放)
    // pBuf指向char *,输出参数
    // pRetLen指向int *;输出参数，实际返回的数据长度，单位字节
    public boolean CLIENT_QueryDevState(LLong lLoginID,int nType,Pointer pBuf,int nBufLen,IntByReference pRetLen,int waittime);

    // 查询远程设备状态(pBuf内存由用户申请释放)
    // nType为DH_DEVSTATE_ALARM_FRONTDISCONNECT时，通道号从1开始
    public boolean CLIENT_QueryRemotDevState(LLong lLoginID,int nType,int nChannelID,Pointer pBuf,int nBufLen,IntByReference pRetLen,int waittime);

    // 获取设备能力接口
    // pInBuf指向void*，输入参数结构体指针       pOutBuf指向void*，输出参数结构体指针
    public boolean CLIENT_GetDevCaps(LLong lLoginID,int nType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    // 新系统能力查询接口，查询系统能力信息(以Json格式，具体见配置SDK)(szOutBuffer内存由用户申请释放)
    // szCommand: 对应命令查看上文
    // szOutBuffer: 获取到的信息, 通过 CLIENT_ParseData 解析
    // error 指向 int * ： 错误码大于0表示设备返回的，小于0表示缓冲不够或数据校验引起的
    public boolean CLIENT_QueryNewSystemInfo(LLong lLoginID,String szCommand,int nChannelID,byte[] szOutBuffer,int dwOutBufferSize,IntByReference error,int waittime);

    // 新系统能力查询接口扩展，查询系统能力信息，入参新增扩展数据(以Json格式，具体见配置SDK)(szOutBuffer内存由用户申请释放)
    public boolean CLIENT_QueryNewSystemInfoEx(LLong lLoginID,String szCommand,int nChannelID,byte[] szOutBuffer,int dwOutBufferSize,IntByReference error,Pointer pExtendInfo,int waittime);

    // 查询系统能力信息(pSysInfoBuffer内存由用户申请释放，大小参照DH_SYS_ABILITY对应的结构体，若nSystemType为 ABILITY_DYNAMIC_CONNECT ，内存大小至少为sizeof(BOOL),若nSystemType为ABILITY_TRIGGER_MODE，内存大小为sizeof(int))
    public boolean CLIENT_QuerySystemInfo(LLong lLoginID,int nSystemType,String pSysInfoBuffer,int nChannelID,byte[] szOutBuffer,int dwOutBufferSize,IntByReference error,int waittime);

    // 订阅视频统计摘要信息
    public LLong CLIENT_AttachVideoStatSummary(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_VIDEOSTAT_SUM pInParam,NetSDKLibStructure.NET_OUT_ATTACH_VIDEOSTAT_SUM pOutParam,int nWaitTime);

    // 取消订阅视频统计摘要信息，lAttachHandle为CLIENT_AttachVideoStatSummary的返回值
    public boolean CLIENT_DetachVideoStatSummary(LLong lAttachHandle);

    // 开始查询视频统计信息/获取人数统计信息
    public LLong CLIENT_StartFindNumberStat(LLong lLoginID,NetSDKLibStructure.NET_IN_FINDNUMBERSTAT pstInParam,NetSDKLibStructure.NET_OUT_FINDNUMBERSTAT pstOutParam);

    // 继续查询视频统计/继续查询人数统计
    public int CLIENT_DoFindNumberStat(LLong lFindHandle,NetSDKLibStructure.NET_IN_DOFINDNUMBERSTAT pstInParam,NetSDKLibStructure.NET_OUT_DOFINDNUMBERSTAT pstOutParam);

    // 结束查询视频统计/结束查询人数统计
    public boolean CLIENT_StopFindNumberStat(LLong lFindHandle);

    // 设置语音对讲模式,客户端方式还是服务器方式
    // emType : 方式类型 参照 EM_USEDEV_MODE
    public boolean CLIENT_SetDeviceMode(LLong lLoginID,int emType,Pointer pValue);

    ///////////////// 录像回放相关接口 ///////////////////////
    // 按时间方式回放
    public LLong CLIENT_PlayBackByTime(LLong lLoginID,int nChannelID,NET_TIME lpStartTime,NET_TIME lpStopTime,Pointer hWnd,fDownLoadPosCallBack cbDownLoadPos,Pointer dwPosUser);

    // 按时间方式回放--扩展接口
    // cbDownLoadPos 对应 fDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public LLong CLIENT_PlayBackByTimeEx(LLong lLoginID,int nChannelID,NET_TIME lpStartTime,NET_TIME lpStopTime,Pointer hWnd,Callback cbDownLoadPos,Pointer dwPosUser,Callback fDownLoadDataCallBack,Pointer dwDataUser);

    public LLong CLIENT_PlayBackByTimeEx2(LLong lLoginID,int nChannelID,NetSDKLibStructure.NET_IN_PLAY_BACK_BY_TIME_INFO pstNetIn,NetSDKLibStructure.NET_OUT_PLAY_BACK_BY_TIME_INFO pstNetOut);

    // 停止录像回放接口
    public boolean CLIENT_StopPlayBack(LLong lPlayHandle);

    // 获取回放OSD时间
    public boolean CLIENT_GetPlayBackOsdTime(LLong lPlayHandle,NET_TIME lpOsdTime,NET_TIME lpStartTime,NET_TIME lpEndTime);

    // 暂停或恢复录像回放
    // bPause: 1 - 暂停	0 - 恢复
    public boolean CLIENT_PausePlayBack(LLong lPlayHandle,int bPause);

    // 快进录像回放
    public boolean CLIENT_FastPlayBack(LLong lPlayHandle);

    // 慢进录像回放
    public boolean CLIENT_SlowPlayBack(LLong lPlayHandle);

    // 恢复正常回放速度
    public boolean CLIENT_NormalPlayBack(LLong lPlayHandle);

    // 设置录像回放速度, emSpeed 对应枚举 EM_PLAY_BACK_SPEED
    public boolean CLIENT_SetPlayBackSpeed(LLong lPlayHandle,int emSpeed);

    // 查询设备当前时间
    public boolean CLIENT_QueryDeviceTime(LLong lLoginID,NET_TIME pDeviceTime,int waittime);

    // 设置设备当前时间
    public boolean CLIENT_SetupDeviceTime(LLong lLoginID,NET_TIME pDeviceTime);

    // 获得亮度、色度、对比度、饱和度的参数
    // param1/param2/param3/param4 四个参数范围0~255
    public boolean CLIENT_ClientGetVideoEffect(LLong lPlayHandle,byte[] nBrightness,byte[] nContrast,byte[] nHue,byte[] nSaturation);

    // 设置亮度、色度、对比度、饱和度的参数
    // nBrightness/nContrast/nHue/nSaturation四个参数为 unsigned byte 范围0~255
    public boolean CLIENT_ClientSetVideoEffect(LLong lPlayHandle,byte nBrightness,byte nContrast,byte nHue,byte nSaturation);

    //------------------------用户管理-----------------------
    // 查询用户信息--扩展(info内存由用户申请释放,大小为sizeof(USER_MANAGE_INFO_EX))
    public boolean CLIENT_QueryUserInfoEx(LLong lLoginID,NetSDKLibStructure.USER_MANAGE_INFO_EX info,int waittime);

    // 查询用户信息--最大支持64通道设备
    // pReserved指向void*
    public boolean CLIENT_QueryUserInfoNew(LLong lLoginID,NetSDKLibStructure.USER_MANAGE_INFO_NEW info,Pointer pReserved,int nWaittime);

    // 设置用户信息接口--操作设备用户--最大支持64通道设备
    // opParam指向void*           subParam指向void*
    // pReserved指向void*
    // opParam（设置用户信息的输入缓冲）和subParam（设置用户信息的辅助输入缓冲）对应结构体类型USER_GROUP_INFO_NEW或USER_INFO_NEW
    public boolean CLIENT_OperateUserInfoNew(LLong lLoginID,int nOperateType,Pointer opParam,Pointer subParam,Pointer pReserved,int nWaittime);

    /**
     * // 查询设备日志，以分页方式查询(pQueryParam, pLogBuffer内存由用户申请释放)
     * CLIENT_NET_API BOOL CALL_METHOD CLIENT_QueryDeviceLog(LLONG lLoginID, QUERY_DEVICE_LOG_PARAM *pQueryParam, char *pLogBuffer, int nLogBufferLen, int *pRecLogNum, int waittime=3000);
     * pQueryParam 对应结构体QUERY_DEVICE_LOG_PARAM
     * pLogBuffer:char *
     * waitTime:默认3000
     */
    public boolean CLIENT_QueryDeviceLog(LLong lLoginID,Pointer pQueryParam,Pointer pLogBuffer,int nLogBufferLen,IntByReference pRecLogNum,int waittime);

    //----------------------语音对讲--------------------------
    // 向设备发起语音对讲请求          pfcb是用户自定义的数据回调接口, pfAudioDataCallBack 回调
    public LLong CLIENT_StartTalkEx(LLong lLoginID,Callback pfcb,Pointer dwUser);

    // 停止语音对讲        lTalkHandle语音对讲句柄，是CLIENT_StartTalkEx的返回 值
    public boolean CLIENT_StopTalkEx(LLong lTalkHandle);

    // 启动本地录音功能(只在Windows平台下有效)，录音采集出来的音频数据通过CLIENT_StartTalkEx的回调函数回调给用户，对应操作是CLIENT_RecordStopEx
    // lLoginID是CLIENT_Login的返回值
    public boolean CLIENT_RecordStartEx(LLong lLoginID);

    // 开始PC端录音
    public boolean CLIENT_RecordStart();

    // 结束PC端录音
    public boolean CLIENT_RecordStop();

    // 停止本地录音(只在Windows平台下有效)，对应操作是CLIENT_RecordStartEx。
    public boolean CLIENT_RecordStopEx(LLong lLoginID);

    // 向设备发送用户的音频数据，这里的数据可以是从CLIENT_StartTalkEx的回调接口中回调出来的数据
    public LLong CLIENT_TalkSendData(LLong lTalkHandle,Pointer pSendBuf,int dwBufSize);

    // 解码音频数据扩展接口(只在Windows平台下有效)    pAudioDataBuf是要求解码的音频数据内容
    public void CLIENT_AudioDec(Pointer pAudioDataBuf,int dwBufSize);

    public boolean CLIENT_AudioDecEx(LLong lTalkHandle,Pointer pAudioDataBuf,int dwBufSize);

    // 音频格式信息
    public class NET_AUDIO_FORMAT extends NetSDKLibStructure.SdkStructure
    {
        public byte             byFormatTag;                          // 编码类型,如0：PCM
        public short            nChannels;                            // 声道数
        public short            wBitsPerSample;                       // 采样深度
        public int              nSamplesPerSec;                       // 采样率
    }

    // 音频格式信息
    public class LPDH_AUDIO_FORMAT extends NetSDKLibStructure.SdkStructure
    {
        public byte             byFormatTag;                          // 编码类型,如0：PCM
        public short            nChannels;                            // 声道数
        public short            wBitsPerSample;                       // 采样深度
        public int              nSamplesPerSec;                       // 采样率
    }

    // 音频编码--初始化(特定标准格式->私有格式) 初始化对讲中的音频编码接口，告诉SDK内部要编码的源音频数据的音频格式，对不支持的音频格式初始化会失败
    public int CLIENT_InitAudioEncode(NET_AUDIO_FORMAT aft);

    // 进行音频的数据二次编码，从标准音频格式转换成设备支持的格式
    // 音频编码--数据编码(lpInBuf, lpOutBuf内存由用户申请释放)
    public int CLIENT_AudioEncode(LLong lTalkHandle,Pointer lpInBuf,IntByReference lpInLen,Pointer lpOutBuf,IntByReference lpOutLen);

    // 音频编码--完成退出  解码功能使用完毕后，告诉接口清理内部资源
    public int CLIENT_ReleaseAudioEncode();

    //----------------------语音对讲音频裸数据相关接口--------------------------
    /**
     * 打开语音对讲，这个接口可以从回调中得到音频裸数据，而CLIENT_StartTalkEx只能得到带音频头的数据
     * @param lLoginID
     * @param pInParam -> NET_IN_START_TALK_INFO
     * @param pOutParam -> NET_OUT_START_TALK_INFO
     * @param nWaittime
     * @return LLong
     */
    public LLong CLIENT_StartTalkByDataType(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaittime);

    /**
     * 发送语音数据到设备 返回值为发送给设备的音频流长度，-1表示接口调用失败
     * @param lTalkHandle
     * @param pInParam -> NET_IN_TALK_SEND_DATA_STREAM
     * @param pOutParam -> NET_OUT_TALK_SEND_DATA_STREAM
     * @return LLong 返回值为发送给设备的音频流长度，-1表示接口调用失败
     */
    public LLong CLIENT_TalkSendDataByStream(LLong lTalkHandle,Pointer pInParam,Pointer pOutParam);

    /**
     * 发送语音文件中的音频数据到设备 成功返回 lTalkHandle， 失败返回 0
     * @param lTalkHandle
     * @param pInParam -> NET_IN_TALK_SEND_DATA_FILE
     * @param pOutParam -> NET_OUT_TALK_SEND_DATA_FILE
     * @return LLong 成功返回 lTalkHandle， 失败返回 0
     */
    public LLong CLIENT_TalkSendDataByFile(LLong lTalkHandle,Pointer pInParam,Pointer pOutParam);

    /**
     * 停止发送音频文件
     */
    public boolean CLIENT_StopTalkSendDataByFile(LLong lTalkHandle);

     /**
      * 用户自定义的数据回调   lTalkHandle是CLIENT_StartTalkByDataType的返回值
      * @param stAudioInfo -> NET_AUDIO_DATA_CB_INFO
      * @param byAudioFlag -> 音频数据来源，参考枚举 EM_AUDIO_SOURCE_FLAG
      * @param dwUser -> 用户自定义数据
      */
    public interface fAudioDataCallBackEx extends Callback {
        public void invoke(LLong lTalkHandle,NET_AUDIO_DATA_CB_INFO stAudioInfo,int emAudioFlag,Pointer dwUser);
    }

    /**
     * 音频文件发送进度回调函数
     * @param lTalkHandle
     * @param dwTotalSize
     * @param dwSendSize
     */
    public interface fTalkSendPosCallBack extends Callback {
        public void invoke(LLong lTalkHandle,int dwTotalSize,int dwSendSize,Pointer dwUser);
    }

    //-------------------允许名单-------------------------
    // 按查询条件查询记录          pInParam查询记录参数        pOutParam返回查询句柄
    // 可以先调用本接口获得查询句柄，再调用  CLIENT_FindNextRecord函数获取记录列表，查询完毕可以调用CLIENT_FindRecordClose关闭查询句柄。
    public boolean CLIENT_FindRecord(LLong lLoginID, NetSDKLibStructure.NET_IN_FIND_RECORD_PARAM pInParam, NetSDKLibStructure.NET_OUT_FIND_RECORD_PARAM pOutParam, int waittime);

    // 查找记录:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值小于nFilecount则相应时间段内的文件查询完毕
    public boolean CLIENT_FindNextRecord(NetSDKLibStructure.NET_IN_FIND_NEXT_RECORD_PARAM pInParam,NetSDKLibStructure.NET_OUT_FIND_NEXT_RECORD_PARAM pOutParam,int waittime);

    // 结束记录查找,lFindHandle是CLIENT_FindRecord的返回值
    public boolean CLIENT_FindRecordClose(LLong lFindHandle);

    // 查找记录条数,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_QueryRecordCount(NetSDKLibStructure.NET_IN_QUEYT_RECORD_COUNT_PARAM pInParam,NetSDKLibStructure.NET_OUT_QUEYT_RECORD_COUNT_PARAM pOutParam,int waittime);

    // 禁止/允许名单操作 ,pstOutParam = null;
    public boolean CLIENT_OperateTrafficList(LLong lLoginID,NetSDKLibStructure.NET_IN_OPERATE_TRAFFIC_LIST_RECORD pstInParam,NetSDKLibStructure.NET_OUT_OPERATE_TRAFFIC_LIST_RECORD pstOutParam,int waittime);

    // 文件上传控制接口，允许名单上传需要三个步骤配合使用，CLIENT_FileTransmit的 NET_DEV_BLACKWHITETRANS_START、  NET_DEV_BLACKWHITETRANS_SEND、   NET_DEV_BLACKWHITETRANS_STOP，如下所示
    // fTransFileCallBack 回调
    public LLong CLIENT_FileTransmit(LLong lLoginID,int nTransType,Pointer szInBuf,int nInBufLen,Callback cbTransFile,Pointer dwUserData,int waittime);

    // 查询设备信息
    public boolean CLIENT_QueryDevInfo(LLong lLoginID,int nQueryType,Pointer pInBuf,Pointer pOutBuf,Pointer pReservedL,int nWaitTime);

    // ------------------车载GPS-------------------------
    // 设置GPS订阅回调函数--扩展, fGPSRevEx 回调
    public void CLIENT_SetSubcribeGPSCallBackEX(Callback OnGPSMessage,Pointer dwUser);

    // 设置GPS订阅回调函数--扩展2， fGPSRevEx2 回调
    public void CLIENT_SetSubcribeGPSCallBackEX2(Callback OnGPSMessage,Pointer dwUser);

    // GPS信息订阅
    // bStart:表明是订阅还是取消          InterTime:订阅时间内GPS发送频率(单位秒)
    // KeepTime:订阅持续时间(单位秒) 值为-1时,订阅时间为极大值,可视为永久订阅
    // 订阅时间内GPS发送频率(单位秒)
    public boolean CLIENT_SubcribeGPS(LLong lLoginID,int bStart,int KeepTime,int InterTime);

    // 设置文件长度, pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_PreUploadRemoteFile(LLong lLoginID,NET_IN_PRE_UPLOAD_REMOTE_FILE pInParam,NET_OUT_PRE_UPLOAD_REMOTE_FILE pOutParam,int nWaitTime);

    // 同步文件上传, 只适用于小文件
    public boolean CLIENT_UploadRemoteFile(LLong lLoginID,NetSDKLibStructure.NET_IN_UPLOAD_REMOTE_FILE pInParam,NetSDKLibStructure.NET_OUT_UPLOAD_REMOTE_FILE pOutParam,int nWaitTime);

    // 显示目录中文件和子目录,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_ListRemoteFile(LLong lLoginID,NetSDKLibStructure.NET_IN_LIST_REMOTE_FILE pInParam,NetSDKLibStructure.NET_OUT_LIST_REMOTE_FILE pOutParam,int nWaitTime);

    // 删除文件或目录,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_RemoveRemoteFiles(LLong lLoginID,NetSDKLibStructure.NET_IN_REMOVE_REMOTE_FILES pInParam,NetSDKLibStructure.NET_OUT_REMOVE_REMOTE_FILES pOutParam,int nWaitTime);

    // 过车记录订阅
    public LLong CLIENT_ParkingControlAttachRecord(LLong lLoginID,NetSDKLibStructure.NET_IN_PARKING_CONTROL_PARAM pInParam,NetSDKLibStructure.NET_OUT_PARKING_CONTROL_PARAM pOutParam,int nWaitTime);

    // 取消过车记录订阅
    public boolean CLIENT_ParkingControlDetachRecord(LLong lAttachHandle);

    // 开始过车记录查询
    public LLong CLIENT_ParkingControlStartFind(LLong lLoginID,NetSDKLibStructure.NET_IN_PARKING_CONTROL_START_FIND_PARAM pInParam,NetSDKLibStructure.NET_OUT_PARKING_CONTROL_START_FIND_PARAM pOutParam,int waittime);

    // 获取过车记录
    public boolean CLIENT_ParkingControlDoFind(LLong lFindeHandle,NetSDKLibStructure.NET_IN_PARKING_CONTROL_DO_FIND_PARAM pInParam,NetSDKLibStructure.NET_OUT_PARKING_CONTROL_DO_FIND_PARAM pOutParam,int waittime);

    // 结束过车记录查询
    public boolean CLIENT_ParkingControlStopFind(LLong lFindHandle);

    // 车位状态订阅,pInParam与pOutParam内存由用户申请释放
    public LLong CLIENT_ParkingControlAttachParkInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_PARK_INFO_PARAM pInParam,NetSDKLibStructure.NET_OUT_PARK_INFO_PARAM pOutParam,int nWaitTime);

    // 取消车位状态订阅
    public boolean CLIENT_ParkingControlDetachParkInfo(LLong lAttachHandle);

    // 清除异常车位车辆信息 NET_IN_REMOVE_PARKING_CAR_INFO, NET_OUT_REMOVE_PARKING_CAR_INFO
    public boolean CLIENT_RemoveParkingCarInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 电源控制,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_PowerControl(LLong lLoginID,NetSDKLibStructure.NET_IN_WM_POWER_CTRL pInParam,NetSDKLibStructure.NET_OUT_WM_POWER_CTRL pOutParam,int nWaitTime);

    // 载入/保存预案,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_LoadMonitorWallCollection(LLong lLoginID,NetSDKLibStructure.NET_IN_WM_LOAD_COLLECTION pInParam,NetSDKLibStructure.NET_OUT_WM_LOAD_COLLECTION pOutParam,int nWaitTime);

    public boolean CLIENT_SaveMonitorWallCollection(LLong lLoginID,NetSDKLibStructure.NET_IN_WM_SAVE_COLLECTION pInParam,NetSDKLibStructure.NET_OUT_WM_SAVE_COLLECTION pOutParam,int nWaitTime);

    // 获取电视墙预案,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_GetMonitorWallCollections(LLong lLoginID,NetSDKLibStructure.NET_IN_WM_GET_COLLECTIONS pInParam,NetSDKLibStructure.NET_OUT_WM_GET_COLLECTIONS pOutParam,int nWaitTime);

    // 查询/设置显示源(pstuSplitSrc内存由用户申请释放),  nWindow为-1表示所有窗口 ; pstuSplitSrc 对应 NET_SPLIT_SOURCE 指针
    public boolean CLIENT_GetSplitSource(LLong lLoginID,int nChannel,int nWindow,NetSDKLibStructure.NET_SPLIT_SOURCE[] pstuSplitSrc,int nMaxCount,IntByReference pnRetCount,int nWaitTime);

    public boolean CLIENT_SetSplitSource(LLong lLoginID,int nChannel,int nWindow,NetSDKLibStructure.NET_SPLIT_SOURCE pstuSplitSrc,int nSrcCount,int nWaitTime);

    // 设置显示源, 支持同时设置多个窗口(pInparam, pOutParam内存由用户申请释放)
    public boolean CLIENT_SplitSetMultiSource(LLong lLoginID,NetSDKLibStructure.NET_IN_SPLIT_SET_MULTI_SOURCE pInParam,NetSDKLibStructure.NET_OUT_SPLIT_SET_MULTI_SOURCE pOutParam,int nWaitTime);

    // 查询矩阵子卡信息(pstuCardList内存由用户申请释放)
    public boolean CLIENT_QueryMatrixCardInfo(LLong lLoginID,NetSDKLibStructure.NET_MATRIX_CARD_LIST pstuCardList,int nWaitTime);

    // 开始查找录像文件帧信息(pInParam, pOutParam内存由用户申请释放)
    public boolean CLIENT_FindFrameInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_FIND_FRAMEINFO_PRAM pInParam,NetSDKLibStructure.NET_OUT_FIND_FRAMEINFO_PRAM pOutParam,int nWaitTime);

    // 获取标签信息
    public boolean CLIENT_FileStreamGetTags(LLong lFindHandle,NetSDKLibStructure.NET_IN_FILE_STREAM_GET_TAGS_INFO pInParam,NetSDKLibStructure.NET_OUT_FILE_STREAM_GET_TAGS_INFO pOutParam,int nWaitTime);

    // 设置标签信息
    public boolean CLIENT_FileStreamSetTags(LLong lFindHandle,NetSDKLibStructure.NET_IN_FILE_STREAM_TAGS_INFO pInParam,NetSDKLibStructure.NET_OUT_FILE_STREAM_TAGS_INFO pOutParam,int nWaitTime);

    // 查询/设置分割模式(pstuSplitInfo内存由用户申请释放)
    public boolean CLIENT_GetSplitMode(LLong lLoginID,int nChannel,NetSDKLibStructure.NET_SPLIT_MODE_INFO pstuSplitInfo,int nWaitTime);

    public boolean CLIENT_SetSplitMode(LLong lLoginID,int nChannel,NetSDKLibStructure.NET_SPLIT_MODE_INFO pstuSplitInfo,int nWaitTime);

    // 开窗/关窗(pInparam, pOutParam内存由用户申请释放)
    public boolean CLIENT_OpenSplitWindow(LLong lLoginID,NetSDKLibStructure.NET_IN_SPLIT_OPEN_WINDOW pInParam,NetSDKLibStructure.NET_OUT_SPLIT_OPEN_WINDOW pOutParam,int nWaitTime);

    public boolean CLIENT_CloseSplitWindow(LLong lLoginID,NetSDKLibStructure.NET_IN_SPLIT_CLOSE_WINDOW pInParam,NetSDKLibStructure.NET_OUT_SPLIT_CLOSE_WINDOW pOutParam,int nWaitTime);

    // 获取当前显示的窗口信息(pInparam, pOutParam内存由用户申请释放)
    public boolean CLIENT_GetSplitWindowsInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_SPLIT_GET_WINDOWS pInParam,NetSDKLibStructure.NET_OUT_SPLIT_GET_WINDOWS pOutParam,int nWaitTime);

    // 查询分割能力(pstuCaps内存由用户申请释放)
    public boolean CLIENT_GetSplitCaps(LLong lLoginID,int nChannel,NetSDKLibStructure.NET_SPLIT_CAPS pstuCaps,int nWaitTime);

    // 下位矩阵切换(pInparam, pOutParam内存由用户申请释放)
    public boolean CLIENT_MatrixSwitch(LLong lLoginID,NetSDKLibStructure.NET_IN_MATRIX_SWITCH pInParam,NetSDKLibStructure.NET_OUT_MATRIX_SWITCH pOutParam,int nWaitTime);

    // 打开刻录会话, 返回刻录会话句柄,pstInParam与pstOutParam内存由用户申请释放
    public LLong CLIENT_StartBurnSession(LLong lLoginID,NetSDKLibStructure.NET_IN_START_BURN_SESSION pstInParam,NetSDKLibStructure.NET_OUT_START_BURN_SESSION pstOutParam,int nWaitTime);

    // 关闭刻录会话
    public boolean CLIENT_StopBurnSession(LLong lBurnSession);

    //------------有盘/无盘刻录----lBurnSession 是 CLIENT_StartBurnSession返回的句柄//
    // 开始刻录,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_StartBurn(LLong lBurnSession,NetSDKLibStructure.NET_IN_START_BURN pstInParam,NetSDKLibStructure.NET_OUT_START_BURN pstOutParam,int nWaitTime);

    // 停止刻录
    public boolean CLIENT_StopBurn(LLong lBurnSession);

    // 暂停/恢复刻录
    public boolean CLIENT_PauseBurn(LLong lBurnSession,int bPause);

    // 获取刻录状态
    public boolean CLIENT_BurnGetState(LLong lBurnSession,NetSDKLibStructure.NET_IN_BURN_GET_STATE pstInParam,NetSDKLibStructure.NET_OUT_BURN_GET_STATE pstOutParam,int nWaitTime);

    // 监听刻录状态,pstInParam与pstOutParam内存由用户申请释放
    public LLong CLIENT_AttachBurnState(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_STATE pstInParam,NetSDKLibStructure.NET_OUT_ATTACH_STATE pstOutParam,int nWaitTime);

    // 取消监听刻录状态,lAttachHandle是CLIENT_AttachBurnState返回值
    public boolean CLIENT_DetachBurnState(LLong lAttachHandle);

    // 开始刻录附件上传
    // 刻录上传开始 返回此次上传操作句柄, 注意以下接口不能在fAttachBurnStateCB回调函数里面调用,pstInParam与pstOutParam内存由用户申请释放
    public LLong CLIENT_StartUploadFileBurned(LLong lLoginID,NetSDKLibStructure.NET_IN_FILEBURNED_START pstInParam,NetSDKLibStructure.NET_OUT_FILEBURNED_START pstOutParam,int nWaitTime);

    //上传刻录附件,lUploadHandle是CLIENT_StartUploadFileBurned返回值
    public boolean CLIENT_SendFileBurned(LLong lUploadHandle);

    //停止刻录附件上传
    // 刻录上传停止,lUploadHandle是CLIENT_StartUploadFileBurned返回值,此接口不能在fBurnFileCallBack回调函数中调用
    public boolean CLIENT_StopUploadFileBurned(LLong lUploadHandle);

    // 下载指定的智能分析数据 - 图片, fDownLoadPosCallBack 回调
    // emType 参考 EM_FILE_QUERY_TYPE
    public LLong CLIENT_DownloadMediaFile(LLong lLoginID,int emType,Pointer lpMediaFileInfo,String sSavedFileName,Callback cbDownLoadPos,Pointer dwUserData,Pointer reserved);

    // 停止下载数据
    public boolean CLIENT_StopDownloadMediaFile(LLong lFileHandle);

    // 下发通知到设备 接口, 以emNotifyType来区分下发的通知类型, pInParam 和 pOutParam 都由用户来分配和释放, emNotifyType对应结构体 NET_EM_NOTIFY_TYPE
    public boolean CLIENT_SendNotifyToDev(LLong lLoginID,int emNotifyType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 查询IO状态(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小), emType 参考 NET_IOTYPE
    public boolean CLIENT_QueryIOControlState(LLong lLoginID,int emType,Pointer pState,int maxlen,IntByReference nIOCount,int waittime);

    // IO控制(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小),emType 参考 NET_IOTYPE
    public boolean CLIENT_IOControl(LLong lLoginID,int emType,Pointer pState,int maxlen);

    // 订阅监测点位信息,pInParam与pOutParam内存由用户申请释放
    public LLong CLIENT_SCADAAttachInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_SCADA_ATTACH_INFO pInParam,NetSDKLibStructure.NET_OUT_SCADA_ATTACH_INFO pOutParam,int nWaitTime);

    // 取消监测点位信息订阅
    public boolean CLIENT_SCADADetachInfo(LLong lAttachHandle);

    // 创建透明串口通道,TransComType高2个字节表示串口序号,低2个字节表示串口类型,目前类型支持 0：串口(232), 1:485
    // baudrate 串口的波特率，1~8分别表示1200，2400，4800，9600，19200，38400，57600，115200
    // databits 串口的数据位 4~8表示4位~8位
    // stopbits 串口的停止位   232串口 ： 数值0 代表停止位1; 数值1 代表停止位1.5; 数值2 代表停止位2.    485串口 ： 数值1 代表停止位1; 数值2 代表停止位2.
    // parity 串口的检验位，0：无校验，1：奇校验；2：偶校验;
    // cbTransCom 串口数据回调，回调出前端设备发过来的信息
    // fTransComCallBack 回调
    public LLong CLIENT_CreateTransComChannel(LLong lLoginID,int TransComType,int baudrate,int databits,int stopbits,int parity,Callback cbTransCom,Pointer dwUser);

    // 透明串口发送数据(pBuffer内存由用户申请释放)
    public boolean CLIENT_SendTransComData(LLong lTransComChannel,byte[] pBuffer,int dwBufSize);

    // 释放通明串口通道
    public boolean CLIENT_DestroyTransComChannel(LLong lTransComChannel);

    // 查询透明串口状态(pCommState内存由用户申请释放), TransComType 低2个字节表示串口类型， 0:串口(232)， 1:485口；高2个字节表示串口通道号，从0开始
    public boolean CLIENT_QueryTransComParams(LLong lLoginID,int TransComType,NetSDKLibStructure.NET_COMM_STATE pCommState,int nWaitTime);

    // 订阅智能分析进度（适用于视频分析源为录像文件时）,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_AttachVideoAnalyseState(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_VIDEOANALYSE_STATE pstInParam,NetSDKLibStructure.NET_OUT_ATTACH_VIDEOANALYSE_STATE pstOutParam,int nWaittime);

    // 停止订阅
    public boolean CLIENT_DetachVideoAnalyseState(LLong lAttachHandle);

    // 抓图, hPlayHandle为预览或回放句柄
    public boolean CLIENT_CapturePicture(LLong hPlayHandle,String pchPicFileName);

    // 抓图, hPlayHandle为预览或回放句柄
    public boolean CLIENT_CapturePictureEx(LLong hPlayHandle,String pchPicFileName,int eFormat);

    // 获取设备自检信息,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_GetSelfCheckInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_SELTCHECK_INFO pInParam,NetSDKLibStructure.NET_SELFCHECK_INFO pOutParam,int nWaitTime);

    // 主动注册功能,启动服务；nTimeout参数已无效 .
    // cbListen对象为  fServiceCallBack 子类
    public LLong CLIENT_ListenServer(String ip,int port,int nTimeout,Callback cbListen,Pointer dwUserData);

    // 停止服务
    public boolean CLIENT_StopListenServer(LLong lServerHandle);

    // 指定回调数据类型 实施预览(预览), 数据回调函数 cbRealData 中得到的码流类型为 emDataType 所指定的类型
    public LLong CLIENT_RealPlayByDataType(LLong lLoginID,NetSDKLibStructure.NET_IN_REALPLAY_BY_DATA_TYPE pstInParam,NetSDKLibStructure.NET_OUT_REALPLAY_BY_DATA_TYPE pstOutParam,int dwWaitTime);

    // 指定回调数据格式  开始回放,  数据回调函数 fDownLoadDataCallBack 中得到的码流类型为 emDataType 所指定的类型
    public LLong CLIENT_PlayBackByDataType(LLong lLoginID,NetSDKLibStructure.NET_IN_PLAYBACK_BY_DATA_TYPE pstInParam,NetSDKLibStructure.NET_OUT_PLAYBACK_BY_DATA_TYPE pstOutParam,int dwWaitTime);

    // 指定码流类型 开始下载, 下载得到的文件和数据回调函数 fDownLoadDataCallBack 中得到的码流类型均为 emDataType 所指定的类型
    // NET_IN_DOWNLOAD_BY_DATA_TYPE pstInParam, NET_OUT_DOWNLOAD_BY_DATA_TYPE pstOutParam
    public LLong CLIENT_DownloadByDataType(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int dwWaitTime);

    /************************************************************************/
    /*                            BUS订阅                                   */
    /************************************************************************/
    // 订阅Bus状态,pstuInBus与pstuOutBus内存由用户申请释放
    public LLong CLIENT_AttachBusState(LLong lLoginID,NetSDKLibStructure.NET_IN_BUS_ATTACH pstuInBus,NetSDKLibStructure.NET_OUT_BUS_ATTACH pstuOutBus,int nWaitTime);

    // 停止订阅Bus状态,lAttachHandle是CLIENT_AttachBusState返回值
    public boolean CLIENT_DetachBusState(LLong lAttachHandle);

    //订阅事件重传,pInParam内存由用户申请释放
    public LLong CLIENT_AttachEventRestore(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_EVENT_RESTORE pInParam,int nWaitTime);

    // 停止订阅事件重传,pInParam内存由用户申请释放
    public boolean CLIENT_DetachEventRestore(LLong lAttachHandle);

    // 设置GPS温湿度订阅回调函数, fGPSTempHumidityRev
    public void CLIENT_SetSubcribeGPSTHCallBack(Callback OnGPSMessage,Pointer dwUser);

    // GPS温湿度信息订阅, bStart为BOOL类型
    public boolean CLIENT_SubcribeGPSTempHumidity(LLong lLoginID,int bStart,int InterTime,Pointer Reserved);

    // 人脸信息记录操作函数
    public boolean CLIENT_FaceInfoOpreate(LLong lLoginID,int emType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 下发人脸图片信息
    public boolean CLIENT_DeliverUserFacePicture(LLong lLoginID,NET_IN_DELIVER_USER_PICTURE pInParam,NET_OUT_DELIVER_USER_PICTURE pOutParam,int nWaitTime);

    //开始查询人脸信息
    public LLong CLIENT_StartFindFaceInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_FACEINFO_START_FIND pstIn,NetSDKLibStructure.NET_OUT_FACEINFO_START_FIND pstOut,int nWaitTime);

    //获取人脸信息
    public boolean CLIENT_DoFindFaceInfo(LLong lFindHandle,NetSDKLibStructure.NET_IN_FACEINFO_DO_FIND pstIn,NetSDKLibStructure.NET_OUT_FACEINFO_DO_FIND pstOut,int nWaitTime);

    //停止查询人脸信息
    public boolean CLIENT_StopFindFaceInfo(LLong lFindHandle);

    /***********************************************************************************
     *						                     诱导屏相关接口									  *
     **********************************************************************************/
    // 设置诱导屏配置信息接口
    public boolean CLIENT_SetGuideScreenCfg(LLong lLoginID,NetSDKLibStructure.NET_IN_SET_GUIDESCREEN_CFG pInParam,NetSDKLibStructure.NET_OUT_SET_GUIDESCREEN_CFG pstOutPqram,int nWaitTime);

    // 添加一个节目信息到诱导屏
    public boolean CLIENT_AddOneProgramme(LLong lLoginID, NetSDKLibStructure.NET_IN_ADD_ONE_PROGRAMME pInParam, NetSDKLibStructure.NET_OUT_ADD_ONE_PROGRAMME pOutParam, int nWaitTime);

    // 通过节目ID 修改节目
    public boolean CLIENT_ModifyOneProgrammeByID(LLong lLoginID, NetSDKLibStructure.NET_IN_MODIFY_ONE_PROGRAMME pInParam, NetSDKLibStructure.NET_OUT_MODIFY_ONE_PROGRAMME pOutParam, int nWaitTime);

    // 批量删除节目信息
    public boolean CLIENT_DelMultiProgrammesById(LLong lLoginID,NetSDKLibStructure.NET_IN_DEL_PROGRAMMES pInParam,NetSDKLibStructure.NET_OUT_DEL_PROGRAMMES pOutParam,int nWaitTime);

    // 增加一个即时节目计划
    public boolean CLIENT_AddOneImmediProgrammePlan(LLong lLoginID,NetSDKLibStructure.NET_IN_ADD_IMME_PROGRAMMEPLAN pInParam,NetSDKLibStructure.NET_OUT_ADD_PROGRAMMEPLAN pOutParam,int nWaitTime);

    // 修改一个即时节目计划
    public boolean CLIENT_ModifyOneImmediProgrammePlan(LLong lLoginID,NetSDKLibStructure.NET_IN_MODIFY_IMME_PROGRAMMEPLAN pInParam,NetSDKLibStructure.NET_OUT_MODIFY_IMME_PROGRAMMEPLAN pOutParam,int nWaitTime);

    // 增加一个定时节目计划
    public boolean CLIENT_AddOneTimerProgrammePlan(LLong lLoginID, NetSDKLibStructure.NET_IN_ADD_TIMER_PROGRAMMEPLAN pInParam, NetSDKLibStructure.NET_OUT_ADD_PROGRAMMEPLAN pOutParam, int nWaitTime);

    // 修改一个定时节目计划
    public boolean CLIENT_ModifyOneTimerProgrammePlan(LLong lLoginID, NetSDKLibStructure.NET_IN_MODIFY_TIMER_PROGRAMMEPLAN pInParam, NetSDKLibStructure.NET_OUT_MODIFY_TIMER_PROGRAMMEPLAN pOutParam, int nWaitTime);

    // 删除多个节目计划
    public boolean CLIENT_DelMultiProgrammePlans(LLong lLoginID,NetSDKLibStructure.NET_IN_DEL_PROGRAMMEPLANS pInParam,NetSDKLibStructure.NET_OUT_DEL_PROGRAMMEPLANS pOutParam,int nWaitTime);

    // 通过诱导屏ID 获取诱导屏配置信息
    public boolean CLIENT_GetOneGuideScreenCfgById(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_GUIDESCREEN_CFG_BYID pInParam,NetSDKLibStructure.NET_OUT_GET_GUIDESCREEN_CFG_BYID pOutParam,int nWaitTime);

    // 获取所有诱导屏配置信息
    public boolean CLIENT_GetAllGuideScreenCfg(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ALL_GUIDESCREEN_CFG pInParam,NetSDKLibStructure.NET_OUT_GET_ALL_GUIDESCREEN_CFG pOutParam,int nWaitTime);

    // 通过节目ID 获取节目信息
    public boolean CLIENT_GetOneProgrammeById(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_PROGRAMME_BYID pInParam,NetSDKLibStructure.NET_OUT_GET_PROGRAMME_BYID pOutParam,int nWaitTime);

    // 获取所有节目信息
    public boolean CLIENT_GetAllProgrammes(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ALL_PROGRAMMES pInParam,NetSDKLibStructure.NET_OUT_GET_ALL_PROGRAMMES pOutParam,int nWaitTime);

    // 获取所有节目的简要信息
    public boolean CLIENT_GetAllBrieflyProgrammes(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ALL_BRIEFLYPROGRAMMES pInParam,NetSDKLibStructure.NET_OUT_GET_ALL_BRIEFLYPROGRAMMES pOutParam,int nWaitTime);

    // 获取所有节目计划信息
    public boolean CLIENT_GetAllProgrammePlans(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ALL_PROGRAMMEPLANS pInParam,NetSDKLibStructure.NET_OUT_GET_ALL_PROGRAMMEPLANS pOutParam,int nWaitTime);

    // 通过节目计划ID 获取节目计划
    public boolean CLIENT_GetOneProgrammePlanByID(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_PROGRAMMEPLAN_BYID pInParam,NetSDKLibStructure.NET_OUT_GET_PROGRAMMEPLAN_BYID pOutParam,int nWaitTime);

    // 设置光带状态信息
    public boolean CLIENT_SetGuideScreenGDStatus(LLong lLoginID,NetSDKLibStructure.NET_IN_SET_GD_STATUS pInParam,NetSDKLibStructure.NET_OUT_SET_GD_STATUS pOutParam,int nWaitTime);

    /***********************************************************************************
     *						                  播放盒与广告机的节目操作接口					      *
     **********************************************************************************/
    // 获取播放盒上全部节目信息
    public boolean CLIENT_GetAllProgramOnPlayBox(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ALL_PLAYBOX_PROGRAM pInParam,NetSDKLibStructure.NET_OUT_GET_ALL_PLAYBOX_PROGRAM pOutParam,int nWaitTime);

    // 通过programme ID 获取播放盒上对应的节目信息
    public boolean CLIENT_GetOneProgramByIdOnPlayBox(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_PLAYBOX_PROGRAM_BYID pInParam,NetSDKLibStructure.NET_OUT_GET_PLAYBOX_PROGRAM_BYID pOutParam,int nWaitTime);

    // 在播放盒上添加一个节目
    public boolean CLIENT_AddOneProgramToPlayBox(LLong lLoginID,NetSDKLibStructure.NET_IN_ADD_ONE_PLAYBOX_PRAGROM pInParam,NetSDKLibStructure.NET_OUT_ADD_ONE_PLAYBOX_PRAGROM pOutParam,int nWaitTime);

    // 在播放盒上修改指定ID的节目信息
    public boolean CLIENT_ModifyProgramOnPlayBoxById(LLong lLoginID,NetSDKLibStructure.NET_IN_MODIFY_PLAYBOX_PROGRAM_BYID pInParam,NetSDKLibStructure.NET_OUT_MODIFY_PLAYBOX_PROGRAM_BYID pOutParam,int nWaitTime);

    // 获取配置信息(szOutBuffer内存由用户申请释放, 具体见枚举类型 NET_EM_CFG_OPERATE_TYPE 说明)
    public boolean CLIENT_GetConfig(LLong lLoginID,int emCfgOpType,int nChannelID,Pointer szOutBuffer,int dwOutBufferSize,int waittime,Pointer reserve);

    // 设置配置信息(szInBuffer内存由用户申请释放, 具体见枚举类型 NET_EM_CFG_OPERATE_TYPE 说明)
    public boolean CLIENT_SetConfig(LLong lLoginID,int emCfgOpType,int nChannelID,Pointer szInBuffer,int dwInBufferSize,int waittime,IntByReference restart,Pointer reserve);

    // 显示私有数据，例如规则框，规则框报警，移动侦测等       lPlayHandle:播放句柄      bTrue=1 打开, bTrue= 0 关闭
    public boolean CLIENT_RenderPrivateData(LLong lPlayHandle,int bTrue);

    // 按设备信息添加显示源,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_MatrixAddCamerasByDevice(LLong lLoginID,NetSDKLibStructure.NET_IN_ADD_LOGIC_BYDEVICE_CAMERA pInParam,NetSDKLibStructure.NET_OUT_ADD_LOGIC_BYDEVICE_CAMERA pOutParam,int nWaitTime);

    // 订阅监测点位报警信息,pInParam与pOutParam内存由用户申请释放
    public LLong CLIENT_SCADAAlarmAttachInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_SCADA_ALARM_ATTACH_INFO pInParam,NetSDKLibStructure.NET_OUT_SCADA_ALARM_ATTACH_INFO pOutParam,int nWaitTime);

    // 取消订阅监测点位报警信息
    public boolean CLIENT_SCADAAlarmDetachInfo(LLong lAttachHandle);

    /***********************************************************************************
     *						           IVSS设备添加相关接口				             *
     **********************************************************************************/
    // 注册设备状态回调
    public LLong CLIENT_AttachDeviceState(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_DEVICE_STATE pInParam,NetSDKLibStructure.NET_OUT_ATTACH_DEVICE_STATE pOutParam,int nWaitTime);

    // 注销设备状态回调
    public boolean CLIENT_DetachDeviceState(LLong lAttachHandle);

    // 添加设备
    public boolean CLIENT_AsyncAddDevice(LLong lLoginID,NetSDKLibStructure.NET_IN_ASYNC_ADD_DEVICE pInParam,NetSDKLibStructure.NET_OUT_ASYNC_ADD_DEVICE pOutParam,int nWaitTime);

    // 注册添加设备回调
    public LLong CLIENT_AttachAddDevice(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTACH_ADD_DEVICE pInParam,NetSDKLibStructure.NET_OUT_ATTACH_ADD_DEVICE pOutParam,int nWaitTime);

    // 注销添加设备回调
    public boolean CLIENT_DetachAddDevice(LLong lAttachHandle);

    // 获取添加中的设备状态
    public boolean CLIENT_GetAddDeviceInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_ADD_DEVICE_LIST_INFO pInParam,NetSDKLibStructure.NET_OUT_GET_ADD_DEVICE_LIST_INFO pOutParam,int nWaitTime);

    // 获取已添加的设备状态
    public boolean CLIENT_GetDeviceInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_DEVICE_LIST_INFO pInParam,NetSDKLibStructure.NET_OUT_GET_DEVICE_LIST_INFO pOutParam,int nWaitTime);

    // 设置连接通道
    public boolean CLIENT_SetConnectChannel(LLong lLoginID,NetSDKLibStructure.NET_IN_SET_CONNECT_CHANNEL pInParam,NetSDKLibStructure.NET_OUT_SET_CONNECT_CHANNEL pOutParam,int nWaitTime);

    // 获取设备通道信息
    public boolean CLIENT_GetChannelInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_CHANNEL_INFO pInParam,NetSDKLibStructure.NET_OUT_GET_CHANNEL_INFO pOutParam,int nWaitTime);

    // 删除设备
    public boolean CLIENT_RemoveDevice(LLong lLoginID,NetSDKLibStructure.NET_IN_REMOVE_DEVICE pInParam,NetSDKLibStructure.NET_OUT_REMOVE_DEVICE pOutParam,int nWaitTime);

    // 中止添加设备任务
    public boolean CLIENT_CancelAddDeviceTask(LLong lLoginID,NetSDKLibStructure.NET_IN_CANCEL_ADD_TASK pInParam,NetSDKLibStructure.NET_OUT_CANCEL_ADD_TASK pOutParam,int nWaitTime);

    // 确认添加设备任务
    public boolean CLIENT_ConfirmAddDeviceTask(LLong lLoginID,NetSDKLibStructure.NET_IN_CONFIRM_ADD_TASK pInParam,NetSDKLibStructure.NET_OUT_CONFIRM_ADD_TASK pOutParam,int nWaitTime);

    // 球机，地磁车位同步上报车位信息,如有车停入、或车从车位开出
    public boolean CLIENT_SyncParkingInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_SYNC_PARKING_INFO pInParam,NetSDKLibStructure.NET_OUT_SYNC_PARKING_INFO pOutParam,int nWaitTime);

    // 初始化账户
    public boolean CLIENT_InitDevAccount(NetSDKLibStructure.NET_IN_INIT_DEVICE_ACCOUNT pInitAccountIn,NetSDKLibStructure.NET_OUT_INIT_DEVICE_ACCOUNT pInitAccountOut,int dwWaitTime,String szLocalIp);

    // 根据设备IP初始化账户
    public boolean CLIENT_InitDevAccountByIP(NetSDKLibStructure.NET_IN_INIT_DEVICE_ACCOUNT pInitAccountIn,NetSDKLibStructure.NET_OUT_INIT_DEVICE_ACCOUNT pInitAccountOut,int dwWaitTime,String szLocalIp,String szDeviceIP);

    public boolean CLIENT_ModifyDevice(NetSDKLibStructure.DEVICE_NET_INFO_EX pDevNetInfo,int nWaitTime,IntByReference iError,String szLocalIp);

    /**
     * 门禁控制器操作接口
     * @param lLoginID 登录句柄
     * @param emtype 门禁控制器操作类型, 对应 枚举{@link NetSDKLibStructure.NET_EM_ACCESS_CTL_MANAGER}
     * @param pstInParam 入参, 根据 emtype 来填
     * @param pstOutParam 出参, 根据 emtype 来填
     * @param nWaitTime 超时等待时间
     * @return true：成功    false：失败
     */
    public boolean CLIENT_OperateAccessControlManager(LLong lLoginID,int emtype,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 设置安全密钥(播放加密码流使用)
     * @param lPlayHandle 拉流句柄
     * @param szKey 密钥
     * @param nKeyLen 密钥的长度
     * @return true：成功    false：失败
     */
    public boolean CLIENT_SetSecurityKey(LLong lPlayHandle,String szKey,int nKeyLen);

    /***********************************************************************************
     *						           	  考勤机相关接口						         *
     **********************************************************************************/
    //考勤新增加用户
    public boolean CLIENT_Attendance_AddUser(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTENDANCE_ADDUSER pstuInAddUser,NetSDKLibStructure.NET_OUT_ATTENDANCE_ADDUSER pstuOutAddUser,int nWaitTime);

    //考勤删除用户
    public boolean CLIENT_Attendance_DelUser(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTENDANCE_DELUSER pstuInDelUser,NetSDKLibStructure.NET_OUT_ATTENDANCE_DELUSER pstuOutDelUser,int nWaitTime);

    //考勤修改用户信息
    public boolean CLIENT_Attendance_ModifyUser(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTENDANCE_ModifyUSER pstuInModifyUser,NetSDKLibStructure.NET_OUT_ATTENDANCE_ModifyUSER pstuOutModifyUser,int nWaitTime);

    //考勤获取用户信息
    public boolean CLIENT_Attendance_GetUser(LLong lLoginID,NetSDKLibStructure.NET_IN_ATTENDANCE_GetUSER pstuInGetUser,NetSDKLibStructure.NET_OUT_ATTENDANCE_GetUSER pstuOutGetUser,int nWaitTime);

    //考勤机  通过用户ID插入信息数据
    public boolean CLIENT_Attendance_InsertFingerByUserID(LLong lLoginID,NetSDKLibStructure.NET_IN_FINGERPRINT_INSERT_BY_USERID pstuInInsert,NetSDKLibStructure.NET_OUT_FINGERPRINT_INSERT_BY_USERID pstuOutInsert,int nWaitTime);

    //考勤机 删除单个用户下所有信息数据
    public boolean CLIENT_Attendance_RemoveFingerByUserID(LLong lLoginID,NetSDKLibStructure.NET_CTRL_IN_FINGERPRINT_REMOVE_BY_USERID pstuInRemove,NetSDKLibStructure.NET_CTRL_OUT_FINGERPRINT_REMOVE_BY_USERID pstuOutRemove,int nWaitTime);

    //考勤机 通过信息ID获取信息数据
    public boolean CLIENT_Attendance_GetFingerRecord(LLong lLoginID,NetSDKLibStructure.NET_CTRL_IN_FINGERPRINT_GET pstuInGet,NetSDKLibStructure.NET_CTRL_OUT_FINGERPRINT_GET pstuOutGet,int nWaitTime);

    //考勤机 通过信息ID删除信息数据
    public boolean CLIENT_Attendance_RemoveFingerRecord(LLong lLoginID,NetSDKLibStructure.NET_CTRL_IN_FINGERPRINT_REMOVE pstuInRemove,NetSDKLibStructure.NET_CTRL_OUT_FINGERPRINT_REMOVE pstuOutRemove,int nWaitTime);

    //考勤机 查找用户
    public boolean CLIENT_Attendance_FindUser(LLong lLoginID, NetSDKLibStructure.NET_IN_ATTENDANCE_FINDUSER pstuInFindUser, NetSDKLibStructure.NET_OUT_ATTENDANCE_FINDUSER pstuOutFindUser, int nWaitTime);

    //考勤机 通过用户ID查找该用户下的所有信息数据
    public boolean CLIENT_Attendance_GetFingerByUserID(LLong lLoginID, NetSDKLibStructure.NET_IN_FINGERPRINT_GETBYUSER pstuIn, NetSDKLibStructure.NET_OUT_FINGERPRINT_GETBYUSER pstuOut, int nWaitTime);

    //获取考勤机在线状态
    public boolean CLIENT_Attendance_GetDevState(LLong lLoginID, NetSDKLibStructure.NET_IN_ATTENDANCE_GETDEVSTATE pstuInParam, NetSDKLibStructure.NET_OUT_ATTENDANCE_GETDEVSTATE pstuOutParam, int nWaitTime);

    /*********************************************************************************************************
     * 									视频诊断功能接口														 *												 												     *
     * 视频诊断参数表配置 CFG_CMD_VIDEODIAGNOSIS_PROFILE														 *
     * 视频诊断任务表配置 CFG_CMD_VIDEODIAGNOSIS_TASK_ONE						 								 *
     * 视频诊断计划表配置 CFG_CMD_VIDEODIAGNOSIS_PROJECT														 *
     * 删除任务接口  CLIENT_DeleteDevConfig																	 *
     * 获取成员配置接口 CLIENT_GetMemberNames	  对应命令  CFG_CMD_VIDEODIAGNOSIS_TASK					         *
     * 获取诊断状态  CLIENT_QueryNewSystemInfo CFG_CMD_VIDEODIAGNOSIS_GETSTATE							 	 *
     *********************************************************************************************************/
    // 实时获取视频诊断结果,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_StartVideoDiagnosis(LLong lLoginID,NetSDKLibStructure.NET_IN_VIDEODIAGNOSIS pstInParam,NetSDKLibStructure.NET_OUT_VIDEODIAGNOSIS pstOutParam);

    // 停止视频诊断结果上报
    public boolean CLIENT_StopVideoDiagnosis(LLong hDiagnosisHandle);

    // 开始视频诊断结果查询,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_StartFindDiagnosisResult(LLong lLoginID,NetSDKLibStructure.NET_IN_FIND_DIAGNOSIS pstInParam,NetSDKLibStructure.NET_OUT_FIND_DIAGNOSIS pstOutParam);

    // 获取视频诊断结果信息,pstInParam与pstOutParam内存由用户申请释放
    public boolean CLIENT_DoFindDiagnosisResult(LLong hFindHandle,NetSDKLibStructure.NET_IN_DIAGNOSIS_INFO pstInParam,NetSDKLibStructure.NET_OUT_DIAGNOSIS_INFO pstOutParam);

    // 结束视频诊断结果查询
    public boolean CLIENT_StopFindDiagnosis(LLong hFindHandle);

    // 获取视频诊断进行状态
    public boolean CLIENT_GetVideoDiagnosisState(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_VIDEODIAGNOSIS_STATE pstInParam,NetSDKLibStructure.NET_OUT_GET_VIDEODIAGNOSIS_STATE pstOutParam,int nWaitTime);

    /********************************************************************************************
     * 									热成像												    *
     ********************************************************************************************/
    // 订阅温度分布数据（热图）,pInParam与pOutParam内存由用户申请释放
    public LLong CLIENT_RadiometryAttach(LLong lLoginID,NetSDKLibStructure.NET_IN_RADIOMETRY_ATTACH pInParam,NetSDKLibStructure.NET_OUT_RADIOMETRY_ATTACH pOutParam,int nWaitTime);

    // 取消订阅温度分布数据,lAttachHandle是 CLIENT_RadiometryAttach 的返回值
    public boolean CLIENT_RadiometryDetach(LLong lAttachHandle);

    // 通知开始获取热图数据,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_RadiometryFetch(LLong lLoginID,NetSDKLibStructure.NET_IN_RADIOMETRY_FETCH pInParam,NetSDKLibStructure.NET_OUT_RADIOMETRY_FETCH pOutParam,int nWaitTime);

    // 热图数据解压与转换接口
    /// \brief
    /// \param  pRadiometryData [IN] 热图数据， 由 fRadiometryAttachCB 获得
    /// \param  pGrayImg [IN, OUT] 解压后的数据，是一张灰度图，
    ///			传空指针表示不需要此数据
    ///         用户需保证传入的缓冲区足够大（不小于 图像像素数*sizeof(unsigned short)）
    ///         每个像素对应一个 unsigned short 型数据，表示图像某个像素的热成像灰度（范围 0 ~ 16383），
    ///         低地址对应画面左上角，高地址对应画面右下角
    /// \param  pTempForPixels [IN, OUT] 每个像素的温度数据
    ///			传空指针表示不需要此数据
    ///         用户需保证传入的缓冲区足够大（不小于 图像像素数*sizeof(float)）
    ///         每个像素对应一个 float 型数据，表示该像素位置的摄氏温度
    ///         低地址对应画面左上角，高地址对应画面右下角
    /// \return TRUE 成功，FALSE 失败
    public boolean CLIENT_RadiometryDataParse(NetSDKLibStructure.NET_RADIOMETRY_DATA pRadiometryData,short[] pGrayImg,float[] pTempForPixels);

    // 开始查询信息（获取查询句柄）(pInBuf, pOutBuf内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小)
    public boolean CLIENT_StartFind(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    // 查询信息(pInBuf, pOutBuf内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小
    public boolean CLIENT_DoFind(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    // 停止查询信息（销毁查询句柄）(pInBuf, pOutBuf内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小)
    public boolean CLIENT_StopFind(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    // 智能锁添加更新用户信息接口
    public boolean CLIENT_UpdateSmartLockUser(LLong lLoginID,NetSDKLibStructure.NET_IN_SMARTLOCK_UPDATE_USER_INFO pstInParam,NetSDKLibStructure.NET_OUT_SMARTLOCK_UPDATE_USER_INFO pstOutParam,int nWaitTime);

    // 获取当前智能锁的注册用户信息
    public boolean CLIENT_GetSmartLockRegisterInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_GET_SMART_LOCK_REGISTER_INFO pstInParam,NetSDKLibStructure.NET_OUT_GET_SMART_LOCK_REGISTER_INFO pstOutParam,int nWaitTime);

    // 智能锁修改用户信息
    public boolean CLIENT_SetSmartLockUsername(LLong lLoginID,NetSDKLibStructure.NET_IN_SET_SMART_LOCK_USERNAME pstInParam,NetSDKLibStructure.NET_OUT_SET_SMART_LOCK_USERNAME pstOutParam,int nWaitTime);

    // 智能锁删除用户接口
    public boolean CLIENT_RemoveSmartLockUser(LLong lLoginID,NetSDKLibStructure.NET_IN_SMARTLOCK_REMOVE_USER_INFO pstInParam,NetSDKLibStructure.NET_OUT_SMARTLOCK_REMOVE_USER_INFO pstOutParam,int nWaitTime);

    // 字符串加密接口 (pInParam, pOutParam内存由用户申请释放) 接口
    public boolean CLIENT_EncryptString(NetSDKLibStructure.NET_IN_ENCRYPT_STRING pInParam,NetSDKLibStructure.NET_OUT_ENCRYPT_STRING pOutParam,int nWaitTime);

    /*************************************************************************************************************
     * 门禁用户、卡、人脸、信息操作新接口
     * 1、添加一个用户
     * 2、根据用户ID可以来添加多张卡、多张人脸、多个信息
     *************************************************************************************************************/
    /**
     *  门禁人员信息管理接口
     * @param emtype 对应 {@link NET_EM_ACCESS_CTL_USER_SERVICE}
     */
    public boolean CLIENT_OperateAccessUserService(LLong lLoginID,int emtype,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    // 开始查询人员信息
    public LLong CLIENT_StartFindUserInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_USERINFO_START_FIND pstIn,NetSDKLibStructure.NET_OUT_USERINFO_START_FIND pstOut,int nWaitTime);

    // 获取人员信息 ,lFindHandle 为 CLIENT_StartFindUserInfo 接口返回值
    public boolean CLIENT_DoFindUserInfo(LLong lFindHandle,NetSDKLibStructure.NET_IN_USERINFO_DO_FIND pstIn,NetSDKLibStructure.NET_OUT_USERINFO_DO_FIND pstOut,int nWaitTime);

    // 停止查询人员信息 ,lFindHandle 为 CLIENT_StartFindUserInfo 接口返回值
    public boolean CLIENT_StopFindUserInfo(LLong lFindHandle);

    /**
     * 门禁卡片信息管理接口
     * @param emtype 对应 {@link NET_EM_ACCESS_CTL_CARD_SERVICE}
     */
    public boolean CLIENT_OperateAccessCardService(LLong lLoginID,int emtype,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    // 开始查询卡片信息
    public LLong CLIENT_StartFindCardInfo(LLong lLoginID,NetSDKLibStructure.NET_IN_CARDINFO_START_FIND pstIn,NetSDKLibStructure.NET_OUT_CARDINFO_START_FIND pstOut,int nWaitTime);

    // 获取卡片信息,lFindHandle 为CLIENT_StartFindCardInfo接口返回值
    public boolean CLIENT_DoFindCardInfo(LLong lFindHandle,NetSDKLibStructure.NET_IN_CARDINFO_DO_FIND pstIn,NetSDKLibStructure.NET_OUT_CARDINFO_DO_FIND pstOut,int nWaitTime);

    // 停止查询卡片信息,lFindHandle 为CLIENT_StartFindCardInfo接口返回值
    public boolean CLIENT_StopFindCardInfo(LLong lFindHandle);

    /**
     * 门禁人脸信息管理接口
     * @param emtype 对应 {@link NET_EM_ACCESS_CTL_FACE_SERVICE}
     */
    public boolean CLIENT_OperateAccessFaceService(LLong lLoginID,int emtype,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 信息信息管理接口
     * @param emtype 对应 {@link NET_EM_ACCESS_CTL_FINGERPRINT_SERVICE}
     */
    public boolean CLIENT_OperateAccessFingerprintService(LLong lLoginID,int emtype,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    // 开始升级
    public LLong CLIENT_StartUpgradeEx(LLong lLoginID,int emtype,String pchFileName,Callback cbUpgrade,Pointer dwUser);

    // 发送数据
    public boolean CLIENT_SendUpgrade(LLong lUpgradeID);

    // 结束升级设备程序
    public boolean CLIENT_StopUpgrade(LLong lUpgradeID);

    // 查询产品定义
    public boolean CLIENT_QueryProductionDefinition(LLong lLoginID,NetSDKLibStructure.NET_PRODUCTION_DEFNITION pstuProdDef,int nWaitTime);

    /**
     * 录播主机相关 添加新的课程记录
     * NET_IN_ADD_COURSE *pstInParam, NET_OUT_ADD_COURSE *pstOutParam
     */
    public boolean CLIENT_AddCourse(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     *  录播主机相关 修改课程
     *  NET_IN_MODIFY_COURSE *pstInParam, NET_OUT_MODIFY_COURSE *pstOutParam
     */
    public boolean CLIENT_ModifyCourse(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     *  录播主机相关 删除课程
     *  NET_IN_DELETE_COURSE *pstInParam, NET_OUT_DELETE_COURSE *pstOutParam
     */
    public boolean CLIENT_DeleteCourse(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 开始查询课程
     * NET_IN_QUERY_COURSE_OPEN *pstInParam, NET_OUT_QUERY_COURSE_OPEN *pstOutParam
     */
    public boolean CLIENT_QueryCourseOpen(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 查询课程
     * NET_IN_QUERY_COURSE *pstInParam, NET_OUT_QUERY_COURSE *pstOutParam
     */
    public boolean CLIENT_QueryCourse(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 结束查询课程
     * NET_IN_QUERY_COURSE_CLOSE *pstInParam, NET_OUT_QUERY_COURSE_CLOSE *pstOutParam
     */
    public boolean CLIENT_QueryCourseClose(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 获取真实预览通道号，pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_GET_REAL_PREVIEW_CHANNEL* pInBuf, NET_OUT_GET_REAL_PREVIEW_CHANNEL* pOutBuf
     */
    public boolean CLIENT_GetRealPreviewChannel(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 录播主机组合通道模式操作 emOperateType NET_COURSECOMPOSITE_MODE_OPERATE_TYPE
     *
     * NET_COURSECOMPOSITE_MODE_OPERATE_TYPE {
     *
     *      NET_COURSECOMPOSITE_MODE_ADD,      // 添加模式,对应结构体
     *                                         // pInParam = NET_IN_COURSECOMPOSITE_CHANNEL_MODE_ADD,
     *                                         // pOutParam = NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_ADD
     * 	    NET_COURSECOMPOSITE_MODE_DELETE,   // 删除模式,对应结构体
     * 	                                       // pInParam = NET_IN_COURSECOMPOSITE_CHANNEL_MODE_DELETE,
     * 	                                       // pOutParam = NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_DELETE
     * 	    NET_COURSECOMPOSITE_MODE_MODIFY,   // 修改模式,对应结构体
     * 	                                       // pInParam = NET_IN_COURSECOMPOSITE_CHANNEL_MODE_MODIFY,
     * 	                                       // pOutParam = NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_MODIFY
     * 	    NET_COURSECOMPOSITE_MODE_GET,      // 获取模式,对应结构体
     * 	                                       // pInParam = NET_IN_COURSECOMPOSITE_CHANNEL_MODE_GET,
     * 	                                       // pOutParam = NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_GET
     * }
     */
    public boolean CLIENT_OperateCourseCompositeChannelMode(LLong lLoginID,int emOperateType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取录播主机默认真实通道号,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_GET_DEFAULT_REAL_CHANNEL* pInBuf, NET_OUT_GET_DEFAULT_REAL_CHANNEL* pOutBuf
     */
    public boolean CLIENT_GetDefaultRealChannel(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 获取录播主机逻辑通道号,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_GET_COURSE_LOGIC_CHANNEL* pInBuf,NET_OUT_GET_COURSE_LOGIC_CHANNEL* pOutBuf
     */
    public boolean CLIENT_GetLogicChannel(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 设置逻辑通道号和真实通道号的绑定关系,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_SET_BLIND_REAL_CHANNEL* pInBuf,NET_OUT_SET_BLIND_REAL_CHANNEL* pOutBuf
     */
    public boolean CLIENT_SetBlindRealChannel(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 设置课程录像模式,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_SET_COURSE_RECORD_MODE* pInBuf, NET_OUT_SET_COURSE_RECORD_MODE* pOutBuf
     */
    public boolean CLIENT_SetCourseRecordMode(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 获取课程录像模式,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_GET_COURSE_RECORD_MODE* pInBuf, NET_OUT_GET_COURSE_RECORD_MODE* pOutBuf
     */
    public boolean CLIENT_GetCourseRecordMode(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 获取录播主机通道输入媒体介质,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_GET_INPUT_CHANNEL_MEDIA* pInBuf,NET_OUT_GET_INPUT_CHANNEL_MEDIA* pOutBuf
     */
    public boolean CLIENT_GetInputChannelMedia(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 开启/关闭指定通道录像
     * NET_IN_SET_COURSE_RECORD_STATE *pInBuf, NET_OUT_SET_COURSE_RECORD_STATE *pOutBuf
     */
    public boolean CLIENT_SetCourseRecordState(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 开始查询课程视频信息,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_QUERY_COURSEMEDIA_FILEOPEN *pInBuf, NET_OUT_QUERY_COURSEMEDIA_FILEOPEN *pOutBuf
     */
    public boolean CLIENT_OpenQueryCourseMediaFile(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 查询课程视频信息,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_QUERY_COURSEMEDIA_FILE *pInBuf, NET_OUT_QUERY_COURSEMEDIA_FILE *pOutBuf
     */
    public boolean CLIENT_DoQueryCourseMediaFile(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 关闭课程视频查询,pInBuf与pOutBuf内存由用户申请释放
     * NET_IN_QUERY_COURSEMEDIA_FILECLOSE *pInBuf, NET_OUT_QUERY_COURSEMEDIA_FILECLOSE *pOutBuf
     */
    public boolean CLIENT_CloseQueryCourseMediaFile(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 录播主机录像信息操作接口 EM_COURSERECORD_OPERATE_TYPE emOperateType
     * {
     *         EM_COURSERECORDE_TYPE_GET_INFO,    //获取教室录像信息,对应结构体
     *              pInParam = NET_IN_COURSERECORD_GETINFO,pOutParam = NET_OUT_COURSERECORD_GETINFO
     *         EM_COURSERECORDE_TYPE_SET_INFO,    //设置教室录像信息,对应结构体
     *              pInParam = NET_IN_COURSERECORD_SETINFO,pOutParam = NET_OUT_COURSERECORD_SETINFO
     *         EM_COURSERECORDE_TYPE_UPDATE_INFO, //将录像信息更新到time时的信息,对应结构体
     *              pInParam = NET_IN_COURSERECORD_UPDATE_INFO, pOutParam = NET_OUT_COURSERECORD_UPDATE_INFO
     *         EM_COURSERECORDE_TYPE_GET_TIME,     //获取当前课程教室已录制时间,对应结构体
     *              pInParam = NET_IN_COURSERECORD_GET_TIME, pOutParam = NET_OUT_COURSERECORD_GET_TIME
     *     }
     */
    public boolean CLIENT_OperateCourseRecordManager(LLong lLoginID,int emOperateType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     *  录播主机组合通道操作 EM_COURSECOMPOSITE_OPERATE_TYPE
     *  {
     *         EM_COURSECOMPOSITE_TYPE_LOCK_CONTROL,                   //控制组合通道与逻辑通道，对应结构体
     *              pInParam = NET_IN_COURSECOMPOSITE_LOCK_CONTROL,pOutParam = NET_OUT_COURSECOMPOSITE_LOCK_CONTROL
     *         EM_COURSECOMPOSITE_TYPE_GET_LOCKINFO,                   //获取组合通道与逻辑通道的锁定信息，对应结构体
     *              pInParam = NET_IN_COURSECOMPOSITE_GET_LOCKINFO，pOutParam = NET_OUT_COURSECOMPOSITE_GET_LOCKINFO
     *         EM_COURSECOMPOSITE_TYPE_GET_INFO,                       //获取组合通道信息,对应结构体
     *              pInParam = NET_IN_COURSECOMPOSITE_GET_INFO,pOutParam = NET_OUT_COURSECOMPOSITE_GET_INFO
     *         EM_COURSECOMPOSITE_TYPE_SET_INFO,                       //设置组合通道信息,对应结构体
     *              pInParam = NET_IN_COURSECOMPOSITE_SET_INFO,pOutParam = NET_OUT_COURSECOMPOSITE_SET_INFO
     *         EM_COURSECOMPOSITE_TYPE_UPDATE_INFO,                    //将组合通道信息更新到time时的信息，对应结构体
     *              pInParam = NET_IN_COURSECOMPOSITE_UPDATE_INFO, pOutParam = NET_OUT_COURSECOMPOSITE_UPDATE_INFO
     *     }
     */
    public boolean CLIENT_OperateCourseCompositeChannel(LLong lLoginID,int emOperateType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取录像状态
     * NET_IN_GET_RECORD_STATE *pInParam, NET_OUT_GET_RECORD_STATE *pOutParam
     */
    public boolean CLIENT_GetRecordState(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 电视墙操作
     * NET_MONITORWALL_OPERATE_TYPE emType, void* pInParam, void* pOutParam
     */
    public boolean CLIENT_OperateMonitorWall(LLong lLoginID,int emType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 分页获取信息标注信息列表
     * NET_IN_SCENICSPOT_GETPOINTINFOS_INFO *pInstuParam, NET_OUT_SCENICSPOT_GETPOINTINFOS_INFO *pstuOutParam
     */
    public boolean CLIENT_ScenicSpotGetPointInfos(LLong lLoginID,Pointer pInstuParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 设置景物点，原编号的景物点将会被覆盖
     * NET_IN_SCENICSPOT_SETPOINTINFO_INFO *pInstuParam, NET_OUT_SCENICSPOT_SETPOINTINFO_INFO *pstuOutParam
     */
    public boolean CLIENT_ScenicSpotSetPointInfo(LLong lLoginID,Pointer pInstuParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取景物点支持的能力
     * NET_IN_SCENICSPOT_GETCAPS_INFO *pInstuParam, NET_OUT_SCENICSPOT_GETCAPS_INFO *pstuOutParam
     */
    public boolean CLIENT_ScenicSpotGetCaps(LLong lLoginID,Pointer pInstuParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 以景物标注点为中心，进行三维定位(倍率不变)
     * NET_IN_SCENICSPOT_TURNTOPOINT_INFO *pInParam, NET_OUT_SCENICSPOT_TURNTOPOINT_INFO *pOutParam
     */
    public boolean CLIENT_ScenicSpotTurnToPoint(LLong lLoginID,Pointer pInstuParam,Pointer pstuOutParam,int nWaitTime);

    /////////////////////////////////////////新增接口 ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 设置停车车位状态
     * NET_IN_SET_PARKINGSPACE_STATE_INFO *pInParam, NET_OUT_SET_PARKINGSPACE_STATE_INFO *pOutParam
     */
    public boolean CLIENT_SetParkingSpaceState(LLong lLoginID,Pointer pstInParm,Pointer pstOutParam,int nWaitTime);

    /**
     * 修改停车记录信息
     * NET_IN_MODIFY_PARKINGRECORD_INFO *pInParam, NET_OUT_MODIFY_PARKINGRECORD_INFO *pOutParam
     */
    public boolean CLIENT_ModifyParkingRecord(LLong lLoginID,Pointer pstInParm,Pointer pstOutParam,int nWaitTime);

    /**
     * 按照事件类型抓图（配合CLIENT_RealLoadPic()、CLIENT_RealLoadPicEx()接口使用, 按照手动抓拍模式(Manual)订阅,图片通过回调给用户）(pInParam, pOutParam内存由用户申请释放)
     * NET_IN_SNAP_BY_EVENT *pInParam, NET_OUT_SNAP_BY_EVENT *pOutParam
     */
    public boolean CLIENT_SnapPictureByEvent(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    public static class NET_IN_SET_MARK_FILE_BY_TIME extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannel;                             //需要锁定的通道号,从0开始,元素为-1时,表示全通道。
        public NET_TIME_EX      stuStartTime;                         //开始时间
        public NET_TIME_EX      stuEndTime;                           //结束时间
        public int              bFlag;                                // 标记动作	true : 标记, false : 清除
        public int              bLockTimeFlag;                        //nLockTime字段标志位， 为 TRUE 时使用nLockTime
        public int              nLockTime;                            //锁定时长，以加锁时间为起点 ，单位为小时

        public NET_IN_SET_MARK_FILE_BY_TIME() {
            this.dwSize = this.size();
        }
    }

    public static class NET_OUT_SET_MARK_FILE_BY_TIME extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_SET_MARK_FILE_BY_TIME() {
            this.dwSize = this.size();
        }
    }

    // 按时间标记录像
    public boolean CLIENT_SetMarkFileByTime(LLong lUpgradeID,NET_IN_SET_MARK_FILE_BY_TIME pInParam,NET_OUT_SET_MARK_FILE_BY_TIME pOutParam,int nWaitTime);

    public static class NET_IN_SET_MARK_FILE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emLockMode;                           // 录像加锁方式，详见EM_MARKFILE_MODE
        public int              emFileNameMadeType;                   // 文件名产生的方式，详见EM_MARKFILE_NAMEMADE_TYPE
        public int              nChannelID;                           // 通道号
        public byte[]           szFilename = new byte[NetSDKLibStructure.MAX_PATH];      // 文件名
        public int              nFramenum;                            // 文件总帧数
        public int              nSize;                                // 文件长度
        public NET_TIME         stuStartTime;                         // 开始时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public int              nDriveNo;                             // 磁盘号(区分网络录像和本地录像的类型,0－127表示本地录像,其中64表示光盘1,128表示网络录像)
        public int              nStartCluster;                        // 起始簇号
        public byte             byRecordFileType;                     // 录象文件类型  0：普通录象；1：报警录象；2：移动检测；3：卡号录象；4：图片, 5: 智能录像
        public byte             byImportantRecID;                     // 0:普通录像 1:重要录像
        public byte             byHint;                               // 文件定位索引(nRecordFileType==4<图片>时,bImportantRecID<<8 +bHint ,组成图片定位索引 )
        public byte             byRecType;                            // 0-主码流录像  1-辅码流1录像 2-辅码流2录像 3-辅码流3录像
        public int              nLockTime;                            // 锁定时长，以加锁时间为起点，单位为小时

        public NET_IN_SET_MARK_FILE() {
            this.dwSize = this.size();
        }
    }

    // 录像加锁方式
    public static class EM_MARKFILE_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_MARK_FILE_BY_TIME_MODE = 0;        // 通过时间方式对录像加锁
        public static final int   EM_MARK_FILE_BY_NAME_MODE = 1;        // 通过文件名方式对录像加锁
    }

    // 文件名产生的方式
    public static class EM_MARKFILE_NAMEMADE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_MARKFILE_NAMEMADE_DEFAULT = 0;     // 默认方式：需要用户传递录像文件名参数szFilename
        public static final int   EM_MARKFILE_NAMEMADE_JOINT = 1;       // 拼接文件名方式：用户传递磁盘号(nDriveNo)、起始簇号(nStartCluster)，不需要传递录像文件名
    }

    public static class NET_OUT_SET_MARK_FILE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_SET_MARK_FILE() {
            this.dwSize = this.size();
        }
    }

    // 按文件标记录像
    public boolean CLIENT_SetMarkFile(LLong lLoginID,NET_IN_SET_MARK_FILE pInParam,NET_OUT_SET_MARK_FILE pOutParam,int nWaitTime);

    public static class NET_IN_DEV_GPS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 该结构体大小
        public int              nChannel;

        public NET_IN_DEV_GPS_INFO() {
            this.dwSize =this.size();
        }
    }

    public static class NET_OUT_DEV_GPS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 该结构体大小
        public NET_TIME         stuLocalTime;                         // 当前时间
        public double           dbLongitude;                          // 经度(单位是百万分之度,范围0-360度)
        public double           dbLatitude;                           // 纬度(单位是百万分之度,范围0-180度)
        public double           dbAltitude;                           // 高度(单位:米)
        public double           dbSpeed;                              // 速度(单位:km/H)
        public double           dbBearing;                            // 方向角(单位:度)
        public int              emAntennasStatus;                     // 天线状态(0:坏 1:好)
        public int              emPositioningResult;                  // 定位状态(0:不定位 1:定位)
        public int              dwSatelliteCount;                     // 卫星个数
        public int              emworkStatus;                         // 工作状态
        public int              nAlarmCount;                          // 报警个数
        public int[]            nAlarmState = new int[128];           // 发生的报警位置,值可能多个
        public float            fHDOP;                                // 水平精度因子

        public NET_OUT_DEV_GPS_INFO() {
            this.dwSize = this.size();
        }
    }

    // 实时抽帧配置,EVS
    public static class CFG_BACKUP_LIVE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 是否启动抽帧
        public int              nBackupRate;                          // 抽帧备份比率，如为0表示只保留I帧，其它情况下表示保留I帧以及紧邻其后的若干P帧
        // 单位：百分比
        // 如果GOP为50，20表示保留50*20%=10帧数据(即1个I帧和9个P帧)。如果计算结果带小数，则取整
        public NetSDKLibStructure.CFG_TIME_SECTION stuTimeSection;                       // 抽帧时间段
    }

    // 定时录像配置信息
    public static class CFG_RECORD_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号(0开始)
        public NetSDKLibStructure.TIME_SECTION_WEEK_DAY_6[] stuTimeSection = (NetSDKLibStructure.TIME_SECTION_WEEK_DAY_6[])new NetSDKLibStructure.TIME_SECTION_WEEK_DAY_6().toArray(NetSDKLibStructure.WEEK_DAY_NUM); // 时间表
        public int              nPreRecTime;                          // 预录时间，为零时表示关闭(0~300)
        public int              bRedundancyEn;                        // 录像冗余开关
        public int              nStreamType;                          // 0－主码流，1－辅码流1，2－辅码流2，3－辅码流3
        public int              nProtocolVer;                         // 协议版本号, 只读
        public int              abHolidaySchedule;                    // 为true时有假日配置信息，bHolidayEn、stuHolTimeSection才有效;
        public int              bHolidayEn;                           // 假日录像使能TRUE:使能,FALSE:未使能
        public NetSDKLibStructure.TIME_SECTION_WEEK_DAY_6 stuHolTimeSection;             // 假日录像时间表
        public int              nBackupLiveNum;                       // 实时抽帧配置个数
        public CFG_BACKUP_LIVE_INFO[] stuBackupLiveInfo = (CFG_BACKUP_LIVE_INFO[]) new CFG_BACKUP_LIVE_INFO().toArray(8); // 实时抽帧配置,EVS
        public int              bSaveVideo;                           // 是否录制视频帧
        public int              bSaveAudio;                           // 录像时是否保存音频数据
        public int              bEnable;                              //录像时是否保存音频数据
        public int              nMaxRecordTime;                       //报警输入使能
        public byte[]           szReserved = new byte[1024];          //单次Pir录像时长限制（单位秒），0表示无限制
    }

    //获取云升级信息入参
    public static class NET_IN_UPGRADER_GETSERIAL extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 此结构体大小

        public NET_IN_UPGRADER_GETSERIAL() {
            this.dwSize = this.size();
        }
    }

    //云升级信息
    public static class NET_UPGRADER_SERIAL_INO extends NetSDKLibStructure.SdkStructure
    {
        public int              emVendor;                             // 厂商,详见ENUM_VENDOR_TYPE
        public int              emStandard;                           // 视频制式, 详见ENUM_STANDARD_TYPE
        public NET_TIME_EX      stuBuild;                             // 发布日期
        public byte[]           szChip = new byte[NetSDKLibStructure.NET_COMMON_STRING_16]; // 可升级的程序名
        public byte[]           szSerial = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 内部型号
        public byte[]           szLanguage = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 语言
        public byte[]           szSn = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 序列号
        public byte[]           szSWVersion = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 软件版本
        public byte[]           szTag = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 自定义标记
        public byte[]           szTag2 = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 自定义标记2
        public byte[]           reserved = new byte[1024];
    }

    //获取云升级信息出参
    public static class NET_OUT_UPGRADER_GETSERIAL extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 此结构体大小
        public NET_UPGRADER_SERIAL_INO[] stuSerialInfo = new NET_UPGRADER_SERIAL_INO[NetSDKLibStructure.MAX_UPGRADER_SERIAL_INFO]; // 云升级信息
        public int              nRetNum;                              // 返回个数

        public NET_OUT_UPGRADER_GETSERIAL() {
            this.dwSize = this.size();

            for (int i = 0; i < stuSerialInfo.length; ++i) {
                stuSerialInfo[i] = new NET_UPGRADER_SERIAL_INO();
            }
        }
    }

    //从设备获取信息，用于向DH云确认是否有升级包
    public boolean CLIENT_GetUpdateSerial(LLong lLoginID,NET_IN_UPGRADER_GETSERIAL pstuInGetSerial,NET_OUT_UPGRADER_GETSERIAL pstuOutGetSerial,int nWaitTime);

    // 云升级软件检查入参
    public static class NET_IN_CLOUD_UPGRADER_CHECK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emVendor;                             // 厂商,详见ENUM_VENDOR_TYPE
        public int              emStandard;                           // 视频制式, 详见ENUM_STANDARD_TYPE
        public NET_TIME_EX      stuBuild;                             // 编译时间，用于比较版本
        public byte[]           szUrl = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 云URL
        public byte[]           szClass = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备大类
        public byte[]           szSerial = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 设备硬件信号系列
        public byte[]           szLanguage = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 语言
        public byte[]           szSN = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备序列号
        public byte[]           szSWVersion = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 版本号，用于显示
        public byte[]           szTag1 = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 预留字段，可用于后续或扩展
        public byte[]           szTag2 = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 预留字段，可用于后续或扩展
        public byte[]           szAccessKeyId = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; //Access Key ID
        public byte[]           szSecretAccessKey = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; //Secret Access Key

        public NET_IN_CLOUD_UPGRADER_CHECK() {
            this.dwSize = this.size();
        }
    }

    //设备制造商
    public static class ENUM_VENDOR_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_VENDOR_TYPE_UNKNOWN = 0;         // 未知
        public static final int   ENUM_VENDOR_TYPE_GENERAL = 1;         // General
        public static final int   ENUM_VENDOR_TYPE_DH = 2;              // DH
        public static final int   ENUM_VENDOR_TYPE_OEM = 3;             // OEM
        public static final int   ENUM_VENDOR_TYPE_LC = 4;              // LC
        public static final int   ENUM_VENDOR_TYPE_EZIP = 5;            // EZIP
    }

    //视频制式
    public static class ENUM_STANDARD_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_STANDARD_TYPE_UNKNOWN = 0;       // 未知
        public static final int   ENUM_STANDARD_TYPE_PAL = 1;           // P-PAL
        public static final int   ENUM_STANDARD_TYPE_NTSC = 2;          // N-NTSC
        public static final int   ENUM_STANDARD_TYPE_PAL_NTSC = 3;      // PN-PAL/NTSC默认P制
        public static final int   ENUM_STANDARD_TYPE_NTSC_PAL = 4;      // NP-NTSC/PAL默认N制
        public static final int   ENUM_STANDARD_TYPE_SECAM = 5;         // S-SECAM
    }

    // 云升级软件检查出参
    public static class NET_OUT_CLOUD_UPGRADER_CHECK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              bHasNewVersion;                       // 是否有可升级版本
        public byte[]           szVersion = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 版本信息
        public byte[]           szAttention = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 提醒设备升级的提示信息
        public byte[]           szPackageUrl = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 设备升级包的URL
        public byte[]           szPackageId = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 升级包ID

        public NET_OUT_CLOUD_UPGRADER_CHECK() {
            this.dwSize = this.size();
        }
    }

    // 检查云端是否有可升级软件, 使用HTTP协议
    public boolean CLIENT_CloudUpgraderCheck(NET_IN_CLOUD_UPGRADER_CHECK pIn,NET_OUT_CLOUD_UPGRADER_CHECK pOut,int dwWaitTime);

    //云下载状态
    public static class emCloudDownloadState extends NetSDKLibStructure.SdkStructure
    {
        public static final int   emCloudDownloadState_Unknown = 0;     // 未知
        public static final int   emCloudDownloadState_Success = 1;     // 云下载成功(需要关闭句柄)
        public static final int   emCloudDownloadState_Failed = 2;      // 云下载失败(不需要关闭句柄，会不断尝试下载)
        public static final int   emCloudDownloadState_Downloading = 3; // 正在下载中
        public static final int   emCloudDownloadState_NoEnoughDiskSpace = 4; // 磁盘空间不足
    }

    // 云下载回调函数
    public interface fCloudDownload_Process_callback extends Callback {
        public void invoke(LLong lDownHandle,int emState,double dwDownloadSpeed,int dwProgressPercentage,Pointer dwUser);
    }

    // 云升级下载升级包入参
    public static class NET_IN_CLOUD_UPGRADER_DOWN extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szPackageUrl = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 设备升级包的URL
        public byte[]           szSaveFile = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 保存文件名
        public Callback         pfProcessCallback;                    // 进度回调，实现fCloudDownload_Process_callback
        public Pointer          dwUser;                               // 回调用户数据

        public NET_IN_CLOUD_UPGRADER_DOWN() {
            this.dwSize = this.size();
        }
    }

    // 云升级下载升级包出参
    public static class NET_OUT_CLOUD_UPGRADER_DOWN extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_CLOUD_UPGRADER_DOWN() {
            this.dwSize = this.size();
        }
    }

    // 云 下载升级软件, 使用HTTP协议
    public LLong CLIENT_CloudUpgraderDownLoad(NET_IN_CLOUD_UPGRADER_DOWN pIn,NET_OUT_CLOUD_UPGRADER_DOWN pOut);

    // 停止云下载, 使用HTTP协议
    public boolean CLIENT_CloudUpgraderStop(LLong lDownloadHandle);

    // 暂停云下载, 使用HTTP协议
    public boolean CLIENT_CloudUpgraderPause(LLong lDownloadHandle,int bPause);

    //升级结果
    public static class NET_UPGRADE_REPORT_RESULT extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_UPGRADE_REPORT_RESULT_UNKNWON = 0; // 未知
        public static final int   NET_UPGRADE_REPORT_RESULT_SUCCESS = 1; // 成功
        public static final int   NET_UPGRADE_REPORT_RESULT_FAILED = 2; // 失败
    }

    //上报升级结果结构体
    public static class NET_UPGRADE_REPORT extends NetSDKLibStructure.SdkStructure
    {
        public int              nDeviceNum;
        public DEVICE_SERIAL[]  szDevSerialArr = (DEVICE_SERIAL[])new DEVICE_SERIAL().toArray(NetSDKLibStructure.NET_UPGRADE_COUNT_MAX); // 序列号
        public byte[]           szPacketID = new byte[NetSDKLibStructure.MAX_COMMON_STRING_128]; // 升级包ID
        public int              emResult;                             // 升级结果,详见NET_UPGRADE_REPORT_RESULT
        public byte[]           szCode = new byte[NetSDKLibStructure.MAX_COMMON_STRING_128]; // 错误码信息
        public byte[]           reserved = new byte[256];
    }

    public static class DEVICE_SERIAL extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDevSerial = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 序列号
    }

    //上报升级结果入参
    public static class NET_IN_UPGRADE_REPORT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nCount;                               // 升级包个数
        public Pointer          pstuUpgradeReport;                    // 升级结果信息 , 大小 nCount * sizeof(NET_UPGRADE_REPORT)
        public byte[]           szAccessKeyId = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; //访问ID
        public byte[]           szSecretAccessKey = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; //访问秘钥
        public byte[]           szUrl = new byte[NetSDKLibStructure.NET_COMMON_STRING_1024]; // 云URL

        public NET_IN_UPGRADE_REPORT() {
            this.dwSize = this.size();
        }
    }

    //上报升级结果出参
    public static class NET_OUT_UPGRADE_REPORT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_UPGRADE_REPORT() {
            this.dwSize = this.size();
        }
    }

    //上报升级结果, 使用HTTP协议
    public boolean CLIENT_CloudUpgraderReport(NET_IN_UPGRADE_REPORT pIn,NET_OUT_UPGRADE_REPORT pOut,int dwWaitTime);

    // 升级状态回调结构体
    public static class NET_CLOUD_UPGRADER_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emState;                              // 升级状态,详见EM_UPGRADE_STATE
        public int              nProgress;                            // 升级百分比
        public byte[]           szFileName = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 升级文件
        public long             nTotalLen;                            // 升级文件总大小，单位字节
        public byte[]           szFileNameEx = new byte[256];         // 升级文件扩展
        public byte[]           szReserved = new byte[1024];          // 扩展字段

        public NET_CLOUD_UPGRADER_STATE() {
            this.dwSize = this.size();
        }
    }

    // 升级状态回调函数
    public interface fUpgraderStateCallback extends Callback {
        public void invoke(LLong lLoginId,LLong lAttachHandle,NET_CLOUD_UPGRADER_STATE pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    // 订阅升级状态入参
    public static class NET_IN_CLOUD_UPGRADER_ATTACH_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Callback         cbUpgraderState;                      // 升级状态回调实现fUpgraderStateCallback
        public Pointer          dwUser;

        public NET_IN_CLOUD_UPGRADER_ATTACH_STATE() {
            this.dwSize = this.size();
        }
    }

    // 订阅升级状态出参
    public static class NET_OUT_CLOUD_UPGRADER_ATTACH_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_CLOUD_UPGRADER_ATTACH_STATE() {
            this.dwSize = this.size();
        }
    }

    // 获取升级状态入参
    public static class NET_IN_CLOUD_UPGRADER_GET_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_IN_CLOUD_UPGRADER_GET_STATE() {
            this.dwSize = this.size();
        }
    }

    // 获取升级状态出参
    public static class NET_OUT_CLOUD_UPGRADER_GET_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emState;                              // 升级状态,详见EM_UPGRADE_STATE
        public int              nProgress;                            // 升级进度
        public byte[]           szFileName = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 升级文

        public NET_OUT_CLOUD_UPGRADER_GET_STATE() {
            this.dwSize = this.size();
        }
    }

    // 升级包和升级状态
    public static class EM_UPGRADE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_UPGRADE_STATE_UNKNOWN = 0;         // 未知状态
        public static final int   EM_UPGRADE_STATE_NONE = 1;            // 没有检测到更新状态
        public static final int   EM_UPGRADE_STATE_INVALID = 2;         // 升级包不正确
        public static final int   EM_UPGRADE_STATE_NOT_ENOUGH_MEMORY = 3; // 内存不够
        public static final int   EM_UPGRADE_STATE_DOWNLOADING = 4;     // 正在下载数据
        public static final int   EM_UPGRADE_STATE_DOWNLOAD_FAILED = 5; // 下载失败
        public static final int   EM_UPGRADE_STATE_DOWNLOAD_SUCCESSED = 6; // 下载成功
        public static final int   EM_UPGRADE_STATE_PREPARING = 7;       // 准备升级
        public static final int   EM_UPGRADE_STATE_UPGRADING = 8;       // 升级中
        public static final int   EM_UPGRADE_STATE_UPGRADE_FAILED = 9;  // 升级失败
        public static final int   EM_UPGRADE_STATE_UPGRADE_SUCCESSED = 10; // 升级成功
        public static final int   EM_UPGRADE_STATE_UPGRADE_CANCELLED = 11; // 取消升级
        public static final int   EM_UPGRADE_STATE_FILE_UNMATCH = 12;   // 升级包不匹配
    }

    // 订阅升级状态观察接口
    public LLong CLIENT_CloudUpgraderAttachState(LLong lLoginID,NET_IN_CLOUD_UPGRADER_ATTACH_STATE pInParam,NET_OUT_CLOUD_UPGRADER_ATTACH_STATE pOutParam,int nWaitTime);

    // 退订升级状态观察接口
    public boolean CLIENT_CloudUpgraderDetachState(LLong lAttachHandle);

    // 获取升级状态
    public boolean CLIENT_CloudUpgraderGetState(LLong lLoginID,NET_IN_CLOUD_UPGRADER_GET_STATE pInParam,NET_OUT_CLOUD_UPGRADER_GET_STATE pOutParam,int nWaitTime);

    // 代理服务器地址
    public static class NET_PROXY_SERVER_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szIP = new byte[NetSDKLibStructure.NET_MAX_IPADDR_LEN_EX]; // IP地址
        public int              nPort;                                // 端口
        public byte[]           byReserved = new byte[84];
    }

    // CLIENT_CheckCloudUpgrader 入参
    public static class NET_IN_CHECK_CLOUD_UPGRADER extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nWay;                                 // 检测路径, 0-直连升级服务器检测, 1-通过代理服务器检测, 2-获取缓存的检测结果
        public NET_PROXY_SERVER_INFO stProxy;                         // 代理服务器地址, way==1时有意义

        public NET_IN_CHECK_CLOUD_UPGRADER() {
            this.dwSize = this.size();
        }
    }

    // 云升级状态
    public static class EM_CLOUD_UPGRADER_CHECK_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CLOUD_UPGRADER_CHECK_STATE_UNKNOWN = 0; // 未知
        public static final int   EM_CLOUD_UPGRADER_CHECK_STATE_NONE = 1; // 没有检测到更新
        public static final int   EM_CLOUD_UPGRADER_CHECK_STATE_REGULAR = 2; // 一般升级 (需要用户确认, 只能向高版本)
        public static final int   EM_CLOUD_UPGRADER_CHECK_STATE_EMERGENCY = 3; // 强制升级 (设备自动检测执行, 可以向低版本)
        public static final int   EM_CLOUD_UPGRADER_CHECK_STATE_AUTOMATIC = 4; // 自动升级 (有新升级包, 自动升级, 当前为使用, 需使能页面自动升级选项)
    }

    // 云升级新版本升级包类型
    public static class EM_CLOUD_UPGRADER_PACKAGE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_UNKNOWN = 0; // 未知
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_ALL = 1; // 整包
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_PTZ = 2; // 云台主控包
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_WEB = 3; // Web
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_LOGO = 4; // Logo
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_CUSTOM = 5; // Custom
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_GUI = 6; // Gui
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_PD = 7; // PD
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_DATA = 8; // Data
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_PTZ_POWER = 9; // 云台电源
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_PTZ_LIGHT = 10; // 云台灯光
        public static final int   EM_CLOUD_UPGRADER_PACKAGE_TYPE_PTZ_HEATER = 11; // 云台加热器
    }

    // CLIENT_CheckCloudUpgrader 出参
    public static class NET_OUT_CHECK_CLOUD_UPGRADER extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emState;                              // 升级状态,详见EM_CLOUD_UPGRADER_CHECK_STATE
        public int              emPackageType;                        // 新版本升级包类型, State不为None需要返回,详见EM_CLOUD_UPGRADER_PACKAGE_TYPE
        public byte[]           szOldVersion = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 旧版本号, State不为None需要返回
        public byte[]           szNewVersion = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 新版本号,State不为None需要返回
        public byte[]           szAttention = new byte[NetSDKLibStructure.NET_COMMON_STRING_2048]; // 新的升级包更新内容
        public byte[]           szPackageURL = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 升级包下载地址(代理升级需要)
        public byte[]           szPackageID = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 升级包ID
        public byte[]           szCheckSum = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 升级包的SHA-256校验和
        public byte[]           szBuildTime = new byte[NetSDKLibStructure.MAX_COMMON_STRING_32]; // 升级包构建时间

        public NET_OUT_CHECK_CLOUD_UPGRADER() {
            this.dwSize = this.size();
        }
    }

    // 在线升级检查是否有可用升级包, pInParam和pOutParam内存由用户申请和释放
    public boolean CLIENT_CheckCloudUpgrader(LLong lLoginID,NET_IN_CHECK_CLOUD_UPGRADER pInParam,NET_OUT_CHECK_CLOUD_UPGRADER pOutParam,int nWaitTime);

    // 升级包信息
    public static class NET_CLOUD_UPGRADER_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szPackageURL = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 升级包下载地址(代理升级需要)
        public byte[]           szPackageID = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 升级包ID
        public byte[]           szCheckSum = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 升级包的SHA-256校验和
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    // CLIENT_ExecuteCloudUpgrader 入参
    public static class NET_IN_EXECUTE_CLOUD_UPGRADER extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public byte[]           szNewVersion = new byte[NetSDKLibStructure.MAX_COMMON_STRING_64]; // 上一次check得到的新版本号
        public int              nWay;                                 // 检测路径, 0-直连升级服务器检测, 1-通过代理服务器检测
        public NET_PROXY_SERVER_INFO stProxy;                         // 代理服务器地址, nWay==1时有意义
        public NET_CLOUD_UPGRADER_INFO stInfo;                        // 升级包信息

        public NET_IN_EXECUTE_CLOUD_UPGRADER() {
            this.dwSize = this.size();
        }
    }

    // CLIENT_ExecuteCloudUpgrader 出参
    public static class NET_OUT_EXECUTE_CLOUD_UPGRADER extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_EXECUTE_CLOUD_UPGRADER() {
            this.dwSize = this.size();
        }
    }

    // 执行在线云升级, pInParam和pOutParam内存由用户申请和释放
    public boolean CLIENT_ExecuteCloudUpgrader(LLong lLoginID,NET_IN_EXECUTE_CLOUD_UPGRADER pInParam,NET_OUT_EXECUTE_CLOUD_UPGRADER pOutParam,int nWaitTime);

    // CLIENT_GetCloudUpgraderState 入参
    public static class NET_IN_GET_CLOUD_UPGRADER_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_IN_GET_CLOUD_UPGRADER_STATE() {
            this.dwSize = this.size();
        }
    }

    // 在线升级状态
    public static class EM_CLOUD_UPGRADER_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CLOUD_UPGRADER_STATE_UNKNOWN = 0;  // 未知
        public static final int   EM_CLOUD_UPGRADER_STATE_NOUPGRADE = 1; // "Noupgrade"-未进行升级
        public static final int   EM_CLOUD_UPGRADER_STATE_PREPARING = 2; // "Preparing"-准备升级
        public static final int   EM_CLOUD_UPGRADER_STATE_DOWNLOADING = 3; // "Downloading"-正在下载数据
        public static final int   EM_CLOUD_UPGRADER_STATE_DOWNLOADFAILED = 4; // "DownloadFailed"-下载失败
        public static final int   EM_CLOUD_UPGRADER_STATE_UPGRADING = 5; // "Upgrading"-正在升级
        public static final int   EM_CLOUD_UPGRADER_STATE_INVALID = 6;  // "Invalid"-升级包不正确
        public static final int   EM_CLOUD_UPGRADER_STATE_FAILED = 7;   // "Failed"-升级包写入Flash失败
        public static final int   EM_CLOUD_UPGRADER_STATE_SUCCEEDED = 8; // "Succeeded"-升级包写入Flash成功
    }

    // CLIENT_GetCloudUpgraderState 出参
    public static class NET_OUT_GET_CLOUD_UPGRADER_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emState;                              // 升级状态，详见EM_CLOUD_UPGRADER_STATE
        public int              nProgress;                            // 升级进度, 状态是Downloading/Upgrading时有意义

        public NET_OUT_GET_CLOUD_UPGRADER_STATE() {
            this.dwSize = this.size();
        }
    }

    // 获取云升级在线升级状态, pInParam和pOutParam内存由用户申请和释放
    public boolean CLIENT_GetCloudUpgraderState(LLong lLoginID,NET_IN_GET_CLOUD_UPGRADER_STATE pInParam,NET_OUT_GET_CLOUD_UPGRADER_STATE pOutParam,int nWaitTime);

    // 事件类型 EVENT_IVS_PHONECALL_DETECT(打电话检测事件) 对应的数据块描述信息
    public static class DEV_EVENT_PHONECALL_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[NetSDKLibStructure.MAX_EVENT_NAME];    // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nGroupID;                             // 事件组ID
        public int              nCountInGroup;                        // 一个事件组内的抓拍张数
        public int              nIndexInGroup;                        // 一个事件组内的抓拍序号，从1开始
        public int              UTCMS;                                // UTC对应的毫秒数
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      stuDetectRegion = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public int              nRuleID;                              // 智能事件规则编号
        public int              nObjectNum;                           // 检测到的物体数量
        public DH_MSG_OBJECT[]  stuObjects = (DH_MSG_OBJECT[])new DH_MSG_OBJECT().toArray(128); // 多个检测到的物体信息
        public int              nSerialUUIDNum;                       // 智能物体数量
        public byte[]           szSerialUUID = new byte[128*22];      // 智能物体全局唯一物体标识
        // 有效数据位21位，包含’\0’
        // 前2位%d%d:01-视频片段, 02-图片, 03-文件, 99-其他
        // 中间14位YYYYMMDDhhmmss:年月日时分秒
        // 后5位%u%u%u%u%u：物体ID，如00001
        public int              bSceneImage;                          // stuSceneImage 是否有效
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage;                     // 全景广角图
        public byte[]           szUserName = new byte[32];            // 用户名称
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[188];           // 保留字节
    }

    // 事件类型 EVENT_IVS_SMOKING_DETECT(吸烟检测事件)对应的数据块描述信息
    public static class DEV_EVENT_SMOKING_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[NetSDKLibStructure.MAX_EVENT_NAME];    // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nGroupID;                             // 事件组ID
        public int              nCountInGroup;                        // 一个事件组内的抓拍张数
        public int              nIndexInGroup;                        // 一个事件组内的抓拍序号，从1开始
        public int              UTCMS;                                // UTC对应的毫秒数
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      stuDetectRegion = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte             szSerialUUID[] = new byte[22];        //智能物体全局唯一物体标识
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImageInfo = new NetSDKLibStructure.SCENE_IMAGE_INFO(); //全景广角图
        public byte             szUserName[] = new byte[32];          //用户名称
        public NET_IMAGE_INFO_EX2 stuImageInfo[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        //图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte             byReserved[] = new byte[1024];        //预留字节
    }

    // 事件类型 EVENT_IVS_FIREWARNING(火警事件) 对应的数据块描述信息
    public static class DEV_EVENT_FIREWARNING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nAction;                              // 1:开始 2:停止
        public int              nFSID;                                // Uint32	火情编号ID
        public int              emPicType;                            // 图片类型,详见 EM_FIREWARNING_PIC_TYPE
        public int              bIsLeaveFireDetect;                   // 是否属于离岗火点检测(TRUE:是 FALSE:不是)
        public Pointer          pstuImageInfo;                        // 图片信息数组,指针对应结构体NET_IMAGE_INFO_EX2的数组
        public int              nImageInfoNum;                        // 图片信息个数
        public int              nWarningInfoCount;                    //火灾信息数量
        public Pointer          pFireWarningInfo;                     //火灾信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_FIREWARNING_INFO}
        public byte[]           byReserved = new byte[1004-2*NetSDKLibStructure.POINTERSIZE]; // 保留字节
    }

    // 图片类型
    public static class EM_FIREWARNING_PIC_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_PIC_UNKNOWN = 0;                   // 未知
        public static final int   EM_PIC_NONE = 1;                      // 无
        public static final int   EM_PIC_OVERVIEW = 2;                  // 全景图
        public static final int   EM_PIC_DETAIL = 3;                    // 细节图
    }

    // 事件类型EVENT_IVS_LEFTDETECTION(物品遗留事件)对应的数据块描述信息
    public static class DEV_EVENT_LEFT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public short            nPreserID;                            // 事件触发的预置点号，从1开始（没有表示未知）
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public byte[]           byReserved2 = new byte[2];            // 字节对齐
        public NetSDKLibStructure.NET_GPS_INFO     stuGPSInfo;                           // GPS信息
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage;                     // 全景广角图
        public NET_IMAGE_INFO_EX2[] stuImageInfo = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体

        public DEV_EVENT_LEFT_INFO()
        {
            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 事件类型 EVENT_IVS_RIOTERDETECTION (聚众事件)对应的数据块描述信息
    public static class DEV_EVENT_RIOTERL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjectIDs = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.NET_MAX_OBJECT_LIST]; // 检测到的物体列表
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;
        public byte[]           byReserved = new byte[2];             // 保留字节
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;// 扩展信息
        public byte[]           szSourceID = new byte[32];            // 事件关联ID。应用场景是同一个物体或者同一张图片做不同分析，产生的多个事件的SourceID相同
        // 缺省时为空字符串，表示无此信息
        // 格式：类型+时间+序列号，其中类型2位，时间14位，序列号5位
        public NET_IMAGE_INFO_EX2[] stuImageInfo = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); // 图片信息数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[328];            // 保留字节,留待扩展.

        public DEV_EVENT_RIOTERL_INFO()
        {
            for (int i = 0; i < stuObjectIDs.length; ++i) {
                stuObjectIDs[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
            }

            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 事件类型EVENT_IVS_TAKENAWAYDETECTION(物品搬移事件)对应的数据块描述信息
    public static class DEV_EVENT_TAKENAWAYDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public short            nPreserID;                            // 事件触发的预置点号，从1开始（没有表示未知）
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;// 扩展信息
        public NET_IMAGE_INFO_EX2[] stuImageInfo = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); // 图片信息数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[418];            // 保留字节,留待扩展.

        public DEV_EVENT_TAKENAWAYDETECTION_INFO()
        {
            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 事件类型EVENT_IVS_PARKINGDETECTION(非法停车事件)对应的数据块描述信息
    public static class DEV_EVENT_PARKINGDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public byte[]           szCustomParkNo = new byte[64];        // 车位名称
        public NET_PRESET_POSITION stPosition;                        // 预置点的坐标和放大倍数 是一个数组，每个成员是int类型
																				    // 第一个参数是水平坐标，范围[0,3599]，表示0度到359.9度，度数扩大10倍表示。 
																				    // 第二个参数是垂直坐标，范围[-1800,1800]，表示-180.0度到+180.0度，度数扩大10倍表示。 
																				    // 第三个参数是放大参数，范围[0,127]，表示最小倍到最大倍的变倍位置
        public int              nCurChannelHFOV;                      // 当前报警通道的横向视场角，单位度，实际角度乘以100
        public int              nCurChannelVFOV;                      // 当前报警通道的纵向视场角，单位度，实际角度乘以100
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage;                     // 全景广角图
        public NET_IMAGE_INFO_EX2[] stuImageInfo = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); // 图片信息数组
        public int              nImageInfoNum;                        // 图片信息个数
        public int              nObjectBoatsNum;                      // 船只物体个数
        public NET_BOAT_OBJECT[] stuBoatObjects = new NET_BOAT_OBJECT[100]; // 船只物品信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[400];            // 保留字节,留待扩展.

        public DEV_EVENT_PARKINGDETECTION_INFO()
        {
            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
			for (int i = 0; i < stuBoatObjects.length; i++) {
			    stuBoatObjects[i] = new NET_BOAT_OBJECT();
			}
        }
    }

    // 事件类型EVENT_IVS_ABNORMALRUNDETECTION(异常奔跑事件)对应的数据块描述信息
    public static class DEV_EVENT_ABNORMALRUNDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public double           dbSpeed;                              // 物体运动速度,km/h
        public double           dbTriggerSpeed;                       // 触发速度,km/h
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nTrackLineNum;                        // 物体运动轨迹顶点数
        public NetSDKLibStructure.NET_POINT[]      TrackLine = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_TRACK_LINE_NUM]; // 物体运动轨迹
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;
        public byte             bRunType;                             // 异常奔跑类型, 0-快速奔跑, 1-突然加速, 2-突然减速
        public byte[]           byReserved = new byte[1];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           szReserved3 = new byte[4];            //字节对齐
        public Pointer          pstuSceneImage;                       //全景图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public byte[]           bReserved = new byte[600-NetSDKLibStructure.POINTERSIZE]; // 保留字节,留待扩展.

        public DEV_EVENT_ABNORMALRUNDETECTION_INFO()
        {
            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }

            for (int i = 0; i < TrackLine.length; ++i) {
                TrackLine[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 设置停车信息,对应CTRLTYPE_CTRL_SET_PARK_INFO命令参数
    public static class NET_CTRL_SET_PARK_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szPlateNumber = new byte[NetSDKLibStructure.MAX_PLATE_NUMBER_LEN]; // 车牌号码
        public int              nParkTime;                            // 停车时长,单位:分钟
        public byte[]           szMasterofCar = new byte[NetSDKLibStructure.MAX_MASTER_OF_CAR_LEN]; // 车主姓名
        public byte[]           szUserType = new byte[NetSDKLibStructure.MAX_USER_TYPE_LEN]; // 用户类型,非通用,用于出入口抓拍一体机
        // monthlyCardUser表示月卡用户,yearlyCardUser表示年卡用户,longTimeUser表示长期用户/,casualUser表示临时用户/Visitor
        public int              nRemainDay;                           // 到期天数
        public byte[]           szParkCharge = new byte[NetSDKLibStructure.MAX_PARK_CHARGE_LEN]; // 停车费
        public int              nRemainSpace;                         // 停车库余位数
        public int              nPassEnable;                          // 0:不允许车辆通过 1:允许车辆通过
        public NET_TIME         stuInTime;                            // 车辆入场时间
        public NET_TIME         stuOutTime;                           // 车辆出场时间
        public int              emCarStatus;                          // 过车状态 详见EM_CARPASS_STATUS
        public byte[]           szCustom = new byte[NetSDKLibStructure.MAX_CUSTOM_LEN];  // 自定义显示字段，默认空
        public byte[]           szSubUserType = new byte[NetSDKLibStructure.MAX_SUB_USER_TYPE_LEN]; // 用户类型（szUserType字段）的子类型
        public byte[]           szRemarks = new byte[NetSDKLibStructure.MAX_REMARKS_LEN]; // 备注信息
        public byte[]           szResource = new byte[NetSDKLibStructure.MAX_RESOURCE_LEN]; // 资源文件（视频或图片）视频支持:mp4格式; 图片支持:BMP/jpg/JPG/jpeg/JPEG/png/PNG格式
        public int              nParkTimeout;                         // 停车超时时间，单位分钟。为0表示未超时，不为0表示超时时间。
        public int              nChannel;                             //通道号

        public NET_CTRL_SET_PARK_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 过车状态
    public static class EM_CARPASS_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CARPASS_STATUS_UNKNOWN = 0;        // 未知状态
        public static final int   EM_CARPASS_STATUS_CARPASS = 1;        // 过车状态
        public static final int   EM_CARPASS_STATUS_NORMAL = 2;         // 无车状态
    }

    // 事件类型EVENT_IVS_MOVEDETECTION(移动事件)对应的数据块描述信息
    public static class DEV_EVENT_MOVE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nTrackLineNum;                        // 物体运动轨迹顶点数
        public NetSDKLibStructure.NET_POINT[]      stuTrackLine = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_TRACK_LINE_NUM]; // 物体运动轨迹
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public int              nAnimalNum;                           // 动物个数
        public Pointer          pstuAnimals;                          // 动物信息,结构体VA_OBJECT_ANIMAL数组指针
        public int              nMsgObjArrayCount;                    // 检测到的物体信息个数
        public Pointer          pMsgObjArray;                         // 检测到的物体信息数组指针，结构体NET_MSG_OBJECT_EX数组指针
        public int              nImageNum;                            // 图片信息个数
        public Pointer          pImageArray;                          // 图片信息数组，结构体NET_IMAGE_INFO_EX2数组指针
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[236];            // 保留字节,留待扩展.

        public DEV_EVENT_MOVE_INFO()
        {
            for (int i = 0; i < DetectRegion.length; ++i) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }

            for (int i = 0; i < stuTrackLine.length; ++i) {
                stuTrackLine[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 监测控制和数据采集设备的点位表路径信息输入参数, 查询条件
    public static class NET_IN_SCADA_POINT_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDevType = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备类型

        public NET_IN_SCADA_POINT_LIST_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 点位表路径信息
    public static class NET_SCADA_POINT_LIST extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nIndexValidNum;                       // 有效的配置下标个数
        public int[]            nIndex = new int[NetSDKLibStructure.MAX_SCADA_POINT_LIST_INDEX]; // SCADADev配置下标值, 从0开始
        public byte[]           szPath = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 点表的完整路径

        public NET_SCADA_POINT_LIST()
        {
            this.dwSize = this.size();
        }
    }

    // 监测控制和数据采集设备的点位表路径信息输出参数, 查询结果
    public static class NET_OUT_SCADA_POINT_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nList;                                // 有效点位表路径信息个数
        public NET_SCADA_POINT_LIST[] stuList = new NET_SCADA_POINT_LIST[NetSDKLibStructure.MAX_SCADA_POINT_LIST_INFO_NUM]; // 点位表路径信息

        public NET_OUT_SCADA_POINT_LIST_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuList.length; ++i) {
                stuList[i] = new NET_SCADA_POINT_LIST();
            }
        }
    }

    // 监测控制和数据采集设备的点位表信息, (对应 DH_DEVSTATE_SCADA_POINT_LIST 命令)
    public static class NET_SCADA_POINT_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public NET_IN_SCADA_POINT_LIST_INFO stuIn;                    // 查询条件
        public NET_OUT_SCADA_POINT_LIST_INFO stuOut;                  // 查询结果

        public NET_SCADA_POINT_LIST_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // SCADA监测点位查询条件
    public static class NET_IN_SCADA_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emPointType;                          // 待查询的点位类型，详见EM_NET_SCADA_POINT_TYPE

        public NET_IN_SCADA_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 点表信息
    public static class NET_SCADA_POINT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDevName = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备名称
        public int              nYX;                                  // 有效遥信个数
        public int[]            anYX = new int[NetSDKLibStructure.MAX_SCADA_YX_NUM];     // 遥信信息
        public int              nYC;                                  // 有效遥测个数
        public float[]          afYC = new float[NetSDKLibStructure.MAX_SCADA_YC_NUM];   // 遥测信息

        public NET_SCADA_POINT_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // SCADA监测点位查询结果
    public static class NET_OUT_SCADA_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nPointInfoNum;                        // 有效点表个数
        public NET_SCADA_POINT_INFO[] stuPointInfo = new NET_SCADA_POINT_INFO[NetSDKLibStructure.MAX_SCADA_POINT_INFO_NUM]; // 点表信息

        public NET_OUT_SCADA_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuPointInfo.length; ++i) {
                stuPointInfo[i] = new NET_SCADA_POINT_INFO();
            }
        }
    }

    // 监测控制和数据采集设备的监测点位信息(对应 DH_DEVSTATE_SCADA_INFO 命令)
    public static class NET_SCADA_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public NET_IN_SCADA_INFO stuIn;                               // 查询条件
        public NET_OUT_SCADA_INFO stuOut;                             // 查询结果

        public NET_SCADA_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // SCADA能力名称
    public static class EM_NET_SCADA_CAPS_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_NET_SCADA_CAPS_TYPE_UNKNOWN = 0;
        public static final int   EM_NET_SCADA_CAPS_TYPE_ALL = 1;       // 所有类型
        public static final int   EM_NET_SCADA_CAPS_TYPE_DEV = 2;       // DevInfo
    }

    // 监测控制和数据采集设备能力信息查询条件
    public static class NET_IN_SCADA_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emType;                               // 查询类型，详见EM_NET_SCADA_CAPS_TYPE

        public NET_IN_SCADA_CAPS()
        {
            this.dwSize = this.size();
        }
    }

    // 监测控制和数据采集设备类型能力信息
    public static class NET_OUT_SCADA_CAPS_ITEM extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDevType = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 设备类型
        public int              nValidName;                           // 有效设备名称个数
        public SCADA_DEVICE_NAME[] stuScadaDevNames = new SCADA_DEVICE_NAME[NetSDKLibStructure.MAX_NET_SCADA_CAPS_NAME]; // 设备名称, 唯一标示设备

        public NET_OUT_SCADA_CAPS_ITEM()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuScadaDevNames.length; ++i) {
                stuScadaDevNames[i] = new SCADA_DEVICE_NAME();
            }
        }
    }

    public static class SCADA_DEVICE_NAME extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDevName = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 数据采集设备名称
    }

    // 监测控制和数据采集设备能力信息查询结果
    public static class NET_OUT_SCADA_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nValidType;                           // 有效设备类型个数
        public NET_OUT_SCADA_CAPS_ITEM[] stuItems = new NET_OUT_SCADA_CAPS_ITEM[NetSDKLibStructure.MAX_NET_SCADA_CAPS_TYPE]; // 最多16个类型

        public NET_OUT_SCADA_CAPS()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuItems.length; ++i) {
                stuItems[i] = new NET_OUT_SCADA_CAPS_ITEM();
            }
        }
    }

    // 监测控制和数据采集设备能力信息(对应 DH_DEVSTATE_SCADA_CAPS 命令)
    public static class NET_SCADA_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public NET_IN_SCADA_CAPS stuIn;                               // 查询条件
        public NET_OUT_SCADA_CAPS stuOut;                             // 查询结果

        public NET_SCADA_CAPS()
        {
            this.dwSize = this.size();
        }
    }

    // 点位信息(通过设备、传感器点位获取)
    public static class NET_SCADA_POINT_BY_ID_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emType;                               // 点位类型，详见EM_NET_SCADA_POINT_TYPE
        public byte[]           szID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 监测点位ID
        public int              nMeasuredVal;                         // 点位类型为YX时有效
        public float            fMeasureVal;                          // 点位类型为YC时有效
        public int              nSetupVal;                            // 点位类型为YK时有效
        public float            fSetupVal;                            // 点位类型为YT时有效
        public int              nStatus;                              // 数据状态, -1:未知, 0:正常, 1:1级告警, 2:2级告警, 3:3级告警, 4:4级告警, 5:操作事件, 6:无效数据
        public NET_TIME         stuTime;                              // 记录时间
        public byte[]           szPointName = new byte[32];           // 点位名称

        public NET_SCADA_POINT_BY_ID_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 通过设备、获取监测点位信息(对应 NET_SCADA_INFO_BY_ID)
    public static class NET_SCADA_INFO_BY_ID extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szSensorID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 输入参数, 探测器ID
        public int              nIDs;                                 // 输入参数, 有效点位ID个数
        public SCADA_ID_EX[]    stuIDs = new SCADA_ID_EX[NetSDKLibStructure.MAX_SCADA_ID_OF_SENSOR_NUM]; // 输入参数, 点位ID
        public int              bIsHandle;                            // 输入参数，返回数据是否经过处理（无效数据过滤等）:"false"：未处理，"true"：处理
        public int              nMaxCount;                            // 输入参数, pstuInfo对应数组个数
        public int              nRetCount;                            // 输出参数, pstInfo实际返回有效个数, 可能大于用户分配个数nMaxCount
        public Pointer          pstuInfo;                             // 输入输出参数, 用户分配缓存,大小为sizeof(NET_SCADA_POINT_BY_ID_INFO)*nMaxCount，指向NET_SCADA_POINT_BY_ID_INFO

        public NET_SCADA_INFO_BY_ID()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuIDs.length; ++i) {
                stuIDs[i] = new SCADA_ID_EX();
            }
        }
    }

    public static class SCADA_ID_EX extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 监测点位ID
    }

    // 监测设备信息
    public static class NET_SCADA_DEVICE_ID_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备id
        public byte[]           szDevName = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 设备名称, 和CFG_SCADA_DEV_INFO配置中的szDevName一致
        public byte[]           reserve = new byte[1024];
    }

    // 获取当前主机所接入的外部设备ID
    public static class NET_SCADA_DEVICE_LIST extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nMax;                                 // 用户分配的结构体个数
        public int              nRet;                                 // 设备实际返回的有效结构体个数
        public Pointer          pstuDeviceIDInfo;                     // 监测设备信息,用户分配内存,大小为sizeof(NET_SCADA_DEVICE_ID_INFO)*nMax，指向NET_SCADA_DEVICE_ID_INFO

        public NET_SCADA_DEVICE_LIST()
        {
            this.dwSize = this.size();
        }
    }

    // 点位阈值信息
    public static class NET_SCADA_ID_THRESHOLD_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emPointType;                          // 点位类型，详见EM_NET_SCADA_POINT_TYPE
        public byte[]           szID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 点位ID
        public float            fThreshold;                           // 告警门限
        public float            fAbsoluteValue;                       // 绝对阈值
        public float            fRelativeValue;                       // 相对阈值
        public int              nStatus;                              // 数据状态, -1:未知, 0:正常, 1:1级告警, 2:2级告警, 3:3级告警, 4:4级告警, 5:操作事件, 6:无效数据

        public NET_SCADA_ID_THRESHOLD_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_SCADAGetThreshold输入参数
    public static class NET_IN_SCADA_GET_THRESHOLD extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 外接设备id
        public int              nIDs;                                 // 有效监测点位个数
        public SCADA_ID[]       stuIDs = new SCADA_ID[NetSDKLibStructure.MAX_SCADA_ID_NUM]; // 待获取的监测点位ID

        public NET_IN_SCADA_GET_THRESHOLD()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuIDs.length; ++i) {
                stuIDs[i] = new SCADA_ID();
            }
        }
    }

    public static class SCADA_ID extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 监测点位ID
    }

    // CLIENT_SCADAGetThreshold输出参数
    public static class NET_OUT_SCADA_GET_THRESHOLD extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nMax;                                 // 用户分配的点位阈值信息个数
        public int              nRet;                                 // 实际返回的点位阈值信息
        public Pointer          pstuThresholdInfo;                    // 点位阈值信息, 用户分配内存,大小为sizeof(NET_SCADA_ID_THRESHOLD_INFO)*nMax，指向NET_SCADA_ID_THRESHOLD_INFO

        public NET_OUT_SCADA_GET_THRESHOLD()
        {
            this.dwSize = this.size();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // CLIENT_SCADASetThreshold输入参数
    public static class NET_IN_SCADA_SET_THRESHOLD extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 外接设备id
        public int              nMax;                                 // 用户分配的点位阈值信息个数
        public Pointer          pstuThresholdInfo;                    // 点位阈值信息, 用户分配内存,大小为sizeof(NET_SCADA_ID_THRESHOLD_INFO)*nMax，指向NET_SCADA_ID_THRESHOLD_INFO

        public NET_IN_SCADA_SET_THRESHOLD()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_SCADASetThreshold输出参数
    public static class NET_OUT_SCADA_SET_THRESHOLD extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nSuccess;                             // 有效的存放设置阈值成功的id个数
        public SCADA_ID[]       stuSuccessIDs = new SCADA_ID[NetSDKLibStructure.MAX_SCADA_ID_NUM]; // 设置阈值成功的id,用户分配内存
        public int              nFail;                                // 用户分配的存放设置阈值失败的id个数
        public SCADA_ID[]       stuFailIDs = new SCADA_ID[NetSDKLibStructure.MAX_SCADA_ID_NUM]; // 设置阈值失败的id, 用户分配内存

        public NET_OUT_SCADA_SET_THRESHOLD()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuSuccessIDs.length; ++i) {
                stuSuccessIDs[i] = new SCADA_ID();
            }

            for (int i = 0; i < stuFailIDs.length; ++i) {
                stuFailIDs[i] = new SCADA_ID();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // CLIENT_StartFindSCADA输入参数
    public static class NET_IN_SCADA_START_FIND extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public NET_TIME         stuStartTime;                         // 开始时间, 必填
        public int              bEndTime;                             // 是否限制结束时间, TRUE: 必填stuEndTime, FLASE: 不限制结束时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // DeviceID, 必填
        public byte[]           szID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 监测点位ID, 必填
        public int              nIDsNum;                              // 监测点ID数组长度
        public SCADA_ID[]       stuIDs = new SCADA_ID[32];            // 监控点ID号数组，SDT离网供电扩展字段

        public NET_IN_SCADA_START_FIND()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuIDs.length; ++i) {
                stuIDs[i] = new SCADA_ID();
            }
        }
    }

    // CLIENT_StartFindSCADA输出参数
    public static class NET_OUT_SCADA_START_FIND extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              dwTotalCount;                         // 符合查询条件的总数

        public NET_OUT_SCADA_START_FIND()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_DoFindSCADA输入参数
    public static class NET_IN_SCADA_DO_FIND extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nStartNo;                             // 起始序号
        public int              nCount;                               // 本次欲获得结果的个数

        public NET_IN_SCADA_DO_FIND()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_DoFindSCADA输出参数
    public static class NET_OUT_SCADA_DO_FIND extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nRetNum;                              // 本次查询到的个数
        public Pointer          pstuInfo;                             // 查询结果, 用户分配内存,大小为sizeof(NET_SCADA_POINT_BY_ID_INFO)*nMaxNum，指向NET_SCADA_POINT_BY_ID_INFO
        public int              nMaxNum;                              // 用户分配内存的个数

        public NET_OUT_SCADA_DO_FIND()
        {
            this.dwSize = this.size();
        }
    }

    // 监控点值设置参数
    public static class NET_SCADA_POINT_SET_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emType;                               // 监控点位类型,取YK、YT两种类型，详见EM_NET_SCADA_POINT_TYPE
        public byte[]           szPointID = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 监控点位ID
        public int              nSetupVal;                            // 点位类型为YK时有效
        public float            fSetupVal;                            // 点位类型为YT时有效

        public NET_SCADA_POINT_SET_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 监控点值设置参数列表,CLIENT_SetSCADAInfo()接口输入参数
    public static class NET_IN_SCADA_POINT_SET_INFO_LIST extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public byte[]           szDevID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 设备ID
        public int              nPointNum;                            // 监控点个数
        public NET_SCADA_POINT_SET_INFO[] stuList = new NET_SCADA_POINT_SET_INFO[NetSDKLibStructure.MAX_SCADA_ID_OF_SENSOR_NUM]; // 监控点列表信息

        public NET_IN_SCADA_POINT_SET_INFO_LIST()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuList.length; ++i) {
                stuList[i] = new NET_SCADA_POINT_SET_INFO();
            }
        }
    }

    // 设置监控点值返回的结果列表,CLIENT_SetSCADAInfo()接口输出参数
    public static class NET_OUT_SCADA_POINT_SET_INFO_LIST extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nSuccess;                             // 有效的控制或调节成功的ID个数
        public SCADA_ID[]       stuSuccessIDs = new SCADA_ID[NetSDKLibStructure.MAX_SCADA_ID_OF_SENSOR_NUM]; // 控制或调节成功的ID的列表
        public int              nFail;                                // 有效的控制或调节失败的ID个数
        public SCADA_ID[]       stuFailIDs = new SCADA_ID[NetSDKLibStructure.MAX_SCADA_ID_OF_SENSOR_NUM]; // 控制或调节失败的ID的列表

        public NET_OUT_SCADA_POINT_SET_INFO_LIST()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuSuccessIDs.length; ++i) {
                stuSuccessIDs[i] = new SCADA_ID();
            }

            for (int i = 0; i < stuFailIDs.length; ++i) {
                stuFailIDs[i] = new SCADA_ID();
            }
        }
    }

    // 获取阈值,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_SCADAGetThreshold(LLong lLoginID,NET_IN_SCADA_GET_THRESHOLD pInParam,NET_OUT_SCADA_GET_THRESHOLD pOutParam,int nWaitTime);

    // 设置阈值,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_SCADASetThreshold(LLong lLoginID,NET_IN_SCADA_SET_THRESHOLD pInParam,NET_OUT_SCADA_SET_THRESHOLD pOutParam,int nWaitTime);

    // 开始查询SCADA点位历史数据,pInParam与pOutParam内存由用户申请释放
    public LLong CLIENT_StartFindSCADA(LLong lLoginID,NET_IN_SCADA_START_FIND pInParam,NET_OUT_SCADA_START_FIND pOutParam,int nWaitTime);

    // 获取SCADA点位历史数据,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_DoFindSCADA(LLong lFindHandle,NET_IN_SCADA_DO_FIND pInParam,NET_OUT_SCADA_DO_FIND pOutParam,int nWaitTime);

    // 停止查询SCADA点位历史数据
    public boolean CLIENT_StopFindSCADA(LLong lFindHandle);

    // 设置监测点位信息,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_SCADASetInfo(LLong lLoginID,NET_IN_SCADA_POINT_SET_INFO_LIST pInParam,NET_OUT_SCADA_POINT_SET_INFO_LIST pOutParam,int nWaitTime);

    public static class CFG_SCADA_DEV_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 是否启用
        public byte[]           szDevType = new byte[NetSDKLibStructure.CFG_COMMON_STRING_64]; // 设备类型
        public byte[]           szDevName = new byte[NetSDKLibStructure.CFG_COMMON_STRING_64]; // 设备名称, 唯一标示设备用
        public int              nSlot;                                // 虚拟槽位号, 详见AlarmSlotBond配置
        public int              nLevel;                               // 如果Slot绑定的是NetCollection类型的话，该字段为-1
        public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
    }

    // 高频次报警
    public static class CFG_HIGH_FREQUENCY extends NetSDKLibStructure.SdkStructure
    {
        public int              nPeriod;                              // 统计周期, 以秒为单位, 默认30分钟(1800s)
        public int              nMaxCount;                            // 在对应统计周期内最大允许上报报警数
    }

    // 告警屏蔽规则配置(对应 CFG_CMD_ALARM_SHIELD_RULE)
    public static class CFG_ALARM_SHIELD_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public CFG_HIGH_FREQUENCY stuHighFreq;                        // 高频次报警, 在一定周期内允许上报的报警次数，以此过滤对于报警的频繁上报导致信息干扰
    }

    // 获取车位锁状态接口，CLIENT_GetParkingLockState 入参
    public static class NET_IN_GET_PARKINGLOCK_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_IN_GET_PARKINGLOCK_STATE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 车位锁状态
    public static class EM_STATE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_STATE_TYPE_UNKNOW = 0;             // 未知
        public static final int   EM_STATE_TYPE_LOCKRISE = 1;           // 车位锁升起
        public static final int   EM_STATE_TYPE_LOCKDOWN = 2;           // 车位锁降下
        public static final int   EM_STATE_TYPE_LOCKERROR = 3;          // 车位锁异常
    }

    // 车位锁状态通信接口参数
    public static class NET_STATE_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nLane;                                // 车位号
        public int              emState;                              // 车位锁状态，详见EM_STATE_TYPE
        public byte[]           byReserved = new byte[256];           // 保留
    }

    // 获取车位锁状态， CLIENT_GetParkingLockState 出参
    public static class NET_OUT_GET_PARKINGLOCK_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nStateListNum;                        // 车位锁状态个数
        public NET_STATE_LIST_INFO[] stuStateList = new NET_STATE_LIST_INFO[NetSDKLibStructure.MAX_PARKINGLOCK_STATE_NUM]; // 车位锁状态

        public NET_OUT_GET_PARKINGLOCK_STATE_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuStateList.length; ++i) {
                stuStateList[i] = new NET_STATE_LIST_INFO();
            }
        }
    }

    // 设置车位锁状态接口，CLIENT_SetParkingLockState 入参
    public static class NET_IN_SET_PARKINGLOCK_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nStateListNum;                        // 车位锁状态个数
        public NET_STATE_LIST_INFO[] stuStateList = new NET_STATE_LIST_INFO[NetSDKLibStructure.MAX_PARKINGLOCK_STATE_NUM]; // 车位锁状态
        public int              nControlType;                         // 控制车位锁状态类型, 0:未知, 1:平台正常控制, 2:平台手动控制

        public NET_IN_SET_PARKINGLOCK_STATE_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuStateList.length; ++i) {
                stuStateList[i] = new NET_STATE_LIST_INFO();
            }
        }
    }

    // 设置车位锁状态接口，CLIENT_SetParkingLockState 出参
    public static class NET_OUT_SET_PARKINGLOCK_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_SET_PARKINGLOCK_STATE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 获取车位锁状态
    public boolean CLIENT_GetParkingLockState(LLong lLoginID,NET_IN_GET_PARKINGLOCK_STATE_INFO pstInParam,NET_OUT_GET_PARKINGLOCK_STATE_INFO pstOutParam,int nWaitTime);

    // 设置车位锁状态
    public boolean CLIENT_SetParkingLockState(LLong lLoginID,NET_IN_SET_PARKINGLOCK_STATE_INFO pstInParm,NET_OUT_SET_PARKINGLOCK_STATE_INFO pstOutParam,int nWaitTIme);

    // 刻录配置
    public static class CFG_JUDICATURE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDiskPassword = new byte[NetSDKLibStructure.MAX_PASSWORD_LEN]; // 光盘密码(废弃, 使用szPassword和nPasswordLen)
        public byte[]           szCaseNo = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 案件编号
        public int              bCaseNoOsdEn;                         // 案件编号叠加使能
        public byte[]           szCaseTitle = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 案件名称
        public int              bCaseTitleOsdEn;                      // 案件名称叠加使能
        public byte[]           szOfficer = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 办案人员
        public int              bOfficerOsdEn;                        // 办案人员叠加使能
        public byte[]           szLocation = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 办案地点
        public int              bLocationOsdEn;                       // 办案地点叠加使能
        public byte[]           szRelatedMan = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 涉案人员
        public int              bRelatedManOsdEn;                     // 涉案人员叠加使能
        public byte[]           szDiskNo = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 光盘编号
        public int              bDiskNoOsdEn;                         // 光盘编号叠加使能
        public int              bCustomCase;                          // TRUE:自定义案件信息,FALSE: 上边szCaseNo等字段有效
        public int              nCustomCase;                          // 实际CFG_CUSTOMCASE个数
        public CFG_CUSTOMCASE[] stuCustomCases = new CFG_CUSTOMCASE[NetSDKLibStructure.MAX_CUSTOMCASE_NUM]; // 自定义案件信息
        public int              bDataCheckOsdEn;                      // 光盘刻录数据校验配置 叠加使能
        public int              bAttachFileEn;                        // 附件上传使能
        public byte[]           szPassword = new byte[NetSDKLibStructure.MAX_PASSWORD_LEN]; // 密码, 刻录光盘时、配置读保护密码
        public int              nPasswordLen;                         // 密码长度
        public CFG_NET_TIME     stuStartTime;                         // 片头信息叠加开始时间
        public int              nPeriod;                              // 片头信息叠加时间长度，单位：分钟

        public CFG_JUDICATURE_INFO()
        {
            for (int i = 0; i < stuCustomCases.length; ++i) {
                stuCustomCases[i] = new CFG_CUSTOMCASE();
            }
        }
    }

    // 自定义案件信息
    public static class CFG_CUSTOMCASE extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szCaseTitle = new byte[NetSDKLibStructure.MAX_OSD_TITLE_LEN]; // 案件名称
        public byte[]           szCaseContent = new byte[NetSDKLibStructure.MAX_OSD_SUMMARY_LEN]; // 案件名称
        public int              bCaseNoOsdEn;                         // 案件编号叠加使能
    }

    // 叠加类型
    public static class NET_EM_OSD_BLEND_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_EM_OSD_BLEND_TYPE_UNKNOWN = 0;    // 未知叠加类型
        public static final int   NET_EM_OSD_BLEND_TYPE_MAIN = 1;       // 叠加到主码流
        public static final int   NET_EM_OSD_BLEND_TYPE_EXTRA1 = 2;     // 叠加到辅码流1
        public static final int   NET_EM_OSD_BLEND_TYPE_EXTRA2 = 3;     // 叠加到辅码流2
        public static final int   NET_EM_OSD_BLEND_TYPE_EXTRA3 = 4;     // 叠加到辅码流3
        public static final int   NET_EM_OSD_BLEND_TYPE_SNAPSHOT = 5;   // 叠加到抓图
        public static final int   NET_EM_OSD_BLEND_TYPE_PREVIEW = 6;    // 叠加到预览视频
    }

    // 编码物件-通道标题
    public static class NET_OSD_CHANNEL_TITLE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emOsdBlendType;                       // 叠加类型，不管是获取还是设置都要设置该字段，详见NET_EM_OSD_BLEND_TYPE
        public int              bEncodeBlend;                         // 是否叠加
        public NET_COLOR_RGBA   stuFrontColor;                        // 前景色
        public NET_COLOR_RGBA   stuBackColor;                         // 背景色
        public NET_RECT         stuRect;                              // 区域, 坐标取值[0~8191], 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public int              emTextAlign;                          // 文本对齐方式 ,参考枚举{@link EM_TITLE_TEXT_ALIGNTYPE}

        public NET_OSD_CHANNEL_TITLE()
        {
            this.dwSize = this.size();
        }
    }

    // 编码物件-时间标题
    public static class NET_OSD_TIME_TITLE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emOsdBlendType;                       // 叠加类型，不管是获取还是设置都要设置该字段，详见NET_EM_OSD_BLEND_TYPE
        public int              bEncodeBlend;                         // 是否叠加
        public NET_COLOR_RGBA   stuFrontColor;                        // 前景色
        public NET_COLOR_RGBA   stuBackColor;                         // 背景色
        public NET_RECT         stuRect;                              // 区域, 坐标取值[0~8191], 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public int              bShowWeek;                            // 是否显示星期

        public NET_OSD_TIME_TITLE()
        {
            this.dwSize = this.size();
        }
    }

    // 编码物件-自定义标题信息
    public static class NET_CUSTOM_TITLE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEncodeBlend;                         // 是否叠加
        public NET_COLOR_RGBA   stuFrontColor;                        // 前景色
        public NET_COLOR_RGBA   stuBackColor;                         // 背景色
        public NET_RECT         stuRect;                              // 区域, 坐标取值[0~8191], 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public byte[]           szText = new byte[NetSDKLibStructure.CUSTOM_TITLE_LEN];  // 标题内容
        public int              emTitleType;                          //叠加标题用途,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_TITLE_TYPE}
        public int              emTextAlign;                          //文本对齐方式,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.EM_TITLE_TEXT_ALIGNTYPE}
        public byte[]           byReserved = new byte[504];           // 保留字节
    }

    // 编码物件-自定义标题
    public static class NET_OSD_CUSTOM_TITLE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emOsdBlendType;                       // 叠加类型，不管是获取还是设置都要设置该字段，详见NET_EM_OSD_BLEND_TYPE
        public int              nCustomTitleNum;                      // 自定义标题数量
        public NET_CUSTOM_TITLE_INFO[] stuCustomTitle = new NET_CUSTOM_TITLE_INFO[NetSDKLibStructure.MAX_CUSTOM_TITLE_NUM]; // 自定义标题

        public NET_OSD_CUSTOM_TITLE()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuCustomTitle.length; ++i) {
                stuCustomTitle[i] = new NET_CUSTOM_TITLE_INFO();
            }
        }
    }

    // 标题文本对齐方式
    public static class EM_TITLE_TEXT_ALIGNTYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_TEXT_ALIGNTYPE_INVALID = 0;        // 无效的对齐方式
        public static final int   EM_TEXT_ALIGNTYPE_LEFT = 1;           // 左对齐
        public static final int   EM_TEXT_ALIGNTYPE_XCENTER = 2;        // X坐标中对齐
        public static final int   EM_TEXT_ALIGNTYPE_YCENTER = 3;        // Y坐标中对齐
        public static final int   EM_TEXT_ALIGNTYPE_CENTER = 4;         // 居中
        public static final int   EM_TEXT_ALIGNTYPE_RIGHT = 5;          // 右对齐
        public static final int   EM_TEXT_ALIGNTYPE_TOP = 6;            // 按照顶部对齐
        public static final int   EM_TEXT_ALIGNTYPE_BOTTOM = 7;         // 按照底部对齐
        public static final int   EM_TEXT_ALIGNTYPE_LEFTTOP = 8;        // 按照左上角对齐
        public static final int   EM_TEXT_ALIGNTYPE_CHANGELINE = 9;     // 换行对齐
    }

    // 自定义标题文本对齐
    public static class NET_OSD_CUSTOM_TITLE_TEXT_ALIGN extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nCustomTitleNum;                      // 自定义标题数量
        public int[]            emTextAlign = new int[NetSDKLibStructure.MAX_CUSTOM_TITLE_NUM]; // 自定义标题文本对齐方式，详见EM_TITLE_TEXT_ALIGNTYPE

        public NET_OSD_CUSTOM_TITLE_TEXT_ALIGN()
        {
            this.dwSize = this.size();
        }
    }

    //编码物件-公共配置信息
    public static class NET_OSD_COMM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public double           fFontSizeScale;                       // 叠加字体大小放大比例
        //当fFontSizeScale≠0时,nFontSize不起作用
        //当fFontSizeScale=0时,nFontSize起作用
        //设备默认fFontSizeScale=1.0
        //如果需要修改倍数，修改该值
        //如果需要按照像素设置，则置该值为0，nFontSize的值生效
        public int              nFontSize;                            // 叠加到主码流上的全局字体大小,单位 px, 默认24.
        //和fFontSizeScale共同作用
        public int              nFontSizeExtra1;                      // 叠加到辅码流1上的全局字体大小,单位 px
        public int              nFontSizeExtra2;                      // 叠加到辅码流2上的全局字体大小,单位 px
        public int              nFontSizeExtra3;                      // 叠加到辅码流3上的全局字体大小,单位 px
        public int              nFontSizeSnapshot;                    // 叠加到抓图流上的全局字体大小, 单位 px
        public int              nFontSizeMergeSnapshot;               // 叠加到抓图流上合成图片的字体大小,单位 px
        public int              emFontSolution;                       // 叠加到主码流上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}
        public int              emFontSolutionExtra1;                 // 叠加到辅码流1上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}
        public int              emFontSolutionExtra2;                 // 叠加到辅码流2上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}
        public int              emFontSolutionExtra3;                 // 叠加到辅码流3上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}
        public int              emFontSolutionSnapshot;               // 叠加到抓图码流上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}
        public int              emFontSolutionMergeSnapshot;          // 叠加到合成抓图流上的字体方案,参考枚举{@link EM_OSD_FONT_SOLUTION}

        public NET_OSD_COMM_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 播报单元类型
    public static class NET_PLAYAUDIO_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_PLAYAUDIO_TYPE_UNKNOWN = 0;
        public static final int   NET_PLAYAUDIO_TYPE_PHRASE = 1;        // 短语类型,不进行解析,依次读每个字,有该字的语音文件支持
        public static final int   NET_PLAYAUDIO_TYPE_FILE = 2;          // 播放指定路径的语音文件(设备端完整路径)
        public static final int   NET_PLAYAUDIO_TYPE_PLATERNUM = 3;     // 播报车牌号码,按车牌号码格式读出
        public static final int   NET_PLAYAUDIO_TYPE_MONEY = 4;         // 播报金额,按金额形式读出
        public static final int   NET_PLAYAUDIO_TYPE_DATE = 5;          // 播报日期,按日期形式读出
        public static final int   NET_PLAYAUDIO_TYPE_TIME = 6;          // 播报时间,按时间形式读出
        public static final int   NET_PLAYAUDIO_TYPE_EMPTY = 7;         // 空类型,停顿一个字符时间
    }

    // 语音播报内容
    public static class NET_CTRL_PLAYAUDIO_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emPlayAudioType;                      // 播报单元类型,详见NET_PLAYAUDIO_TYPE，详见NET_PLAYAUDIO_TYPE
        public byte[]           szDetail = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 详细内容各类型详细内容：
        // Phrase类型:"欢迎"
        // File类型: "/home/停车.pcm"
        // PlateNumbe类型: "浙A12345"
        // Money类型: "80.12元"
        // Date类型: "2014年4月10日"
        // Time类型: "1天10小时20分5秒
        public int              nRepeatTimes;                         // 播放重复次数
        public int              emPriority;                           // 播放优先级,对应枚举EM_PLAYAUDIO_PRIORITY
        public byte[]           szPlayID = new byte[16];              //本次投放唯一标识符
        public int              nDuration;                            //播放持续时间, 单位:秒

        public NET_CTRL_PLAYAUDIO_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_ControlDevice接口的 DH_CTRL_START_PLAYAUDIOEX 命令参数
    public static class NET_CTRL_START_PLAYAUDIOEX extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nAudioCount;                          // 播报内容数目
        public NET_CTRL_PLAYAUDIO_INFO[] stuAudioInfos = new NET_CTRL_PLAYAUDIO_INFO[NetSDKLibStructure.NET_MAX_PLAYAUDIO_COUNT]; // 语音播报内容
        public int              nListRepeatTimes;                     // 语音播报内容重复次数, 描述所有播报单元

        public NET_CTRL_START_PLAYAUDIOEX()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuAudioInfos.length; ++i) {
                stuAudioInfos[i] = new NET_CTRL_PLAYAUDIO_INFO();
            }
        }
    }

    // 串口基本属性
    public static class CFG_COMM_PROP extends NetSDKLibStructure.SdkStructure
    {
        public byte             byDataBit;                            // 数据位；0：5，1：6，2：7，3：8
        public byte             byStopBit;                            // 停止位；0：1位，1：1.5位，2：2位
        public byte             byParity;                             // 校验位；0：无校验，1：奇校验；2：偶校验
        public byte             byBaudRate;                           // 波特率；0：300，1：600，2：1200，3：2400，4：4800，
        // 5：9600，6：19200，7：38400，8：57600，9：115200
    }

    // 归位预置点配置
    public static class CFG_PRESET_HOMING extends NetSDKLibStructure.SdkStructure
    {
        public int              nPtzPresetId;                         // 云台预置点编号	0~65535
        //-1表示无效
        public int              nFreeSec;                             // 空闲的时间，单位为秒
    }

    // 云台配置
    public static class CFG_PTZ_INFO extends NetSDKLibStructure.SdkStructure
    {
        // 能力
        public byte             abMartixID;
        public byte             abCamID;
        public byte             abPTZType;
        // 信息
        public int              nChannelID;                           // 通道号(0开始)
        public int              bEnable;                              // 使能开关
        public byte[]           szProName = new byte[NetSDKLibStructure.MAX_NAME_LEN];   // 协议名称
        public int              nDecoderAddress;                      // 解码器地址；0 - 255
        public CFG_COMM_PROP    struComm;
        public int              nMartixID;                            // 矩阵号
        public int              nPTZType;                             // 云台类型0-兼容，本地云台 1-远程网络云台
        public int              nCamID;                               // 摄像头ID
        public int              nPort;                                // 使用的串口端口号
        public CFG_PRESET_HOMING stuPresetHoming;                     // 一段时间不操作云台，自动归位到某个预置点
        public int              nControlMode;                         // 控制模式, 0:"RS485"串口控制(默认);1:"Coaxial" 同轴口控制
    }

    // 抓拍物体信息
    public static class NET_SNAP_OBJECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_RECT         stuBoundingBox;                       // 物体包围盒, 点坐标归一化到[0, 8192]坐标
        public byte[]           byReserved = new byte[512];           // 保留字节
    }

    // CLIENT_SnapPictureByAnalyseObject 接口输入参数
    public static class NET_IN_SNAP_BY_ANALYSE_OBJECT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannelID;                           // 通道号
        public byte[]           byReserved = new byte[4];             // 字节对齐
        public int              nSnapObjectNum;                       // 抓拍物体个数
        public NET_SNAP_OBJECT_INFO[] stuSnapObjects = new NET_SNAP_OBJECT_INFO[32]; // 抓拍物体信息

        public NET_IN_SNAP_BY_ANALYSE_OBJECT()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuSnapObjects.length; ++i) {
                stuSnapObjects[i] = new NET_SNAP_OBJECT_INFO();
            }
        }
    }

    // CLIENT_SnapPictureByAnalyseObject 接口输出参数
    public static class NET_OUT_SNAP_BY_ANALYSE_OBJECT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_SNAP_BY_ANALYSE_OBJECT()
        {
            this.dwSize = this.size();
        }
    }

    // 选中目标进行抓拍
    public boolean CLIENT_SnapPictureByAnalyseObject(LLong lLoginID,NET_IN_SNAP_BY_ANALYSE_OBJECT pInParam,NET_OUT_SNAP_BY_ANALYSE_OBJECT pOutParam,int nWaitTime);

    // 热成像着火点报警
    public static class ALARM_FIREWARNING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nPresetId;                            // 该字段废弃，请由NET_ALARM_FIREWARNING_INFO事件获取此信息
        public int              nState;                               // 0 - 开始,1 - 结束,-1:无意义
        public NetSDKLibStructure.DH_RECT stBoundingBox;              // 该字段废弃,请由NET_ALARM_FIREWARNING_INFO事件获取此信息
        public int              nTemperatureUnit;                     // 该字段废弃,请由NET_ALARM_FIREWARNING_INFO事件获取此信息
        public float            fTemperature;                         // 该字段废弃,请由NET_ALARM_FIREWARNING_INFO事件获取此信息
        public int              nDistance;                            // 该字段废弃,请由DH_ALARM_FIREWARNING_INFO事件获取此信息
        public NetSDKLibStructure.GPS_POINT stGpsPoint;               // 该字段废弃,请由NET_ALARM_FIREWARNING_INFO事件获取此信息
        public int              nChannel;                             // 对应视频通道号
        public byte[]           reserved = new byte[252];
    }

    // 时间类型
    public static class EM_TIME_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_TIME_TYPE_ABSLUTE = 0;            // 绝对时间
        public static final int   NET_TIME_TYPE_RELATIVE = 1;           // 相对时间,相对于视频文件头帧为时间基点,头帧对应于UTC(0000-00-00 00:00:00)
    }

    // 卡号省份
    public static class EM_CARD_PROVINCE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CARD_UNKNOWN = 10;                 // 解析出错，未知省份
        public static final int   EM_CARD_BEIJING = 11;                 // 北京
        public static final int   EM_CARD_TIANJIN = 12;                 // 天津
        public static final int   EM_CARD_HEBEI = 13;                   // 河北
        public static final int   EM_CARD_SHANXI_TAIYUAN = 14;          // 山西
        public static final int   EM_CARD_NEIMENGGU = 15;               // 内蒙古
        public static final int   EM_CARD_LIAONING = 21;                // 辽宁
        public static final int   EM_CARD_JILIN = 22;                   // 吉林
        public static final int   EM_CARD_HEILONGJIANG = 23;            // 黑龙江
        public static final int   EM_CARD_SHANGHAI = 31;                // 上海
        public static final int   EM_CARD_JIANGSU = 32;                 // 江苏
        public static final int   EM_CARD_ZHEJIANG = 33;                // 浙江
        public static final int   EM_CARD_ANHUI = 34;                   // 安徽
        public static final int   EM_CARD_FUJIAN = 35;                  // 福建
        public static final int   EM_CARD_JIANGXI = 36;                 // 江西
        public static final int   EM_CARD_SHANDONG = 37;                // 山东
        public static final int   EM_CARD_HENAN = 41;                   // 河南
        public static final int   EM_CARD_HUBEI = 42;                   // 湖北
        public static final int   EM_CARD_HUNAN = 43;                   // 湖南
        public static final int   EM_CARD_GUANGDONG = 44;               // 广东
        public static final int   EM_CARD_GUANGXI = 45;                 // 广西
        public static final int   EM_CARD_HAINAN = 46;                  // 海南
        public static final int   EM_CARD_CHONGQING = 50;               // 重庆
        public static final int   EM_CARD_SICHUAN = 51;                 // 四川
        public static final int   EM_CARD_GUIZHOU = 52;                 // 贵州
        public static final int   EM_CARD_YUNNAN = 53;                  // 云南
        public static final int   EM_CARD_XIZANG = 54;                  // 西藏
        public static final int   EM_CARD_SHANXI_XIAN = 61;             // 陕西
        public static final int   EM_CARD_GANSU = 62;                   // 甘肃
        public static final int   EM_CARD_QINGHAI = 63;                 // 青海
        public static final int   EM_CARD_NINGXIA = 64;                 // 宁夏
        public static final int   EM_CARD_XINJIANG = 65;                // 新疆
        public static final int   EM_CARD_XIANGGANG = 71;               // 香港
        public static final int   EM_CARD_AOMEN = 82;                   // 澳门
        public static final int   EM_CARD_TAIWAN = 83;                  // 台湾
    }

    // 车辆类型
    public static class EM_CAR_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CAR_0 = 0;                         // 其他车辆
        public static final int   EM_CAR_1 = 1;                         // 大型普通客车
        public static final int   EM_CAR_2 = 2;                         // 大型双层客车
        public static final int   EM_CAR_3 = 3;                         // 大型卧铺客车
        public static final int   EM_CAR_4 = 4;                         // 大型铰接客车
        public static final int   EM_CAR_5 = 5;                         // 大型越野客车
        public static final int   EM_CAR_6 = 6;                         // 大型轿车
        public static final int   EM_CAR_7 = 7;                         // 大型专用客车
        public static final int   EM_CAR_8 = 8;                         // 大型专用校车
        public static final int   EM_CAR_9 = 9;                         // 中型普通客车
        public static final int   EM_CAR_10 = 10;                       // 中型双层客车
        public static final int   EM_CAR_11 = 11;                       // 中型卧铺客车
        public static final int   EM_CAR_12 = 12;                       // 中型铰接客车
        public static final int   EM_CAR_13 = 13;                       // 中型越野客车
        public static final int   EM_CAR_14 = 14;                       // 中型轿车
        public static final int   EM_CAR_15 = 15;                       // 中型专用客车
        public static final int   EM_CAR_16 = 16;                       // 中型专用校车
        public static final int   EM_CAR_17 = 17;                       // 小型普通客车
        public static final int   EM_CAR_18 = 18;                       // 小型越野客车
        public static final int   EM_CAR_19 = 19;                       // 小型轿车
        public static final int   EM_CAR_20 = 20;                       // 小型专用客车
        public static final int   EM_CAR_21 = 21;                       // 小型专用校车
        public static final int   EM_CAR_22 = 22;                       // 小型面包车
        public static final int   EM_CAR_23 = 23;                       // 微型普通客车
        public static final int   EM_CAR_24 = 24;                       // 微型越野客车
        public static final int   EM_CAR_25 = 25;                       // 微型轿车
        public static final int   EM_CAR_26 = 26;                       // 微型面包车
        public static final int   EM_CAR_27 = 27;                       // 重型半挂牵引车
        public static final int   EM_CAR_28 = 28;                       // 重型全挂牵引车
        public static final int   EM_CAR_29 = 29;                       // 中型半挂牵引车
        public static final int   EM_CAR_30 = 30;                       // 中型全挂牵引车
        public static final int   EM_CAR_31 = 31;                       // 轻型半挂牵引车
        public static final int   EM_CAR_32 = 32;                       // 轻型全挂牵引车
        public static final int   EM_CAR_33 = 33;                       // 大型非载货专项作业车
        public static final int   EM_CAR_34 = 34;                       // 大型载货专项作业车
        public static final int   EM_CAR_35 = 35;                       // 中型非载货专项作业车
        public static final int   EM_CAR_36 = 36;                       // 中型载货专项作业车
        public static final int   EM_CAR_37 = 37;                       // 小型非载货专项作业车
        public static final int   EM_CAR_38 = 38;                       // 小型载货专项作业车
        public static final int   EM_CAR_39 = 39;                       // 微型非载货专项作业车
        public static final int   EM_CAR_40 = 40;                       // 微型载货专项作业车
        public static final int   EM_CAR_41 = 41;                       // 重型非载货专项作业车
        public static final int   EM_CAR_42 = 42;                       // 重型载货专项作业车
        public static final int   EM_CAR_43 = 43;                       // 轻型非载货专项作业车
        public static final int   EM_CAR_44 = 44;                       // 轻型载货专项作业车
        public static final int   EM_CAR_45 = 45;                       // 普通正三轮摩托车
        public static final int   EM_CAR_46 = 46;                       // 轻便正三轮摩托车
        public static final int   EM_CAR_47 = 47;                       // 正三轮载客摩托车
        public static final int   EM_CAR_48 = 48;                       // 正三轮载货摩托车
        public static final int   EM_CAR_49 = 49;                       // 侧三轮摩托车
        public static final int   EM_CAR_50 = 50;                       // 普通二轮摩托车
        public static final int   EM_CAR_51 = 51;                       // 轻便二轮摩托车
        public static final int   EM_CAR_52 = 52;                       // 无轨电车
        public static final int   EM_CAR_53 = 53;                       // 有轨电车
        public static final int   EM_CAR_54 = 54;                       // 三轮汽车
        public static final int   EM_CAR_55 = 55;                       // 轮式装载机械
        public static final int   EM_CAR_56 = 56;                       // 轮式挖掘机械
        public static final int   EM_CAR_57 = 57;                       // 轮式平地机械
        public static final int   EM_CAR_58 = 58;                       // 重型普通货车
        public static final int   EM_CAR_59 = 59;                       // 重型厢式货车
        public static final int   EM_CAR_60 = 60;                       // 重型封闭货车
        public static final int   EM_CAR_61 = 61;                       // 重型罐式货车
        public static final int   EM_CAR_62 = 62;                       // 重型平板货车
        public static final int   EM_CAR_63 = 63;                       // 重型集装箱车
        public static final int   EM_CAR_64 = 64;                       // 重型自卸货车
        public static final int   EM_CAR_65 = 65;                       // 重型特殊结构货车
        public static final int   EM_CAR_66 = 66;                       // 重型仓栅式货车
        public static final int   EM_CAR_67 = 67;                       // 重型车辆运输车
        public static final int   EM_CAR_68 = 68;                       // 重型厢式自卸货车
        public static final int   EM_CAR_69 = 69;                       // 重型罐式自卸货车
        public static final int   EM_CAR_70 = 70;                       // 重型平板自卸货车
        public static final int   EM_CAR_71 = 71;                       // 重型集装箱自卸货车
        public static final int   EM_CAR_72 = 72;                       // 重型特殊结构自卸货车
        public static final int   EM_CAR_73 = 73;                       // 重型仓栅式自卸货车
        public static final int   EM_CAR_74 = 74;                       // 中型普通货车
        public static final int   EM_CAR_75 = 75;                       // 中型厢式货车
        public static final int   EM_CAR_76 = 76;                       // 中型封闭货车
        public static final int   EM_CAR_77 = 77;                       // 中型罐式货车
        public static final int   EM_CAR_78 = 78;                       // 中型平板货车
        public static final int   EM_CAR_79 = 79;                       // 中型集装箱车
        public static final int   EM_CAR_80 = 80;                       // 中型自卸货车
        public static final int   EM_CAR_81 = 81;                       // 中型特殊结构货车
        public static final int   EM_CAR_82 = 82;                       // 中型仓栅式货车
        public static final int   EM_CAR_83 = 83;                       // 中型车辆运输车
        public static final int   EM_CAR_84 = 84;                       // 中型厢式自卸货车
        public static final int   EM_CAR_85 = 85;                       // 中型罐式自卸货车
        public static final int   EM_CAR_86 = 86;                       // 中型平板自卸货车
        public static final int   EM_CAR_87 = 87;                       // 中型集装箱自卸货车
        public static final int   EM_CAR_88 = 88;                       // 中型特殊结构自卸货车
        public static final int   EM_CAR_89 = 89;                       // 中型仓栅式自卸货车
        public static final int   EM_CAR_90 = 90;                       // 轻型普通货车
        public static final int   EM_CAR_91 = 91;                       // 轻型厢式货车
        public static final int   EM_CAR_92 = 92;                       // 轻型封闭货车
        public static final int   EM_CAR_93 = 93;                       // 轻型罐式货车
        public static final int   EM_CAR_94 = 94;                       // 轻型平板货车
        public static final int   EM_CAR_95 = 95;                       // 轻型自卸货车
        public static final int   EM_CAR_96 = 96;                       // 轻型特殊结构货车
        public static final int   EM_CAR_97 = 97;                       // 轻型仓栅式货车
        public static final int   EM_CAR_98 = 98;                       // 轻型车辆运输车
        public static final int   EM_CAR_99 = 99;                       // 轻型厢式自卸货车
        public static final int   EM_CAR_100 = 100;                     // 轻型罐式自卸货车
        public static final int   EM_CAR_101 = 101;                     // 轻型平板自卸货车
        public static final int   EM_CAR_102 = 102;                     // 轻型特殊结构自卸货车
        public static final int   EM_CAR_103 = 103;                     // 轻型仓栅式自卸货车
        public static final int   EM_CAR_104 = 104;                     // 微型普通货车
        public static final int   EM_CAR_105 = 105;                     // 微型厢式货车
        public static final int   EM_CAR_106 = 106;                     // 微型封闭货车
        public static final int   EM_CAR_107 = 107;                     // 微型罐式货车
        public static final int   EM_CAR_108 = 108;                     // 微型自卸货车
        public static final int   EM_CAR_109 = 109;                     // 微型特殊结构货车
        public static final int   EM_CAR_110 = 110;                     // 微型仓栅式货车
        public static final int   EM_CAR_111 = 111;                     // 微型车辆运输车
        public static final int   EM_CAR_112 = 112;                     // 微型厢式自卸货车
        public static final int   EM_CAR_113 = 113;                     // 微型罐式自卸货车
        public static final int   EM_CAR_114 = 114;                     // 微型特殊结构自卸货车
        public static final int   EM_CAR_115 = 115;                     // 微型仓栅式自卸货车
        public static final int   EM_CAR_116 = 116;                     // 普通低速货车
        public static final int   EM_CAR_117 = 117;                     // 厢式低速货车
        public static final int   EM_CAR_118 = 118;                     // 罐式低速货车
        public static final int   EM_CAR_119 = 119;                     // 自卸低速货车
        public static final int   EM_CAR_120 = 120;                     // 仓栅式低速货车
        public static final int   EM_CAR_121 = 121;                     // 厢式自卸低速货车
        public static final int   EM_CAR_122 = 122;                     // 罐式自卸低速货车
        public static final int   EM_CAR_123 = 123;                     // 重型普通全挂车
        public static final int   EM_CAR_124 = 124;                     // 重型厢式全挂车
        public static final int   EM_CAR_125 = 125;                     // 重型罐式全挂车
        public static final int   EM_CAR_126 = 126;                     // 重型平板全挂车
        public static final int   EM_CAR_127 = 127;                     // 重型集装箱全挂车
        public static final int   EM_CAR_128 = 128;                     // 重型自卸全挂车
        public static final int   EM_CAR_129 = 129;                     // 重型仓栅式全挂车
        public static final int   EM_CAR_130 = 130;                     // 重型旅居全挂车
        public static final int   EM_CAR_131 = 131;                     // 重型专项作业全挂车
        public static final int   EM_CAR_132 = 132;                     // 重型厢式自卸全挂车
        public static final int   EM_CAR_133 = 133;                     // 重型罐式自卸全挂车
        public static final int   EM_CAR_134 = 134;                     // 重型平板自卸全挂车
        public static final int   EM_CAR_135 = 135;                     // 重型集装箱自卸全挂车
        public static final int   EM_CAR_136 = 136;                     // 重型仓栅式自卸全挂车
        public static final int   EM_CAR_137 = 137;                     // 重型专项作业自卸全挂车
        public static final int   EM_CAR_138 = 138;                     // 中型普通全挂车
        public static final int   EM_CAR_139 = 139;                     // 中型厢式全挂车
        public static final int   EM_CAR_140 = 140;                     // 中型罐式全挂车
        public static final int   EM_CAR_141 = 141;                     // 中型平板全挂车
        public static final int   EM_CAR_142 = 142;                     // 中型集装箱全挂车
        public static final int   EM_CAR_143 = 143;                     // 中型自卸全挂车
        public static final int   EM_CAR_144 = 144;                     // 中型仓栅式全挂车
        public static final int   EM_CAR_145 = 145;                     // 中型旅居全挂车
        public static final int   EM_CAR_146 = 146;                     // 中型专项作业全挂车
        public static final int   EM_CAR_147 = 147;                     // 中型厢式自卸全挂车
        public static final int   EM_CAR_148 = 148;                     // 中型罐式自卸全挂车
        public static final int   EM_CAR_149 = 149;                     // 中型平板自卸全挂车
        public static final int   EM_CAR_150 = 150;                     // 中型集装箱自卸全挂车
        public static final int   EM_CAR_151 = 151;                     // 中型仓栅式自卸全挂车
        public static final int   EM_CAR_152 = 152;                     // 中型专项作业自卸全挂车
        public static final int   EM_CAR_153 = 153;                     // 轻型普通全挂车
        public static final int   EM_CAR_154 = 154;                     // 轻型厢式全挂车
        public static final int   EM_CAR_155 = 155;                     // 轻型罐式全挂车
        public static final int   EM_CAR_156 = 156;                     // 轻型平板全挂车
        public static final int   EM_CAR_157 = 157;                     // 轻型自卸全挂车
        public static final int   EM_CAR_158 = 158;                     // 轻型仓栅式全挂车
        public static final int   EM_CAR_159 = 159;                     // 轻型旅居全挂车
        public static final int   EM_CAR_160 = 160;                     // 轻型专项作业全挂车
        public static final int   EM_CAR_161 = 161;                     // 轻型厢式自卸全挂车
        public static final int   EM_CAR_162 = 162;                     // 轻型罐式自卸全挂车
        public static final int   EM_CAR_163 = 163;                     // 轻型平板自卸全挂车
        public static final int   EM_CAR_164 = 164;                     // 轻型集装箱自卸全挂车
        public static final int   EM_CAR_165 = 165;                     // 轻型仓栅式自卸全挂车
        public static final int   EM_CAR_166 = 166;                     // 轻型专项作业自卸全挂车
        public static final int   EM_CAR_167 = 167;                     // 重型普通半挂车
        public static final int   EM_CAR_168 = 168;                     // 重型厢式半挂车
        public static final int   EM_CAR_169 = 169;                     // 重型罐式半挂车
        public static final int   EM_CAR_170 = 170;                     // 重型平板半挂车
        public static final int   EM_CAR_171 = 171;                     // 重型集装箱半挂车
        public static final int   EM_CAR_172 = 172;                     // 重型自卸半挂车
        public static final int   EM_CAR_173 = 173;                     // 重型特殊结构半挂车
        public static final int   EM_CAR_174 = 174;                     // 重型仓栅式半挂车
        public static final int   EM_CAR_175 = 175;                     // 重型旅居半挂车
        public static final int   EM_CAR_176 = 176;                     // 重型专项作业半挂车
        public static final int   EM_CAR_177 = 177;                     // 重型低平板半挂车
        public static final int   EM_CAR_178 = 178;                     // 重型车辆运输半挂车
        public static final int   EM_CAR_179 = 179;                     // 重型罐式自卸半挂车
        public static final int   EM_CAR_180 = 180;                     // 重型平板自卸半挂车
        public static final int   EM_CAR_181 = 181;                     // 重型集装箱自卸半挂车
        public static final int   EM_CAR_182 = 182;                     // 重型特殊结构自卸半挂车
        public static final int   EM_CAR_183 = 183;                     // 重型仓栅式自卸半挂车
        public static final int   EM_CAR_184 = 184;                     // 重型专项作业自卸半挂车
        public static final int   EM_CAR_185 = 185;                     // 重型低平板自卸半挂车
        public static final int   EM_CAR_186 = 186;                     // 重型中置轴旅居挂车
        public static final int   EM_CAR_187 = 187;                     // 重型中置轴车辆运输车
        public static final int   EM_CAR_188 = 188;                     // 重型中置轴普通挂车
        public static final int   EM_CAR_189 = 189;                     // 中型普通半挂车
        public static final int   EM_CAR_190 = 190;                     // 中型厢式半挂车
        public static final int   EM_CAR_191 = 191;                     // 中型罐式半挂车
        public static final int   EM_CAR_192 = 192;                     // 中型平板半挂车
        public static final int   EM_CAR_193 = 193;                     // 中型集装箱半挂车
        public static final int   EM_CAR_194 = 194;                     // 中型自卸半挂车
        public static final int   EM_CAR_195 = 195;                     // 中型特殊结构半挂车
        public static final int   EM_CAR_196 = 196;                     // 中型仓栅式半挂车
        public static final int   EM_CAR_197 = 197;                     // 中型旅居半挂车
        public static final int   EM_CAR_198 = 198;                     // 中型专项作业半挂车
        public static final int   EM_CAR_199 = 199;                     // 中型低平板半挂车
        public static final int   EM_CAR_200 = 200;                     // 中型车辆运输半挂车
        public static final int   EM_CAR_201 = 201;                     // 中型罐式自卸半挂车
        public static final int   EM_CAR_202 = 202;                     // 中型平板自卸半挂车
        public static final int   EM_CAR_203 = 203;                     // 中型集装箱自卸半挂车
        public static final int   EM_CAR_204 = 204;                     // 中型特殊结构自卸挂车
        public static final int   EM_CAR_205 = 205;                     // 中型仓栅式自卸半挂车
        public static final int   EM_CAR_206 = 206;                     // 中型专项作业自卸半挂车
        public static final int   EM_CAR_207 = 207;                     // 中型低平板自卸半挂车
        public static final int   EM_CAR_208 = 208;                     // 中型中置轴旅居挂车
        public static final int   EM_CAR_209 = 209;                     // 中型中置轴车辆运输车
        public static final int   EM_CAR_210 = 210;                     // 中型中置轴普通挂车
        public static final int   EM_CAR_211 = 211;                     // 轻型普通半挂车
        public static final int   EM_CAR_212 = 212;                     // 轻型厢式半挂车
        public static final int   EM_CAR_213 = 213;                     // 轻型罐式半挂车
        public static final int   EM_CAR_214 = 214;                     // 轻型平板半挂车
        public static final int   EM_CAR_215 = 215;                     // 轻型自卸半挂车
        public static final int   EM_CAR_216 = 216;                     // 轻型仓栅式半挂车
        public static final int   EM_CAR_217 = 217;                     // 轻型旅居半挂车
        public static final int   EM_CAR_218 = 218;                     // 轻型专项作业半挂车
        public static final int   EM_CAR_219 = 219;                     // 轻型低平板半挂车
        public static final int   EM_CAR_220 = 220;                     // 轻型车辆运输半挂车
        public static final int   EM_CAR_221 = 221;                     // 轻型罐式自卸半挂车
        public static final int   EM_CAR_222 = 222;                     // 轻型平板自卸半挂车
        public static final int   EM_CAR_223 = 223;                     // 轻型集装箱自卸半挂车
        public static final int   EM_CAR_224 = 224;                     // 轻型特殊结构自卸挂车
        public static final int   EM_CAR_225 = 225;                     // 轻型仓栅式自卸半挂车
        public static final int   EM_CAR_226 = 226;                     // 轻型专项作业自卸半挂车
        public static final int   EM_CAR_227 = 227;                     // 轻型低平板自卸半挂车
        public static final int   EM_CAR_228 = 228;                     // 轻型中置轴旅居挂车
        public static final int   EM_CAR_229 = 229;                     // 轻型中置轴车辆运输车
        public static final int   EM_CAR_230 = 230;                     // 轻型中置轴普通挂车
    }

    // 号牌类型
    public static class EM_PLATE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_PLATE_OTHER = 0;                   // 其他车
        public static final int   EM_PLATE_BIG_CAR = 1;                 // 大型汽车
        public static final int   EM_PLATE_SMALL_CAR = 2;               // 小型汽车
        public static final int   EM_PLATE_EMBASSY_CAR = 3;             // 使馆汽车
        public static final int   EM_PLATE_CONSULATE_CAR = 4;           // 领馆汽车
        public static final int   EM_PLATE_ABROAD_CAR = 5;              // 境外汽车
        public static final int   EM_PLATE_FOREIGN_CAR = 6;             // 外籍汽车
        public static final int   EM_PLATE_LOW_SPEED_CAR = 7;           // 低速车
        public static final int   EM_PLATE_COACH_CAR = 8;               // 教练车
        public static final int   EM_PLATE_MOTORCYCLE = 9;              // 摩托车
        public static final int   EM_PLATE_NEW_POWER_CAR = 10;          // 新能源车
        public static final int   EM_PLATE_POLICE_CAR = 11;             // 警用车
        public static final int   EM_PLATE_HONGKONG_MACAO_CAR = 12;     // 港澳两地车
        public static final int   EM_PLATE_WJPOLICE_CAR = 13;           //
        public static final int   EM_PLATE_OUTERGUARD_CAR = 14;         //
    }

    // 车身颜色
    public static class EM_CAR_COLOR_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CAR_COLOR_WHITE = 0;               // 白色
        public static final int   EM_CAR_COLOR_BLACK = 1;               // 黑色
        public static final int   EM_CAR_COLOR_RED = 2;                 // 红色
        public static final int   EM_CAR_COLOR_YELLOW = 3;              // 黄色
        public static final int   EM_CAR_COLOR_GRAY = 4;                // 灰色
        public static final int   EM_CAR_COLOR_BLUE = 5;                // 蓝色
        public static final int   EM_CAR_COLOR_GREEN = 6;               // 绿色
        public static final int   EM_CAR_COLOR_PINK = 7;                // 粉色
        public static final int   EM_CAR_COLOR_PURPLE = 8;              // 紫色
        public static final int   EM_CAR_COLOR_DARK_PURPLE = 9;         // 暗紫色
        public static final int   EM_CAR_COLOR_BROWN = 10;              // 棕色
        public static final int   EM_CAR_COLOR_MAROON = 11;             // 粟色
        public static final int   EM_CAR_COLOR_SILVER_GRAY = 12;        // 银灰色
        public static final int   EM_CAR_COLOR_DARK_GRAY = 13;          // 暗灰色
        public static final int   EM_CAR_COLOR_WHITE_SMOKE = 14;        // 白烟色
        public static final int   EM_CAR_COLOR_DEEP_ORANGE = 15;        // 深橙色
        public static final int   EM_CAR_COLOR_LIGHT_ROSE = 16;         // 浅玫瑰色
        public static final int   EM_CAR_COLOR_TOMATO_RED = 17;         // 番茄红色
        public static final int   EM_CAR_COLOR_OLIVE = 18;              // 橄榄色
        public static final int   EM_CAR_COLOR_GOLDEN = 19;             // 金色
        public static final int   EM_CAR_COLOR_DARK_OLIVE = 20;         // 暗橄榄色
        public static final int   EM_CAR_COLOR_YELLOW_GREEN = 21;       // 黄绿色
        public static final int   EM_CAR_COLOR_GREEN_YELLOW = 22;       // 绿黄色
        public static final int   EM_CAR_COLOR_FOREST_GREEN = 23;       // 森林绿
        public static final int   EM_CAR_COLOR_OCEAN_BLUE = 24;         // 海洋绿
        public static final int   EM_CAR_COLOR_DEEP_SKYBLUE = 25;       // 深天蓝
        public static final int   EM_CAR_COLOR_CYAN = 26;               // 青色
        public static final int   EM_CAR_COLOR_DEEP_BLUE = 27;          // 深蓝色
        public static final int   EM_CAR_COLOR_DEEP_RED = 28;           // 深红色
        public static final int   EM_CAR_COLOR_DEEP_GREEN = 29;         // 深绿色
        public static final int   EM_CAR_COLOR_DEEP_YELLOW = 30;        // 深黄色
        public static final int   EM_CAR_COLOR_DEEP_PINK = 31;          // 深粉色
        public static final int   EM_CAR_COLOR_DEEP_PURPLE = 32;        // 深紫色
        public static final int   EM_CAR_COLOR_DEEP_BROWN = 33;         // 深棕色
        public static final int   EM_CAR_COLOR_DEEP_CYAN = 34;          // 深青色
        public static final int   EM_CAR_COLOR_ORANGE = 35;             // 橙色
        public static final int   EM_CAR_COLOR_DEEP_GOLDEN = 36;        // 深金色
        public static final int   EM_CAR_COLOR_OTHER = 255;             // 未识别、其他
    }

    // 使用性质
    public static class EM_USE_PROPERTY_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_USE_PROPERTY_NONOPERATING = 0;     // 非营运
        public static final int   EM_USE_PROPERTY_HIGWAY = 1;           // 公路客运,旅游客运
        public static final int   EM_USE_PROPERTY_BUS = 2;              // 公交客运
        public static final int   EM_USE_PROPERTY_TAXI = 3;             // 出租客运
        public static final int   EM_USE_PROPERTY_FREIGHT = 4;          // 货运
        public static final int   EM_USE_PROPERTY_LEASE = 5;            // 租赁
        public static final int   EM_USE_PROPERTY_SECURITY = 6;         // 警用,消防,救护,工程救险
        public static final int   EM_USE_PROPERTY_COACH = 7;            // 教练
        public static final int   EM_USE_PROPERTY_SCHOOLBUS = 8;        // 幼儿校车,小学生校车,其他校车
        public static final int   EM_USE_PROPERTY_FOR_DANGE_VEHICLE = 9; // 危化品运输
        public static final int   EM_USE_PROPERTY_OTHER = 10;           // 其他
        public static final int   EM_USE_PROPERTY_ONLINE_CAR_HAILING = 11; // 网约车
    }

    // 大类业务方案
    public static class EM_CLASS_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CLASS_UNKNOWN = 0;                 // 未知业务
        public static final int   EM_CLASS_VIDEO_SYNOPSIS = 1;          // 视频浓缩
        public static final int   EM_CLASS_TRAFFIV_GATE = 2;            // 卡口
        public static final int   EM_CLASS_ELECTRONIC_POLICE = 3;       // 电警
        public static final int   EM_CLASS_SINGLE_PTZ_PARKING = 4;      // 单球违停
        public static final int   EM_CLASS_PTZ_PARKINBG = 5;            // 主从违停
        public static final int   EM_CLASS_TRAFFIC = 6;                 // 交通事件"Traffic"
        public static final int   EM_CLASS_NORMAL = 7;                  // 通用行为分析"Normal"
        public static final int   EM_CLASS_PS = 8;
        public static final int   EM_CLASS_ATM = 9;                     // 金融行为分析"ATM"
        public static final int   EM_CLASS_METRO = 10;                  // 地铁行为分析
        public static final int   EM_CLASS_FACE_DETECTION = 11;         // 目标检测"FaceDetection"
        public static final int   EM_CLASS_FACE_RECOGNITION = 12;       // 目标识别"FaceRecognition"
        public static final int   EM_CLASS_NUMBER_STAT = 13;            // 人数统计"NumberStat"
        public static final int   EM_CLASS_HEAT_MAP = 14;               // 热度图"HeatMap"
        public static final int   EM_CLASS_VIDEO_DIAGNOSIS = 15;        // 视频诊断"VideoDiagnosis"
        public static final int   EM_CLASS_VIDEO_ENHANCE = 16;          // 视频增强
        public static final int   EM_CLASS_SMOKEFIRE_DETECT = 17;       // 烟火检测
        public static final int   EM_CLASS_VEHICLE_ANALYSE = 18;        // 车辆特征识别"VehicleAnalyse"
        public static final int   EM_CLASS_PERSON_FEATURE = 19;         // 人员特征识别
        public static final int   EM_CLASS_SDFACEDETECTION = 20;        // 多预置点目标检测"SDFaceDetect"
        //配置一条规则但可以在不同预置点下生效
        public static final int   EM_CLASS_HEAT_MAP_PLAN = 21;          // 球机热度图计划"HeatMapPlan"
        public static final int   EM_CLASS_NUMBERSTAT_PLAN = 22;        // 球机客流量统计计划 "NumberStatPlan"
        public static final int   EM_CLASS_ATMFD = 23;                  // 金融目标检测，包括正常人脸、异常人脸、相邻人脸、头盔人脸等针对ATM场景特殊优化
        public static final int   EM_CLASS_HIGHWAY = 24;                // 高速交通事件检测"Highway"
        public static final int   EM_CLASS_CITY = 25;                   // 城市交通事件检测 "City"
        public static final int   EM_CLASS_LETRACK = 26;                // 民用简易跟踪"LeTrack"
        public static final int   EM_CLASS_SCR = 27;                    // 打靶相机"SCR"
        public static final int   EM_CLASS_STEREO_VISION = 28;          // 立体视觉(双目)"StereoVision"
        public static final int   EM_CLASS_HUMANDETECT = 29;            // 人体检测"HumanDetect"
        public static final int   EM_CLASS_FACE_ANALYSIS = 30;          // 人脸分析 "FaceAnalysis"
        public static final int   EM_CALSS_XRAY_DETECTION = 31;         // X光检测 "XRayDetection"
        public static final int   EM_CLASS_STEREO_NUMBER = 32;          // 双目相机客流量统计 "StereoNumber"
        public static final int   EM_CLASS_CROWDDISTRIMAP = 33;         // 人群分布图
        public static final int   EM_CLASS_OBJECTDETECT = 34;           // 目标检测
        public static final int   EM_CLASS_FACEATTRIBUTE = 35;          // IVSS目标检测 "FaceAttribute"
        public static final int   EM_CLASS_FACECOMPARE = 36;            // IVSS目标识别 "FaceCompare"
        public static final int   EM_CALSS_STEREO_BEHAVIOR = 37;        // 立体行为分析 "StereoBehavior"
        public static final int   EM_CALSS_INTELLICITYMANAGER = 38;     // 智慧城管 "IntelliCityMgr"
        public static final int   EM_CALSS_PROTECTIVECABIN = 39;        // 防护舱（ATM舱内）"ProtectiveCabin"
        public static final int   EM_CALSS_AIRPLANEDETECT = 40;         // 飞机行为检测 "AirplaneDetect"
        public static final int   EM_CALSS_CROWDPOSTURE = 41;           // 人群态势（人群分布图服务）"CrowdPosture"
        public static final int   EM_CLASS_PHONECALLDETECT = 42;        // 打电话检测 "PhoneCallDetect"
        public static final int   EM_CLASS_SMOKEDETECTION = 43;         // 烟雾检测 "SmokeDetection"
        public static final int   EM_CLASS_BOATDETECTION = 44;          // 船只检测 "BoatDetection"
        public static final int   EM_CLASS_SMOKINGDETECT = 45;          // 吸烟检测 "SmokingDetect"
        public static final int   EM_CLASS_WATERMONITOR = 46;           // 水利监测 "WaterMonitor"
        public static final int   EM_CLASS_GENERATEGRAPHDETECTION = 47; // 生成图规则 "GenerateGraphDetection"
        public static final int   EM_CLASS_TRAFFIC_PARK = 48;           // 交通停车 "TrafficPark"
        public static final int   EM_CLASS_OPERATEMONITOR = 49;         // 作业检测 "OperateMonitor"
        public static final int   EM_CLASS_INTELLI_RETAIL = 50;         // 智慧零售大类 "IntelliRetail"
        public static final int   EM_CLASS_CLASSROOM_ANALYSE = 51;      // 教育智慧课堂"ClassroomAnalyse"
        public static final int   EM_CLASS_FEATURE_ABSTRACT = 52;       // 特征向量提取大类 "FeatureAbstract"
        public static final int   EM_CLASS_CROWD_ABNORMAL = 62;         // 人群异常检测 "CrowdAbnormal"
        public static final int   EM_CLASS_ANATOMY_TEMP_DETECT = 63;    // 人体温智能检测 "AnatomyTempDetect"
        public static final int   EM_CLASS_WEATHER_MONITOR = 64;        // 天气监控 "WeatherMonitor"
        public static final int   EM_CLASS_ELEVATOR_ACCESS_CONTROL = 65; // 电梯门禁 "ElevatorAccessControl"
        public static final int   EM_CLASS_BREAK_RULE_BUILDING = 66;    // 违章建筑	"BreakRuleBuilding"
        public static final int   EM_CLASS_FOREIGN_DETECT = 67;         // 异物检测 "ForeignDetection"
        public static final int   EM_CLASS_PANORAMA_TRAFFIC = 68;       // 全景交通 "PanoramaTraffic"
        public static final int   EM_CLASS_CONVEY_OR_BLOCK = 69;        // 传送带阻塞 "ConveyorBlock"
        public static final int   EM_CLASS_KITCHEN_ANIMAL = 70;         // 厨房有害动物检测 "KitchenAnimal"
        public static final int   EM_CLASS_ALLSEEINGEYE = 71;           // 万物检测 "AllSeeingEye"
        public static final int   EM_CLASS_INTELLI_FIRE_CONTROL = 72;   // 智慧消防 "IntelliFireControl"
        public static final int   EM_CLASS_CONVERYER_BELT = 73;         // 传送带检测 "ConveyerBelt"
        public static final int   EM_CLASS_INTELLI_LOGISTICS = 74;      // 智慧物流 "IntelliLogistics"
        public static final int   EM_CLASS_SMOKE_FIRE = 75;             // 烟火检测"SmokeFire"
        public static final int   EM_CLASS_OBJECT_MONITOR = 76;         // 物品监控"ObjectMonitor"
        public static final int   EM_CLASS_INTELLI_PARKING = 77;        // 智能停车"IntelliParking"
    }

    // 交通车辆行驶方向类型
    public static class EM_TRAFFICCAR_MOVE_DIRECTION extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_TRAFFICCAR_MOVE_DIRECTION_UNKNOWN = 0; // 未知的
        public static final int   EM_TRAFFICCAR_MOVE_DIRECTION_STRAIGHT = 1; // 直行
        public static final int   EM_TRAFFICCAR_MOVE_DIRECTION_TURN_LEFT = 2; // 左转
        public static final int   EM_TRAFFICCAR_MOVE_DIRECTION_TURN_RIGHT = 3; // 右转
        public static final int   EM_TRAFFICCAR_MOVE_DIRECTION_TURN_AROUND = 4; // 掉头
    }

    // 货物通道信息（物流）
    public static class NET_CUSTOM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nCargoChannelNum;                     // 货物通道个数
        public float[]          fCoverageRate = new float[NetSDKLibStructure.MAX_CARGO_CHANNEL_NUM]; // 货物覆盖率
        public byte[]           byReserved = new byte[40];            // 保留字节
    }

    // 车辆物件类型
    public static class EM_COMM_ATTACHMENT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   COMM_ATTACHMENT_TYPE_UNKNOWN = 0;     // 未知类型
        public static final int   COMM_ATTACHMENT_TYPE_FURNITURE = 1;   // 摆件
        public static final int   COMM_ATTACHMENT_TYPE_PENDANT = 2;     // 挂件
        public static final int   COMM_ATTACHMENT_TYPE_TISSUEBOX = 3;   // 纸巾盒
        public static final int   COMM_ATTACHMENT_TYPE_DANGER = 4;      // 危险品
        public static final int   COMM_ATTACHMENT_TYPE_PERFUMEBOX = 5;  // 香水
    }

    // 按功能划分的车辆类型
    public static class EM_VEHICLE_TYPE_BY_FUNC extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_UNKNOWN = 0;  // 未知
        /*以下为特种车辆类型*/
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_TANK_CAR = 1; // 危化品车辆
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_SLOT_TANK_CAR = 2; // 槽罐车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_DREGS_CAR = 3; // 渣土车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_CONCRETE_MIXER_TRUCK = 4; // 混凝土搅拌车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_TAXI = 5;     // 出租车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_POLICE = 6;   // 警车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_AMBULANCE = 7; // 救护车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_GENERAL = 8;  // 普通车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_WATERING_CAR = 9; // 洒水车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_FIRE_ENGINE = 10; // 消防车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_MACHINESHOP_TRUCK = 11; // 工程车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_POWER_LOT_VEHICLE = 12; // 粉粒物料车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_SUCTION_SEWAGE_TRUCK = 13; // 吸污车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_NORMAL_TANK_TRUCK = 14; // 普通罐车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_SCHOOL_BUS = 15; // 校车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_EXCAVATOR = 16; // 挖掘机
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_BULLDOZER = 17; // 推土车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_CRANE = 18;   // 吊车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_PUMP_TRUCK = 19; // 泵车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_POULTRY = 20; // 禽畜车
        public static final int   EM_VEHICLE_TYPE_BY_FUNC_TRACTOR = 21; // 拖拉机
    }

    // 标准车辆类型
    public static class EM_STANDARD_VEHICLE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_STANDARD_VEHICLE_UNKNOWN = 0;      // 未知
        public static final int   EM_STANDARD_VEHICLE_MOTOR = 1;        // 机动车
        public static final int   EM_STANDARD_VEHICLE_BUS = 2;          // 公交车
        public static final int   EM_STANDARD_VEHICLE_UNLICENSED_MOTOR = 3; // 无牌机动车
        public static final int   EM_STANDARD_VEHICLE_LARGE_CAR = 4;    // 大型汽车
        public static final int   EM_STANDARD_VEHICLE_MICRO_CAR = 5;    // 小型汽车
        public static final int   EM_STANDARD_VEHICLE_EMBASSY_CAR = 6;  // 使馆汽车
        public static final int   EM_STANDARD_VEHICLE_MARGINAL_CAR = 7; // 领馆汽车
        public static final int   EM_STANDARD_VEHICLE_AREAOUT_CAR = 8;  // 境外汽车
        public static final int   EM_STANDARD_VEHICLE_FOREIGN_CAR = 9;  // 外籍汽车
        public static final int   EM_STANDARD_VEHICLE_FARM_TRANS_CAR = 10; // 农用运输车
        public static final int   EM_STANDARD_VEHICLE_TRACTOR = 11;     // 拖拉机
        public static final int   EM_STANDARD_VEHICLE_TRAILER = 12;     // 挂车
        public static final int   EM_STANDARD_VEHICLE_COACH_CAR = 13;   // 教练汽车
        public static final int   EM_STANDARD_VEHICLE_TRIAL_CAR = 14;   // 试验汽车
        public static final int   EM_STANDARD_VEHICLE_TEMPORARYENTRY_CAR = 15; // 临时入境汽车
        public static final int   EM_STANDARD_VEHICLE_TEMPORARYENTRY_MOTORCYCLE = 16; // 临时入境摩托
        public static final int   EM_STANDARD_VEHICLE_TEMPORARY_STEER_CAR = 17; // 临时行驶车
        public static final int   EM_STANDARD_VEHICLE_LARGE_TRUCK = 18; // 大货车
        public static final int   EM_STANDARD_VEHICLE_MID_TRUCK = 19;   // 中货车
        public static final int   EM_STANDARD_VEHICLE_MICRO_TRUCK = 20; // 小货车
        public static final int   EM_STANDARD_VEHICLE_MICROBUS = 21;    // 面包车
        public static final int   EM_STANDARD_VEHICLE_SALOON_CAR = 22;  // 轿车
        public static final int   EM_STANDARD_VEHICLE_CARRIAGE = 23;    // 小轿车
        public static final int   EM_STANDARD_VEHICLE_MINI_CARRIAGE = 24; // 微型轿车
        public static final int   EM_STANDARD_VEHICLE_SUV_MPV = 25;     // SUV或者MPV
        public static final int   EM_STANDARD_VEHICLE_SUV = 26;         // SUV
        public static final int   EM_STANDARD_VEHICLE_MPV = 27;         // MPV
        public static final int   EM_STANDARD_VEHICLE_PASSENGER_CAR = 28; // 客车
        public static final int   EM_STANDARD_VEHICLE_MOTOR_BUS = 29;   // 大客车
        public static final int   EM_STANDARD_VEHICLE_MID_PASSENGER_CAR = 30; // 中客车
        public static final int   EM_STANDARD_VEHICLE_MINI_BUS = 31;    // 小客车
        public static final int   EM_STANDARD_VEHICLE_PICKUP = 32;      // 皮卡车
        public static final int   EM_STANDARD_VEHICLE_OILTANK_TRUCK = 33; // 油罐车
    }

    // 报警事件类型 EVENT_ALARM_LOCALALARM(外部报警),EVENT_ALARM_MOTIONALARM(动检报警)报警)
    public static class DEV_EVENT_ALARM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           Reserved = new byte[4];               // 保留字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.EVENT_COMM_INFO stCommInfo;                           // 公共信息
        public byte             byEventAction;                        // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[3];             // 保留字节
        public int              emSenseType;                          // 传感器类型,参考NET_SENSE_METHOD
        public int              emDefenceAreaType;                    // 防区类型 ,参考EM_NET_DEFENCE_AREA_TYPE
        public NET_GPS_STATUS_INFO stuGPS;                            // GPS信息
        public byte[]           szUserID = new byte[32];              // 本地报警时登陆的用户ID
        public byte[]           szUserName = new byte[128];           // 本地报警时登陆的用户名
        public byte[]           szSN = new byte[32];                  // 设备序列号
        public int              bExAlarmIn;                           // 外部输入报警
        public NET_FILE_PROCESS_INFO stuFileProcessInfo;              // 图片与智能事件信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReservedEx = new byte[512];         // 保留字节
    }

    // 报警事件类型 EVENT_ALARM_VIDEOBLIND(视频遮挡报警)
    public static class DEV_EVENT_ALARM_VIDEOBLIND extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           Reserved = new byte[4];               // 保留字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NET_TIME_EX      stuTime;                              // 事件发生的时间, (设备时间, 不一定是utc时间)
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public NET_IMAGE_INFO_EX stuImageInfo[] = (NET_IMAGE_INFO_EX[])new NET_IMAGE_INFO_EX().toArray(6); //图片信息扩展
        public int              nImageInfo;                           //图片信息扩展的个数
        public NET_IMAGE_INFO_EX2 stuImageInfoEx2[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoEx2Num;                     //图片信息个数
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public int              bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte             byReserved[] = new byte[1023];        //预留字节
    }

    // 事件类型 EVENT_IVS_HIGHSPEED(车辆超速报警事件）对应的数据块描述信息
    public static class DEV_EVENT_HIGHSPEED_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 事件动作,1表示持续性事件开始,2表示持续性事件结束;
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public byte[]           byReserved = new byte[4];             // 保留字节
        public double           dbPTS;                                // 时间戳(单位是毫秒)
        public NET_GPS_STATUS_INFO stGPSStatusInfo;                   // GPS信息
        public int              nSpeedLimit;                          // 车连限速值(km/h)
        public int              nCurSpeed;                            // 当前车辆速度(km/h)
        public int              nMaxSpeed;                            // 最高速度(Km/h)
        public NET_TIME_EX      stuStartTime;                         // 开始时间(需求),nAction为2时上报此字段
        public byte[]           byReserved1 = new byte[1024];         // 保留字节
    }

    // 事件类型EVENT_IVS_TIREDPHYSIOLOGICAL(生理疲劳驾驶事件)对应的数据块描述信息
    public static class DEV_EVENT_TIREDPHYSIOLOGICAL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_TIREDPHYSIOLOGICAL_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 事件类型EVENT_IVS_TRAFFIC_TIREDLOWERHEAD(开车低头报警事件)对应的数据块描述信息
    public static class DEV_EVENT_TIREDLOWERHEAD_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_TIREDLOWERHEAD_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 事件类型EVENT_IVS_TRAFFIC_DRIVERLEAVEPOST(开车离岗报警事件)对应的数据块描述信息
    public static class DEV_EVENT_DRIVERLEAVEPOST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_DRIVERLEAVEPOST_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 事件类型 EVENT_IVS_TRAFFIC_DRIVERYAWN (开车打哈欠事件) 对应的数据块描述信息
    public static class DEV_EVENT_DRIVERYAWN_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           szReserved1 = new byte[4];            // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_DRIVERYAWN_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 事件类型 EVENT_IVS_FORWARDCOLLISION_WARNNING(前向碰撞预警) 对应的数据块描述信息
    public static class DEV_EVENT_FORWARDCOLLISION_WARNNING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nEventID;                             // 事件ID
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public NET_GPS_STATUS_INFO stuGPSStatusInfo;                  // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节

        public DEV_EVENT_FORWARDCOLLISION_WARNNING_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 事件类型 EVNET_IVS_LANEDEPARTURE_WARNNING(车道偏移预警) 对应的数据块描述信息
    public static class DEV_EVENT_LANEDEPARTURE_WARNNING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nEventID;                             // 事件ID
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public NET_GPS_STATUS_INFO stuGPSStatusInfo;                  // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_LANEDEPARTURE_WARNNING_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    //图片路径类型
    public static class NET_PICTURE_PATH_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_PATH_LOCAL_PATH = 0;              // 本地硬盘或者sd卡成功写入路径
        public static final int   NET_PATH_FTP_PATH = 1;                // 设备成功写到ftp服务器的路径
        public static final int   NET_PATH_VIDEO_PATH = 2;              // 当前接入需要获取当前违章的关联视频的FTP上传路径
    }

    public static class NET_RESERVED_PATH extends NetSDKLibStructure.SdkStructure
    {
        public int              nMaxPathNum;                          // 图片路径总数,为0时采用设备默认路径
        public int[]            emPictruePaths = new int[NetSDKLibStructure.MAX_PIC_PATH_NUM]; // 图片路径类型，详见NET_PICTURE_PATH_TYPE
    }

    //离线传输参数
    public static class NET_OFFLINE_PARAM extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szClientID = new byte[20];            // 客户端mac地址，冒号分隔形式
        public byte[]           szUUID = new byte[64];                //客户端惟一标识
        public byte[]           byReserved = new byte[44];            // 保留
    }

    public static class NET_RESERVED_COMMON extends NetSDKLibStructure.SdkStructure
    {
        public int              dwStructSize;
        public Pointer          pIntelBox;                            // 兼容RESERVED_TYPE_FOR_INTEL_BOX，指向ReservedDataIntelBox
        public int              dwSnapFlagMask;                       // 抓图标志(按位),0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"
        public Pointer          pstuOfflineParam;                     // 离线传输参数，指向NET_OFFLINE_PARAM
        public Pointer          pstuPath;                             // 兼容RESERVED_TYPE_FOR_PATH，指向NET_RESERVED_PATH
        public int              emPathMode;                           // 返回的图片存储路径模式,对应枚举EM_PATH_MODE
        /**
         * 对应结构体{@link EM_FILTER_IMAGE_TYPE}
         */
        public Pointer          pImageType;                           // 返回的图片类型, 由用户申请释放
        public int              nImageTypeNum;                        // pImageType 有效个数
        public int              bFlagCustomInfo;                      // szCustomInfo 标志位 TRUE时 使用szCustomInfo字段
        public byte[]           szCustomInfo = new byte[512];         // 客户自定义信息 customInfo是getFiltercaps中能力对应的订阅参数的格式化字符串表示,字符串格式为：订阅参数以&分隔，订阅参数的名字和值用=连接
        public int              bSetEventsType;                       // 是否设置事件类型
        public int              nVOLayer;                             // 视频输出口图层, 0:未知 1:视频层 2:GUI层 3:动态跑马显示
        public int              emOrder;                              // 设备给客户端传离线图片需要按照的顺序，如果是倒序上传:越晚产生的优先上传，越早产生的后传,参考枚举EM_EVENT_ORDER
        public int              nVOImageType;                         //输出口抓屏图片格式  0:未知 1:NV12 2:YUV422层 3:RGB565 4:XRGB8888 5:JPEG
        public int              bNeedFeatureVectorVaild;              //bNeedFeatureVector字段是否有效
        public int              bNeedFeatureVector;                   //是否需要上传特征向量，只有在NeedData为false时有效

    	public NET_RESERVED_COMMON()
        {
            this.dwStructSize = this.size();
        }
    }

    // 事件类型EVENT_IVS_TRAFFIC_DRIVER_SMOKING (驾驶员抽烟事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_DRIVER_SMOKING extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public int              nTriggerType;                         // TriggerType:触发类型,0车检器,1雷达,2视频
        public int              PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nSequence;                            // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte             byEventAction;                        // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;    BYTE                    byReserved1[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public byte[]           byReserved1 = new byte[2];
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nLane;                                // 对应车道号
        public int              nMark;                                // 底层产生的触发抓拍帧标记
        public int              nFrameSequence;                       // 视频分析帧序号
        public int              nSource;                              // 视频分析的数据源地址
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public NetSDKLibStructure.NET_MSG_OBJECT   stuVehicle;                           // 车身信息
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;       // 交通车辆信息
        public int              nSpeed;                               // 车辆实际速度,Km/h
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public NetSDKLibStructure.NET_RESOLUTION_INFO stuResolution;                     // 对应图片的分辨率
        public NetSDKLibStructure.EVENT_COMM_INFO stCommInfo;                           // 公共信息
        public NetSDKLibStructure.NET_GPS_INFO stuGPSInfo;                           // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public Pointer          pstuImageInfo;                        //图片信息数组,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IMAGE_INFO_EX3}
        public int              nImageInfoNum;                        //图片信息个数
        public Pointer          pstuSceneImage;                       //全景图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public byte[]           byReserved = new byte[948-2*NetSDKLibStructure.POINTERSIZE]; // 保留字节
    }

    // 事件类型EVENT_IVS_TRAFFIC_DRIVER_CALLING(驾驶员打电话事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_DRIVER_CALLING extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public int              nTriggerType;                         // TriggerType:触发类型,0车检器,1雷达,2视频
        public int              PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nSequence;                            // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte             byEventAction;                        // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;    BYTE                    byReserved1[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public byte[]           byReserved1 = new byte[2];
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nLane;                                // 对应车道号
        public int              nMark;                                // 底层产生的触发抓拍帧标记
        public int              nFrameSequence;                       // 视频分析帧序号
        public int              nSource;                              // 视频分析的数据源地址
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体
        public NetSDKLibStructure.NET_MSG_OBJECT   stuVehicle;                           // 车身信息
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;       // 交通车辆信息
        public int              nSpeed;                               // 车辆实际速度,Km/h
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public NetSDKLibStructure.NET_RESOLUTION_INFO stuResolution;                     // 对应图片的分辨率
        public NetSDKLibStructure.EVENT_COMM_INFO  stCommInfo;                           // 公共信息
        public NetSDKLibStructure.NET_GPS_INFO     stuGPSInfo;                           // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public Pointer          pstuImageInfo;                        //图片信息数组,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IMAGE_INFO_EX3}
        public int              nImageInfoNum;                        //图片信息个数
        public Pointer          pstuSceneImage;                       //全景图图片信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public byte[]           byReserved = new byte[948-NetSDKLibStructure.POINTERSIZE*2]; // 保留字节

		public DEV_EVENT_TRAFFIC_DRIVER_CALLING() {		
			for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
				stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
		}
    }

    // 事件类型EVENT_IVS_TRAFFIC_DRIVERLOOKAROUND(开车左顾右盼报警事件)对应的数据块描述信息
    public static class DEV_EVENT_DRIVERLOOKAROUND_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NET_GPS_STATUS_INFO stuGPSStatus;                      // GPS信息
        public byte[]           szDriverID = new byte[32];            // 司机ID
        public byte[]           szVideoPath = new byte[256];          // 违章关联视频FTP上传路径
        public int              nRelatingVideoInfoNum;                // 违章关联的多个视频信息个数
        public NET_RELATING_VIDEO_INFO[] stuRelatingVideoInfo = new NET_RELATING_VIDEO_INFO[16]; // 违章关联的多个视频信息数组，最多支持16个视频
        public byte[]           bReserved = new byte[736];            // 保留字节,留待扩展.

        public DEV_EVENT_DRIVERLOOKAROUND_INFO() {
        	for (int i = 0; i < stuRelatingVideoInfo.length; i++) {
        		stuRelatingVideoInfo[i] = new NET_RELATING_VIDEO_INFO();
			}
        }
    }

    // 进站离站状态
    public static class NET_BUS_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_BUS_STATE_UNKNOWN = 0;            // 未知
        public static final int   NET_BUS_STATE_ILLEGAL = 1;            // 非法
        public static final int   NET_BUS_STATE_LEGAL = 2;              // 合法
    }

    // 报警事件类型NET_ALARM_ENCLOSURE_ALARM(电子围栏事件)对应的数据描述信息
    public static class ALARM_ENCLOSURE_ALARM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              dwAlarmType;                          // 报警类型, 按位分别表示,
        // 0:LimitSpeed, 1:DriveAllow, 2:ForbidDrive, 3:LoadGoods, 4:UploadGoods
        public int              dwAlarmDetail;                        // 报警描述, 按位分别表示,
        // 0:DriveIn, 1:DriveOut, 2:Overspeed, 3:SpeedClear
        public int              emState;                              // 是否按规定时间触发事件，详见NET_BUS_STATE
        public int              dwDriverNo;                           // 司机编号
        public int              dwEnclosureID;                        // 围栏ID
        public int              dwLimitSpeed;                         // 限速
        public int              dwCurrentSpeed;                       // 当前速度
        public NET_TIME_EX      stuTime;                              // 当前时间
        public NET_GPS_STATUS_INFO stuGPSStatusInfo;                  // GPS信息
        public int              bRealUTC;                             //stuRealUTC 是否有效，bRealUTC 为 TRUE 时，用 stuRealUTC，否则用 stuTime 字段
        public NET_TIME_EX      stuRealUTC = new NET_TIME_EX();       //事件发生的时间(标准UTC时间),参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.NET_TIME_EX}

        public ALARM_ENCLOSURE_ALARM_INFO()
        {
            this.dwSize = this.size();
        }
    }

    public static class DEV_SET_RESULT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwType;                               // 类型(即GetDevConfig和SetDevConfig的类型)
        public short            wResultCode;                          // 返回码；0：成功,1：失败,2：数据不合法,3：暂时无法设置,4：没有权限
        public short            wRebootSign;                          // 重启标志；0：不需要重启,1：需要重启才生效
        public int[]            dwReserved = new int[2];              // 保留
    }

    // ALARM_ENCLOSURE_INFO
    // 电子围栏报警
    public static class ALARM_ENCLOSURE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nTypeNumber;                          // 有效电子围栏类型个数
        public byte[]           bType = new byte[16];                 // 电子围栏类型, 见 ENCLOSURE_TYPE
        public int              nAlarmTypeNumber;                     // 有效报警类型个数
        public byte[]           bAlarmType = new byte[16];            // 报警类型,见ENCLOSURE_ALARM_TYPE
        public byte[]           szDriverId = new byte[32];            // 司机工号
        public int              unEnclosureId;                        // 电子围栏ID
        public int              unLimitSpeed;                         // 限速,单位km/h
        public int              unCurrentSpeed;                       // 当前速度
        public NET_TIME         stAlarmTime;                          // 报警发生时间
        public int              dwLongitude;                          // 经度(单位是百万分之度,范围0-360度)如东经120.178274度表示为300178274
        public int              dwLatidude;                           // 纬度(单位是百万分之度,范围0-180度)如北纬30.183382度表示为12018338
        // 经纬度的具体转换方式可以参考结构体 NET_WIFI_GPS_INFO 中的注释
        public byte             bOffline;                             // 0-实时 1-补传
        public byte[]           reserve = new byte[3];                // 字节对齐
        public int              unTriggerCount;                       // 围栏触发次数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public int              bIsAlarmEnclosureInfoEx;              // 该值为TRUE时应使用 ALARM_ENCLOSURE_INFO_EX 结构体中字段
        public ALARM_ENCLOSURE_INFO_EX stuAlarmEnclosureInfoEx = new ALARM_ENCLOSURE_INFO_EX(); // 当走三代事件时数据存在该结构体
        public byte[]           byReserved = new byte[128];           // 保留字节
    }

    // NETDEV_3GFLOW_EXCEED_STATE_INFO
    // 3G流量超出阈值状态信息
    public static class NETDEV_3GFLOW_EXCEED_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte             bState;                               // 3G流量超出阈值状态,0表示未超出阀值,1表示超出阀值
        public byte[]           reserve1 = new byte[3];               // 字节对齐
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           reserve = new byte[32];               // 保留字节
    }

    // 飞行器类型
    public static class ENUM_UAV_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_TYPE_GENERIC = 0;            // 通用
        public static final int   ENUM_UAV_TYPE_FIXED_WING = 1;         // 固定翼
        public static final int   ENUM_UAV_TYPE_QUADROTOR = 2;          // 四轴
        public static final int   ENUM_UAV_TYPE_COAXIAL = 3;            // 共轴
        public static final int   ENUM_UAV_TYPE_HELICOPTER = 4;         // 直机
        public static final int   ENUM_UAV_TYPE_ANTENNA_TRACKER = 5;    // 地面跟踪天线
        public static final int   ENUM_UAV_TYPE_GCS = 6;                // 地面站
        public static final int   ENUM_UAV_TYPE_AIRSHIP = 7;            // 有控飞艇
        public static final int   ENUM_UAV_TYPE_FREE_BALLOON = 8;       // 自由飞气球
        public static final int   ENUM_UAV_TYPE_ROCKET = 9;             // 火箭
        public static final int   ENUM_UAV_TYPE_GROUND_ROVER = 10;      // 地面车辆
        public static final int   ENUM_UAV_TYPE_SURFACE_BOAT = 11;      // 水面船艇
        public static final int   ENUM_UAV_TYPE_SUBMARINE = 12;         // 潜艇
        public static final int   ENUM_UAV_TYPE_HEXAROTOR = 13;         // 六轴
        public static final int   ENUM_UAV_TYPE_OCTOROTOR = 14;         // 八轴
        public static final int   ENUM_UAV_TYPE_TRICOPTER = 15;         // 三轴
        public static final int   ENUM_UAV_TYPE_FLAPPING_WING = 16;     // 扑翼机
        public static final int   ENUM_UAV_TYPE_KITE = 17;              // 风筝
        public static final int   ENUM_UAV_TYPE_ONBOARD_CONTROLLER = 18; // 控制器
        public static final int   ENUM_UAV_TYPE_VTOL_DUOROTOR = 19;     // 两翼VTOL
        public static final int   ENUM_UAV_TYPE_VTOL_QUADROTOR = 20;    // 四翼VTOL
        public static final int   ENUM_UAV_TYPE_VTOL_TILTROTOR = 21;    // 倾转旋翼机
        public static final int   ENUM_UAV_TYPE_VTOL_RESERVED2 = 22;    // VTOL 保留2
        public static final int   ENUM_UAV_TYPE_VTOL_RESERVED3 = 23;    // VTOL 保留3
        public static final int   ENUM_UAV_TYPE_VTOL_RESERVED4 = 24;    // VTOL 保留4
        public static final int   ENUM_UAV_TYPE_VTOL_RESERVED5 = 25;    // VTOL 保留5
        public static final int   ENUM_UAV_TYPE_GIMBAL = 26;            // 常平架
        public static final int   ENUM_UAV_TYPE_ADSB = 27;              // ADSB
    }

    // 飞行器模式
    public static class ENUM_UAV_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_MODE_UNKNOWN = 0;            // 未知模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_MANUAL = 0;  // 固定翼 手动模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_CIRCLE = 1;  // 固定翼 绕圈模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_STABILIZE = 2; // 固定翼 自稳模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_TRAINING = 3; // 固定翼 训练模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_ACRO = 4;    // 固定翼 特技模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_FLY_BY_WIRE_A = 5; // 固定翼 A翼飞行模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_FLY_BY_WIRE_B = 6; // 固定翼 B翼飞行模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_CRUISE = 7;  // 固定翼 巡航模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_AUTOTUNE = 8; // 固定翼 自动统调
        public static final int   ENUM_UAV_MODE_FIXED_WING_AUTO = 10;   // 固定翼 智能模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_RTL = 11;    // 固定翼 返航模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_LOITER = 12; // 固定翼 定点模式
        public static final int   ENUM_UAV_MODE_FIXED_WING_GUIDED = 15; // 固定翼 引导模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_STABILIZE = 100; // 四轴 自稳模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_ACRO = 101;   // 四轴 特技模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_ALT_HOLD = 102; // 四轴 定高模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_AUTO = 103;   // 四轴 智能模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_GUIDED = 104; // 四轴 引导模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_LOITER = 105; // 四轴 定点模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_RTL = 106;    // 四轴 返航模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_CIRCLE = 107; // 四轴 绕圈模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_LAND = 109;   // 四轴 降落模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_OF_LOITER = 110; // 四轴 启用光流的悬停模式需要光流传感器来保持位置和高度
        public static final int   ENUM_UAV_MODE_QUADROTOR_TOY = 111;    // 四轴 飘移模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_SPORT = 113;  // 四轴 运动模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_AUTOTUNE = 115; // 四轴 自动统调
        public static final int   ENUM_UAV_MODE_QUADROTOR_POSHOLD = 116; // 四轴 保持模式
        public static final int   ENUM_UAV_MODE_QUADROTOR_BRAKE = 117;  // 四轴 制动模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_MANUAL = 200; // 地面车辆 手动模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_LEARNING = 202; // 地面车辆 学习模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_STEERING = 203; // 地面车辆 驾驶模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_HOLD = 204; // 地面车辆 锁定模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_AUTO = 210; // 地面车辆 智能模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_RTL = 211; // 地面车辆 返航模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_GUIDED = 215; // 地面车辆 引导模式
        public static final int   ENUM_UAV_MODE_GROUND_ROVER_INITIALIZING = 216; // 地面车辆 初始化模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_STABILIZE = 300; // 六轴 自稳模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_ACRO = 301;   // 六轴 特技模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_ALT_HOLD = 302; // 六轴 定高模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_AUTO = 303;   // 六轴 智能模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_GUIDED = 304; // 六轴 引导模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_LOITER = 305; // 六轴 定点模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_RTL = 306;    // 六轴 返航模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_CIRCLE = 307; // 六轴 绕圈模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_LAND = 309;   // 六轴 降落模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_OF_LOITER = 310; // 六轴 启用光流的悬停模式需要光流传感器来保持位置和高度
        public static final int   ENUM_UAV_MODE_HEXAROTOR_DRIFT = 311;  // 六轴 飘移模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_SPORT = 313;  // 六轴 运动模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_AUTOTUNE = 315; // 六轴 自动统调
        public static final int   ENUM_UAV_MODE_HEXAROTOR_POSHOLD = 316; // 六轴 保持模式
        public static final int   ENUM_UAV_MODE_HEXAROTOR_BRAKE = 317;  // 六轴 制动模式
    }

    // 无人机系统状态
    public static class ENUM_UAV_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_STATE_UNINIT = 0;            // 未初始化, 状态未知
        public static final int   ENUM_UAV_STATE_BOOT = 1;              // 正在启动
        public static final int   ENUM_UAV_STATE_CALIBRATING = 2;       // 正在校准,未准备好起飞.
        public static final int   ENUM_UAV_STATE_STANDBY = 3;           // 系统地面待命,随时可以起飞.
        public static final int   ENUM_UAV_STATE_ACTIVE = 4;            // 开车/开航. 发动机已经启动.
        public static final int   ENUM_UAV_STATE_CRITICAL = 5;          // 系统处于失常飞行状态,仍能导航.
        public static final int   ENUM_UAV_STATE_EMERGENCY = 6;         // 系统处于失常飞行状态,若干设备失灵,坠落状态.
        public static final int   ENUM_UAV_STATE_POWEROFF = 7;          // 系统刚执行了关机指令,正在关闭.
    }

    // UAV 系统当前模式
    public static class NET_UAV_SYS_MODE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              bSafetyArmedEnabled;                  // 主发动机使能, 准备好起飞.
        public int              bManualInputEnabled;                  // 遥控输入信号使能.
        public int              bHILEnabled;                          // HIL硬件环在线模拟使能.所有发动机, 舵机及其他动作设备阻断, 但内部软件处于全部可操作状态.
        public int              bStabilizeEnabled;                    // 高度/位置电子增稳使能.在此状态下,飞行器仍需要外部操作指令以实现操作
        public int              bGuidedEnabled;                       // 导航使能.导航数据和指令来自导航/航点指令表文件
        public int              bAutoEnabled;                         // 全自主航行模式使能.系统自行决定目的地.前一项“导航使能”可以设置为TURE或FLASE状态
        public int              bTestEnabled;                         // 测试模式使能.本标识仅供临时的系统测试之用,不应该用于实际航行的应用中.
        public int              bReserved;                            // 保留模式
    }

    // 心跳状态信息
    public static class NET_UAV_HEARTBEAT extends NetSDKLibStructure.SdkStructure
    {
        public int              emUAVMode;                            // 飞行模式和飞行器形态类型相关，详见ENUM_UAV_MODE
        public int              emUAVType;                            // 飞行器形态类型，详见ENUM_UAV_TYPE
        public int              emSystemStatus;                       // 系统状态，详见ENUM_UAV_STATE
        public NET_UAV_SYS_MODE_STATE stuBaseMode;                    // 系统当前模式
        public byte[]           byReserved = new byte[8];             // 保留字节
    }

    // 传感器
    public static class ENUM_UAV_SENSOR extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_SENSOR_UNKNOWN = 0;          // 未知类型
        public static final int   ENUM_UAV_SENSOR_3D_GYRO = 1;          // 三轴陀螺
        public static final int   ENUM_UAV_SENSOR_3D_ACCEL = 2;         // 三轴加速度/倾角仪
        public static final int   ENUM_UAV_SENSOR_3D_MAG = 3;           // 三轴磁罗盘
        public static final int   ENUM_UAV_SENSOR_ABSOLUTE_PRESSURE = 4; // 绝对气压计
        public static final int   ENUM_UAV_SENSOR_DIFFERENTIAL_PRESSURE = 5; // 相对气压计
        public static final int   ENUM_UAV_SENSOR_GPS = 6;              // GPS
        public static final int   ENUM_UAV_SENSOR_OPTICAL_FLOW = 7;     // 光测设备
        public static final int   ENUM_UAV_SENSOR_VISION_POSITION = 8;  // 计算机视觉定位仪
        public static final int   ENUM_UAV_SENSOR_LASER_POSITION = 9;   // 激光定位
        public static final int   ENUM_UAV_SENSOR_EXTERNAL_GROUND_TRUTH = 10; // 外部激光定位（Vicon 或徕卡）
        public static final int   ENUM_UAV_SENSOR_ANGULAR_RATE_CONTROL = 11; // 三轴角速度控制器
        public static final int   ENUM_UAV_SENSOR_ATTITUDE_STABILIZATION = 12; // 高度稳定器
        public static final int   ENUM_UAV_SENSOR_YAW_POSITION = 13;    // 方向稳定器（锁尾等）
        public static final int   ENUM_UAV_SENSOR_Z_ALTITUDE_CONTROL = 14; // 高度控制器
        public static final int   ENUM_UAV_SENSOR_XY_POSITION_CONTROL = 15; // X/Y位置控制器
        public static final int   ENUM_UAV_SENSOR_MOTOR_OUTPUTS = 16;   // 马达输出控制器
        public static final int   ENUM_UAV_SENSOR_RC_RECEIVER = 17;     // RC 接收器
        public static final int   ENUM_UAV_SENSOR_3D_GYRO2 = 18;        // 2nd 三轴陀螺
        public static final int   ENUM_UAV_SENSOR_3D_ACCEL2 = 19;       // 2nd 三轴加速度/倾角仪
        public static final int   ENUM_UAV_SENSOR_3D_MAG2 = 20;         // 2nd 三轴磁罗盘
        public static final int   ENUM_UAV_GEOFENCE = 21;               // 地理围栏
        public static final int   ENUM_UAV_AHRS = 22;                   // 姿态子系统运行状况
        public static final int   ENUM_UAV_TERRAIN = 23;                // 地形子系统运行状况
        public static final int   ENUM_UAV_REVERSE_MOTOR = 24;          // 保留马达
    }

    // 传感器信息
    public static class NET_UAV_SENSOR extends NetSDKLibStructure.SdkStructure
    {
        public int              emType;                               // 传感器类型，详见ENUM_UAV_SENSOR
        public int              bEnabled;                             // 使能状态
        public int              bHealthy;                             // 传感器状态
    }

    // 系统状态信息
    public static class NET_UAV_SYS_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public int              nPresentSensorNum;                    // 可见传感器个数, 最大支持32
        public NET_UAV_SENSOR[] stuSensors = new NET_UAV_SENSOR[NetSDKLibStructure.UAV_MAX_SENSOR_NUM]; // 传感器信息
        public int              nBatteryVoltage;                      // 电池电压, 单位: 毫伏
        public int              nBatteryCurrent;                      // 电池电流, 单位: 10毫安
        public int              nChargeDischargeNum;                  // 电池充放电次数
        public int              nHomeDistance;                        // 距离Home的距离, 单位: 米
        public int              nRemainingFlightTime;                 // 剩余飞行时间, 单位: 秒
        public int              nRemainingBattery;                    // 剩余电量百分比 -1: 正在估测剩余电量
        public byte[]           byReserverd = new byte[16];           // 保留字节

        public NET_UAV_SYS_STATUS()
        {
            for (int i = 0; i < stuSensors.length; ++i) {
                stuSensors[i] = new NET_UAV_SENSOR();
            }
        }
    }

    // 卫星信息
    public static class NET_SATELLITE_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public int              bUsed;                                // 卫星使用状态 FLASE: 未使用 TURE: 使用
        public int              nID;                                  // 卫星ID
        public int              nElevation;                           // 卫星在天空中的仰角 单位: 度
        public int              nDireciton;                           // 卫星方位角 单位: 度
        public int              nSNR;                                 // 信噪比, 信号强度百分比
    }

    // GPS 可见卫星的状态信息
    public static class NET_UAV_GPS_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public int              nVisibleNum;                          // 可见卫星个数, 最多支持20个
        public NET_SATELLITE_STATUS[] stuSatellites = new NET_SATELLITE_STATUS[NetSDKLibStructure.UAV_MAX_SATELLITE_NUM]; // 卫星信息

        public NET_UAV_GPS_STATUS()
        {
            for (int i = 0; i < stuSatellites.length; ++i) {
                stuSatellites[i] = new NET_SATELLITE_STATUS();
            }
        }
    }

    // 姿态信息
    public static class NET_UAV_ATTITUDE extends NetSDKLibStructure.SdkStructure
    {
        public float            fRollAngle;                           // 滚转角, 单位: 度
        public float            fPitchAngle;                          // 俯仰角, 单位: 度
        public float            fYawAngle;                            // 偏航角, 单位: 度
        public byte[]           bReserved = new byte[16];             // 保留字节
    }

    // 遥控通道信息
    public static class NET_UAV_RC_CHANNELS extends NetSDKLibStructure.SdkStructure
    {
        public int              nControllerSignal;                    // 遥控器信号百分比, 255: 非法未知
        public byte[]           byReserved = new byte[80];            // 保留字节
    }

    // 平视显示信息
    public static class NET_UAV_VFR_HUD extends NetSDKLibStructure.SdkStructure
    {
        public float            fGroundSpeed;                         // 水平速度, 单位: 米/秒
        public float            fAltitude;                            // 高度, 单位: 米
        public float            fClimbSpeed;                          // 垂直速度, 单位: 米/秒
        public byte[]           byReserved = new byte[12];
    }

    // 故障等级
    public static class ENUM_UAV_SEVERITY extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_SEVERITY_EMERGENCY = 0;      // 系统不可用, 最紧急状态
        public static final int   ENUM_UAV_SEVERITY_ALERT = 1;          // 警报. 非致命性系统故障, 应立即应对.
        public static final int   ENUM_UAV_SEVERITY_CRITICAL = 2;       // 警报: 主要系统故障, 应立即应对
        public static final int   ENUM_UAV_SEVERITY_ERROR = 3;          // 故障: 次系统故障/备份系统故障
        public static final int   ENUM_UAV_SEVERITY_WARNING = 4;        // 警告
        public static final int   ENUM_UAV_SEVERITY_NOTICE = 5;         // 注意: 出现失常现象, 单非错误故障. 应该排查其现象根源
        public static final int   ENUM_UAV_SEVERITY_INFO = 6;           // 提示: 一般性操作消息, 可用于日志. 此消息不需要应对行动
        public static final int   ENUM_UAV_SEVERITY_DEBUG = 7;          // 调试信息。正常操作的时候不该出现
    }

    // 报警文本信息
    public static class NET_UAV_STATUSTEXT extends NetSDKLibStructure.SdkStructure
    {
        public int              emSeverity;                           // 故障等级，详见ENUM_UAV_SEVERITY
        public byte[]           szText = new byte[60];                // 文本信息
        public byte[]           byReserved = new byte[4];             // 保留字节
    }

    // 全球定位数据
    public static class NET_UAV_GLOBAL_POSITION extends NetSDKLibStructure.SdkStructure
    {
        public float            fLatitude;                            // 纬度, 单位: 角度
        public float            fLongitude;                           // 经度, 单位: 角度
        public int              nAltitude;                            // 海拔高度, 单位: 厘米
        public int              nRelativeAltitude;                    // 相对高度, 单位: 厘米
        public int              nXSpeed;                              // X速度（绝对速度、北东地坐标系）, 单位: 厘米每秒
        public int              nYSpeed;                              // Y速度（绝对速度、北东地坐标系）, 单位: 厘米每秒
        public int              nZSpeed;                              // Z速度（绝对速度、北东地坐标系）, 单位: 厘米每秒
        public byte[]           byReserved = new byte[12];
    }

    //  GPS原始数据
    public static class NET_UAV_GPS_RAW extends NetSDKLibStructure.SdkStructure
    {
        public int              nDHOP;                                // GPS水平定位经度因子, 单位厘米. 65535 表示未知
        public int              nGroudSpeed;                          // GPS地速, 厘米每秒. 65535 表示未知
        public int              nVisibleStatellites;                  // 卫星数, 255 表示未知
        public int              nVDOP;                                // GPS 垂直定位因子，单位厘米。65535表示未知
        public int              nCourseOverGround;                    // 整体移动方向, 非机头移动方向. 单位: 100*度
        public int              nFixType;                             // 定位类型.  0 或 1 尚未定位, 2: 2D 定位, 3: 3D 定位
        public byte[]           byReserved = new byte[20];
    }

    // 系统时间
    public static class NET_UAV_SYS_TIME extends NetSDKLibStructure.SdkStructure
    {
        public NET_TIME_EX      UTC;                                  // UTC 时间
        public int              dwBootTime;                           // 启动时间, 单位毫秒
    }

    // 当前航点
    public static class NET_UAV_MISSION_CURRENT extends NetSDKLibStructure.SdkStructure
    {
        public int              nSequence;                            // 序号 0 ~ 700
        public byte[]           byReserved = new byte[16];            // 保留字节
    }

    // 到达航点
    public static class NET_UAV_MISSION_REACHED extends NetSDKLibStructure.SdkStructure
    {
        public int              nSequence;                            // 序号 0 ~ 700
        public byte[]           byReserved = new byte[16];            // 保留字节
    }

    // 云台姿态
    public static class NET_UAV_MOUNT_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public float            fRollAngle;                           // 滚转角, 单位: 度
        public float            fPitchAngle;                          // 俯仰角, 单位: 度
        public float            fYawAngle;                            // 偏航角, 单位: 度
        public int              nTargetSystem;                        // 目标系统
        public int              nTargetComponent;                     // 目标部件
        public int              nMountMode;                           // 云台模式, 参照 NET_UAVCMD_MOUNT_CONFIGURE
        public byte[]           byReserved = new byte[8];             // 保留字节
    }

    // Home点位置信息
    public static class NET_UAV_HOME_POSITION extends NetSDKLibStructure.SdkStructure
    {
        public float            fLatitude;                            // 纬度, 单位: 角度
        public float            fLongitude;                           // 经度, 单位: 角度
        public int              nAltitude;                            // 海拔高度, 单位: 厘米
        public float            fLocalX;                              // X 点
        public float            fLocalY;                              // Y 点
        public float            fLocalZ;                              // Z 点
        public float            fApproachX;                           // 本地 x 矢量点
        public float            fApproachY;
        public float            fApproachZ;
        public byte[]           byReserved = new byte[16];
    }

    // 无人机实时消息类型
    public static class EM_UAVINFO_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_UAVINFO_TYPE_UNKNOWN = 0;          // 未知类型
        public static final int   EM_UAVINFO_TYPE_HEARTBEAT = 1;        // 心跳状态 *pInfo = NET_UAV_HEARTBEAT
        public static final int   EM_UAVINFO_TYPE_SYS_STATUS = 2;       // 系统状态 *pInfo = NET_UAV_SYS_STATUS
        public static final int   EM_UAVINFO_TYPE_GPS_STATUS = 3;       // GPS状态 *pInfo = NET_UAV_GPS_STATUS
        public static final int   EM_UAVINFO_TYPE_ATTITUDE = 4;         // 姿态信息 *pInfo = NET_UAV_ATTITUDE
        public static final int   EM_UAVINFO_TYPE_RC_CHANNELS = 5;      // 遥控通道信息 *pInfo = NET_UAV_RC_CHANNELS
        public static final int   EM_UAVINFO_TYPE_VFR_HUD = 6;          // 平视显示信息 *pInfo = NET_UAV_VFR_HUD
        public static final int   EM_UAVINFO_TYPE_STATUSTEXT = 7;       // 报警文本信息 *pInfo = NET_UAV_STATUSTEXT
        public static final int   EM_UAVINFO_TYPE_GLOBAL_POSITION = 8;  // 全球定位数据 *pInfo = NET_UAV_GLOBAL_POSITION
        public static final int   EM_UAVINFO_TYPE_GPS_RAW = 9;          // GPS原始数据 *pInfo = NET_UAV_GPS_RAW
        public static final int   EM_UAVINFO_TYPE_SYS_TIME = 10;        // 系统时间 *pInfo = NET_UAV_SYS_TIME
        public static final int   EM_UAVINFO_TYPE_MISSION_CURRENT = 11; // 当前航点 *pInfo = NET_UAV_MISSION_CURRENT
        public static final int   EM_UAVINFO_TYPE_MOUNT_STATUS = 12;    // 云台姿态 *pInfo = NET_UAV_MOUNT_STATUS
        public static final int   EM_UAVINFO_TYPE_HOME_POSITION = 13;   // Home点位置信息 *pInfo = NET_UAV_HOME_POSITION
        public static final int   EM_UAVINFO_TYPE_MISSION_REACHED = 14; // 到达航点 *pInfo = NET_UAV_MISSION_REACHED
    }

    // 无人机实时回调数据
    public static class NET_UAVINFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emType;                               // 消息类型，详见EM_UAVINFO_TYPE
        public Pointer          pInfo;                                // 消息内容，指向void
        public int              dwInfoSize;                           // 消息大小
        public byte[]           byReserved = new byte[512];           // 保留字节
    }

    // 无人机实时数据回调
    public interface fUAVInfoCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NET_UAVINFO pstuUAVInfo,int dwUAVInfoSize,Pointer dwUser);
    }

    // 订阅无人机实时消息入参
    public static class NET_IN_ATTACH_UAVINFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Callback         cbNotify;                             // 实时回调函数，实现fUAVInfoCallBack
        public Pointer          dwUser;                               // 用户信息

        public NET_IN_ATTACH_UAVINFO()
        {
            this.dwSize = this.size();
        }
    }

    // 订阅无人机实时消息入参
    public static class NET_OUT_ATTACH_UAVINFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_ATTACH_UAVINFO()
        {
            this.dwSize = this.size();
        }
    }

    // 无人机通用设置命令类型 结构体大小与 NET_UAVCMD_PARAM_BUFFER 保持一致
    public static class ENUM_UAVCMD_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAVCMD_UNKNOWN = -1;
        public static final int   ENUM_UAVCMD_NAV_TAKEOFF = 0;          // 地面起飞或手抛起飞 NET_UAVCMD_TAKEOFF
        public static final int   ENUM_UAVCMD_NAV_LOITER_UNLIM = 1;     // 悬停 NET_UAVCMD_LOITER_UNLIMITED
        public static final int   ENUM_UAVCMD_NAV_RETURN_TO_LAUNCH = 2; // 返航降落 NET_UAVCMD_RETURN_TO_LAUNCH
        public static final int   ENUM_UAVCMD_NAV_LAND = 3;             // 设定点着陆  NET_UAVCMD_LAND
        public static final int   ENUM_UAVCMD_CONDITION_YAW = 4;        // 变换航向 NET_UAVCMD_CONDITION_YAW
        public static final int   ENUM_UAVCMD_DO_CHANGE_SPEED = 5;      // 改变速度 NET_UAVCMD_CHANGE_SPEED
        public static final int   ENUM_UAVCMD_DO_SET_HOME = 6;          // 设置返航点 NET_UAVCMD_SET_HOME
        public static final int   ENUM_UAVCMD_DO_FLIGHTTERMINATION = 7; // 立即停转电机, 飞机锁定 NET_UAVCMD_FLIGHT_TERMINATION
        public static final int   ENUM_UAVCMD_MISSION_START = 8;        // 开始航点任务 NET_UAVCMD_MISSION_START
        public static final int   ENUM_UAVCMD_COMPONENT_ARM_DISARM = 9; // 电调解锁, 电调锁定 NET_UAVCMD_COMPONENT_ARM_DISARM
        public static final int   ENUM_UAVCMD_PREFLIGHT_REBOOT_SHUTDOWN = 10; // 重启飞行器 NET_UAVCMD_REBOOT_SHUTDOWN
        public static final int   ENUM_UAVCMD_DO_SET_RELAY = 11;        // 继电器控制 NET_UAVCMD_SET_RELAY
        public static final int   ENUM_UAVCMD_DO_REPEAT_RELAY = 12;     // 继电器循环控制 NET_UAVCMD_REPEAT_RELAY
        public static final int   ENUM_UAVCMD_DO_FENCE_ENABLE = 13;     // 电子围栏启用禁用 NET_UAVCMD_FENCE_ENABLE
        public static final int   ENUM_UAVCMD_MOUNT_CONFIGURE = 14;     // 云台模式配置 NET_UAVCMD_MOUNT_CONFIGURE
        public static final int   ENUM_UAVCMD_GET_HOME_POSITION = 15;   // 异步获取Home点位置, 实时数据回调中返回 NET_UAVCMD_GET_HOME_POSITION
        public static final int   ENUM_UAVCMD_IMAGE_START_CAPTURE = 16; // 开始抓拍 NET_UAVCMD_IMAGE_START_CAPTURE
        public static final int   ENUM_UAVCMD_IMAGE_STOP_CAPTURE = 17;  // 停止抓拍 NET_UAVCMD_IMAGE_STOP_CAPTURE
        public static final int   ENUM_UAVCMD_VIDEO_START_CAPTURE = 18; // 开始录像 NET_UAVCMD_VIDEO_START_CAPTURE
        public static final int   ENUM_UAVCMD_VIDEO_STOP_CAPTURE = 19;  // 停止录像 NET_UAVCMD_VIDEO_STOP_CAPTURE
        public static final int   ENUM_UAVCMD_NAV_WAYPOINT = 20;        // 航点 NET_UAVCMD_NAV_WAYPOINT
        public static final int   ENUM_UAVCMD_NAV_LOITER_TURNS = 21;    // 循环绕圈 NET_UAVCMD_NAV_LOITER_TURNS
        public static final int   ENUM_UAVCMD_NAV_LOITER_TIME = 22;     // 固定时间等待航点 NET_UAVCMD_NAV_LOITER_TIME
        public static final int   ENUM_UAVCMD_NAV_SPLINE_WAYPOINT = 23; // 曲线航点 NET_UAVCMD_NAV_SPLINE_WAYPOINT
        public static final int   ENUM_UAVCMD_NAV_GUIDED_ENABLE = 24;   // 引导模式开关 NET_UAVCMD_NAV_GUIDED_ENABLE
        public static final int   ENUM_UAVCMD_DO_JUMP = 25;             // 跳转到任务单某个位置. 并执行N次 NET_UAVCMD_DO_JUMP
        public static final int   ENUM_UAVCMD_DO_GUIDED_LIMITS = 26;    // 引导模式执行控制限制 NET_UAVCMD_DO_GUIDED_LIMITS
        public static final int   ENUM_UAVCMD_CONDITION_DELAY = 27;     // 动作延时 NET_UAVCMD_CONDITION_DELAY
        public static final int   ENUM_UAVCMD_CONDITION_DISTANCE = 28;  // 动作距离. 前往设定距离(到下一航点),然后继续 NET_UAVCMD_CONDITION_DISTANCE
        public static final int   ENUM_UAVCMD_DO_SET_ROI = 29;          // 相机兴趣点 NET_UAVCMD_DO_SET_ROI
        public static final int   ENUM_UAVCMD_DO_DIGICAM_CONTROL = 30;  // 相机控制 NET_UAVCMD_DO_DIGICAM_CONTROL
        public static final int   ENUM_UAVCMD_DO_MOUNT_CONTROL = 31;    // 云台角度控制 NET_UAVCMD_DO_MOUNT_CONTROL
        public static final int   ENUM_UAVCMD_DO_SET_CAM_TRIGG_DIST = 32; // 聚焦距离 NET_UAVCMD_DO_SET_CAM_TRIGG_DIST
        public static final int   ENUM_UAVCMD_SET_MODE = 33;            // 设置模式 NET_UAVCMD_SET_MODE
        public static final int   ENUM_UAVCMD_NAV_GUIDED = 34;          // 设定引导点 NET_UAVCMD_NAV_GUIDED
        public static final int   ENUM_UAVCMD_MISSION_PAUSE = 35;       // 飞行任务暂停 NET_UAVCMD_MISSION_PAUSE
        public static final int   ENUM_UAVCMD_MISSION_STOP = 36;        // 飞行任务停止 NET_UAVCMD_MISSION_STOP
        public static final int   ENUM_UAVCMD_LOAD_CONTROL = 37;        // 负载控制 NET_UAVCMD_LOAD_CONTROL
        public static final int   ENUM_UAVCMD_RC_CHANNELS_OVERRIDE = 38; // 模拟摇杆 NET_UAVCMD_RC_CHANNELS_OVERRIDE
        public static final int   ENUM_UAVCMD_HEART_BEAT = 39;          // 心跳 NET_UAVCMD_HEART_BEAT
    }

    // 无人机命令通用信息
    public static class NET_UAVCMD_COMMON extends NetSDKLibStructure.SdkStructure
    {
        public int              nTargetSystem;                        // 目标系统
        public int              nTargetComponent;                     // 目标部件, 0 - 所有部件
        public int              nConfirmation;                        // 确认次数, 0 - 为首次命令. 用于航点任务为0
        public byte[]           byReserved = new byte[4];             // 保留
    }

    // 地面起飞命令 ENUM_UAVCMD_NAV_TAKEOFF
    public static class NET_UAVCMD_TAKEOFF extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fMinimumPitch;                        // 最小爬升率（有空速传感器时适用）设定的爬升率（无传感器）
        public float            fYawAngle;                            // 指向设定.（有罗盘）如无罗盘, 则忽略此参数.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 悬停命令 ENUM_UAVCMD_NAV_LOITER_UNLIM
    public static class NET_UAVCMD_LOITER_UNLIMITED extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fRadius;                              // 盘旋半径(m), 正值顺时针, 负值逆时针.
        public float            fYawAngle;                            // 指向设定, 仅适用可悬停机型
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 返航降落 ENUM_UAVCMD_NAV_RETURN_TO_LAUNCH
    public static class NET_UAVCMD_RETURN_TO_LAUNCH extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public byte[]           byReserved = new byte[28];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 设定点着陆 ENUM_UAVCMD_NAV_LAND
    public static class NET_UAVCMD_LAND extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fYawAngle;                            // 指向设定, 仅适用可悬停机型.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[12];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 变换航向 ENUM_UAVCMD_CONDITION_YAW
    public static class NET_UAVCMD_CONDITION_YAW extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fTargetAngle;                         // 目标角度: [0-360], 0为北
        public float            fSpeed;                               // 转向速率: [度/秒]
        public float            fDirection;                           // 指向: 负值逆时针, 正值顺时针
        public float            fRelativeOffset;                      // 相对偏置或绝对角[1,0]
        public byte[]           byReserved = new byte[12];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 改变速度 ENUM_UAVCMD_DO_CHANGE_SPEED
    public static class NET_UAVCMD_CHANGE_SPEED extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fSpeedType;                           // 速度类型（0=空速, 1=地速）
        public float            fSpeed;                               // 速度（米/秒, -1表示维持原来速度不变）
        public float            fThrottle;                            // 油门开度, 百分比数据，-1表示维持原来数值不变
        public byte[]           byReserved = new byte[16];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 设置返航点 ENUM_UAVCMD_DO_SET_HOME
    public static class NET_UAVCMD_SET_HOME extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nLocation;                            // 返航点: 1 = 使用当前点, 0 - 设定点
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[12];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 立即停转电机 ENUM_UAVCMD_DO_FLIGHTTERMINATION
    public static class NET_UAVCMD_FLIGHT_TERMINATION extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fActivated;                           // 触发值: 大于0.5 被触发
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 开始航点任务  ENUM_UAVCMD_MISSION_START
    public static class NET_UAVCMD_MISSION_START extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nFirstItem;                           // 第一项 n, 起始点的任务号
        public int              nLastItem;                            // 最后一项 m, 终点的任务号
        public byte[]           byReserved = new byte[20];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 飞行任务暂停  ENUM_UAVCMD_MISSION_PAUSE
    public static class NET_UAVCMD_MISSION_PAUSE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public byte[]           byReserved = new byte[28];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 飞行任务停止  ENUM_UAVCMD_MISSION_STOP
    public static class NET_UAVCMD_MISSION_STOP extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public byte[]           byReserved = new byte[28];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 负载类型
    public static class EM_LOAD_CONTROL_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_LOAD_CONTROL_COMMON = 0;           // 通用设备 NET_LOAD_CONTROL_COMMON
        public static final int   EM_LOAD_CONTROL_PHOTO = 1;            // 拍照设备 NET_LOAD_CONTROL_PHOTO
        public static final int   EM_LOAD_CONTROL_VIDEO = 2;            // 视频设备 NET_LOAD_CONTROL_VIDEO
        public static final int   EM_LOAD_CONTROL_AUDIO = 3;            // 音频设备 NET_LOAD_CONTROL_AUDIO
        public static final int   EM_LOAD_CONTROL_LIGHT = 4;            // 灯光设备 NET_LOAD_CONTROL_LIGHT
        public static final int   EM_LOAD_CONTROL_RELAY = 5;            // 继电器设备NET_LOAD_CONTROL_RELAY
        public static final int   EM_LOAD_CONTROL_TIMING = 6;           // 定时拍照设备NET_LOAD_CONTROL_TIMING
        public static final int   EM_LOAD_CONTROL_DISTANCE = 7;         // 定距拍照设备NET_LOAD_CONTROL_DISTANCE
    }

    // 通用设备
    public static class NET_LOAD_CONTROL_COMMON extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           byReserved = new byte[24];            // 实际请使用对应负载填入
    }

    // 拍照设备
    public static class NET_LOAD_CONTROL_PHOTO extends NetSDKLibStructure.SdkStructure
    {
        public float            fCycle;                               // 拍照周期 单位s
        public byte[]           byReserved = new byte[20];            // 对齐 NET_MISSION_ITEM_COMMON
    }

    // 视频设备
    public static class NET_LOAD_CONTROL_VIDEO extends NetSDKLibStructure.SdkStructure
    {
        public int              nSwitch;                              // 开关 0-结束录像 1-开始录像
        public byte[]           byReserved = new byte[20];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 音频设备
    public static class NET_LOAD_CONTROL_AUDIO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           byReserved = new byte[24];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 灯光设备
    public static class NET_LOAD_CONTROL_LIGHT extends NetSDKLibStructure.SdkStructure
    {
        public int              nSwitch;                              // 开关 0-关闭 1-打开
        public byte[]           byReserved = new byte[20];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 继电器设备
    public static class NET_LOAD_CONTROL_RELAY extends NetSDKLibStructure.SdkStructure
    {
        public int              nSwitch;                              // 开关 0-关闭 1-打开
        public byte[]           byReserved = new byte[20];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 定时拍照设备
    public static class NET_LOAD_CONTROL_TIMING extends NetSDKLibStructure.SdkStructure
    {
        public int              nInterval;                            // 拍照时间间隔 单位:s
        public int              nSwitch;                              // 起停控制 0-停止 1-启用
        public byte[]           byReserved = new byte[16];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 定距拍照设备
    public static class NET_LOAD_CONTROL_DISTANCE extends NetSDKLibStructure.SdkStructure
    {
        public int              nInterval;                            // 拍照距离间隔 单位:m
        public int              nSwitch;                              // 起停控制 0-停止 1-启用
        public byte[]           byReserved = new byte[16];            // 对齐 NET_LOAD_CONTROL_COMMON
    }

    // 负载控制
    public static class NET_UAVCMD_LOAD_CONTROL extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              emLoadType;                           // 负载类型，详见EM_LOAD_CONTROL_TYPE
        public NET_LOAD_CONTROL_COMMON stuLoadInfo;                   // 负载控制信息
    }

    // 电调解锁/锁定 ENUM_UAVCMD_COMPONENT_ARM_DISARM,
    public static class NET_UAVCMD_COMPONENT_ARM_DISARM extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              bArm;                                 // TRUE - 解锁, FALSE - 锁定
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 重启飞行器 ENUM_UAVCMD_PREFLIGHT_REBOOT_SHUTDOWN
    public static class NET_UAVCMD_REBOOT_SHUTDOWN extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nCtrlAutopilot;                       // 控制飞控 0 - 空 1 - 重启 2 - 关机
        public int              nCtrlOnboardComputer;                 // 控制机载计算机 0 - 空 1 - 机载计算机重启 2 - 机载计算机关机
        public byte[]           byReserved = new byte[20];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 继电器控制 ENUM_UAVCMD_DO_SET_RELAY
    public static class NET_UAVCMD_SET_RELAY extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nRelayNumber;                         // 继电器号
        public int              nCtrlRelay;                           // 0=关，1=开。
        public byte[]           byReserved = new byte[20];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 继电器循环控制 ENUM_UAVCMD_DO_REPEAT_RELAY
    public static class NET_UAVCMD_REPEAT_RELAY extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nRelayNumber;                         // 继电器号
        public int              nCycleCount;                          // 循环次数
        public int              nCycleTime;                           // 周期（十进制，秒）
        public byte[]           byReserved = new byte[16];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 电子围栏启用禁用 ENUM_UAVCMD_DO_FENCE_ENABLE
    public static class NET_UAVCMD_FENCE_ENABLE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nEnableState;                         // 启用状态 0 - 禁用 1 - 启用 2 - 仅地面禁用
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 云台模式设置 ENUM_UAVCMD_MOUNT_CONFIGURE
    public static class NET_UAVCMD_MOUNT_CONFIGURE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nMountMode;                           // 云台模式
        // 0 - 预留; 1 - 水平模式, RC 不可控; 2 - UAV模式, RC 不可控 ;
        // 3 - 航向锁定模式, RC可控; 4 - 预留; 5-垂直90度模式, RC不可控 6 - 航向跟随模式, RC可控
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 异步获取Home点位置 ENUM_UAVCMD_GET_HOME_POSITION
    public static class NET_UAVCMD_GET_HOME_POSITION extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public byte[]           byReserved = new byte[28];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 开始抓拍 ENUM_UAVCMD_IMAGE_START_CAPTURE Start image capture sequence
    public static class NET_UAVCMD_IMAGE_START_CAPTURE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nDurationTime;                        // 连拍持续时间
        public int              nTatolNumber;                         // 抓拍数量 0 - 表示无限制
        public int              emResolution;                         // 分辨率为 CAPTURE_SIZE_NR时, 表示自定义。目前仅支持 CAPTURE_SIZE_VGA 和 CAPTURE_SIZE_720
        public int              nCustomWidth;                         // 自定义水平分辨率 单位: 像素 pixel
        public int              nCustomHeight;                        // 自定义垂直分辨率 单位: 像素 pixel
        public int              nCameraID;                            // 相机ID
        public byte[]           byReserved = new byte[4];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 分辨率枚举
    public static class CAPTURE_SIZE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   CAPTURE_SIZE_D1 = 0;                  // 704*576(PAL)  704*480(NTSC),兼容WWxHH,下同
        public static final int   CAPTURE_SIZE_HD1 = 1;                 // 352*576(PAL)  352*480(NTSC)
        public static final int   CAPTURE_SIZE_BCIF = 2;                // 704*288(PAL)  704*240(NTSC)
        public static final int   CAPTURE_SIZE_CIF = 3;                 // 352*288(PAL)  352*240(NTSC)
        public static final int   CAPTURE_SIZE_QCIF = 4;                // 176*144(PAL)  176*120(NTSC)
        public static final int   CAPTURE_SIZE_VGA = 5;                 // 640*480
        public static final int   CAPTURE_SIZE_QVGA = 6;                // 320*240
        public static final int   CAPTURE_SIZE_SVCD = 7;                // 480*480
        public static final int   CAPTURE_SIZE_QQVGA = 8;               // 160*128
        public static final int   CAPTURE_SIZE_SVGA = 9;                // 800*592
        public static final int   CAPTURE_SIZE_XVGA = 10;               // 1024*768
        public static final int   CAPTURE_SIZE_WXGA = 11;               // 1280*800
        public static final int   CAPTURE_SIZE_SXGA = 12;               // 1280*1024
        public static final int   CAPTURE_SIZE_WSXGA = 13;              // 1600*1024
        public static final int   CAPTURE_SIZE_UXGA = 14;               // 1600*1200
        public static final int   CAPTURE_SIZE_WUXGA = 15;              // 1920*1200
        public static final int   CAPTURE_SIZE_LTF = 16;                // 240*192,ND1
        public static final int   CAPTURE_SIZE_720 = 17;                // 1280*720
        public static final int   CAPTURE_SIZE_1080 = 18;               // 1920*1080
        public static final int   CAPTURE_SIZE_1_3M = 19;               // 1280*960
        public static final int   CAPTURE_SIZE_2M = 20;                 // 1872*1408,2_5M
        public static final int   CAPTURE_SIZE_5M = 21;                 // 3744*1408
        public static final int   CAPTURE_SIZE_3M = 22;                 // 2048*1536
        public static final int   CAPTURE_SIZE_5_0M = 23;               // 2432*2050
        public static final int   CPTRUTE_SIZE_1_2M = 24;               // 1216*1024
        public static final int   CPTRUTE_SIZE_1408_1024 = 25;          // 1408*1024
        public static final int   CPTRUTE_SIZE_8M = 26;                 // 3296*2472
        public static final int   CPTRUTE_SIZE_2560_1920 = 27;          // 2560*1920(5_1M)
        public static final int   CAPTURE_SIZE_960H = 28;               // 960*576(PAL) 960*480(NTSC)
        public static final int   CAPTURE_SIZE_960_720 = 29;            // 960*720
        public static final int   CAPTURE_SIZE_NHD = 30;                // 640*360
        public static final int   CAPTURE_SIZE_QNHD = 31;               // 320*180
        public static final int   CAPTURE_SIZE_QQNHD = 32;              // 160*90
        public static final int   CAPTURE_SIZE_960_540 = 33;            // 960*540
        public static final int   CAPTURE_SIZE_640_352 = 34;            // 640*352
        public static final int   CAPTURE_SIZE_640_400 = 35;            // 640*400
        public static final int   CAPTURE_SIZE_320_192 = 36;            // 320*192
        public static final int   CAPTURE_SIZE_320_176 = 37;            // 320*176
        public static final int   CAPTURE_SIZE_SVGA1 = 38;              // 800*600
        public static final int   CAPTURE_SIZE_NR = 255;
    }

    // 停止抓拍 ENUM_UAVCMD_IMAGE_STOP_CAPTURE
    public static class NET_UAVCMD_IMAGE_STOP_CAPTURE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nCameraID;                            // 相机ID
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 开始录像 ENUM_UAVCMD_VIDEO_START_CAPTURE
    public static class NET_UAVCMD_VIDEO_START_CAPTURE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nCameraID;                            // 相机ID 0 - 表示所有相机
        public int              nFrameSpeed;                          // 帧率 单位: 秒 -1 表示: 最高帧率
        public int              emResolution;                         // 分辨率 为 CAPTURE_SIZE_NR时, 表示自定义。目前仅支持 CAPTURE_SIZE_VGA 和 CAPTURE_SIZE_720
        public int              nCustomWidth;                         // 自定义水平分辨率 单位: 像素 pixel
        public int              nCustomHeight;                        // 自定义垂直分辨率 单位: 像素 pixel
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 停止录像 ENUM_UAVCMD_VIDEO_STOP_CAPTURE
    public static class NET_UAVCMD_VIDEO_STOP_CAPTURE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nCameraID;                            // 相机ID
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 航点 ENUM_UAVCMD_NAV_WAYPOINT
    public static class NET_UAVCMD_NAV_WAYPOINT extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nHoldTime;                            // 驻留时间. 单位: 秒
        public float            fAcceptanceRadius;                    // 触发半径. 单位: 米. 进入此半径, 认为该航点结束.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 循环绕圈 ENUM_UAVCMD_NAV_LOITER_TURNS
    public static class NET_UAVCMD_NAV_LOITER_TURNS extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nTurnNumber;                          // 圈数.
        public float            fRadius;                              // 盘旋半径(m), 正值顺时针, 负值逆时针.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 在航点盘旋N秒  ENUM_UAVCMD_NAV_LOITER_TIME
    public static class NET_UAVCMD_NAV_LOITER_TIME extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nTime;                                // 时间. 单位: 秒
        public float            fRadius;                              // 盘旋半径(m), 正值顺时针, 负值逆时针.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[8];             // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 曲线航点 ENUM_UAVCMD_NAV_SPLINE_WAYPOINT
    public static class NET_UAVCMD_NAV_SPLINE_WAYPOINT extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nHoldTime;                            // 驻留时间 Hold time in decimal seconds.
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[12];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 引导模式开关 ENUM_UAVCMD_NAV_GUIDED_ENABLE
    public static class NET_UAVCMD_NAV_GUIDED_ENABLE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              bEnable;                              // 使能
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 跳转 ENUM_UAVCMD_DO_JUMP
    public static class NET_UAVCMD_DO_JUMP extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nSequenceNumber;                      // 任务序号
        public int              nRepeatCount;                         // 重复次数
        public byte[]           byReserved = new byte[20];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 引导模式执行控制限制 ENUM_UAVCMD_DO_GUIDED_LIMITS
    public static class NET_UAVCMD_DO_GUIDED_LIMITS extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nMaxTime;                             // 最大时间. 单位: 秒
        public float            fMinAltitude;                         // 最低限制高度. 单位: 米
        public float            fMaxAltitude;                         // 最大限制高度. 单位: 米
        public float            fHorizontalDistance;                  // 水平限制距离. 单位: 米
        public byte[]           byReserved = new byte[12];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 动作延时 ENUM_UAVCMD_CONDITION_DELAY
    public static class NET_UAVCMD_CONDITION_DELAY extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              nDelay;                               // 延迟时间. 单位: 秒
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 动作距离 ENUM_UAVCMD_CONDITION_DISTANCE
    public static class NET_UAVCMD_CONDITION_DISTANCE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fDistance;                            // 距离. 单位: 米
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 无人机兴趣点类型
    public static class ENUM_UAV_ROI_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAV_ROI_MODE_NONE = 0;           // 无兴趣点
        public static final int   ENUM_UAV_ROI_MODE_WPNEXT = 1;         // 面向下一航点
        public static final int   ENUM_UAV_ROI_MODE_WPINDEX = 2;        // 面向指定兴趣点
        public static final int   ENUM_UAV_ROI_MODE_LOCATION = 3;       // 当前航点
    }

    // 相机兴趣点 ENUM_UAVCMD_DO_SET_ROI
    public static class NET_UAVCMD_DO_SET_ROI extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              emROIMode;                            // 兴趣点模式，详见ENUM_UAV_ROI_MODE
        public int              nId;                                  // 指定航点或编号, 根据emROIMode而定
        public int              nROIIndex;                            // ROI 编号
        public byte[]           byReserved = new byte[16];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 相机控制 ENUM_UAVCMD_DO_DIGICAM_CONTROL
    public static class NET_UAVCMD_DO_DIGICAM_CONTROL extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public byte[]           byReserved = new byte[28];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 云台角度控制 ENUM_UAVCMD_DO_MOUNT_CONTROL
    public static class NET_UAVCMD_DO_MOUNT_CONTROL extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fPitchAngle;                          // 俯仰角, 单位: 度. 0: 一键回中, -90 : 一键置90度
        public float            fYawAngle;                            // 航向角, 单位: 度. 0: 一键回中, -90 : 一键置90度
        public byte[]           byReserved = new byte[20];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 聚焦距离 ENUM_UAVCMD_DO_SET_CAM_TRIGG_DIST
    public static class NET_UAVCMD_DO_SET_CAM_TRIGG_DIST extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fDistance;                            // 聚焦距离
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 设置模式 ENUM_UAVCMD_SET_MODE
    public static class NET_UAVCMD_SET_MODE extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public int              emUAVMode;                            // 飞行模式，详见ENUM_UAV_MODE
        public byte[]           byReserved = new byte[24];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 设定引导点 ENUM_UAVCMD_NAV_GUIDED
    public static class NET_UAVCMD_NAV_GUIDED extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 通用信息
        public float            fLatitude;                            // 纬度
        public float            fLongitude;                           // 经度
        public float            fAltitude;                            // 高度
        public byte[]           byReserved = new byte[16];            // 对齐 NET_UAVCMD_PARAM_BUFFER
    }

    // 航点命令对应的通用参数, 需要转换成 ENUM_UAVCMD_TYPE 对应的结构体
    public static class NET_UAVCMD_PARAM_BUFFER extends NetSDKLibStructure.SdkStructure
    {
        public NET_UAVCMD_COMMON stuCommon;                           // 命令通用信息
        public byte[]           byParamBuffer = new byte[28];         // 参数缓存
    }

    // 摇杆模拟：输入参数
    public static class NET_UAVCMD_RC_CHANNELS_OVERRIDE extends NetSDKLibStructure.SdkStructure
    {
        public short            nChan1;                               // 滚转角，范围[1091, 1937]，如果未改变，填 UINT16_MAX
        public short            nChan2;                               // 俯仰角，范围[1091, 1937]，如果未改变，填 UINT16_MAX
        public short            nChan3;                               // 油门，范围[1091, 1937]，如果未改变，填 UINT16_MAX
        public short            nChan4;                               // 偏航角，[1091, 1937]，如果未改变，填 UINT16_MAX
        public short            nChan5;                               // 模式切换：取值1091,1514，1937，如果未改变，填 UINT16_MAX
        public short            nChan6;                               // 云台航向，范围[1091,1937]，如果未改变，填 UINT16_MAX
        public short            nChan7;                               // 云台俯仰，范围[1091,1937]，如果未改变，填 UINT16_MAX
        public short            nChan8;                               // 起落架，取值1091，1937，如果未改变，填 UINT16_MAX
        public short            nChan9;                               // 云台模式， 取值1091,1514，1937，如果未改变，填 UINT16_MAX
        public short            nChan10;                              // 一键返航，取值1091,1937，如果未改变，填 UINT16_MAX
        public short            nChan11;                              // 一键起降，取值1091,1937，如果未改变，填 UINT16_MAX
        public short            nChan12;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan13;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan14;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan15;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan16;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan17;                              // 当前没有用到，填 UINT16_MAX
        public short            nChan18;                              // 当前没有用到，填 UINT16_MAX
        public byte             nTargetSystem;                        // 目标系统
        public byte             nTargetComponent;                     // 目标组件
        public byte[]           szReserved = new byte[6];             // 保留字段，对齐NET_UAVCMD_PARAM_BUFFER
    }

    // 心跳结构体
    public static class NET_UAVCMD_HEART_BEAT extends NetSDKLibStructure.SdkStructure
    {
        public int              nCustomMode;                          // 自动驾驶仪用户自定义模式
        public byte             nType;                                // MAV 类型
        public byte             nAutoPilot;                           // 自动驾驶仪类型
        public byte             nBaseMode;                            // 系统模式
        public byte             nSystemStatus;                        // 系统状态值
        public byte             nMavlinkVersion;                      // MAVLink 版本信息
        public byte[]           szReserved = new byte[35];            // 保留字段，对齐NET_UAVCMD_PARAM_BUFFER
    }

    // 订阅无人机实时消息 pstuInParam 和 pstuOutParam 由设备申请释放
    public LLong CLIENT_AttachUAVInfo(LLong lLoginID,NET_IN_ATTACH_UAVINFO pstuInParam,NET_OUT_ATTACH_UAVINFO pstuOutParam,int nWaitTime);

    // 退订无人机实时消息 lAttachHandle 是 CLIENT_AttachUAVInfo 返回值
    public boolean CLIENT_DetachUAVInfo(LLong lAttachHandle);

    // 警戒区入侵方向
    public static class NET_CROSSREGION_DIRECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CROSSREGION_DIRECTION_UNKNOW = 0;
        public static final int   EM_CROSSREGION_DIRECTION_ENTER = 1;   // 进入
        public static final int   EM_CROSSREGION_DIRECTION_LEAVE = 2;   // 离开
        public static final int   EM_CROSSREGION_DIRECTION_APPEAR = 3;  // 出现
        public static final int   EM_CROSSREGION_DIRECTION_DISAPPEAR = 4; // 消失
    }

    // 电源类型
    public static class EM_POWER_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_POWER_TYPE_MAIN = 0;               // 主电源
        public static final int   EM_POWER_TYPE_BACKUP = 1;             // 备用电源
    }

    // 电源故障事件类型
    public static class EM_POWERFAULT_EVENT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_POWERFAULT_EVENT_UNKNOWN = -1;     // 未知
        public static final int   EM_POWERFAULT_EVENT_LOST = 0;         // 掉电、电池不在位
        public static final int   EM_POWERFAULT_EVENT_LOST_ADAPTER = 1; // 适配器不在位
        public static final int   EM_POWERFAULT_EVENT_LOW_BATTERY = 2;  // 电池欠压
        public static final int   EM_POWERFAULT_EVENT_LOW_ADAPTER = 3;  // 适配器欠压
    }

    // 电源故障事件
    public static class ALARM_POWERFAULT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              emPowerType;                          // 电源类型，详见EM_POWER_TYPE
        public int              emPowerFaultEvent;                    // 电源故障事件，详见EM_POWERFAULT_EVENT_TYPE
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public int              nAction;                              // 0:开始 1:停止
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体

        public ALARM_POWERFAULT_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 防拆报警事件
    public static class ALARM_CHASSISINTRUDED_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nAction;                              // 0:开始 1:停止
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public int              nChannelID;                           // 通道号
        public byte[]           szReaderID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 读卡器ID
        public int              nEventID;                             // 事件ID
        public byte[]           szSN = new byte[32];                  // 无线设备序列号
        public int              bRealUTC;                             // RealUTC 是否有效，bRealUTC 为 TRUE 时，用 RealUTC，否则用 stuTime 字段
        public NET_TIME_EX      RealUTC;                              // 事件发生的时间（标准UTC）
        public int              emDevType;                            //设备类型,参考EM_ALARM_CHASSISINTRUDED_DEV_TYPE

        public ALARM_CHASSISINTRUDED_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 传感器感应方式枚举类型
    public static class NET_SENSE_METHOD extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_SENSE_UNKNOWN = -1;               // 未知类型
        public static final int   NET_SENSE_DOOR = 0;                   // 门磁
        public static final int   NET_SENSE_PASSIVEINFRA = 1;           // 被动红外
        public static final int   NET_SENSE_GAS = 2;                    // 气感
        public static final int   NET_SENSE_SMOKING = 3;                // 烟感
        public static final int   NET_SENSE_WATER = 4;                  // 水感
        public static final int   NET_SENSE_ACTIVEFRA = 5;              // 主动红外
        public static final int   NET_SENSE_GLASS = 6;                  // 玻璃破碎
        public static final int   NET_SENSE_EMERGENCYSWITCH = 7;        // 紧急开关
        public static final int   NET_SENSE_SHOCK = 8;                  // 震动
        public static final int   NET_SENSE_DOUBLEMETHOD = 9;           // 双鉴(红外+微波)
        public static final int   NET_SENSE_THREEMETHOD = 10;           // 三技术
        public static final int   NET_SENSE_TEMP = 11;                  // 温度
        public static final int   NET_SENSE_HUMIDITY = 12;              // 湿度
        public static final int   NET_SENSE_WIND = 13;                  // 风速
        public static final int   NET_SENSE_CALLBUTTON = 14;            // 呼叫按钮
        public static final int   NET_SENSE_GASPRESSURE = 15;           // 气体压力
        public static final int   NET_SENSE_GASCONCENTRATION = 16;      // 燃气浓度
        public static final int   NET_SENSE_GASFLOW = 17;               // 气体流量
        public static final int   NET_SENSE_OTHER = 18;                 // 其他
        public static final int   NET_SENSE_OIL = 19;                   // 油量检测,汽油、柴油等车辆用油检测
        public static final int   NET_SENSE_MILEAGE = 20;               // 里程数检测
        public static final int   NET_SENSE_URGENCYBUTTON = 21;         // 紧急按钮
        public static final int   NET_SENSE_STEAL = 22;                 // 盗窃
        public static final int   NET_SENSE_PERIMETER = 23;             // 周界
        public static final int   NET_SENSE_PREVENTREMOVE = 24;         // 防拆
        public static final int   NET_SENSE_DOORBELL = 25;              // 门铃
        public static final int   NET_SENSE_ALTERVOLT = 26;             // 交流电压传感器
        public static final int   NET_SENSE_DIRECTVOLT = 27;            // 直流电压传感器
        public static final int   NET_SENSE_ALTERCUR = 28;              // 交流电流传感器
        public static final int   NET_SENSE_DIRECTCUR = 29;             // 直流电流传感器
        public static final int   NET_SENSE_RSUGENERAL = 30;            // 高新兴通用模拟量	4~20mA或0~5V
        public static final int   NET_SENSE_RSUDOOR = 31;               // 高新兴门禁感应
        public static final int   NET_SENSE_RSUPOWEROFF = 32;           // 高新兴断电感应
        public static final int   NET_SENSE_TEMP1500 = 33;              // 1500温度传感器
        public static final int   NET_SENSE_TEMPDS18B20 = 34;           // DS18B20温度传感器
        public static final int   NET_SENSE_HUMIDITY1500 = 35;          // 1500湿度传感器
        public static final int   NET_SENSE_INFRARED = 36;              // 红外报警
        public static final int   NET_SENSE_FIREALARM = 37;             // 火警
        public static final int   NET_SENSE_CO2 = 38;                   // CO2浓度检测,典型值:0~5000ppm
        public static final int   NET_SNESE_SOUND = 39;                 // 噪音检测,典型值:30~130dB
        public static final int   NET_SENSE_PM25 = 40;                  // PM2.5检测,典型值:0~1000ug/m3
        public static final int   NET_SENSE_SF6 = 41;                   // SF6浓度检测,典型值:0~3000ppm
        public static final int   NET_SENSE_O3 = 42;                    // 臭氧浓度检测,典型值:0~100ppm
        public static final int   NET_SENSE_AMBIENTLIGHT = 43;          // 环境光照检测,典型值:0~20000Lux
        public static final int   NET_SENSE_SIGNINBUTTON = 44;          // 签入按钮
        public static final int   NET_SENSE_LIQUIDLEVEL = 45;           // 液位
        public static final int   NET_SENSE_DISTANCE = 46;              // 测距
        public static final int   NET_SENSE_WATERFLOW = 47;             // 水流量
        public static final int   NET_SENSE_NUM = 54;                   // 枚举类型总数
    }

    // 防区类型
    public static class NET_DEFENCEAREA_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_DEFENCEAREA_TYPE_UNKNOWN = 0;     // 未知类型防区
        public static final int   NET_DEFENCEAREA_TYPE_ALARM = 1;       // 开关量防区
    }

    // 旁路状态类型
    public static class NET_BYPASS_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_BYPASS_MODE_UNKNOW = 0;           // 未知状态
        public static final int   NET_BYPASS_MODE_BYPASS = 1;           // 旁路
        public static final int   NET_BYPASS_MODE_NORMAL = 2;           // 正常
        public static final int   NET_BYPASS_MODE_ISOLATED = 3;         // 隔离
    }

    // 旁路状态变化事件的信息
    public static class ALARM_BYPASSMODE_CHANGE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 通道号
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public int              emDefenceType;                        // 防区类型，详见NET_DEFENCEAREA_TYPE
        public int              nIsExtend;                            // 是否为扩展(通道)防区, 1:扩展通道, 0: 非扩展通道
        public int              emMode;                               // 变化后的模式，详见NET_BYPASS_MODE
        public int              dwID;                                 // ID号, 遥控器编号或键盘地址, emTriggerMode为NET_EM_TRIGGER_MODE_NET类型时为0
        public int              emTriggerMode;                        // 触发方式，详见NET_EM_TRIGGER_MODE

        public ALARM_BYPASSMODE_CHANGE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 报警输入源事件详情(只要有输入就会产生改事件,不论防区当前的模式,无法屏蔽)
    public static class ALARM_INPUT_SOURCE_SIGNAL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:开始 1:停止
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体

        public ALARM_INPUT_SOURCE_SIGNAL_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 门禁状态类型
    public static class NET_ACCESS_CTL_STATUS_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_UNKNOWN = 0;
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_OPEN = 1;  // 开门
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_CLOSE = 2; // 关门
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_ABNORMAL = 3; // 异常
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_FAKELOCKED = 4; // 假锁
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_CLOSEALWAYS = 5; // 常闭
        public static final int   NET_ACCESS_CTL_STATUS_TYPE_OPENALWAYS = 6; // 常开
    }

    // 多人组合开门事件(对应NET_ALARM_OPENDOORGROUP类型)
    public static class ALARM_OPEN_DOOR_GROUP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 门通道号(从0开始)
        public NET_TIME         stuTime;                              // 事件时间

        public ALARM_OPEN_DOOR_GROUP_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 获取信息事件(对应NET_ALARM_FINGER_PRINT类型)
    public static class ALARM_CAPTURE_FINGER_PRINT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 门通道号(从0开始)
        public NET_TIME         stuTime;                              // 事件时间
        public byte[]           szReaderID = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 门读卡器ID
        public int              nPacketLen;                           // 单个信息数据包长度
        public int              nPacketNum;                           // 信息数据包个数
        public Pointer          szFingerPrintInfo;                    // 信息数据(数据总长度即nPacketLen*nPacketNum)，指向byte
        public int              bCollectResult;                       // 采集结果
        public byte[]           szCardNo = new byte[32];              // 信息所属人员卡号
        public byte[]           szUserID = new byte[32];              // 信息所属人员ID
        public int              nErrorCode;                           //指纹采集失败的错误码, -1 未知, 0, 通用成功 1, 通用失败 2, 采集失败 3, 合成失败 4, 插入失败 5, 超时 6, 采集暂停 7, 指纹重复 8,未知错误，9,指纹已满
        public int              nFingerImageDataLen;                  //指纹图像数据长度
        public byte[]           szFingerImageData = new byte[204800]; //指纹图像数据

        public ALARM_CAPTURE_FINGER_PRINT_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 子系统状态类型
    public static class EM_SUBSYSTEM_STATE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_SUBSYSTEM_STATE_UNKNOWN = 0;       // 未知
        public static final int   EM_SUBSYSTEM_STATE_ACTIVE = 1;        // 已激活
        public static final int   EM_SUBSYSTEM_STATE_INACTIVE = 2;      // 未激活
    }

    // 子系统状态改变事件
    public static class ALARM_SUBSYSTEM_STATE_CHANGE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 子系统序号(从0开始)
        public NET_TIME         stuTime;                              // 事件发生的时间
        public int              emState;                              // 变化后的状态，详见EM_SUBSYSTEM_STATE_TYPE

        public ALARM_SUBSYSTEM_STATE_CHANGE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // PSTN掉线事件
    public static class ALARM_PSTN_BREAK_LINE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 电话线序号(从0开始)
        public int              nAction;                              // 0:开始 1:停止
        public NET_TIME         stuTime;                              // 事件发生的时间
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体

        public ALARM_PSTN_BREAK_LINE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 反复进入事件详细信息
    public static class ALARM_ACCESS_CTL_REPEAT_ENTER_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nDoor;                                // 门通道号
        public byte[]           szDoorName = new byte[NetSDKLibStructure.NET_MAX_DOORNAME_LEN]; // 门禁名称
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public byte[]           szCardNo = new byte[NetSDKLibStructure.NET_MAX_CARDNO_LEN]; // 卡号
        public int              nEventID;                             // 事件ID
        public byte[]           szUserID = new byte[64];              //用户ID，唯一标识一用户
        public byte[]           szReaderID = new byte[64];            //门读卡器ID,10进制

        public ALARM_ACCESS_CTL_REPEAT_ENTER_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 胁迫卡刷卡事件详细信息
    public static class ALARM_ACCESS_CTL_DURESS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nDoor;                                // 门通道号
        public byte[]           szDoorName = new byte[NetSDKLibStructure.NET_MAX_DOORNAME_LEN]; // 门禁名称
        public byte[]           szCardNo = new byte[NetSDKLibStructure.NET_MAX_CARDNO_LEN]; // 胁迫卡号
        public NET_TIME         stuTime;                              // 报警事件发生的时间
        public int              nEventID;                             // 事件ID
        public byte[]           szSN = new byte[32];                  // 无线设备序列号
        public byte[]           szUserID = new byte[12];              // 用户ID
        public int              bRealUTC;                             // RealUTC 是否有效，bRealUTC 为 TRUE 时，用 RealUTC，否则用 stuTime 字段
        public NET_TIME_EX      RealUTC;                              // 事件发生的时间（标准UTC）
        public byte[]           szUserIDEx = new byte[64];            // 用户ID扩展

        public ALARM_ACCESS_CTL_DURESS_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 区域检测事件动作
    public static class NET_CROSSREGION_ACTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CROSSREGION_ACTION_UNKNOW = 0;
        public static final int   EM_CROSSREGION_ACTION_INSIDE = 1;     // 在区域内
        public static final int   EM_CROSSREGION_ACTION_CROSS = 2;      // 穿越区域
        public static final int   EM_CROSSREGION_ACTION_APPEAR = 3;     // 出现
        public static final int   EM_CROSSREGION_ACTION_DISAPPEAR = 4;  // 消失
    }

    // 警戒线入侵方向
    public static class NET_CROSSLINE_DIRECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CROSSLINE_DIRECTION_UNKNOW = 0;
        public static final int   EM_CROSSLINE_DIRECTION_LEFT2RIGHT = 1; // 左到右
        public static final int   EM_CROSSLINE_DIRECTION_RIGHT2LEFT = 2; // 右到左
        public static final int   EM_CROSSLINE_DIRECTION_ANY = 3;
    }

    // 警戒线事件(对应事件 NET_EVENT_CROSSLINE_DETECTION)
    public static class ALARM_EVENT_CROSSLINE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 通道号
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public int              emCrossDirection;                     // 入侵方向，详见NET_CROSSLINE_DIRECTION_INFO
        public int              nOccurrenceCount;                     // 规则被触发生次数
        public int              nLevel;                               // 事件级别,GB30147需求项
        public int              bIsObjectInfo;                        // 是否检测到物体信息
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体信息
        public int              nRetObjectNum;                        // 实际返回多个检测到的物体信息
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.MAX_TARGET_OBJECT_NUM]; // 多个检测到的物体信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           szPresetName = new byte[64];          //事件触发的预置点名称
        public int              nPresetID;                            //事件触发的预置点号

        public ALARM_EVENT_CROSSLINE_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuObjects.length; ++i) {
                stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
            }
        }
    }

    //警戒区事件(对应事件 NET_EVENT_CROSSREGION_DETECTION)
    public static class ALARM_EVENT_CROSSREGION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannelID;                           // 通道号
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public int              emDirection;                          // 警戒区入侵方向，详见NET_CROSSREGION_DIRECTION_INFO
        public int              emActionType;                         // 警戒区检测动作类型，详见NET_CROSSREGION_ACTION_INFO
        public int              nOccurrenceCount;                     // 规则被触发生次数
        public int              nLevel;                               // 事件级别,GB30147需求项
        public byte[]           szName = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 名称
        public int              bIsObjectInfo;                        // 是否检测到物体信息
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体信息
        public int              nRetObjectNum;                        // 实际返回多个检测到的物体信息
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.MAX_TARGET_OBJECT_NUM]; // 多个检测到的物体信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           szMac = new byte[32];                 // 事件触发源的Mac地址
        public byte[]           szReserved = new byte[1024];          // 预留字节

        public ALARM_EVENT_CROSSREGION_INFO()
        {
            this.dwSize = this.size();

            for (int i = 0; i < stuObjects.length; ++i) {
                stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
            }
        }
    }

    // 探测器状态
    public static class EM_SENSOR_ABNORMAL_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_SENSOR_ABNORMAL_STATUS_UNKNOWN = 0;
        public static final int   NET_SENSOR_ABNORMAL_STATUS_SHORT = 1; // 短路
        public static final int   NET_SENSOR_ABNORMAL_STATUS_BREAK = 2; // 断路
        public static final int   NET_SENSOR_ABNORMAL_STATUS_INTRIDED = 3; // 被拆开
    }

    //事件类型(NET_ALARM_SENSOR_ABNORMAL) 探测器状态异常报警
    public static class ALARM_SENSOR_ABNORMAL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nAction;                              // 0:开始 1:停止
        public int              nChannelID;                           // 视频通道号
        public NET_TIME_EX      stuTime;                              // 事件发生的时间
        public int              emStatus;                             // 探测器状态，详见EM_SENSOR_ABNORMAL_STATUS
        public int              emSenseMethod;                        // SenseMethod, 感应方式,参见具体枚举定义NET_SENSE_METHOD
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[124];           // 预留字段
    }

    // 防区布防撤防状态类型
    public static class EM_DEFENCEMODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DEFENCEMODE_UNKNOWN = 0;           // "unknown"   未知
        public static final int   EM_DEFENCEMODE_ARMING = 1;            // "Arming"    布防
        public static final int   EM_DEFENCEMODE_DISARMING = 2;         // "Disarming" 撤防
    }

    //触发方式
    public static class EM_ARMMODECHANGE_TRIGGERMODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ARMMODECHANGE_TRIGGERMODE_UNKNOWN = 0; // 未知
        public static final int   EM_ARMMODECHANGE_TRIGGERMODE_NET = 1; // 网络用户
        public static final int   EM_ARMMODECHANGE_TRIGGERMODE_KEYBOARD = 2; // 键盘
        public static final int   EM_ARMMODECHANGE_TRIGGERMODE_REMOTECONTROL = 3; // 遥控器
    }

    //防区类型
    public static class EM_ARMMODECHANGE_DEFENCEAREATYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_UNKNOWN = 0; // 未知
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_INTIME = 1; // 及时
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_DELAY = 2; // 延时
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_FULLDAY = 3; // 全天
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_FOLLOW = 4; // 跟随
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_MEDICAL = 5; // 医疗紧急
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_PANIC = 6; // 恐慌
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_FIRE = 7; // 火警
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_FULLDAYSOUND = 8; // 全天有声
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_FULLDAYSILENT = 9; // 全天无声
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_ENTRANCE1 = 10; // 出入防区1
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_ENTRANCE2 = 11; // 出入防区2
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_INSIDE = 12; // 内部防区
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_OUTSIDE = 13; // 外部防区
        public static final int   EM_ARMMODECHANGE_DEFENCEAREATYPE_PEOPLEDETECT = 14; // 人员检测
    }

    // 事件类型NET_ALARM_DEFENCE_ARMMODECHANGE (防区布撤防状态改变事件)
    public static class ALARM_DEFENCE_ARMMODECHANGE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emDefenceStatus;                      // 布撤防状态，详见EM_DEFENCEMODE
        public int              nDefenceID;                           // 防区号
        public NET_TIME_EX      stuTime;                              // 时间
        public int              emTriggerMode;                        // 触发方式，详见EM_ARMMODECHANGE_TRIGGERMODE
        public int              emDefenceAreaType;                    // 防区类型，详见EM_ARMMODECHANGE_DEFENCEAREATYPE
        public int              nID;                                  // 遥控器编号或键盘地址
        public int              nAlarmSubSystem;                      // 子系统号
        public byte[]           szName = new byte[64];                // 防区名称
        public byte[]           szNetClientAddr = new byte[64];       // 用户IP或网络地址
        public byte[]           reserved = new byte[368];             // 预留
    }

    // 工作状态
    public static class EM_SUBSYSTEMMODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_SUBSYSTEMMODE_UNKNOWN = 0;         // "unknown"   未知
        public static final int   EM_SUBSYSTEMMODE_ACTIVE = 1;          // "active"    激活
        public static final int   EM_SUBSYSTEMMODE_INACTIVE = 2;        // "inactive"  未激活
        public static final int   EM_SUBSYSTEMMODE_UNDISTRIBUTED = 3;   // "undistributed" 未分配
        public static final int   EM_SUBSYSTEMMODE_ALLARMING = 4;       // "AllArming" 全部布防
        public static final int   EM_SUBSYSTEMMODE_ALLDISARMING = 5;    // "AllDisarming" 全部撤防
        public static final int   EM_SUBSYSTEMMODE_PARTARMING = 6;      // "PartArming" 部分布防
    }

    //触发方式
    public static class EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE_UNKNOWN = 0; // 未知
        public static final int   EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE_NET = 1; // 网络用户
        public static final int   EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE_KEYBOARD = 2; // 键盘
        public static final int   EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE_REMOTECONTROL = 3; // 遥控器
    }

    // 事件类型 NET_ALARM_SUBSYSTEM_ARMMODECHANGE (子系统布撤防状态改变事件)
    public static class ALARM_SUBSYSTEM_ARMMODECHANGE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emSubsystemMode;                      // 布撤防状态 (只支持AllArming，AllDisarming，PartArming三种状态)，详见EM_SUBSYSTEMMODE
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public byte[]           szSubSystemname = new byte[64];       // 子系统名称
        public int              nSubSystemID;                         // 子系统编号
        public int              emTriggerMode;                        // 触发方式，详见EM_SUBSYSTEM_ARMMODECHANGE_TRIGGERMODE
        public int              nID;                                  // 键盘或遥控器地址
        public byte[]           szNetClientAddr = new byte[64];       // 网络用户IP地址或网络地址
        public byte[]           reserved = new byte[440];             // 预留
    }

    // 立体视觉站立事件区域内人员列表
    public static class MAN_STAND_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.NET_POINT        stuCenter;                            // 站立人员所在位置,8192坐标系
        public byte[]           szSerialUUID = new byte[22];          // 智能物体全局唯一物体标识
        // 有效数据位21位，包含’\0’
        // 前2位%d%d:01-视频片段, 02-图片, 03-文件, 99-其他
        // 中间14位YYYYMMDDhhmmss:年月日时分秒
        // 后5位%u%u%u%u%u：物体ID，如00001
        public NetSDKLibStructure.DH_RECT          stuBoundingBox;                       // 包围盒
        public byte[]           szReversed = new byte[90];            // 保留字节
    }

    // 事件类型EVENT_IVS_MAN_STAND_DETECTION(立体视觉站立事件)对应数据块描述信息
    public static class DEV_EVENT_MANSTAND_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐,非保留字节
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        ///////////////////////////////以上为公共字段，除nChannelID外的其他字段是为了预留公共字段空间//////////////////////////////
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public int              nManListCount;                        // 区域人员列表数量
        public MAN_STAND_LIST_INFO[] stuManList = new MAN_STAND_LIST_INFO[NetSDKLibStructure.MAX_MAN_LIST_COUNT]; // 区域内人员列表
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szReversed = new byte[2048];          // 保留字节

        public DEV_EVENT_MANSTAND_DETECTION_INFO()
        {
            for (int i = 0; i < stuManList.length; ++i) {
                stuManList[i] = new MAN_STAND_LIST_INFO();
            }
        }
    }

    // 课堂行为动作类型
    public static class EM_CLASSROOM_ACTION extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CLASSROOM_ACTION_UNKNOWN = 0;      // 未知
        public static final int   EM_CLASSROOM_ACTION_PLAY_PHONE = 1;   // 玩手机
        public static final int   EM_CLASSROOM_ACTION_HANDSUP = 2;      // 举手
        public static final int   EM_CLASSROOM_ACTION_LISTEN = 3;       // 听讲
        public static final int   EM_CLASSROOM_ACTION_READ_WRITE = 4;   // 读写
        public static final int   EM_CLASSROOM_ACTION_TABLE = 5;        // 趴桌子
    }

    // 事件类型 EVENT_IVS_CLASSROOM_BEHAVIOR (课堂行为分析事件) 对应的数据块描述信息
    public static class DEV_EVENT_CLASSROOM_BEHAVIOR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              emClassType;                          // 智能事件所属大类，详见EM_SCENE_CLASS_TYPE
        public int              nRuleID;                              // 智能事件规则编号，用于标示哪个规则触发的事件
        public int              nObjectID;                            // 物体ID
        public int              nSequence;                            // 帧序号
        public int              emClassroomAction;                    // 课堂行为动作，详见EM_CLASSROOM_ACTION
        public NetSDKLibStructure.NET_POINT[]      stuDetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public int              nPresetID;                            // 事件触发的预置点号
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置点名称
        public byte[]           szSerialUUID = new byte[22];          // 智能物体全局唯一物体标识
        // 格式如下：前2位%d%d:01-视频片段,02-图片,03-文件,99-其他;
        //中间14位YYYYMMDDhhmmss:年月日时分秒;后5位%u%u%u%u%u：物体ID，如00001
        public byte[]           byReserved1 = new byte[2];            // 用于字节对齐
        public NET_RECT         stuBoundingBox;                       // 包围盒
        public NetSDKLibStructure.NET_INTELLIGENCE_IMAGE_INFO stuSceneImage;             // 人脸底图信息
        public NetSDKLibStructure.NET_INTELLIGENCE_IMAGE_INFO stuFaceImage;              // 人脸小图信息
        public NetSDKLibStructure.NET_FACE_ATTRIBUTE_EX stuFaceAttributes;               // 人脸属性
        public NET_IMAGE_INFO_EX2 stuImageInfo[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        //图片信息个数
        public byte             byReserved[] = new byte[1024];        //预留字节

        public DEV_EVENT_CLASSROOM_BEHAVIOR_INFO()
        {
            for (int i = 0; i < stuDetectRegion.length; ++i) {
                stuDetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 抓拍类型
    public static class NET_EM_SNAP_SHOT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_EM_SNAP_SHOT_TYPE_UNKNOWN = 0;    // 未知
        public static final int   NET_EM_SNAP_SHOT_TYPE_NEAR = 1;       // 近景
        public static final int   NET_EM_SNAP_SHOT_TYPE_MEDIUM = 2;     // 中景
        public static final int   NET_EM_SNAP_SHOT_TYPE_FAR = 3;        // 远景
        public static final int   NET_EM_SNAP_SHOT_TYPE_FEATURE = 4;    // 车牌特写
    }

    // 抓拍间隔模式
    public static class NET_EM_SNAP_SHOT_INTERVAL_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_EM_SNAP_SHOT_INTERVAL_UNKNOWN = 0; // 未知
        public static final int   NET_EM_SNAP_SHOT_INTERVAL_TIME = 1;   // 按固定时间间隔，该模式下nSingleInterval有效
        public static final int   NET_EM_SNAP_SHOT_INTERVAL_FRAMEADAPTSPEED = 2; // 速度自适应帧间隔
        public static final int   NET_EM_SNAP_SHOT_INTERVAL_FRAME = 3;  // 固定帧间隔
    }

    // 规则集抓拍参数
    public static class NET_SNAP_SHOT_WITH_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nRuleId;
        public int              dwRuleType;                           // 规则类型，详见dhnetsdk.h中"智能分析事件类型"
        public int              nSnapShotNum;                         // 抓拍图片张数
        public int[]            emSnapShotType = new int[NetSDKLibStructure.MAX_SNAP_SHOT_NUM]; // 抓拍图片类型数组，详见NET_EM_SNAP_SHOT_TYPE
        public int[]            nSingleInterval = new int[NetSDKLibStructure.MAX_SNAP_SHOT_NUM]; // 抓图时间间隔数组,单位秒，数组第一个时间:5~180 默认10， 其余时间(N张抓拍有N-1个间隔时):1~3600 默认20
        public int              emIntervalMode;                       // 抓拍间隔模式，详见NET_EM_SNAP_SHOT_INTERVAL_MODE
        public byte[]           byReserved = new byte[1024];          // 预留
    }

    // 抓拍参数
    public static class NET_SCENE_SNAP_SHOT_WITH_RULE2_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nPresetID;                            // 场景预置点号
        public int              nRetSnapShotRuleNum;                  // stuSnapShotWithRule中有效数据个数
        public NET_SNAP_SHOT_WITH_RULE_INFO[] stuSnapShotWithRule = new NET_SNAP_SHOT_WITH_RULE_INFO[32]; // 规则集抓拍参数
        public byte[]           byReserved = new byte[1024];          // 预留

        public NET_SCENE_SNAP_SHOT_WITH_RULE2_INFO()
        {
            for (int i = 0; i < stuSnapShotWithRule.length; ++i) {
                stuSnapShotWithRule[i] = new NET_SNAP_SHOT_WITH_RULE_INFO();
            }
        }
    }

    // 场景抓拍设置 对应枚举 NET_EM_CFG_SCENE_SNAP_SHOT_WITH_RULE2
    public static class NET_CFG_SCENE_SNAP_SHOT_WITH_RULE2_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nMaxRuleNum;                          // pstuSnapShotWithRule中用户分配的内存个数
        public int              nRetRuleNum;                          // pstuSnapShotWithRule中实际有效的数据个数
        public Pointer          pstuSceneSnapShotWithRule;            // 抓拍参数,由用户分配和释放内存，大小为nMaxRuleNum * sizeof(NET_SCENE_SNAP_SHOT_WITH_RULE2_INFO)，指向NET_SCENE_SNAP_SHOT_WITH_RULE2_INFO

        public NET_CFG_SCENE_SNAP_SHOT_WITH_RULE2_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 车辆动作
    public static class EM_VEHICLE_ACTION extends Structure {
        public static final int   EM_VEHICLE_ACTION_UNKNOWN = 0;        // 未知
        public static final int   EM_VEHICLE_ACTION_APPEAR = 1;         // "Appear"在检测区域内
        public static final int   EM_VEHICLE_ACTION_DISAPPEAR = 2;      // "Disappear"离开检测区域
    }

    // 检测到的车辆信息
    public static class NET_DETECT_VEHICLE_INFO extends NetSDKLibStructure.SdkStructure {
        public int              emAction;                             // 检测车辆动作
        public int /*UINT*/     nObjectID;                            // 物体ID
        public NetSDKLibStructure.EVENT_PIC_INFO   stuVehicleImage;                      // 车辆抓图信息
        public NET_COLOR_RGBA   stuColor;                             // 车身主要颜色
        public int              emCategoryType;                       // 车辆类型,参考枚举EM_CATEGORY_TYPE
        public int /*UINT*/     nFrameSequence;                       // 帧序号
        public int /*UINT*/     nCarLogoIndex;                        // 车辆车标
        public int /*UINT*/     nSubBrand;                            // 车辆子品牌
        public int /*UINT*/     nBrandYear;                           // 车辆品牌年款
        public int /*UINT*/     nConfidence;                          // 置信度,值越大表示置信度越高, 范围 0~255
        public NET_RECT         stuBoundingBox;                       // 包围盒, 0-8191相对坐标
        public byte[]           szText = new byte[128];               // 车标
        public int /*UINT*/     nSpeed;                               // 车速,单位为km/h
        public int /*UINT*/     nDirection;                           // 车辆行驶方向, 0:未知, 1:上行方向, 2:下行方向
        public byte[]           bReserved = new byte[512];            // 保留字节
    }

    // 检测的车牌信息
    public static class NET_DETECT_PLATE_INFO extends NetSDKLibStructure.SdkStructure {
        public int /*UINT*/     nObjectID;                            // 车牌ID
        public int /*UINT*/     nRelativeID;                          // 关联的车辆ID
        public NetSDKLibStructure.EVENT_PIC_INFO   stuPlateImage;                        // 车牌图片信息
        public int              emPlateType;                          // 车牌类型，参考枚举EM_NET_PLATE_TYPE
        public int              emPlateColor;                         // 车牌颜色，参考枚举EM_NET_PLATE_COLOR_TYPE
        public int /*UINT*/     nConfidence;                          // 置信度,值越大表示置信度越高, 范围 0~255
        public byte[]           szCountry = new byte[3];              // 车牌国家
        public byte             bReserved1;                           // 字节对齐
        public byte[]           szPlateNumber = new byte[128];        // 车牌号码
        public byte[]           bReserved = new byte[512];            // 保留字节
    }

    // 加油站车辆检测事件 (对应 DEV_EVENT_GASSTATION_VEHICLE_DETECT_INFO)
    public static class DEV_EVENT_GASSTATION_VEHICLE_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲1:开始 2:停止
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nRuleID;                              // 智能事件规则编号，用于标示哪个规则触发的事件
        public int              emClassType;                          // 智能事件所属大类
        public NET_DETECT_VEHICLE_INFO stuDetectVehicleInfo;          // 检测到的车辆信息
        public NET_DETECT_PLATE_INFO stuDetectPlateInfo;              // 检测到的车牌信息
        public int              bIsGlobalScene;                       // 是否有场景图
        public NetSDKLibStructure.EVENT_PIC_INFO   stuSceneImage;                        // 场景图信息, bIsGlobalScene 为 TRUE 时有效
        public int              nCarCandidateNum;                     // 候选车辆数量
        public NetSDKLibStructure.NET_CAR_CANDIDATE_INFO[] stuCarCandidate = (NetSDKLibStructure.NET_CAR_CANDIDATE_INFO[])new NetSDKLibStructure.NET_CAR_CANDIDATE_INFO().toArray(NetSDKLibStructure.MAX_CAR_CANDIDATE_NUM); // 候选车辆数据
        /*public boolean                  bIsEmptyPlace;                              // 是否是空车位报警*/
        public NET_FUEL_DISPENSER_INFO stuFuelDispenser;              // 从加油机获取的信息，IVSS对接加油机及N8000
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;       // 交通车辆信息
        public byte[]           bReserved = new byte[872];            // 保留字节
    }

    // 事件级别，GB30147需求
    public static class EM_EVENT_LEVEL extends Structure
    {
        public static final int   EM_EVENT_LEVEL_HINT = 0;              // 提示
        public static final int   EM_EVENT_LEVEL_GENERAL = 1;           // 普通
        public static final int   EM_EVENT_LEVEL_WARNING = 2;           // 警告
    }

    // 事件类型EVENT_IVS_SHOPPRESENCE(商铺占道经营事件)对应的数据块描述信息
    public static class DEV_EVENT_SHOPPRESENCE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 检测到的物体，推荐使用字段stuObjects获取物体信息
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置点名称
        public int              emEventLevel;                         // 事件级别，GB30147需求
        public byte[]           szShopAddress = new byte[256];        // 商铺地址
        public int              nViolationDuration;                   // 违法持续时长，单位：秒，缺省值0表示无意义
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = (NetSDKLibStructure.NET_MSG_OBJECT[]) new NetSDKLibStructure.NET_MSG_OBJECT().toArray(NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM); // 检测到的物体
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public Pointer          pstuImageInfo;                        // 图片信息数组,指针对应NET_IMAGE_INFO_EX2数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX(); // 全景图图片信息,事件前2~5s抓图
        public Pointer          pstuMosaicImage;                      // 合成图,指针对应SCENE_IMAGE_INFO_EX数组
        public int              nMosaicImageNum;                      // 合成图个数
        public Pointer          pstuAdvanceImage;                     // 事件发生前抓图，指针对应SCENE_IMAGE_INFO_EX数组
        public int              nAdvanceImageNum;                     // 事件发生前抓图个数
        public int              nVehicleSpeed;                        //车速, 单位km/h
        public double           dbHeadingAngle;                       //航向角, 以正北方向为基准输出车辆运动方向同正北方向的角度:范围 0~360,顺时针正,单位为度
        public double[]         dbLongitude = new double[3];          //经度,格式:度,分,秒(秒为浮点数)
        public double[]         dbLatitude = new double[3];           //纬度,格式:度,分,秒(秒为浮点数)
        public byte[]           byReserved2 = new byte[1188-NetSDKLibStructure.POINTERSIZE*3]; // 保留字节
    }

    // 事件类型 EVENT_IVS_FLOWBUSINESS (流动摊贩事件) 对应的数据块描述信息
    public static class DEV_EVENT_FLOWBUSINESS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nDetectRegionNum;                     // 检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 检测区域
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public int              nViolationDuration;                   // 违法持续时长，单位：秒 缺省值0表示无意义
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = (NetSDKLibStructure.NET_MSG_OBJECT[]) new NetSDKLibStructure.NET_MSG_OBJECT().toArray(NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM); // 检测到的物体
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public byte[]           byReserved = new byte[2044];          // 保留字节
    }

    // 立体视觉区域内人数统计事件区域人员列表
    public static class MAN_NUM_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.DH_RECT          stuBoudingBox;                        // 人员包围盒,8192坐标系
        public int              nStature;                             // 人员身高，单位cm
        public byte[]           szReversed = new byte[128];           // 保留字节
    }

    /**
     * @author 260611
     * @description 事件类型EVENT_IVS_MAN_NUM_DETECTION(立体视觉区域内人数统计事件)对应数据块描述信息
     * @date 2023/01/10 19:44:49
     */
    public class DEV_EVENT_MANNUM_DETECTION_INFO extends NetSDKLibStructure.SdkStructure {
        /**
         * 通道号
         */
        public int              nChannelID;
        /**
         * 事件名称
         */
        public byte[]           szName = new byte[128];
        /**
         * 字节对齐, 非保留字节
         */
        public byte[]           bReserved1 = new byte[4];
        /**
         * 时间戳(单位是毫秒)
         */
        public double           PTS;
        /**
         * 事件发生的时间
         */
        public com.coalbot.camera.sdk.sdk.dahua.structure.NET_TIME_EX UTC = new com.coalbot.camera.sdk.sdk.dahua.structure.NET_TIME_EX();
        /**
         * 事件ID
         */
        public int              nEventID;
        /**
         * 0:脉冲 1:开始 2:停止
         */
        public int              nAction;
        /**
         * 区域人员列表数量
         */
        public int              nManListCount;
        /**
         * 区域内人员列表
         */
        public MAN_NUM_LIST_INFO[] stuManList = new MAN_NUM_LIST_INFO[64];
        /**
         * 智能事件公共信息
         */
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo = new NetSDKLibStructure.EVENT_INTELLI_COMM_INFO();
        /**
         * 区域ID(一个预置点可以对应多个区域ID)
         */
        public int              nAreaID;
        /**
         * 变化前人数
         */
        public int              nPrevNumber;
        /**
         * 当前人数
         */
        public int              nCurrentNumber;
        /**
         * 事件关联ID。应用场景是同一个物体或者同一张图片做不同分析，产生的多个事件的SourceID相同
         * 缺省时为空字符串，表示无此信息
         * 格式：类型+时间+序列号，其中类型2位，时间14位，序列号5位
         */
        public byte[]           szSourceID = new byte[32];
        /**
         * null
         */
        public byte[]           szRuleName = new byte[128];
        /**
         * 检测模式 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_EVENT_DETECT_TYPE}
         */
        public int              emDetectType;
        /**
         * 实际触发报警的人数
         */
        public int              nAlertNum;
        /**
         * 报警类型. 0:未知, 1:从人数正常到人数异常, 2:从人数异常到人数正常
         */
        public int              nAlarmType;
        /**
         * 图片信息数组
         */
        public Pointer          pstuImageInfo;
        /**
         * 图片信息个数
         */
        public int              nImageInfoNum;
        /**
         * 事件公共扩展字段结构体
         */
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND();
        /**
         * 检测区个数
         */
        public int              nDetectRegionNum;
    	/**
    	 * 检测区
    	 */
        public NET_POINT_EX[]   stuDetectRegion = new NET_POINT_EX[20];
        /**
         * 保留字节
         */
        public byte[]           szReversed = new byte[700];

        public DEV_EVENT_MANNUM_DETECTION_INFO() {
            for (int i = 0; i < stuManList.length; i++) {
                stuManList[i] = new MAN_NUM_LIST_INFO();
            }
            for (int i = 0; i < stuDetectRegion.length; i++) {
            	stuDetectRegion[i] = new NET_POINT_EX();
    		}
        }
    }

    public static class EM_ALARM_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ALARM_TYPE_UNKNOWN = 0;            // 未知类型
        public static final int   EM_ALARM_TYPE_CROWD_DENSITY = 1;      // 拥挤人群密度报警
        public static final int   EM_ALARM_TYPE_NUMBER_EXCEED = 2;      // 人数超限报警
        public static final int   EM_ALARM_TYPE_CROWD_DENSITY_AND_NUMBER_EXCEED = 3; // 拥挤人群密度报警和人数超限报警
    }

    // 全局拥挤人群密度列表(圆形)信息
    public static class NET_CROWD_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.NET_POINT stuCenterPoint = new NetSDKLibStructure.NET_POINT();     // 中心点坐标,8192坐标系
        public int              nRadiusNum;                           // 半径像素点个数
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    // 人数超限的报警区域ID列表信息
    public static class NET_REGION_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nRegionID;                            // 配置的区域下标
        public int              nPeopleNum;                           // 区域内人数统计值
        public byte[]           szName = new byte[32];                // 配置的名称
        public DH_POINT[]       stuDetectRegion = new DH_POINT[20];   // 配置的检测区域坐标
        public int              nDetectRegionNum;                     // 配置的检测区域坐标个数
        public byte[]           byReserved = new byte[908];           // 保留字节
    }

    // 全局拥挤人群密度列表(矩形)信息
    public static class NET_CROWD_RECT_LIST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.NET_POINT[]      stuRectPoint = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.RECT_POINT); // 矩形的左上角点与右下角点,8192坐标系，表示矩形的人群密度矩形框
        public byte[]           byReserved = new byte[32];            // 保留字节
    }

    // 事件类型 EVENT_IVS_CROWDDETECTION(人群密度检测事件）对应的数据块描述信息
    public static class DEV_EVENT_CROWD_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nEventID;                             // 事件ID
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventAction;                         // 事件动作,1表示持续性事件开始,2表示持续性事件结束;
        public int              emAlarmType;                          // 报警业务类型
        public byte[]           szName = new byte[NetSDKLibStructure.MAX_CROWD_DETECTION_NAME_LEN]; // 事件名称
        public int              nCrowdListNum;                        // 返回的全局拥挤人群密度列表个数 （圆形描述）
        public int              nRegionListNum;                       // 返回的人数超限的报警区域ID列表个数
        public NET_CROWD_LIST_INFO[] stuCrowdList = new NET_CROWD_LIST_INFO[NetSDKLibStructure.MAX_CROWD_LIST_NUM]; // 全局拥挤人群密度列表信息（圆形描述）
        public NET_REGION_LIST_INFO[] stuRegionList = new NET_REGION_LIST_INFO[NetSDKLibStructure.MAX_REGION_LIST_NUM]; // 人数超限的报警区域ID列表信息
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public int              nCrowdRectListNum;                    // 返回的全局拥挤人群密度列表个数 (矩形描述)
        public NET_CROWD_RECT_LIST_INFO[] stuCrowdRectList = new NET_CROWD_RECT_LIST_INFO[NetSDKLibStructure.MAX_CROWD_RECT_LIST]; // 全局拥挤人群密度列表信息(矩形描述)
        public int              nGlobalPeopleNum;                     // 检测区全局总人数
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[680];           // 保留扩展字节
    }

    // 人群密度检测事件(对应事件NET_ALARM_CROWD_DETECTION)
    public static class ALARM_CROWD_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nEventID;                             // 事件ID
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventAction;                         // 事件动作,1表示持续性事件开始,2表示持续性事件结束;
        public int              emAlarmType;                          // 报警业务类型
        public byte[]           szName = new byte[NetSDKLibStructure.MAX_CROWD_DETECTION_NAME_LEN]; // 事件名称
        public int              nCrowdListNum;                        // 返回的全局拥挤人群密度列表个数
        public int              nRegionListNum;                       // 返回的人数超限的报警区域ID列表个数
        public NET_CROWD_LIST_INFO[] stuCrowdList = new NET_CROWD_LIST_INFO[NetSDKLibStructure.MAX_CROWD_LIST_NUM]; // 全局拥挤人群密度列表信息
        public NET_REGION_LIST_INFO[] stuRegionList = new NET_REGION_LIST_INFO[NetSDKLibStructure.MAX_REGION_LIST_NUM]; // 人数超限的报警区域ID列表信息
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[896];           // 保留扩展字节
    }

    // 对象目标类型
    public static class EM_OBJECT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_OBJECT_TYPE_UNKNOWN = -1;          // 未知
        public static final int   EM_OBJECT_TYPE_FACE = 0;              // 目标
        public static final int   EM_OBJECT_TYPE_HUMAN = 1;             // 人体
        public static final int   EM_OBJECT_TYPE_VECHILE = 2;           // 机动车
        public static final int   EM_OBJECT_TYPE_NOMOTOR = 3;           // 非机动车
        public static final int   EM_OBJECT_TYPE_ALL = 4;               // 所有类型
    }

    // CLIENT_StartMultiFindFaceRecognition 接口输入参数
    public static class NET_IN_STARTMULTIFIND_FACERECONGNITION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Pointer          pChannelID;                           // 通道号
        public int              nChannelCount;                        //  通道申请个数
        public int              bPersonEnable;                        // 人员信息查询条件是否有效
        public NetSDKLibStructure.FACERECOGNITION_PERSON_INFO stPerson;                  // 人员信息查询条件
        public NetSDKLibStructure.NET_FACE_MATCH_OPTIONS stMatchOptions;                 // 人脸匹配选项
        public NetSDKLibStructure.NET_FACE_FILTER_CONDTION stFilterInfo;                 // 查询过滤条件
        // 图片二进制数据
        public Pointer          pBuffer;                              // 缓冲地址
        public int              nBufferLen;                           // 缓冲数据长度
        public int              bPersonExEnable;                      // 人员信息查询条件是否有效, 并使用人员信息扩展结构体
        public FACERECOGNITION_PERSON_INFOEX stPersonInfoEx;          // 人员信息扩展
        public int              emObjectType;                         // 搜索的目标类型,参考EM_OBJECT_TYPE
        public int              nChannelNum;                          // 通道有效个数
        public byte[]           szChannelString = new byte[512*32];   // 通道号(使用)
        public int              nProcessType;                         // 以图搜图类型, -1: 未知, 0: 特征值搜索, 1: SMD属性特征搜索
        public int              bIsUsingTaskID;                       // 是否使能订阅的TaskID字段
        public int              nTaskIDNum;                           // 订阅的TaskID数组有效个数
        public int[]            nTaskID = new int[128];               // 订阅的TaskID, bIsUsingTaskID为TRUE,nTaskIDNum为0表示订阅所有任务结果

        public NET_IN_STARTMULTIFIND_FACERECONGNITION() {
            this.dwSize = this.size();
        }
    }

    // CLIENT_StartMultiFindFaceRecognition 接口输出参数
    public static class NET_OUT_STARTMULTIFIND_FACERECONGNITION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nTotalCount;                          // 返回的符合查询条件的记录个数
        // -1表示总条数未生成,要推迟获取
        // 使用CLIENT_AttachFaceFindState接口状态
        public LLong            lFindHandle;                          // 查询句柄
        public int              nToken;                               // 获取到的查询令牌

        public NET_OUT_STARTMULTIFIND_FACERECONGNITION() {
            this.dwSize = this.size();
        }
    }

    // 开始目标检测/注册库的多通道查询
    public Boolean CLIENT_StartMultiFindFaceRecognition(LLong lLoginID,NET_IN_STARTMULTIFIND_FACERECONGNITION pstInParam,NET_OUT_STARTMULTIFIND_FACERECONGNITION pstOutParam,int nWaitTime);

    // 事件类型 EVENT_IVS_PEDESTRIAN_JUNCTION (行人卡口事件) 对应的数据块描述信息
    public static class DEV_EVENT_PEDESTRIAN_JUNCTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public int              nGroupID;                             // 事件组ID, 同一个人抓拍过程内nGroupID相同
        public int              nCountInGroup;                        // 一个事件组内的抓拍张数
        public int              nIndexInGroup;                        // 一个事件组内的抓拍序号，从1开始
        public double           PTS;                                  // 事件戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              UTCMS;                                // UTC时间对应的毫秒数
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.NET_MSG_OBJECT   stuObject;                            // 人脸信息
        public int              nLane;                                // 人行道号
        public int              nSequence;                            // 表示抓拍序号,如3/2/1,1表示抓拍结束,0表示异常结束
        public NetSDKLibStructure.VA_OBJECT_NONMOTOR stuNonMotor;                        // 非机动车信息
        public int              bHasNonMotor;                         // stuNonMotor 字段是否有效
        public NetSDKLibStructure.NET_MSG_OBJECT   stuVehicle;                           // 行人信息
        public Pointer          pstuSceneImage;                       //全景图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public int              nImageInfoNum;                        //图片信息个数
        public byte[]           szReserved1 = new byte[4];            //字节对齐
        public Pointer          pstuImageInfo;                        //图片信息数组,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IMAGE_INFO_EX3}
        public Pointer          pstuTrafficCar;                       //车辆信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO}
        public byte[]           byReserved = new byte[336-4*NetSDKLibStructure.POINTERSIZE]; // 保留字节
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //扩展协议字段,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EVENT_INFO_EXTEND}
    }

    // 多人多开门方式组合(对应 CFG_CMD_OPEN_DOOR_GROUP 命令),表示每个通道的组合信息，
    // 第一个通道的组合的开门优先级最高，后面依次递减
    public static class CFG_OPEN_DOOR_GROUP_INFO extends NetSDKLibStructure.SdkStructure {
        public int              nGroup;                               // 有效组合数
        public CFG_OPEN_DOOR_GROUP[] stuGroupInfo = new CFG_OPEN_DOOR_GROUP[NetSDKLibStructure.CFG_MAX_OPEN_DOOR_GROUP_NUM]; // 多人开门组合信息
        public int              nGroupMaxNum;                         // 组合总数，指明pGroupInfoEx实际内存空间大小，获取和下发均需要用户赋值,非0时nGroup、stuGroupInfo字段不生效
        public int              nGroupRetNum;                         // 实际有效的组合数，获取时由动态库赋值，下发时由用户赋值
        public Pointer          pGroupInfoEx;                         //多人开门组合信息扩展，内存有用户申请,指针对应CFG_OPEN_DOOR_GROUP数组
        public byte[]           szReserved = new byte[2048];          // 保留字节
    }

    // 多人组合开门组信息
    public static class CFG_OPEN_DOOR_GROUP extends NetSDKLibStructure.SdkStructure {
        public int              nUserCount;                           // 用户数目，表示需要组合才能开门的人数
        public int              nGroupNum;                            // 有效组数目
        public CFG_OPEN_DOOR_GROUP_DETAIL[] stuGroupDetail = new CFG_OPEN_DOOR_GROUP_DETAIL[NetSDKLibStructure.CFG_MAX_OPEN_DOOR_GROUP_DETAIL_NUM]; // 多人组合开门组的详细信息
        public Boolean          bGroupDetailEx;                       // TRUE: stuGroupDetail
        // 字段无效、pstuGroupDetailEx字段有效, FALSE:
        // stuGroupDetail
        // 字段有效、pstuGroupDetailEx字段无效
        public int              nMaxGroupDetailNum;                   // 多人组合开门组的详细信息最大个数
        public Pointer          pstuGroupDetailEx;                    /*
         * 多人组合开门组的详细信息扩展, 由用户申请内存,
         * 大小为sizeof(CFG_OPEN_DOOR_GROUP_DETAIL
         * )*nMaxUserCount, 当多人组合开门组的详细信息个数大于
         * CFG_MAX_OPEN_DOOR_GROUP_DETAIL_NUM
         * 时使用此字段
         */
    }

    // 多人组合开门组详细信息
    public static class CFG_OPEN_DOOR_GROUP_DETAIL extends NetSDKLibStructure.SdkStructure {
        public byte[]           szUserID = new byte[NetSDKLibStructure.CFG_MAX_USER_ID_LEN]; // 用户ID
        public int              emMethod;                             // 开门方式
        public int              nMethodExNum;                         // 开门方式扩展个数
        public int[]            emMethodEx = new int[NetSDKLibStructure.CFG_MAX_METHODEX_NUM]; // 开门方式扩展
        public int              emCombineMethod;                      //多人开门支持任意组合开门方式,参见枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.CFG_DOOR_OPEN_METHOD}
        public byte[]           szReserved = new byte[1020];          //预留字段
    }

    public static class EM_CFG_OPEN_DOOR_GROUP_METHOD extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_UNKNOWN = 0;
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_CARD = 1; // 刷卡
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_PWD = 2; // 密码
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_FINGERPRINT = 3; // 信息
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_ANY = 4; // 任意组合方式开门
        public static final int   EM_CFG_OPEN_DOOR_GROUP_METHOD_FACE = 5; // 人脸
    }

    // 开始查找X光机包裹信息
    public  LLong CLIENT_StartFindXRayPkg(LLong lLoginID,NET_IN_START_FIND_XRAY_PKG pInParam,NET_OUT_START_FIND_XRAY_PIC pOutParam,int nWaitTime);

    // 查询X光机包裹的信息
    public  Boolean CLIENT_DoFindXRayPkg(LLong lFindID,NET_IN_DO_FIND_XRAY_PKG pInParam,NET_OUT_DO_FIND_XRAY_PKG pOutParam,int nWaitTime);

    // 结束查询X光机包裹的信息
    public  Boolean CLIENT_StopFindXRayPkg(LLong lFindID);

    // 物品类型
    public static class EM_INSIDE_OBJECT_TYPE 
    {
        public static final int   EM_INSIDE_OBJECT_UNKNOWN = 0;         // 算法未识别物品
        public static final int   EM_INSIDE_OBJECT_KNIFE = 1;           // 刀具
        public static final int   EM_INSIDE_OBJECT_BOTTLELIQUID = 2;    // 瓶装液体
        public static final int   EM_INSIDE_OBJECT_GUN = 3;             // 枪支
        public static final int   EM_INSIDE_OBJECT_UMBRELLA = 4;        // 雨伞
        public static final int   EM_INSIDE_OBJECT_PHONE = 5;           // 手机
        public static final int   EM_INSIDE_OBJECT_NOTEBOOK = 6;        // 笔记本
        public static final int   EM_INSIDE_OBJECT_POWERBANK = 7;       // 充电宝
        public static final int   EM_INSIDE_OBJECT_SHOES = 8;           // 鞋子
        public static final int   EM_INSIDE_OBJECT_ROD = 9;             // 杠子
        public static final int   EM_INSIDE_OBJECT_METAL = 10;          // 金属
        public static final int   EM_INSIDE_OBJECT_EXPLOSIVE = 11;      // 爆炸物
        public static final int   EM_INSIDE_OBJECT_CONTAINERSPRAY = 12; // 喷雾喷灌
        public static final int   EM_INSIDE_OBJECT_EXPLOSIVE_FIREWORKS = 13; // 烟花爆竹
        public static final int   EM_INSIDE_OBJECT_LIGHTER = 14;        // 打火机
        public static final int   EM_INSIDE_OBJECT_STICK = 15;          // 警棍
        public static final int   EM_INSIDE_OBJECT_BRASSKNUCKLE = 16;   // 指虎
        public static final int   EM_INSIDE_OBJECT_HANDCUFFS = 17;      // 手铐
        public static final int   EM_INSIDE_OBJECT_IVORY = 18;          // 象牙
        public static final int   EM_INSIDE_OBJECT_BOOK = 19;           // 书籍
        public static final int   EM_INSIDE_OBJECT_CD = 20;             // 光盘
        public static final int   EM_INSIDE_OBJECT_HAMMERS = 21;        // 锤子
        public static final int   EM_INSIDE_OBJECT_PLIERS = 22;         // 钳子
        public static final int   EM_INSIDE_OBJECT_AXE = 23;            // 斧头
        public static final int   EM_INSIDE_OBJECT_SCREW_DRIVER = 24;   // 螺丝刀
        public static final int   EM_INSIDE_OBJECT_WRENCH = 25;         // 扳手
        public static final int   EM_INSIDE_OBJECT_ELECTRIC_SHOCK_STICK = 26; // 电击棍
        public static final int   EM_INSIDE_OBJECT_THERMOS = 27;        // 保温杯
        public static final int   EM_INSIDE_OBJECT_GLASS_BOTTLES = 28;  // 玻璃杯
        public static final int   EM_INSIDE_OBJECT_PLASTIC_BOTTLE = 29; // 塑料瓶
        public static final int   EM_INSIDE_OBJECT_IGNITION_OIL = 30;   // 打火机油
        public static final int   EM_INSIDE_OBJECT_NAIL_POLISH = 31;    // 指甲油
        public static final int   EM_INSIDE_OBJECT_BLUNT_INSTRUMENT = 32; // 工具
        public static final int   EM_INSIDE_OBJECT_SCISSORS = 33;       // 剪刀
        public static final int   EM_INSIDE_OBJECT_ELECTRONIC = 34;     // 电子产品
        public static final int   EM_INSIDE_OBJECT_PISTOL = 35;         //	手枪
        public static final int   EM_INSIDE_OBJECT_FOLDINGKNIFE = 36;   //	折叠刀
        public static final int   EM_INSIDE_OBJECT_SHARPKNIFE = 37;     //	尖刀
        public static final int   EM_INSIDE_OBJECT_KITCHENKNIFE = 38;   //	菜刀
        public static final int   EM_INSIDE_OBJECT_UTILITYKNIFE = 39;   //	美工刀
        public static final int   EM_INSIDE_OBJECT_FIREWORKS = 40;      //	烟花
        public static final int   EM_INSIDE_OBJECT_FIRECRACKER = 41;    //	爆竹
        public static final int   EM_INSIDE_OBJECT_POWDER = 42;         //	粉末
        public static final int   EM_INSIDE_OBJECT_IMPENETERABLE_MATERALS = 43; //难穿透物品
        public static final int   EM_INSIDE_OBJECT_CIGARETTE = 44;      //香烟
        public static final int   EM_INSIDE_OBJECT_BATTERY = 45;        //电池
        public static final int   EM_INSIDE_OBJECT_GUNPARTS = 46;       //零部件
        public static final int   EM_INSIDE_OBJECT_MATCH = 47;          //火柴
        public static final int   EM_INSIDE_OBJECT_GUNGRIP = 48;        //握把
        public static final int   EM_INSIDE_OBJECT_GUNMAGAZINE = 49;    //弹夹
        public static final int   EM_INSIDE_OBJECT_GUNSLEEVE = 50;      //套筒
        public static final int   EM_INSIDE_OBJECT_GUNBARREL = 51;      //枪管
        public static final int   EM_INSIDE_OBJECT_BULLET = 52;         //子弹
        public static final int   EM_INSIDE_OBJECT_GRENADE = 53;        //手雷
        public static final int   EM_INSIDE_OBJECT_CERAMICSHEET = 54;   //陶瓷片
        public static final int   EM_INSIDE_OBJECT_GLASSSHEET = 55;     //玻璃片
        public static final int   EM_INSIDE_OBJECT_IPADBASE = 56;       //IPAD底壳
        public static final int   EM_INSIDE_OBJECT_SLINGSHOT = 57;      //弹弓
        public static final int   EM_INSIDE_OBJECT_DRUG = 58;           //毒品
        public static final int   EM_INSIDE_OBJECT_EXPLOSIVEPACKAGE = 59; //炸药包
        public static final int   EM_INSIDE_OBJECT_CELLBATTERY = 60;    //纽扣电池
        public static final int   EM_INSIDE_OBJECT_LEADBATTERY = 61;    //铅蓄电池
        public static final int   EM_INSIDE_OBJECT_METALLIGHTER = 62;   //金属打火机
        public static final int   EM_INSIDE_OBJECT_COSMETICBOTTLE = 63; //化妆瓶
        public static final int   EM_INSIDE_OBJECT_CONTAINERCAN = 64;   //易拉罐
        public static final int   EM_INSIDE_OBJECT_AIRBOTTLE = 65;      //气罐
        public static final int   EM_INSIDE_OBJECT_SQUAREKNIFE = 66;    //方刀
        public static final int   EM_INSIDE_OBJECT_WALKIETALKIE = 67;   ///// 对讲机
        public static final int   EM_INSIDE_OBJECT_ROUTER = 68;         ///// 路由器
        public static final int   EM_INSIDE_OBJECT_MICROPHONE = 69;     ///// 话筒
        public static final int   EM_INSIDE_OBJECT_UNMANED_AERIAL_VEHICLE = 70; ///// 无人机
        public static final int   EM_INSIDE_OBJECT_ELECTRICAL_RELAY = 71; ///// 继电器
        public static final int   EM_INSIDE_OBJECT_DETONATOR = 72;      ///// 雷管
        public static final int   EM_INSIDE_OBJECT_BLASTINGFUSE = 73;   ///// 导火索
        public static final int   EM_INSIDE_OBJECT_EXPLOSIVEFLUID = 74; ///// 流体爆炸物
        public static final int   EM_INSIDE_OBJECT_NAILGUN = 75;        ///// 炮钉枪
        public static final int   EM_INSIDE_OBJECT_NAIL = 76;           ///// 炮钉
        public static final int   EM_INSIDE_OBJECT_DRYBATTERY = 77;     ///// 干电池
        public static final int   EM_INSIDE_OBJECT_LITHIUMBATTERY = 78; ///// 锂电池
        public static final int   EM_INSIDE_OBJECT_SAW = 79;            ///// 锯子
        public static final int   EM_INSIDE_OBJECT_TABLEKNIFE = 80;     ///// 餐刀
        public static final int   EM_INSIDE_OBJECT_PLASTICUTTER = 81;   ///// 勾刀
        public static final int   EM_INSIDE_OBJECT_BOLT = 82;           ///// 弩箭
        public static final int   EM_INSIDE_OBJECT_CROSSBOW = 83;       ///// 弩
        public static final int   EM_INSIDE_OBJECT_SHAVER = 84;         ///// 剃须刀
        public static final int   EM_INSIDE_OBJECT_ELECTRIC_TOOTHBRUSH = 85; ///// 电动牙刷
        public static final int   EM_INSIDE_OBJECT_OILDRUM = 86;        ///// 油桶
        public static final int   EM_INSIDE_OBJECT_SAFELIQUID = 87;     // 安全液体
        public static final int   EM_INSIDE_OBJECT_SUSPECTEDLIQUID = 88; // 可疑液体
        public static final int   EM_INSIDE_OBJECT_DANGEROUSLIQUID = 89; // 危险液体
        public static final int   EM_INSIDE_OBJECT_BULLETPROOFVEST = 90; // 防弹衣
        public static final int   EM_INSIDE_OBJECT_CONTROLLEDKNIFE = 91; // 受控刀具
        public static final int   EM_INSIDE_OBJECT_BOXEDBEVERAGE = 92;  // 盒装液体
        public static final int   EM_INSIDE_OBJECT_BAGGEDBEVERAGE = 93; // 袋装液体
        public static final int   EM_INSIDE_OBJECT_GLASSGLUE = 94;      // 玻璃胶
        public static final int   EM_INSIDE_OBJECT_TOILETWATER = 95;    // 花露水
        public static final int   EM_INSIDE_OBJECT_WINEBOTTLE = 96;     // 酒瓶
    }

    // 危险等级
    public static class EM_DANGER_GRADE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DANGER_GRADE_UNKNOWN = -1;         // 未知
        public static final int   EM_DANGER_GRADE_NORMAL = 0;           // 普通级别
        public static final int   EM_DANGER_GRADE_WARN = 1;             // 警示级别
        public static final int   EM_DANGER_GRADE_DANGER = 2;           // 危险级别
    }

    // CLIENT_StartFindXRayPkg 接口输入参数
    public static class NET_IN_START_FIND_XRAY_PKG extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emTimeOrder;                          // 查询结果按时间排序
        public NET_TIME         stuStartTime;                         // 查询的开始时间
        public NET_TIME         stuEndTime;                           // 查询的结束时间
        public int[]            nSimilarityRange = new int[2];        // 相似度范围,下标0:表示最小值, 下标1:表示最大值
        public int              nObjTypeNum;                          // 物体类型的数量
        public int[]            emObjType = new int[32];              // 物品类型,参考枚举EM_INSIDE_OBJECT_TYPE
        public int              nObjTypeCount;                        // 自定义物体类型的数量
        public NET_XRAY_INSIDE_ONJECT_TYPE[] stuObjType = (NET_XRAY_INSIDE_ONJECT_TYPE[]) new NET_XRAY_INSIDE_ONJECT_TYPE().toArray(32); // 自定义物品类型

        public NET_IN_START_FIND_XRAY_PKG()
        {
            this.dwSize = this.size();
        }
    }

    // X光机物体信息
    public static class NET_PKG_OBJECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emObjType;                            // 物品类型
        public int              emDangerGrade;                        // 物品危险等级
        public int              nSimilarity;                          // 相似度,0~100
        public byte[]           szObjectType = new byte[32];          //自定义物品类型，emObjType为 EM_INSIDE_OBJECT_UNKNOWN 时使用
        public int              nCommonObjectType;                    //物品属性： 0：未知 16：无液体 17：含液体 18：遮挡 32：安全 33：警示 34：高危
        public int              nCommonGrade;                         //物品属性等级 -1:未知 0: 普通级别 1: 警示级别 2: 危险级别
        public byte[]           byReserved = new byte[92];            // 保留字节
    }

    // CLIENT_StartFindXRayPkg 接口输出参数
    public static class NET_OUT_START_FIND_XRAY_PIC extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTotal;                               // 包裹总数

        public NET_OUT_START_FIND_XRAY_PIC()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_DoFindXRayPkg 接口输入参数
    public static class NET_IN_DO_FIND_XRAY_PKG extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nOffset;                              // 查询偏移
        public int              nCount;                               // 需要查找的数目

        public NET_IN_DO_FIND_XRAY_PKG()
        {
            this.dwSize = this.size();
        }
    }

    // 视角信息数
    public static class NET_PKG_VIEW_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emViewType;                           // 视图类型
        public int              nEnergyImageLength;                   // 能量图大小 单位字节
        public byte[]           szEnergyImagePath = new byte[128];    // 能量图绝对路径
        public int              nColorImageLength;                    // 彩图大小单位字节
        public byte[]           szColorImagePath = new byte[128];     // 彩图绝对路径
        public int              nColorOverlayImageLength;             // 彩图叠加图大小单位字节
        public byte[]           szColorOverlayImagePath = new byte[128]; // 彩图叠加图绝对路径
        public NET_PKG_OBJECT_INFO[] stuObject = new NET_PKG_OBJECT_INFO[32]; // 物体数组
        public int              nObjectCount;                         // 物体数量
        public int              nVisibleLightLength;                  //可见光图片大小, 单位字节
        public byte[]           szVisibleLightPath = new byte[128];   //可见光图片绝对路径
        public byte[]           byReserved = new byte[892];           // 保留字节
    }

    // X光机的包裹信息
    public static class NET_XRAY_PKG_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_TIME         stuTime;                              // 包裹产生时间(含时区)
        public int              nChannelIn;                           // 关联的进口IPC通道号,从0开始,-1表示无效
        public int              nChannelOut;                          // 关联的出口IPC通道号,从0开始,-1表示无效
        public byte[]           szUser = new byte[128];               // 用户名
        public NET_PKG_VIEW_INFO[] stuViewInfo = new NET_PKG_VIEW_INFO[2]; // 视角信息数组
        public byte[]           byReserved = new byte[1024];          // 保留字节

        public NET_XRAY_PKG_INFO() {
            for (int i = 0; i < stuViewInfo.length; i ++) {
                stuViewInfo[i] = new NET_PKG_VIEW_INFO();
            }
        }
    }

    // CLIENT_DoFindXRayPkg 接口输出参数
    public static class NET_OUT_DO_FIND_XRAY_PKG extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nMaxCount;                            // 用户指定分配结构体个数,需大于等于NET_IN_DO_FIND_XRAY_PKG的nCount
        public int              nRetCount;                            // 实际返回的查询数量
        public Pointer          pstuXRayPkgInfo;                      // X光机的包裹信息,缓存大小由用户指定

        public NET_OUT_DO_FIND_XRAY_PKG()
        {
            this.dwSize = this.size();
        }
    }

    //事件类型	EVENT_IVS_TRAFFIC_PARKINGSPACEPARKING(车位有车事件)对应的规则配置
    public static class CFG_TRAFFIC_PARKINGSPACEPARKING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szRuleName = new byte[NetSDKLibStructure.MAX_NAME_LEN];  // 规则名称,不同规则不能重名
        public byte             bRuleEnable;                          // 规则使能
        public byte[]           bReserved = new byte[3];              // 保留字段
        public int              nObjectTypeNum;                       // 相应物体类型个数
        public byte[]           szObjectTypes = new byte[NetSDKLibStructure.MAX_OBJECT_LIST_SIZE*NetSDKLibStructure.MAX_NAME_LEN]; // 相应物体类型列表
        public int              nPtzPresetId;                         // 云台预置点编号	0~65535
        public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
        // public CFG_TIME_SECTION[]	stuTimeSection=new CFG_TIME_SECTION[WEEK_DAY_NUM*MAX_REC_TSECT_EX];			// 事件响应时间段
        public NetSDKLibStructure.CFG_TIME_SECTION[] stuTimeSection = (NetSDKLibStructure.CFG_TIME_SECTION[])new NetSDKLibStructure.CFG_TIME_SECTION().toArray(NetSDKLibStructure.WEEK_DAY_NUM*NetSDKLibStructure.MAX_REC_TSECT_EX); // 事件响应时间段
        public int              nLane;                                // 车位号
        public int              nDelayTime;                           // 检测到报警发生到开始上报的时间, 单位：秒，范围1~65535
        public int              nDetectRegionPoint;                   // 检测区域顶点数
        public NetSDKLibStructure.CFG_POLYGON[]    stuDetectRegion = (NetSDKLibStructure.CFG_POLYGON[]) new NetSDKLibStructure.CFG_POLYGON().toArray(NetSDKLibStructure.MAX_POLYGON_NUM); // 检测区域
        public int              nPlateSensitivity;                    // 有牌检测灵敏度(控制抓拍)
        public int              nNoPlateSensitivity;                  // 无牌检测灵敏度（控制抓拍）
        public int              nLightPlateSensitivity;               // 有牌检测灵敏度（控制车位状态灯）
        public int              nLightNoPlateSensitivity;             // 无牌检测灵敏度（控制车位状态灯）
        public int              bForbidParkingEnable;                 // 禁止停车使能 TRUE:禁止 FALSE:未禁止
    }

    //事件类型	EVENT_IVS_TRAFFIC_PARKINGSPACENOPARKING(车位无车事件)对应的规则配置
    public static class CFG_TRAFFIC_PARKINGSPACENOPARKING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szRuleName = new byte[NetSDKLibStructure.MAX_NAME_LEN];  // 规则名称,不同规则不能重名
        public byte             bRuleEnable;                          // 规则使能
        public byte[]           bReserved = new byte[3];              // 保留字段
        public int              nObjectTypeNum;                       // 相应物体类型个数
        public byte[]           szObjectTypes = new byte[NetSDKLibStructure.MAX_OBJECT_LIST_SIZE*NetSDKLibStructure.MAX_NAME_LEN]; // 相应物体类型列表
        public int              nPtzPresetId;                         // 云台预置点编号	0~65535
        public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
        public NetSDKLibStructure.CFG_TIME_SECTION[] stuTimeSection = (NetSDKLibStructure.CFG_TIME_SECTION[])new NetSDKLibStructure.CFG_TIME_SECTION().toArray(NetSDKLibStructure.WEEK_DAY_NUM*NetSDKLibStructure.MAX_REC_TSECT_EX); // 事件响应时间段
        public int              nLane;                                // 车位号
        public int              nDelayTime;                           // 检测到报警发生到开始上报的时间, 单位：秒，范围1~65535
        public int              nDetectRegionPoint;                   // 检测区域顶点数
        public NetSDKLibStructure.CFG_POLYGON[]    stuDetectRegion = (NetSDKLibStructure.CFG_POLYGON[]) new NetSDKLibStructure.CFG_POLYGON().toArray(NetSDKLibStructure.MAX_POLYGON_NUM); // 检测区域
        public int              nPlateSensitivity;                    // 有牌检测灵敏度(控制抓拍)
        public int              nNoPlateSensitivity;                  // 无牌检测灵敏度（控制抓拍）
        public int              nLightPlateSensitivity;               // 有牌检测灵敏度（控制车位状态灯）
        public int              nLightNoPlateSensitivity;             // 无牌检测灵敏度（控制车位状态灯）
    }

    // 事件类型 EVENT_IVS_CITY_MOTORPARKING (城市机动车违停事件) 对应的数据块描述信息
    public static class DEV_EVENT_CITY_MOTORPARKING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC = new NET_TIME_EX();              // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo = new NetSDKLibStructure.NET_EVENT_FILE_INFO(); // 事件对应文件信息
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.NET_MAX_OBJECT_NUM]; // 检测到的物体
        public int              nDetectRegionNum;                     // 检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 检测区域
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo = new NetSDKLibStructure.EVENT_INTELLI_COMM_INFO(); // 智能事件公共信息
        public int              nParkingDuration;                     // 违停持续时长，单位：秒 缺省值0表示无意义
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              bPtzPosition;                         // stuPtzPosition 是否有效
        public PTZ_NORMALIZED_POSITION_UNIT stuPtzPosition = new PTZ_NORMALIZED_POSITION_UNIT(); // 云台信息
        public int              emMotorStatus;                        // 车辆状态，{@link EM_CITYMOTOR_STATUS}
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO(); // 全景广角图信息
        public int              emPreAlarm;                           // 是否为违规预警图片(预警触发后一定时间，违规物体还没有离开，才判定为违规)，参考EM_PREALARM
        public Pointer          pstuImageInfo;                        //图片信息数组,NET_IMAGE_INFO_EX2的数组
        public int              nImageInfoNum;                        //图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte             byVehicleHeadDirection;               // 车头朝向 0-未知,1-正面,2-侧面,3-背面
        public byte[]           szReversed = new byte[3];             //预留字节
        public int              nDetectRegionNumber;                  //检测区编号
        public byte[]           szDetectRegionName = new byte[128];   //检测区名称
        public byte[]           byReserved = new byte[876];           //预留字节

        public DEV_EVENT_CITY_MOTORPARKING_INFO() {
            for (int i = 0; i < stuObjects.length; i++) {
                stuObjects[i] = new NetSDKLibStructure.NET_MSG_OBJECT();
            }
            for (int i = 0; i < DetectRegion.length; i++) {
                DetectRegion[i] = new NetSDKLibStructure.NET_POINT();
            }
        }
    }

    // 事件类型 EVENT_IVS_CITY_NONMOTORPARKING (城市非机动车违停事件) 对应的数据块描述信息
    public static class DEV_EVENT_CITY_NONMOTORPARKING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nDetectRegionNum;                     // 检测区域顶点数
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 检测区域
        public int              nAlarmNum;                            // 报警阈值
        public int              nNoMotorNum;                          // 非机动车的个数
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public int              nViolationDuration;                   // 违法持续时长，单位：秒 缺省值0表示无意义
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM]; // 检测到的物体
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage;                        // 全景广角图
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[892];           // 保留字节
    }

    // 事件类型 EVENT_IVS_HOLD_UMBRELLA (违规撑伞检测事件) 对应的数据块描述信息
    public static class DEV_EVENT_HOLD_UMBRELLA_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM]; // 检测到的物体
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public int              nViolationDuration;                   // 违法持续时长，单位：秒 缺省值0表示无意义
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public byte[]           bReserved = new byte[4092];           // 保留字节,留待扩展.
    }

    // 事件类型 EVENT_IVS_GARBAGE_EXPOSURE (垃圾暴露检测事件) 对应的数据块描述信息
    public static class DEV_EVENT_GARBAGE_EXPOSURE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM]; // 检测到的物体
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public int              nViolationDuration;                   // 违法持续时长，单位：秒 缺省值0表示无意义
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public Pointer          pstuImageInfo;                        // 图片信息数组,指针对应NET_IMAGE_INFO_EX2的数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); // 事件公共扩展字段结构体
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX(); //全景图图片信息,事件前2~5s抓图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public Pointer          pstuMosaicImage;                      //合成图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public int              nMosaicImageNum;                      //合成图个数
        public Pointer          pstuAdvanceImage;                     //事件发生前抓图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure.SCENE_IMAGE_INFO_EX}
        public int              nAdvanceImageNum;                     //事件发生前抓图个数
        public int              nVehicleSpeed;                        //车速, 单位km/h
        public double           dbHeadingAngle;                       //航向角,以正北方向为基准输出车辆运动方向同正北方向的角度,范围 0~360,顺时针正,单位为度
        public double[]         dbLongitude = new double[3];          //经度,格式:度,分,秒(秒为浮点数)
        public double[]         dbLatitude = new double[3];           //纬度,格式:度,分,秒(秒为浮点数)
        public byte[]           bReserved = new byte[2160-NetSDKLibStructure.POINTERSIZE*2]; // 保留字节,留待扩展.
    }

    // 事件类型 EVENT_IVS_DUSTBIN_OVER_FLOW (垃圾桶满溢检测事件) 对应的数据块描述信息
    public static class DEV_EVENT_DUSTBIN_OVER_FLOW_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM]; // 检测到的物体
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public int              nViolationDuration;                   // 违法持续时长，单位：秒 缺省值0表示无意义
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public Pointer          pstuImageInfo;                        // 图片信息数组,指针对应NET_IMAGE_INFO_EX2数组
        public int              nImageInfoNum;                        // 图片信息个数
        public int              nRuleId;                              // 规则编号
        public byte[]           szRuleName = new byte[128];           // 规则名称
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); // 事件公共扩展字段结构体
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX(); // 全景图图片信息,事件前2~5s抓图
        public Pointer          pstuMosaicImage;                      // 合成图,指针对应SCENE_IMAGE_INFO_EX数组
        public int              nMosaicImageNum;                      // 合成图个数
        public Pointer          pstuAdvanceImage;                     // 事件发生前抓图，指针对应SCENE_IMAGE_INFO_EX数组
        public int              nAdvanceImageNum;                     // 事件发生前抓图个数
        public byte[]           bReserved = new byte[2088-NetSDKLibStructure.POINTERSIZE*2]; // 保留字节,留待扩展.
    }

    // 事件类型 EVENT_IVS_DOOR_FRONT_DIRTY (门前脏乱检测事件) 对应的数据块描述信息
    public static class DEV_EVENT_DOOR_FRONT_DIRTY_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public NetSDKLibStructure.NET_POINT[]      DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 规则检测区域
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public NetSDKLibStructure.NET_MSG_OBJECT[] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[NetSDKLibStructure.HDBJ_MAX_OBJECTS_NUM]; // 检测到的物体
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szPresetName = new byte[64];          // 事件触发的预置名称
        public byte[]           szShopAddress = new byte[256];        // 商铺地址名称
        public int              nViolationDuration;                   // 违法持续时长，单位：秒，缺省值0表示无意义
        public byte[]           szSourceID = new byte[32];            // 事件关联ID,同一个物体或图片生成多个事件时SourceID相同
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public byte[]           bReserved = new byte[4092];           // 保留字节,留待扩展.
    }

    // CLIENT_StartRemoteUpgrade-输入参数
    public static class NET_IN_START_REMOTE_UPGRADE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 此结构体大小
        public int              nListNum;                             // 需要升级的远程通道个数
        public Pointer          pstuList;                             // 需要升级的远程通道信息
        public Pointer          pReserved;                            // 字节对齐
        public byte[]           szFileName = new byte[256];           // 升级文件名称
        public Callback         cbRemoteUpgrade;                      // 升级进度回调函数
        public Pointer          dwUser;                               // 用户数据
        public int              nPacketSize;                          // 每次分包发送大小，为0默认为16K

        public NET_IN_START_REMOTE_UPGRADE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // CLIENT_StartRemoteUpgrade-输出参数
    public static class NET_OUT_START_REMOTE_UPGRADE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_START_REMOTE_UPGRADE_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_AttachRemoteUpgradeState-输入参数
    public static class NET_IN_ATTACH_REMOTEUPGRADE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 此结构体大小
        public Callback         cbCallback;                           // 回调
        public Pointer          dwUser;                               // 用户数据

        public NET_IN_ATTACH_REMOTEUPGRADE_STATE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_AttachRemoteUpgradeState-输出参数
    public static class NET_OUT_ATTACH_REMOTEUPGRADE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 此结构体大小

        public NET_OUT_ATTACH_REMOTEUPGRADE_STATE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 远程通道信息
    public static class NET_REMOTE_UPGRADE_CHNL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannel;                             // 远程通道号
        public byte[]           byReserved = new byte[512];           // 预留字段
    }

    // 升级远程设备程序回调函数
    public interface fRemoteUpgradeCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lUpgradeID,int emState,int nParam1,int nParam2,Pointer dwUser);
    }

    // 远程升级回调类型
    public static class EM_REMOTE_UPGRADE_CB_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_REMOTE_UPGRADE_CB_TYPE_APPENDING = 0; // 推送回调	nParam1 文件总大小 nParam2 已发送大小
        public static final int   EM_REMOTE_UPGRADE_CB_TYPE_EXECUTE = 1; // 执行回调	nParam1 执行execute的结果
        public static final int   EM_REMOTE_UPGRADE_CB_TYPE_FAILED = 2; // 失败回调  nParam1 错误码
        public static final int   EM_REMOTE_UPGRADE_CB_TYPE_CANCEL = 3; // 取消回调
    }

    // 升级状态回调函数
    public interface fRemoteUpgraderStateCallback extends Callback {
        public void invoke(LLong lLoginId,LLong lAttachHandle,NET_REMOTE_UPGRADER_NOTIFY_INFO pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    // 远程设备升级消息上报
    public static class NET_REMOTE_UPGRADER_NOTIFY_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nStateNum;                            // 状态数
        public Pointer          pstuStates;                           // 状态列表
        public byte[]           byReserved = new byte[1024];          // 预留
    }

    // 远程设备升级状态
    public static class NET_REMOTE_UPGRADER_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannel;                             // 通道号
        public int              emState;                              // 状态(对应的枚举值EM_REMOTE_UPGRADE_STATE)
        public int              nProgress;                            // 进度
        public byte[]           szDeviceID = new byte[128];           // 远程设备ID
    }

    // 远程设备升级状态
    public static class EM_REMOTE_UPGRADE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_REMOTE_UPGRADE_STATE_UNKNOWN = 0;  // 未知
        public static final int   EM_REMOTE_UPGRADE_STATE_INIT = 1;     // 初始状态(未升级)
        public static final int   EM_REMOTE_UPGRADE_STATE_DOWNLOADING = 2; // 正在下载数据
        public static final int   EM_REMOTE_UPGRADE_STATE_UPGRADING = 3; // 正在升级
        public static final int   EM_REMOTE_UPGRADE_STATE_FAILED = 4;   // 升级失败
        public static final int   EM_REMOTE_UPGRADE_STATE_SUCCEEDED = 5; // 升级成功
        public static final int   EM_REMOTE_UPGRADE_STATE_CANCELLED = 6; // 取消升级
        public static final int   EM_REMOTE_UPGRADE_STATE_PREPARING = 7; // 准备升级中
    }

    //开始升级远程设备程序
    public LLong CLIENT_StartRemoteUpgrade(LLong lLoginID,NET_IN_START_REMOTE_UPGRADE_INFO pInParam,NET_OUT_START_REMOTE_UPGRADE_INFO pOutParam,int nWaitTime);

    //结束升级远程设备程序
    public Boolean CLIENT_StopRemoteUpgrade(LLong lUpgradeID);

    // 订阅ipc升级状态观察接口
    public LLong CLIENT_AttachRemoteUpgradeState(LLong lLoginID,NET_IN_ATTACH_REMOTEUPGRADE_STATE pInParam,NET_OUT_ATTACH_REMOTEUPGRADE_STATE pOutParam,int nWaitTime);

    // 取消订阅升级状态接口
    public Boolean CLIENT_DetachRemoteUpgradeState(LLong lAttachHandle);

    // 设置子连接网络参数, pSubConnectNetParam 资源由用户申请和释放
    public Boolean CLIENT_SetSubConnectNetworkParam(LLong lLoginID,NET_SUBCONNECT_NETPARAM pSubConnectNetParam);

    // 设置子链接网络参数
    public static class NET_SUBCONNECT_NETPARAM extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nNetPort;                             // 网络映射端口号
        public byte[]           szNetIP = new byte[NetSDKLibStructure.NET_MAX_IPADDR_EX_LEN]; // 网络映射IP地址

        public NET_SUBCONNECT_NETPARAM()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    public static class EM_ANALYSE_TASK_START_RULE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ANALYSE_TASK_START_NOW = 0;        // 立刻启动
        public static final int   EM_ANALYSE_TASK_START_LATER = 1;      // 稍候手动启动
    }

    // 视频分析支持的对象类型
    public static class EM_ANALYSE_OBJECT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ANALYSE_OBJECT_TYPE_UNKNOWN = 0;   // 未知的
        public static final int   EM_ANALYSE_OBJECT_TYPE_HUMAN = 1;     // 人
        public static final int   EM_ANALYSE_OBJECT_TYPE_VEHICLE = 2;   // 车辆
        public static final int   EM_ANALYSE_OBJECT_TYPE_FIRE = 3;      // 火
        public static final int   EM_ANALYSE_OBJECT_TYPE_SMOKE = 4;     // 烟雾
        public static final int   EM_ANALYSE_OBJECT_TYPE_PLATE = 5;     // 片状物体
        public static final int   EM_ANALYSE_OBJECT_TYPE_HUMANFACE = 6; // 人脸
        public static final int   EM_ANALYSE_OBJECT_TYPE_CONTAINER = 7; // 货柜
        public static final int   EM_ANALYSE_OBJECT_TYPE_ANIMAL = 8;    // 动物
        public static final int   EM_ANALYSE_OBJECT_TYPE_TRAFFICLIGHT = 9; // 红绿灯
        public static final int   EM_ANALYSE_OBJECT_TYPE_PASTEPAPER = 10; // 贴纸 贴片
        public static final int   EM_ANALYSE_OBJECT_TYPE_HUMANHEAD = 11; // 人的头部
        public static final int   EM_ANALYSE_OBJECT_TYPE_ENTITY = 12;   // 普通物体
        public static final int   EM_ANALYSE_OBJECT_TYPE_PACKAGE = 13;  ///// 包裹  
        public static final int   EM_ANALYSE_OBJECT_TYPE_SCRAPSTEEL_DANGER = 14; /////废钢危险品
    }

    // 事件类型 EVENT_IVS_SMART_KITCHEN_CLOTHES_DETECTION(智慧厨房穿着检测事件)对应的数据块描述信息
    public static class DEV_EVENT_SMART_KITCHEN_CLOTHES_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nRuleID;                              // 用于
        public int              emClassType;                          // 智能事件所属大类
        public byte[]           szClassAlias = new byte[16];          // 智能事件所属大类别名
        public NetSDKLibStructure.HUMAN_IMAGE_INFO stuHumanImage;                        // 人体图片信息
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage;                        // 全景广角图片信息
        public NetSDKLibStructure.FACE_IMAGE_INFO  stuFaceImage;                         // 人脸图片信息
        public int              nObjectID;                            // 目标ID
        public int              emHasMask;                            // 检测是否有戴口罩（对应枚举值EM_NONMOTOR_OBJECT_STATUS）
        public int              emHasChefHat;                         // 检测是否有戴厨师帽（对应枚举值EM_NONMOTOR_OBJECT_STATUS）
        public int              emHasChefClothes;                     // 检测是否有穿厨师服（对应枚举值EM_NONMOTOR_OBJECT_STATUS）
        public int              emChefClothesColor;                   // 厨师服颜色（对应枚举值EM_OBJECT_COLOR_TYPE）
        public NET_IMAGE_INFO_EX2 stuImageInfo[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        //图片信息个数
        public byte             bReserved[] = new byte[1024];         //预留字节
    }

    // 事件类型EVENT_IVS_BANNER_DETECTION(拉横幅事件)对应数据块描述信息
    public static class DEV_EVENT_BANNER_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nEventID;                             // 事件ID
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nAction;                              // 1:开始 2:停止
        public int              emClassType;                          // 智能事件所属大类(对应EM_CLASS_TYPE枚举)
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nObjectNum;                           // 检测到的物体个数
        public NetSDKLibStructure.NET_MSG_OBJECT [] stuObjects = new NetSDKLibStructure.NET_MSG_OBJECT[32]; // 检测到的物体
        public int              nDetectRegionNum;                     // 检测区域顶点数
        public NetSDKLibStructure.NET_POINT []     DetectRegion = new NetSDKLibStructure.NET_POINT[NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM]; // 检测区域
        public int              nCount;                               // 事件触发次数
        public int              nPresetID;                            // 预置点
        public NetSDKLibStructure.NET_EXTENSION_INFO stuExtensionInfo;                   // 扩展信息
        public Pointer          pstuImageInfo;                        //图片信息数组, refer to {@link NET_IMAGE_INFO_EX3}
        public int              nImageInfoNum;                        //图片信息个数
        public int              bSceneImage;                          //pstuSceneImage是否有效
        public Pointer          pstuSceneImage;                       //全景广角图, refer to {@link SCENE_IMAGE_INFO_EX}
        public byte[]           byReserved = new byte[1020-2*NetSDKLibStructure.POINTERSIZE]; //保留字节,留待扩展.

        public DEV_EVENT_BANNER_DETECTION_INFO(){
    		for(int i=0;i<stuObjects.length;i++){
    			stuObjects[i]=new NetSDKLibStructure.NET_MSG_OBJECT();
    		}
    		for(int i=0;i<DetectRegion.length;i++){
    			DetectRegion[i]=new NetSDKLibStructure.NET_POINT();
    		}
        }
    }

    // 事件类型EVENT_IVS_BANNER_DETECTION(拉横幅检测事件)对应的规则配置
    public static class NET_BANNER_DETECTION_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nDetectRegionPoint;                   // 检测区顶点数
        public POINTCOORDINATE	[] stuDetectRegion = new POINTCOORDINATE[20]; // 检测区域
        public int              nMinDuration;                         // 最短持续时间, 单位:秒，范围1-600, 默认30
        public int              nReportInterval;                      // 重复报警间隔,单位:秒,范围0-600,默认30,为0表示不重复
        public int              nSensitivity;                         // 检测灵敏度,范围1-10
        public int              nBannerPercent;                       // 近景抓拍时横幅在画面的百分比，范围1~100，默认80
        public byte[]           bReserved = new byte[520];            // 保留字节

        public NET_BANNER_DETECTION_RULE_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    public static class POINTCOORDINATE extends NetSDKLibStructure.SdkStructure
    {
        public int              nX;                                   // 第一个元素表示景物点的x坐标(0~8191)
        public int              nY;                                   // 第二个元素表示景物点的y坐标(0~8191)
    }

    // 事件类型 EVENT_IVS_SMART_KITCHEN_CLOTHES_DETECTION(智慧类型衣着检测)对应的规则配置
    public static class NET_SMART_KITCHEN_CLOTHES_DETECTION_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              bMaskEnable;                          // 是否开启口罩检测  （TRUE:开启 FALSE:关闭）
        public int              bChefHatEnable;                       // 是否开启厨师帽检测（TRUE:开启 FALSE:关闭）
        public int              bChefClothesEnable;                   // 是否开启厨师服检测（TRUE:开启 FALSE:关闭）
        public int              nChefClothesColorNum;                 // 配置检查允许的厨师服颜色个数
        public int	[]           emChefClothesColors = new int[8];     // 厨师衣服颜色(对应的枚举值EM_CFG_CHEF_CLOTHES_COLORS)
        public int              nReportInterval;                      // 重复报警间隔,单位:秒,范围0-600,默认30,为0表示不重复
        public byte[]           byReserved = new byte[4096];          // 保留字节

        public NET_SMART_KITCHEN_CLOTHES_DETECTION_RULE_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 智能分析规则信息
    public static class NET_ANALYSE_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emClassType;                          // 分析大类类型(对应的枚举值EM_SCENE_CLASS_TYPE)
        public int              dwRuleType;                           // 规则类型, 详见dhnetsdk.h中"智能分析事件类型"
        // EVENT_IVS_FACEANALYSIS(人脸分析)对应结构体 NET_FACEANALYSIS_RULE_INFO
		// EVENT_IVS_NONMOTORDETECT(非机动车)对应结构体 NET_NONMOTORDETECT_RULE_INFO
		// EVENT_IVS_VEHICLEDETECT(机动车) 对应结构体 NET_VEHICLEDETECT_RULE_INFO
		// EVENT_IVS_HUMANTRAIT(人体) 对应结构体NET_HUMANTRAIT_RULE_INFO
		// EVENT_IVS_XRAY_DETECT_BYOBJECT(X光按物体检测) 对应结构体 NET_XRAY_DETECT_BYPBJECT_RULE_INFO
		// EVENT_IVS_WORKCLOTHES_DETECT(工装检测)对应结构体NET_WORKCLOTHDETECT_RULE_INFO
		// EVENT_IVS_WORKSTATDETECTION(作业统计)对应结构体NET_WORKSTATDETECTION_RULE_INFO
		// EVENT_IVS_CROSSLINEDETECTION(警戒线)对应结构体NET_CROSSLINE_RULE_INFO
		// EVENT_IVS_CROSSREGIONDETECTION(警戒区)对应结构体 NET_CROSSREGION_RULE_INFO
        // EVENT_IVS_FEATURE_ABSTRACT(特征提取)对应结构体 NET_FEATURE_ABSTRACT_RULE_INFO
        // EVENT_IVS_ELECTRIC_GLOVE_DETECT(电力检测手套检测事件)对应结构体NET_ELECTRIC_GLOVE_DETECT_RULE_INFO
        // EVENT_IVS_ELECTRIC_LADDER_DETECT(电力检测梯子检测事件)对应结构体NET_ELECTRIC_LADDER_DETECT_RULE_INFO
        // EVENT_IVS_ELECTRIC_CURTAIN_DETECT(电力检测布幔检测事件)对应结构体NET_ELECTRIC_CURTAIN_DETECT_RULE_INFO
        // EVENT_IVS_ELECTRIC_FENCE_DETECT(电力检测围栏检测事件)对应结构体NET_ELECTRIC_FENCE_DETECT_RULE_INFO
        // EVENT_IVS_ELECTRIC_SIGNBOARD_DETECT(电力检测标识牌检测事件)对应结构体NET_ELECTRIC_SIGNBOARD_DETECT_RULE_INFO
        // EVENT_IVS_ELECTRIC_BELT_DETECT(电力检测安全带检测事件)对应结构体NET_ELECTRIC_BELT_DETECT_RULE_INFO
		// EVENT_IVS_BANNER_DETECTION（拉横幅检测事件）对应结构体NET_BANNER_DETECTION_RULE_INFO
		// EVENT_IVS_SMART_KITCHEN_CLOTHES_DETECTION(智慧厨房衣着检测事件)对应结构体NET_SMART_KITCHEN_CLOTHES_DETECTION_RULE_INFO
		// EVENT_IVS_WATER_STAGE_MONITOR(水位检测事件)对应结构体NET_WATER_STAGE_MONITOR_RULE_INFO
		// EVENT_IVS_FLOATINGOBJECT_DETECTION(漂浮物检测事件)对应结构体 NET_FLOATINGOBJECT_DETECTION_RULE_INFO
        // EVENT_IVS_RIOTERDETECTION (人群聚集) 对应结构体 NET_RIOTERDETECTION_RULE_INFO
        // EVENT_IVS_LEFTDETECTION (物品遗留事件) 对应结构体 NET_LEFTDETECTION_RULE_INFO
        // EVENT_IVS_PARKINGDETECTION (非法停车事件) 对应结构体 NET_PARKINGDETECTION_RULE_INFO
        // EVENT_IVS_WANDERDETECTION( 徘徊事件)对应结构体 NET_WANDERDETECTION_RULE_INFO
        // EVENT_IVS_VIDEOABNORMALDETECTION (视频异常)对应结构体 NET_VIDEOABNORMALDETECTION_RULE_INFO
        // EVENT_IVSS_FACEATTRIBUTE (人脸属性检测) 对应结构体 NET_FACEATTRIBUTE_RULE_INFO
        // EVENT_IVS_MOVEDETECTION (移动检测) 对应结构体 NET_MOVEDETECTION_RULE_INFO
		// EVENT_IVSS_FACECOMPARE(IVSS目标识别事件) 对应结构体 NET_FACECOMPARE_INFO
		// EVENT_IVS_CONVEYER_BELT_DETECT(传送带检测) 对应结构体 NET_CONVEYER_BELT_DETECT_RULE_INFO
        // EVENT_IVS_NUMBERSTAT(数量统计事件) 对应结构体NET_NUMBERSTAT_RULE_INFO
        // EVENT_IVS_STEREO_FIGHTDETECTION(立体行为分析打架/剧烈运动检测) 对应结构体NET_STEREO_FIGHTDETECTION_RULE_INFO
        // EVENT_IVS_SMOKEDETECTION(烟雾报警检测) 对应结构体 NET_SMOKEDETECTION_RULE_INFO
        // EVENT_IVS_FIREDETECTION(火警检测)对应结构体 NET_FIREDETECTION_RULE_INFO
        // EVENT_IVS_PHONECALL_DETECT(打电话检测)对应结构体 NET_PHONECALL_DETECT_RULE_INFO
        // EVENT_IVS_SMOKING_DETECT(吸烟检测)对应结构体 NET_SMOKING_DETECT_RULE_INFO
        // EVENT_IVS_STEREO_STEREOFALLDETECTION(立体行为分析跌倒检测)对应结构体 NET_STEREO_STEREOFALLDETECTION_RULE_INFO
        // EVENT_IVS_WATER_LEVEL_DETECTION(水位尺检测)对应结构体 NET_WATER_LEVEL_DETECTION_RULE_INFO
        // EVENT_IVS_CLIMBDETECTION(攀高检测)对应结构体 NET_CLIMBDETECTION_RULE_INFO
        // EVENT_IVS_ARTICLE_DETECTION(物品检测)对应结构体NET_ARTICLE_DETECTION_RULE_INFO
        // EVENT_IVS_MAN_NUM_DETECTION(立体视觉区域内人数统计事件)对应结构体NET_IVS_MAN_NUM_DETECTION_RULE_INFO
		// EVENT_IVS_DIALRECOGNITION(仪表检测事件)对应结构体NET_IVS_DIALRECOGNITION_RULE_INFO
		// EVENT_IVS_ELECTRICFAULT_DETECT(仪表类缺陷检测事件)对应结构体NET_IVS_ELECTRICFAULT_DETECT_RULE_INFO
		// EVENT_IVS_TRAFFIC_ROAD_BLOCK(交通路障检测事件) 对应结构体 NET_TRAFFIC_ROAD_BLOCK_RULE_INFO
		// EVENT_IVS_TRAFFIC_ROAD_CONSTRUCTION(交通道路施工检测事件) 对应结构体 NET_TRAFFIC_ROAD_CONSTRUCTION_RULE_INFO
		// EVENT_IVS_TRAFFIC_FLOWSTATE(交通流量统计事件) 对应结构体 NET_TRAFFIC_FLOWSTAT_RULE_INFO
		// EVENT_IVS_TRAFFIC_OVERSPEED(超速事件) 对应结构体 NET_TRAFFIC_OVERSPEED_RULE_INFO
		// EVENT_IVS_TRAFFIC_UNDERSPEED(欠速事件) 对应结构体 NET_TRAFFIC_UNDERSPEED_RULE_INFO
		// EVENT_IVS_TRAFFIC_OVERYELLOWLINE(压黄线事件) 对应结构体 NET_TRAFFIC_OVERYELLOWLINE_RULE_INFO
		// EVENT_IVS_TRAFFIC_CROSSLANE(违章变道事件) 对应结构体 NET_TRAFFIC_CROSSLANE_RULE_INFO
		// EVENT_IVS_TRAFFICJAM(交通拥堵事件) 对应结构体 NET_TRAFFIC_JAM_RULE_INFO
		// EVENT_IVS_TRAFFIC_PEDESTRAIN(交通行人事件) 对应结构体 NET_TRAFFIC_PEDESTRAIN_RULE_INFO
		// EVENT_IVS_TRAFFIC_THROW(抛洒物事件) 对应结构体 NET_TRAFFIC_THROW_RULE_INFO
		// EVENT_IVS_TRAFFIC_RETROGRADE(逆行检测事件) 对应结构体 NET_TRAFFIC_RETROGRADE_RULE_INFO
		// EVENT_IVS_TRAFFICACCIDENT(交通事故事件) 对应结构体 NET_TRAFFIC_ACCIDENT_RULE_INFO
		// EVENT_IVS_TRAFFIC_BACKING(倒车事件) 对应结构体 NET_TRAFFIC_BACKING_RULE_INFO
		// EVENT_IVS_FOG_DETECTION(起雾检测事件) 对应结构体 NET_FOG_DETECTION_RULE_INFO
		// EVENT_IVS_CROSSREGIONDETECTION(警戒区事件) 对应结构体 NET_CROSSREGION_RULE_INFO
		// EVENT_IVS_TRAFFIC_PARKING(交通违章停车事件) 对应结构体 NET_TRAFFIC_PARKING_RULE_INFO
		// EVENT_IVS_FINANCE_CASH_TRANSACTION(智慧金融现金交易检测事件) 对应结构体 NET_FINANCE_CASH_TRANSACTION_RULE_INFO
		// EVENT_IVS_LEAVEDETECTION(离岗检测事件) 对应结构体 NET_LEAVEDETECTION_RULE_INFO
		// EVENT_IVS_LADLE_NO_DETECTION(钢包编号识别事件) 对应规则配置为空
		// EVENT_IVS_DIALRECOGNITION_EX(仪表检测事件)对应结构体NET_IVS_DIALRECOGNITION_RULE_INFO
        public Pointer          pReserved;                            // 规则配置, 具体结构体类型根据dwRuleType来确定, 具体信息见dwRuleType的注释
        public int              nObjectTypeNum;                       // 检测物体类型个数, 为0 表示不指定物体类型
        public int	[]           emObjectTypes = new int[16];          // 检测物体类型列表(对应的枚举值EM_ANALYSE_OBJECT_TYPE)
        public byte[]           szRuleName = new byte[128];           //规则名称，不带预置点的设备规则名称不能重名，带预置点的设备，同一预置点内规则名称不能重名，不同预置点之间规则名称可以重名
        public int              IsUsingEnable;                        //是否使用Enable字段
        public int              bEnable;                              //规则使能
        public Pointer          pstuEventHandler;                     //视频分析联动信息，内存由用户申请释放,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_ANALYSE_RULE_EVENT_HANDLER_INFO}
        public byte[]           byReserved = new byte[820-NetSDKLibStructure.POINTERSIZE]; // 保留字节
    }

    // 智能分析规则
    public static class NET_ANALYSE_RULE extends NetSDKLibStructure.SdkStructure
    {
        public NET_ANALYSE_RULE_INFO [] stuRuleInfos = new NET_ANALYSE_RULE_INFO[NetSDKLibStructure.MAX_ANALYSE_RULE_COUNT]; // 分析规则信息
        public int              nRuleCount;                           // 分析规则条数
        public byte[]           byReserved = new byte[1028];          // 保留字节

    public NET_ANALYSE_RULE(){

        for(int i=0;i<stuRuleInfos.length;i++){
            stuRuleInfos[i]=new NET_ANALYSE_RULE_INFO();
        }


    }
    }

    // 推送图片文件信息
    public static class NET_PUSH_PICFILE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emStartRule;                          // 智能任务启动规则(对应的枚举值EM_ANALYSE_TASK_START_RULE)
        public NET_ANALYSE_RULE stuRuleInfo = new NET_ANALYSE_RULE(); // 分析规则信息
        public byte[]           szTaskUserData = new byte[256];       // 任务数据
        public byte[]           szMQConfig = new byte[4096];          // MQ配置信息，参考Paas协议配置中心-算子配置，小型化方案使用。当远程访问类型为RabbitMq时,尝试从该字段获取MQ配置
        public int              nIsRepeat;                            // 是否许可重复,0默认是可以重复,1表示不能重复
        public NET_ANALYSE_TASK_GLOBAL stuGlobal = new NET_ANALYSE_TASK_GLOBAL(); // 全局配置
        public NET_ANALYSE_TASK_MODULE stuModule = new NET_ANALYSE_TASK_MODULE(); // 模块配置
        public int              bUseTransmit;                         //是否使用透传
        public Pointer          pszRules;                             //分析规则内容
        public Pointer          pszGlobal;                            //全局配置内容
        public Pointer          pszModule;                            //模块配置内容

        public NET_PUSH_PICFILE_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小

               @Override
        public int fieldOffset(String name) {
            return super.fieldOffset(name);
        }
    }

    // 智能分析数据源类型
    public static class EM_DATA_SOURCE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DATA_SOURCE_REMOTE_REALTIME_STREAM = 1; // 远程实时流 , 对应 NET_REMOTE_REALTIME_STREAM_INFO
        public static final int   EM_DATA_SOURCE_PUSH_PICFILE = 2;      // 主动推送图片文件, 对应 NET_PUSH_PICFILE_INFO
    }

    // CLIENT_AddAnalyseTask 接口输出参数
    public static class NET_OUT_ADD_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID
        public int              nVirtualChannel;                      // 任务对应的虚拟通道号
        public byte[]           szUrl = new byte[256];                // 智能码流rtsp地址

        public NET_OUT_ADD_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 添加智能分析任务, 输入参数pInParam的结构体类型根据emDataSourceType的值来决定, pInParam 和 pOutParam 资源由用户申请和释放
    public Boolean CLIENT_AddAnalyseTask(LLong lLoginID,int emDataSourceType,Pointer pInParam,NET_OUT_ADD_ANALYSE_TASK pOutParam,int nWaitTime);

    // CLIENT_StartAnalyseTask 接口输入参数
    public static class NET_IN_START_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID

        public NET_IN_START_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_StartAnalyseTask 接口输出参数
    public static class NET_OUT_START_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_START_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 视频流协议类型
    public static class EM_STREAM_PROTOCOL_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_STREAM_PROTOCOL_UNKNOWN = 0;       // 未知
        public static final int   EM_STREAM_PROTOCOL_PRIVATE_V2 = 1;    // 私有二代
        public static final int   EM_STREAM_PROTOCOL_PRIVATE_V3 = 2;    // 私有三代
        public static final int   EM_STREAM_PROTOCOL_RTSP = 3;          // rtsp
        public static final int   EM_STREAM_PROTOCOL_ONVIF = 4;         // Onvif
        public static final int   EM_STREAM_PROTOCOL_GB28181 = 5;       // GB28181
        public static final int   EM_STREAM_PROTOCOL_HIKVISION = 6;
        public static final int   EM_STREAM_PROTOCOL_BSCP = 7;          // 蓝星
    }

    // 远程实时视频源信息
    public static class NET_REMOTE_REALTIME_STREAM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emStartRule;                          // 智能任务启动规则，Polling任务时无效(参考)EM_ANALYSE_TASK_START_RULE
        public NET_ANALYSE_RULE stuRuleInfo = new NET_ANALYSE_RULE(); // 分析规则信息
        public int              emStreamProtocolType;                 // 视频流协议类型(参考EM_STREAM_PROTOCOL_TYPE)
        public byte[]           szPath = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 视频流地址
        public byte[]           szIp = new byte[NetSDKLibStructure.NET_MAX_IPADDR_OR_DOMAIN_LEN]; // IP 地址
        public short            wPort;                                // 端口号
        public byte[]           byReserved = new byte[2];             // 用于字节对齐
        public byte[]           szUser = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 用户名
        public byte[]           szPwd = new byte[NetSDKLibStructure.NET_COMMON_STRING_64]; // 密码
        public int              nChannelID;                           // 通道号
        public int              nStreamType;                          // 码流类型, 0:主码流; 1:辅1码流; 2:辅2码流;
        public byte[]           szTaskUserData = new byte[256];       // 任务数据
        public byte[]           szMQConfig = new byte[4096];          // MQ配置信息，参考Paas协议配置中心-算子配置，小型化方案使用。当远程访问类型为RabbitMq时,尝试从该字段获取MQ配置
        public int              nIsRepeat;                            // 是否许可重复,0默认是可以重复,1表示不能重复
        public NET_ANALYSE_TASK_GLOBAL stuGlobal = new NET_ANALYSE_TASK_GLOBAL(); // 全局配置
        public NET_ANALYSE_TASK_MODULE stuModule = new NET_ANALYSE_TASK_MODULE(); // 模块配置
        public byte[]           szChannelId = new byte[32];           // 平台通道信息(专用)
        public int              bUseTransmit;                         //是否使用透传
        public Pointer          pszRules;                             //分析规则内容
        public Pointer          pszGlobal;                            //全局配置内容
        public Pointer          pszModule;                            //模块配置内容

        public NET_REMOTE_REALTIME_STREAM_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 启动智能分析任务, pInParam 和 pOutParam 资源由用户申请和释放
    public Boolean CLIENT_StartAnalyseTask(LLong lLoginID,NET_IN_START_ANALYSE_TASK pInParam,NET_OUT_START_ANALYSE_TASK pOutParam,int nWaitTime);

    // CLIENT_RemoveAnalyseTask 接口输入参数
    public static class NET_IN_REMOVE_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID

        public NET_IN_REMOVE_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_RemoveAnalyseTask 接口输出参数
    public static class NET_OUT_REMOVE_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_REMOVE_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 删除(停止)智能分析任务, pInParam 和 pOutParam 资源由用户申请和释放
    public Boolean CLIENT_RemoveAnalyseTask(LLong lLoginID,NET_IN_REMOVE_ANALYSE_TASK pInParam,NET_OUT_REMOVE_ANALYSE_TASK pOutParam,int nWaitTime);

    // CLIENT_FindAnalyseTask 接口输入参数
    public static class NET_IN_FIND_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_IN_FIND_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_FindAnalyseTask 接口输出参数
    public static class NET_OUT_FIND_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskNum;                             // 智能分析任务个数
        public NET_ANALYSE_TASKS_INFO [] stuTaskInfos = new NET_ANALYSE_TASKS_INFO[256]; // 智能分析任务信息

        public NET_OUT_FIND_ANALYSE_TASK()
        {
            for(int i=0;i<stuTaskInfos.length;i++){
                stuTaskInfos[i]=new NET_ANALYSE_TASKS_INFO();
            }
            this.dwSize = this.size();

        }// 此结构体大小
    }

    // 智能分析任务信息
    public static class NET_ANALYSE_TASKS_INFO extends NetSDKLibStructure.SdkStructure
    {
        /**
         任务ID
         */
        public			int            nTaskID;
        /**
         分析状态 {@link EM_ANALYSE_STATE}
         */
        public			int            emAnalyseState;
        /**
         错误码 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_ANALYSE_TASK_ERROR}
         */
        public			int            emErrorCode;
        /**
         字节对齐
         */
        public			byte[]         byReserved1 = new byte[4];
        /**
         任务数据
         */
        public			byte[]         szTaskUserData = new byte[256];
        /**
         录像分析进度，当任务添加接口CLIENT_AddAnalyseTask emDataSourceType参数为录像分析"EM_DATA_SOURCE_REMOTE_PICTURE_FILE"时有效 范围1~100，100表示分析完成
         */
        public			int            nVideoAnalysisProcess;
        /**
         智能流rtsp地址，实时流时才填写
         */
        public			byte[]         szUrl = new byte[256];
        /**
         智能大类类型 {@link NetSDKLibStructure.EM_SCENE_CLASS_TYPE}
         */
        public			int            emClassType;
        /**
         数据源类型 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_DATA_SOURCE_TYPE}
         */
        public			int            emSourceType;
        /**
         任务使用的分析子卡ID.-1表示无效子卡，大于等于0的值表示子卡ID号
         emErrorCode为EM_ANALYSE_TASK_ERROR_ANALYZER_OFF_LINE或EM_ANALYSE_TASK_ERROR_ANALYZER_ON_LINE时此字段有效
         */
        public			int            nChipId;
        public int              nFilesNum;                            //上传的文件列表有效个数，最大值为32
        public Pointer          pstuFiles;                            //上传的文件列表, 内存由SDK申请释放,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_EMERGENCY_FILE_INFO}
        /**
         保留字节
         */
        public			byte[]         byReserved = new byte[424-NetSDKLibStructure.POINTERSIZE];

        public			NET_ANALYSE_TASKS_INFO(){
        }
    }

    // 分析状态
    public static class EM_ANALYSE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ANALYSE_STATE_UNKNOWN = 0;         // 未知
        public static final int   EM_ANALYSE_STATE_IDLE = 1;            // 已创建但未运行
        public static final int   EM_ANALYSE_STATE_ANALYSING = 2;       // 分析中
        public static final int   EM_ANALYSE_STATE_ANALYSING_WAITPUSH = 3; // 分析中并等待push数据
        public static final int   EM_ANALYSE_STATE_FINISH = 4;          // 正常完成
        public static final int   EM_ANALYSE_STATE_ERROR = 5;           // 执行异常
        public static final int   EM_ANALYSE_STATE_REMOVED = 6;         // 被删除
        public static final int   EM_ANALYSE_STATE_ROUNDFINISH = 7;     // 完成一轮视频源分析
        public static final int   EM_ANALYSE_STATE_STARTING = 8;        //任务开启状态
    }

    /**
     * 查找智能分析任务信息, pInParam 和 pOutParam 资源由用户申请和释放
     *  param[in] nWaitTime 接口超时时间, 单位毫秒
     *  return TRUE表示成功 FALSE表示失败
     */
    public Boolean CLIENT_FindAnalyseTask(LLong lLoginID, NET_IN_FIND_ANALYSE_TASK pInParam, NET_OUT_FIND_ANALYSE_TASK pOutParam, int nWaitTime);

    // CLIENT_PushAnalysePictureFile 接口输入参数
    public static class NET_IN_PUSH_ANALYSE_PICTURE_FILE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID
        public NET_PUSH_PICTURE_INFO [] stuPushPicInfos = (NET_PUSH_PICTURE_INFO[]) new NET_PUSH_PICTURE_INFO().toArray(NetSDKLibStructure.MAX_ANALYSE_PICTURE_FILE_NUM); // 推送图片信息
        public int              nPicNum;                              // 推送图片数量
        public int              nBinBufLen;                           // 数据缓冲区长度, 单位:字节
        public Pointer          pBinBuf;                              // 数据缓冲区, 由用户申请和释放
        public NET_PUSH_PICTURE_INFO_EXTERN[] stuPushPicInfoExterns = new NET_PUSH_PICTURE_INFO_EXTERN[32]; //推图信息扩展字段，数量复用nPicNum,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_PUSH_PICTURE_INFO_EXTERN}

        public NET_IN_PUSH_ANALYSE_PICTURE_FILE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // X光机视角类型
    public static class EM_XRAY_VIEW_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_XRAY_VIEW_TYPE_UNKNOWN = -1;       // 未知
        public static final int   EM_XRAY_VIEW_TYPE_MASTER = 0;         // 主视角
        public static final int   EM_XRAY_VIEW_TYPE_SLAVE = 1;          // 从视角
    }

    // 客户自定义信息, X光机专用
    public static class NET_XRAY_CUSTOM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emViewType;                           // 视角类型
        public byte[]           szSerialNumber = new byte[128];       // 流水号
        public byte[]           byReserved = new byte[124];           // 保留字节

        @Override
        public String toString() {
            return "NET_XRAY_CUSTOM_INFO{" +
                    "emViewType=" + emViewType +
                    ", szSerialNumber=" + new String(szSerialNumber) +
                    '}';
        }
    }

    // 智能分析图片信息
    public static class NET_PUSH_PICTURE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szFileID = new byte[NetSDKLibStructure.NET_COMMON_STRING_128]; // 文件ID
        public int              nOffset;                              // 文件数据在二进制数据中的偏移, 单位:字节
        public int              nLength;                              // 文件数据长度, 单位:字节
        public NET_XRAY_CUSTOM_INFO stuXRayCustomInfo;                // 客户自定义信息, X光机专用
        public byte[]           szUrl = new byte[512];                // 远程文件url地址  带访问所需必要信息 包含用户名 密码
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // CLIENT_PushAnalysePictureFile 接口输出参数
    public static class NET_OUT_PUSH_ANALYSE_PICTURE_FILE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_PUSH_ANALYSE_PICTURE_FILE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 推送智能分析图片文件，当CLIENT_AddAnalyseTask的数据源类型emDataSourceType为 EM_DATA_SOURCE_PUSH_PICFILE 时使用
    public Boolean CLIENT_PushAnalysePictureFile(LLong lLoginID,NET_IN_PUSH_ANALYSE_PICTURE_FILE pInParam,NET_OUT_PUSH_ANALYSE_PICTURE_FILE pOutParam,int nWaitTime);

    /*--------任务开始：  CLIENT_AttachAnalyseTaskState / CLIENT_DetachAnalyseTaskState --------*/
    // 智能分析任务状态回调信息
    public static class NET_CB_ANALYSE_TASK_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_ANALYSE_TASKS_INFO[] stuTaskInfos = (NET_ANALYSE_TASKS_INFO[])new NET_ANALYSE_TASKS_INFO().toArray(NetSDKLibStructure.MAX_ANALYSE_TASK_NUM); // 智能分析任务信息
        public int              nTaskNum;                             // 任务个数
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    // 智能分析状态订阅函数原型, lAttachHandle 为 CLIENT_AttachAnalyseTaskState 函数的返回值
    public interface fAnalyseTaskStateCallBack extends Callback {
        public int invoke(LLong lAttachHandle,Pointer pstAnalyseTaskStateInfo,Pointer dwUser);
    }

    // CLIENT_AttachAnalyseTaskState 接口输入参数
    public static class NET_IN_ATTACH_ANALYSE_TASK_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int[]            nTaskIDs = new int[NetSDKLibStructure.MAX_ANALYSE_TASK_NUM]; // 智能分析任务ID
        public int              nTaskIdNum;                           // 智能分析任务个数, 0表示订阅全部任务
        public fAnalyseTaskStateCallBack cbAnalyseTaskState;          // 智能分析任务状态订阅函数
        public Pointer          dwUser;                               // 用户数据

        public NET_IN_ATTACH_ANALYSE_TASK_STATE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 订阅智能分析任务状态, pInParam 资源由用户申请和释放
    public LLong CLIENT_AttachAnalyseTaskState(LLong lLoginID,NET_IN_ATTACH_ANALYSE_TASK_STATE pInParam,int nWaitTime);

    // 取消订阅智能分析任务状态, lAttachHandle 为 CLIENT_AttachAnalyseTaskState接口的返回值
    public Boolean CLIENT_DetachAnalyseTaskState(LLong lAttachHandle);

    /*--------任务结束：  CLIENT_AttachAnalyseTaskState / CLIENT_DetachAnalyseTaskState --------*/
    /*--------任务开始：  CLIENT_AttachAnalyseTaskResult / CLIENT_DetachAnalyseTaskResult --------*/
    // 事件类型
    public static class EM_ANALYSE_EVENT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ANALYSE_EVENT_UNKNOWN = 0;         // 未知
        public static final int   EM_ANALYSE_EVENT_ALL = 1;             // 所有事件
        public static final int   EM_ANALYSE_EVENT_FACE_DETECTION = 2;  // 目标检测事件, 对应结构体 DEV_EVENT_FACEDETECT_INFO
        public static final int   EM_ANALYSE_EVENT_FACE_RECOGNITION = 3; // 目标识别事件, 对应结构体 DEV_EVENT_FACERECOGNITION_INFO
        public static final int   EM_ANALYSE_EVENT_TRAFFICJUNCTION = 4; // 交通路口事件, 对应结构体 DEV_EVENT_TRAFFICJUNCTION_INFO
        public static final int   EM_ANALYSE_EVENT_HUMANTRAIT = 5;      // 人体特征事件, 对应结构体 DEV_EVENT_HUMANTRAIT_INFO
        public static final int   EM_ANALYSE_EVENT_XRAY_DETECTION = 6;  // X光机检测事件, 对应结构体 DEV_EVENT_XRAY_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_WORKCLOTHESDETECT = 7; // 工装(安全帽/工作服等)检测事件, 对应结构体 DEV_EVENT_WORKCLOTHES_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_WORKSTATDETECTION = 8; // 作业检测事件, 对应结构体 DEV_EVENT_WORKSTATDETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_CORSSLINEDETECTION = 9; // 警戒线事件, 对应结构体 DEV_EVENT_CROSSLINE_INFO
        public static final int   EM_ANALYSE_EVENT_CROSSREGIONDETECTION = 10; // 警戒区事件, 对应结构体 DEV_EVENT_CROSSREGION_INFO
        public static final int   EM_ANALYSE_EVENT_FEATURE_ABSTRACT = 11; // 特征提取事件 DEV_EVENT_FEATURE_ABSTRACT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_GLOVE_DETECT = 12; // 电力检测手套检测事件,  对应结构体 DEV_EVENT_ELECTRIC_GLOVE_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_LADDER_DETECT = 13; // 电力检测梯子检测事件,  对应结构体 DEV_EVENT_ELECTRIC_LADDER_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_CURTAIN_DETECT = 14; // 电力检测布幔检测事件,  对应结构体 DEV_EVENT_ELECTRIC_CURTAIN_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_FENCE_DETECT = 15; // 电力检测围栏检测事件,  对应结构体 DEV_EVENT_ELECTRIC_FENCE_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_SIGNBOARD_DETECT = 16; // 电力检测标识牌检测事件,  对应结构体 DEV_EVENT_ELECTRIC_SIGNBOARD_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRIC_BELT_DETECT = 17; // 电力检测安全带检测事件,  对应结构体 DEV_EVENT_ELECTRIC_BELT_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_BANNER_DETECTION = 18; // 拉横幅检测事件,	对应的结构体 DEV_EVENT_BANNER_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_SMART_KITCHEN_CLOTHES_DETECTION = 19; // 智慧厨房穿着检测事件, 对应结构体 DEV_EVENT_SMART_KITCHEN_CLOTHES_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_WATER_STAGE_MONITOR = 20; // 水位监测事件, 对应结构体DEV_EVENT_WATER_STAGE_MONITOR_INFO
        public static final int   EM_ANALYSE_EVENT_FLOATINGOBJECT_DETECTION = 21; // 漂浮物检测事件,  对应结构体 DEV_EVENT_FLOATINGOBJECT_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_RIOTERDETECTION = 22; // 人群聚集 对应结构体 DEV_EVENT_RIOTERL_INFO)
        public static final int   EM_ANALYSE_EVENT_IVS_LEFTDETECTION = 23; // 物品遗留事件 对应结构体 DEV_EVENT_LEFT_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_PARKINGDETECTION = 24; // 非法停车事件 对应结构体 DEV_EVENT_PARKINGDETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_WANDERDETECTION = 25; // 徘徊事件对应结构体 DEV_EVENT_WANDER_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_VIDEOABNORMALDETECTION = 26; // 视频异常对应结构体 DEV_EVENT_VIDEOABNORMALDETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_MOVEDETECTION = 27;  // 运动检测事件, 对应结构体 DEV_EVENT_MOVE_INFO
        public static final int   EM_ANALYSE_EVENT_VIDEO_NORMAL_DETECTION = 28; // 视频正常事件,在视频诊断检测周期结束时,将未报错的诊断项上报正常事件,对应结构体 DEV_EVENT_VIDEO_NORMAL_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_CONVEYER_BELT_BULK = 29; // 传送带大块异物检测事件, 对应结构体 DEV_EVENT_CONVEYER_BELT_BULK_INFO
        public static final int   EM_ANALYSE_EVENT_CONVEYER_BELT_NONLOAD = 30; // 传送带空载检测事件, 对应结构体 DEV_EVENT_CONVEYER_BELT_NONLOAD_INFO
        public static final int   EM_ANALYSE_EVENT_CONVEYER_BELT_RUNOFF = 31; // 传送带跑偏检测事件, 对应结构体 DEV_EVENT_CONVEYER_BELT_RUNOFF_INFO
        public static final int   EM_ANALYSE_EVENT_CONVEYER_BELT_BLOCK = 32; // 传送带阻塞检测事件, 对应结构体 DEV_EVENT_CONVEYORBLOCK_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_NUMBER_STAT = 33;    // 数量统计事件, 对应 结构体 DEV_EVENT_NUMBERSTAT_INFO
        public static final int   EM_ANALYSE_EVENT_FIGHTDETECTION = 34; // 斗殴事件, 对应结构体 DEV_EVENT_FIGHT_INFO
        public static final int   EM_ANALYSE_EVENT_SMOKEDETECTION = 35; // 烟雾报警检测事件, 对应结构体 DEV_EVENT_SMOKE_INFO
        public static final int   EM_ANALYSE_EVENT_FIREDETECTION = 36;  // 火警检测事件, 对应结构体 DEV_EVENT_FIRE_INFO
        public static final int   EM_ANALYSE_EVENT_PHONECALL_DETECT = 37; // 打电话检测事件, 对应结构体 DEV_EVENT_PHONECALL_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_SMOKING_DETECT = 38; // 吸烟检测事件, 对应结构体 DEV_EVENT_SMOKING_DETECT_INFO
        public static final int   EM_ANALYSE_EVENT_TUMBLE_DETECTION = 39; // 跌倒检测事件, 对应结构体 DEV_EVENT_TUMBLE_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_WATER_LEVEL_DETECTION = 40; // 水位尺检测事件, 对应结构体 DEV_EVENT_WATER_LEVEL_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_CLIMBDETECTION = 41; // 攀高检测事件, 对应结构体 DEV_EVENT_IVS_CLIMB_INFO
        public static final int   EM_ANALYSE_EVENT_MAN_NUM_DETECTION = 42; // 立体视觉区域内人数统计事件, 对应结构体DEV_EVENT_MANNUM_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_DIALRECOGNITION = 43; // 仪表检测事件, 对应结构体DEV_EVENT_DIALRECOGNITION_INFO
        public static final int   EM_ANALYSE_EVENT_ELECTRICFAULT_DETECT = 44; // 仪表类缺陷检测事件, 对应结构体DEV_EVENT_ELECTRICFAULTDETECT_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_ROAD_BLOCK = 45; // 交通路障检测事件,对应结构体 DEV_EVENT_TRAFFIC_ROAD_BLOCK_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_ROAD_CONSTRUCTION = 46; //交通道路施工检测事件,对应结构体 DEV_EVENT_TRAFFIC_ROAD_CONSTRUCTION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_FLOWSTATE = 47; // 交通流量统计事件,对应结构体 DEV_EVENT_TRAFFIC_FLOW_STATE
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_OVERSPEED = 48; // 超速事件,对应结构体 DEV_EVENT_TRAFFIC_OVERSPEED_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_UNDERSPEED = 49; // 欠速事件,对应结构体 DEV_EVENT_TRAFFIC_UNDERSPEED_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_OVERYELLOWLINE = 50; // 压黄线事件,对应结构体 DEV_EVENT_TRAFFIC_OVERYELLOWLINE_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_CROSSLANE = 51; // 违章变道事件, 对应结构体 DEV_EVENT_TRAFFIC_CROSSLANE_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFICJAM = 52; // 交通拥堵事件, 对应结构体 DEV_EVENT_TRAFFICJAM_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_PEDESTRAIN = 53; // 交通行人事件, 对应结构体 DEV_EVENT_TRAFFIC_PEDESTRAIN_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_THROW = 54; // 抛洒物事件, 对应结构体 DEV_EVENT_TRAFFIC_THROW_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_RETROGRADE = 55; // 交通逆行事件, 对应结构体 DEV_EVENT_TRAFFIC_RETROGRADE_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFICACCIDENT = 56; // 交通事故事件, 对应结构体 DEV_EVENT_TRAFFICACCIDENT_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_BACKING = 57; // 倒车事件, 对应结构体 DEV_EVENT_IVS_TRAFFIC_BACKING_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_FOG_DETECTION = 58; // 起雾检测事件, 对应结构体 DEV_EVENT_FOG_DETECTION
        public static final int   EM_ANALYSE_EVENT_IVS_CROSSREGIONDETECTION = 59; // 警戒区事件, 对应结构体 DEV_EVENT_CROSSREGION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_TRAFFIC_PARKING = 60; // 交通违章停车事件，对应结构体 DEV_EVENT_TRAFFIC_PARKING_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_FINANCE_CASH_TRANSACTION = 61; //智慧金融现金交易检测事件,对应结构体 DEV_EVENT_FINANCE_CASH_TRANSACTION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_LEAVEDETECTION = 62; // 离岗检测事件,对应结构体 DEV_EVENT_IVS_LEAVE_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_LADLE_NO_DETECTION = 63; // 钢包编号识别事件,对应结构体 DEV_EVENT_LADLE_NO_DETECTION_INFO
        public static final int   EM_ANALYSE_EVENT_IVS_OPEN_INTELLI = 78; ///// 开放智能事件(对应 DEV_EVENT_OPEN_INTELLI_INFO)
        public static final int   EM_ANALYSE_EVENT_IVS_GROUND_THING_DETECTION = 86; ///// 地物识别(对应 NET_DEV_EVENT_GROUND_THING_DETECTION_INFO)
        public static final int   EM_ANALYSE_EVENT_IVS_OCR_DETECTION = 87; ///// OCR检测事件(对应 NET_DEV_EVENT_OCR_DETECTION_INFO)
        public static final int   EM_ANALYSE_EVENT_CROSSLINEDETECTION_EX = 2000; // 警戒线事件(扩展), 对应结构体 DEV_EVENT_CROSSLINE_INFO_EX
    }

    // 文件分析状态
    public static class EM_FILE_ANALYSE_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_FILE_ANALYSE_UNKNOWN = -1;         // 未知
        public static final int   EM_FILE_ANALYSE_EXECUTING = 0;        // 分析中
        public static final int   EM_FILE_ANALYSE_FINISH = 1;           // 分析完成
        public static final int   EM_FILE_ANALYSE_FAILED = 2;           // 分析失败
    }

    // 二次录像分析事件信息
    public static class NET_SECONDARY_ANALYSE_EVENT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emEventType;                          // 事件类型(对应的枚举值EM_ANALYSE_EVENT_TYPE)
        public byte[]           byReserved1 = new byte[4];            // 字节对齐
        public Pointer          pstEventInfo;                         // 事件信息, 根据emEventType确定不同的结构体
        // EM_ANALYSE_EVENT_FACE_DETECTION (目标检测事件), 对应结构体 DEV_EVENT_FACEDETECT_INFO
        // EM_ANALYSE_EVENT_FACE_RECOGNITION (目标识别事件), 对应结构体 DEV_EVENT_FACERECOGNITION_INFO
        // EM_ANALYSE_EVENT_TRAFFICJUNCTION (交通路口事件), 对应结构体 DEV_EVENT_TRAFFICJUNCTION_INFO
        // EM_ANALYSE_EVENT_HUMANTRAIT (人体特征事件), 对应结构体 DEV_EVENT_HUMANTRAIT_INFO
        // EM_ANALYSE_EVENT_XRAY_DETECTION(X光机检测事件), 对应结构体 DEV_EVENT_XRAY_DETECTION_INFO
        // EM_ANALYSE_EVENT_WORKCLOTHESDETECT (工装(安全帽/工作服等)检测事件), 对应结构体 DEV_EVENT_WORKCLOTHES_DETECT_INFO
        // EM_ANALYSE_EVENT_WORKSTATDETECTION (作业检测事件), 对应结构体 DEV_EVENT_WORKSTATDETECTION_INFO
        // EM_ANALYSE_EVENT_CORSSLINEDETECTION (警戒线事件), 对应结构体 DEV_EVENT_CROSSLINE_INFO
        // EM_ANALYSE_EVENT_CROSSLINEDETECTION_EX (警戒线事件(扩展)), 对应结构体 DEV_EVENT_CROSSLINE_INFO_EX
        // EM_ANALYSE_EVENT_CROSSREGIONDETECTION (警戒区事件), 对应结构体 DEV_EVENT_CROSSREGION_INFO
        // EM_ANALYSE_EVENT_FEATURE_ABSTRACT(特征提取), 对应结构体 DEV_EVENT_FEATURE_ABSTRACT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_GLOVE_DETECT(电力检测手套检测事件),  对应结构体 DEV_EVENT_ELECTRIC_GLOVE_DETECT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_LADDER_DETECT(电力检测梯子检测事件),  对应结构体 DEV_EVENT_ELECTRIC_LADDER_DETECT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_CURTAIN_DETECT(电力检测布幔检测事件),  对应结构体 DEV_EVENT_ELECTRIC_CURTAIN_DETECT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_FENCE_DETECT(电力检测围栏检测事件),  对应结构体 DEV_EVENT_ELECTRIC_FENCE_DETECT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_SIGNBOARD_DETECT(电力检测标识牌检测事件),  对应结构体 DEV_EVENT_ELECTRIC_SIGNBOARD_DETECT_INFO
        // EM_ANALYSE_EVENT_ELECTRIC_BELT_DETECT(电力检测安全带检测事件),  对应结构体 DEV_EVENT_ELECTRIC_BELT_DETECT_INFO
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    // 任务自定义数据
    public static class NET_TASK_CUSTOM_DATA extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szClientIP = new byte[128];           // 客户端IP
        public byte[]           szDeviceID = new byte[128];           // 设备ID
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // 智能分析结果订阅的过滤条件
    public static class NET_ANALYSE_RESULT_FILTER extends NetSDKLibStructure.SdkStructure
    {
        public int[]            dwAlarmTypes = new int[NetSDKLibStructure.MAX_ANALYSE_FILTER_EVENT_NUM]; // 过滤事件, 详见dhnetsdk.h中"智能分析事件类型"
        public int              nEventNum;                            // 过滤事件数量
        public int              nImageDataFlag;                       // 是否包含图片, 0-包含,  1-不包含
        public byte[]           byReserved1 = new byte[4];            // 对齐
        public int              nImageTypeNum;                        // pImageType有效个数
        /**
         * 对应枚举类型为EM_FILTER_IMAGE_TYPE,int数组按位取值
         */
        public Pointer          pImageType;                           // 过滤上报的图片类型
        public byte[]           byReserved = new byte[1004];          // 保留字节
    }

    // CLIENT_AttachAnalyseTaskResult 接口输入参数
    public static class NET_IN_ATTACH_ANALYSE_RESULT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int[]            nTaskIDs = new int[NetSDKLibStructure.MAX_ANALYSE_TASK_NUM]; // 智能分析任务ID
        public int              nTaskIdNum;                           // 智能分析任务个数, 0表示订阅全部任务
        public NET_ANALYSE_RESULT_FILTER stuFilter;                   // 过滤条件
        public byte[]           byReserved = new byte[4];             // 字节对齐
        public fAnalyseTaskResultCallBack cbAnalyseTaskResult;        // 智能分析任务结果订阅函数
        public Pointer          dwUser;                               // 用户数据

        public NET_IN_ATTACH_ANALYSE_RESULT()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 智能分析任务结果信息
    public static class NET_ANALYSE_TASK_RESULT extends NetSDKLibStructure.SdkStructure
    {
        public int              nTaskID;                              // 任务ID
        public byte[]           szFileID = new byte [NetSDKLibStructure.NET_COMMON_STRING_128]; // 文件ID, 分析文件时有效
        public int              emFileAnalyseState;                   // 文件分析状态(对应的枚举值EM_FILE_ANALYSE_STATE)
        public byte[]           szFileAnalyseMsg = new byte[NetSDKLibStructure.NET_COMMON_STRING_256]; // 文件分析额外信息, 一般都是分析失败的原因
        public NET_SECONDARY_ANALYSE_EVENT_INFO[] stuEventInfos = new NET_SECONDARY_ANALYSE_EVENT_INFO[NetSDKLibStructure.MAX_SECONDARY_ANALYSE_EVENT_NUM]; // 事件信息
        public int              nEventCount;                          // 实际的事件个数
        public NET_TASK_CUSTOM_DATA stuCustomData = new NET_TASK_CUSTOM_DATA(); // 自定义数据
        public byte[]           szUserData = new byte[64];            // 频源数据，标示视频源信息，对应addPollingTask中UserData字段。
        public byte[]           szTaskUserData = new byte[256];       // 任务数据
        public Pointer          pstuEventInfosEx;                     // 扩展事件信息 NET_SECONDARY_ANALYSE_EVENT_INFO
        public int              nRetEventInfoExNum;                   // 返回扩展事件信息个数
        public byte[]           szUserDefineData = new byte[512];     // 用户定义数据，对应analyseTaskManager.analysePushPictureFileByRule中UserDefineData字段
        public byte[]           byReserved = new byte[184];           // 保留字节

        public NET_ANALYSE_TASK_RESULT() {
            for(int i = 0; i < stuEventInfos.length; i ++){
                this.stuEventInfos[i] = new NET_SECONDARY_ANALYSE_EVENT_INFO();
            }
        }
    }

    // 智能分析任务结果回调信息
    public static class NET_CB_ANALYSE_TASK_RESULT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_ANALYSE_TASK_RESULT[] stuTaskResultInfos = new NET_ANALYSE_TASK_RESULT[NetSDKLibStructure.MAX_ANALYSE_TASK_NUM]; // 智能分析任务结果信息
        public int              nTaskResultNum;                       // 任务个数
        public byte[]           byReserved = new byte[1028];          // 保留字节

        public NET_CB_ANALYSE_TASK_RESULT_INFO() {
            for(int i = 0; i < NetSDKLibStructure.MAX_ANALYSE_TASK_NUM; i ++){
                this.stuTaskResultInfos[i] = new NET_ANALYSE_TASK_RESULT();
            }
        }
    }

    // 智能分析状态订阅函数原型, lAttachHandle 是 CLIENT_AttachAnalyseTaskResult接口的返回值,pstAnalyseTaskResult对应结构体NET_CB_ANALYSE_TASK_RESULT_INFO
    public interface fAnalyseTaskResultCallBack extends Callback {
        public int invoke(LLong lAttachHandle,Pointer pstAnalyseTaskResult,Pointer pBuf,int dwBufSize,Pointer dwUser);
    }

    // 取消订阅智能分析结果, lAttachHandle 为 CLIENT_AttachAnalyseTaskResult接口的返回值@@
    public Boolean CLIENT_DetachAnalyseTaskResult(LLong lAttachHandle);

    // 订阅智能分析结果, pInParam 资源由用户申请和释放
    public LLong CLIENT_AttachAnalyseTaskResult(LLong lLoginID,NET_IN_ATTACH_ANALYSE_RESULT pInParam,int nWaitTime);

    /*--------任务结束：  CLIENT_AttachAnalyseTaskResult / CLIENT_DetachAnalyseTaskResult --------*/
    /*--------任务开始：  T0058223ERR191213010-TASK1  停留事件：DEV_EVENT_STAY_INFO--------*/
    // 视频分析物体信息结构体
    public static class DH_MSG_OBJECT extends NetSDKLibStructure.SdkStructure
    {
        public int              nObjectID;                            // 物体ID,每个ID表示一个唯一的物体
        public byte[]           szObjectType = new byte[128];         // 物体类型
        public int              nConfidence;                          // 置信度(0~255),值越大表示置信度越高
        public int              nAction;                              // 物体动作:1:Appear 2:Move 3:Stay 4:Remove 5:Disappear 6:Split 7:Merge 8:Rename
        public NetSDKLibStructure.DH_RECT          BoundingBox;                          // 包围盒
        public NetSDKLibStructure.NET_POINT        Center;                               // 物体型心
        public int              nPolygonNum;                          // 多边形顶点个数
        public NetSDKLibStructure.NET_POINT[]      Contour = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_POLYGON_NUM); // 较精确的轮廓多边形
        public int              rgbaMainColor;                        // 表示车牌、车身等物体主要颜色；按字节表示,分别为红、绿、蓝和透明度,例如:RGB值为(0,255,0),透明度为0时, 其值为0x00ff0000.
        public byte[]           szText = new byte[128];               // 物体上相关的带0结束符文本,比如车牌,集装箱号等等
        public byte[]           szObjectSubType = new byte[62];       // 物体子类别,根据不同的物体类型,可以取以下子类型：
        // Vehicle Category:"Unknown"  未知,"Motor" 机动车,"Non-Motor":非机动车,"Bus": 公交车,"Bicycle" 自行车,"Motorcycle":摩托车,"PassengerCar":客车,
        // "LargeTruck":大货车,    "MidTruck":中货车,"SaloonCar":轿车,"Microbus":面包车,"MicroTruck":小货车,"Tricycle":三轮车,    "Passerby":行人
        // "DregsCar":渣土车, "Excavator":挖掘车, "Bulldozer":推土车, "Crane":吊车, "PumpTruck":泵车, "MachineshopTruck":工程车
        //  Plate Category："Unknown" 未知,"Normal" 蓝牌黑牌,"Yellow" 黄牌,"DoubleYellow" 双层黄尾牌,"Police" 警牌,
        // "SAR" 港澳特区号牌,"Trainning" 教练车号牌
        // "Personal" 个性号牌,"Agri" 农用牌,"Embassy" 使馆号牌,"Moto" 摩托车号牌,"Tractor" 拖拉机号牌,"Other" 其他号牌
        // "Civilaviation"民航号牌,"Black"黑牌
        // "PureNewEnergyMicroCar"纯电动新能源小车,"MixedNewEnergyMicroCar,"混合新能源小车,"PureNewEnergyLargeCar",纯电动新能源大车
        // "MixedNewEnergyLargeCar"混合新能源大车
        // HumanFace Category:"Normal" 普通人脸,"HideEye" 眼部遮挡,"HideNose" 鼻子遮挡,"HideMouth" 嘴部遮挡,"TankCar"槽罐车(装化学药品、危险品)
        public short            wColorLogoIndex;                      // 车标索引
        public short            wSubBrand;                            // 车辆子品牌 需要通过映射表得到真正的子品牌 映射表详见开发手册
        public byte             byReserved1;
        public byte             bPicEnble;                            // 是否有物体对应图片文件信息
        public NetSDKLibStructure.NET_PIC_INFO     stPicInfo;                            // 物体对应图片信息
        public byte             bShotFrame;                           // 是否是抓拍张的识别结果
        public byte             bColor;                               // 物体颜色(rgbaMainColor)是否可用
        public byte             byReserved2;                          // 保留字节,留待扩展
        public byte             byTimeType;                           // 时间表示类型,详见EM_TIME_TYPE说明
        public NET_TIME_EX      stuCurrentTime;                       // 针对视频浓缩,当前时间戳（物体抓拍或识别时,会将此识别智能帧附在一个视频帧或jpeg图片中,此帧所在原始视频中的出现时间）
        public NET_TIME_EX      stuStartTime;                         // 开始时间戳（物体开始出现时）
        public NET_TIME_EX      stuEndTime;                           // 结束时间戳（物体最后出现时）
        public NetSDKLibStructure.DH_RECT          stuOriginalBoundingBox;               // 包围盒(绝对坐标)
        public NetSDKLibStructure.DH_RECT          stuSignBoundingBox;                   // 车标坐标包围盒
        public int              dwCurrentSequence;                    // 当前帧序号（抓下这个物体时的帧）
        public int              dwBeginSequence;                      // 开始帧序号（物体开始出现时的帧序号）
        public int              dwEndSequence;                        // 结束帧序号（物体消逝时的帧序号）
        public long             nBeginFileOffset;                     // 开始时文件偏移, 单位: 字节（物体开始出现时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public long             nEndFileOffset;                       // 结束时文件偏移, 单位: 字节（物体消逝时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public byte[]           byColorSimilar = new byte[NetSDKLibStructure.EM_COLOR_TYPE.NET_COLOR_TYPE_MAX]; // 物体颜色相似度,取值范围：0-100,数组下标值代表某种颜色,详见EM_COLOR_TYPE
        public byte[]           byUpperBodyColorSimilar = new byte[NetSDKLibStructure.EM_COLOR_TYPE.NET_COLOR_TYPE_MAX]; //上半身物体颜色相似度(物体类型为人时有效)
        public byte[]           byLowerBodyColorSimilar = new byte[NetSDKLibStructure.EM_COLOR_TYPE.NET_COLOR_TYPE_MAX]; //下半身物体颜色相似度(物体类型为人时有效)
        public int              nRelativeID;                          // 相关物体ID
        public byte[]           szSubText = new byte[20];             // "ObjectType"为"Vehicle"或者"Logo"时,表示车标下的某一车系,比如奥迪A6L,由于车系较多,SDK实现时透传此字段,设备如实填写。
        public short            wBrandYear;                           // 车辆品牌年款 需要通过映射表得到真正的年款 映射表详见开发手册

        protected int getNativeAlignment(Class<?> type, Object value, boolean isFirstElement) {
            int alignment = super.getNativeAlignment(type, value, isFirstElement);
            return Math.min(4, alignment);
        }

        @Override
        public String toString() {
            return "DH_MSG_OBJECT{" +
                    "nObjectID=" + nObjectID +
                    ", szObjectType=" + new String(szObjectType) +
                    ", nConfidence=" + nConfidence +
                    ", nAction=" + nAction +
                    ", BoundingBox=" + BoundingBox +
                    ", Center=" + Center +
                    ", nPolygonNum=" + nPolygonNum +
                    ", Contour=" + Arrays.toString(Contour) +
                    ", rgbaMainColor=" + rgbaMainColor +
                    ", szText=" + new String(szText) +
                    ", szObjectSubType=" + new String(szObjectSubType) +
                    ", wColorLogoIndex=" + wColorLogoIndex +
                    ", wSubBrand=" + wSubBrand +
                    ", byReserved1=" + byReserved1 +
                    ", bPicEnble=" + bPicEnble +
                    ", bShotFrame=" + bShotFrame +
                    ", bColor=" + bColor +
                    ", byReserved2=" + byReserved2 +
                    ", byTimeType=" + byTimeType +
                    ", stuCurrentTime=" + stuCurrentTime +
                    ", stuStartTime=" + stuStartTime +
                    ", stuEndTime=" + stuEndTime +
                    ", stuOriginalBoundingBox=" + stuOriginalBoundingBox +
                    ", stuSignBoundingBox=" + stuSignBoundingBox +
                    ", dwCurrentSequence=" + dwCurrentSequence +
                    ", dwBeginSequence=" + dwBeginSequence +
                    ", dwEndSequence=" + dwEndSequence +
                    ", nBeginFileOffset=" + nBeginFileOffset +
                    ", nEndFileOffset=" + nEndFileOffset +
                    ", byColorSimilar=" + new String(byColorSimilar) +
                    ", byUpperBodyColorSimilar=" + new String(byUpperBodyColorSimilar) +
                    ", byLowerBodyColorSimilar=" + new String(byLowerBodyColorSimilar) +
                    ", nRelativeID=" + nRelativeID +
                    ", szSubText=" + new String(szSubText) +
                    ", wBrandYear=" + wBrandYear +
                    '}';
        }
    }

    // 事件类型EVENT_IVS_STAYDETECTION(停留事件)对应的数据块描述信息
    public static class DEV_EVENT_STAY_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public DH_MSG_OBJECT    stuObject;                            // 检测到的物体
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[2];             // 保留字节
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从 0 开始
        public int              nDetectRegionNum;                     //较精确的轮廓多边形                								// 规则检测区域顶点数
        public DH_POINT[]       DetectRegion = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public int              nObjectNum;                           // 检测到的物体个数
        public DH_MSG_OBJECT[]  stuObjectIDs = (DH_MSG_OBJECT[])new DH_MSG_OBJECT().toArray(NetSDKLibStructure.DH_MAX_OBJECT_NUM); // 检测到的物体
        public int              nAreaID;                              // 区域ID(一个预置点可以对应多个区域ID)
        public int              bIsCompliant;                         // 该场景下是否合规
        public PTZ_PRESET_UNIT  stPosition;                           // 预置点的坐标和放大倍数
        public int              nCurChannelHFOV;                      // 当前报警通道的横向视场角,单位：度，实际角度乘以100
        public int              nCurChannelVFOV;                      // 当前报警通道的垂直视场角,单位：度，实际角度乘以100
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage;                        // 全景广角图
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_LINK_INFO    stuLinkInfo;                          // 联动信息，保存其他设备传输的信息
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[624];            // 保留字节,留待扩展.
    }

    /*--------任务结束：  T0058223ERR191213010-TASK1  停留事件：DEV_EVENT_STAY_INFO--------*/
    /*--------任务开始：  ERR191213083-TASK1  发动机数据上报：DH_ALARM_ENGINE_FAILURE_STATUS --------*/
    // 发动机故障状态
    public static class EM_ENGINE_FAILURE_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ENGINE_FAILURE_UNKNOWN = 0;        // 未知
        public static final int   EM_ENGINE_FAILURE_NOTACTIVE = 1;      // "Not active"
        public static final int   EM_ENGINE_FAILURE_ACTIVE = 2;         // "Active"
        public static final int   EM_ENGINE_FAILURE_BLINK = 3;          // "Blink"
        public static final int   EM_ENGINE_FAILURE_NOTAVAILABLE = 4;   // "Not Available"
    }

    // 发动机故障状态上报事件( DH_ALARM_ENGINE_FAILURE_STATUS )
    public static class ALARM_ENGINE_FAILURE_STATUS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              emStatus;                             // 发动机故障状态 详见 EM_ENGINE_FAILURE_STATUS
        public NET_GPS_STATUS_INFO stuGPS;                            // GPS信息(车载需求)
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    /*--------任务开始：  ERR191213083-TASK1  发动机数据上报：DH_ALARM_ENGINE_FAILURE_STATUS--------*/
    /************************************************************************/
    /*视频上传交通运输部需求   从这往下                                 */
    /************************************************************************/
    // 获取转码虚拟通道号(虚拟通道号用于预览与回放), pInParam 和pOutParam 由用户申请和释放
    public boolean CLIENT_GetVirtualChannelOfTransCode(LLong lLoginID,NET_IN_GET_VIRTUALCHANNEL_OF_TRANSCODE pInParam,NET_OUT_GET_VIRTUALCHANNEL_OF_TRANSCODE pOutParam,int nWaitTime);

    // 虚拟通道转码策略
    public static class NET_VIRTUALCHANNEL_POLICY extends NetSDKLibStructure.SdkStructure
    {
        public int              bDeleteByCaller;                      // 是否由用户管理虚拟通道, TRUE:由用户管理  FALSE:由设备管理
        public int              bContinuous;                          // 是否持续转码
        public int              nVirtualChannel;                      //虚拟通道号。当大于0时表示虚拟通道号由客户端指定和管理,范围处于 CLIENT_GetCapsOfTransCode返回的nMinVirtualChannel~nMaxVirtualChannel两值中间。否则由压缩设备管理
        public byte[]           byReserved = new byte[508];           // 保留字节
    }

    //CLIENT_GetVirtualChannelOfTransCode 接口输入参数
    public static class NET_IN_GET_VIRTUALCHANNEL_OF_TRANSCODE extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public NET_VIDEO_SOURCE_INFO stuVideoSourceInfo;              // 视频源信息
        public NET_TRANSCODE_VIDEO_FORMAT stuTransVideoFormat;        // 转码视频格式
        public NET_TRANSCODE_AUDIO_FORMAT stuTransAudioFormat;        // 转码音频格式
        public NET_VIRTUALCHANNEL_POLICY stuVirtualChnPolicy;         // 虚拟通道转码策略
        public NET_TRANSCODE_SNAP_FORMAT stuSnapFormat;               // 转码抓图格式参数
        public NET_TRANSCODE_WATER_MARK[] stuWaterMark = (NET_TRANSCODE_WATER_MARK[]) new NET_TRANSCODE_WATER_MARK().toArray(4); // 水印配置
        public NET_TRANSCODE_IMAGE_WATER_MARK[] stuImageWaterMark = new NET_TRANSCODE_IMAGE_WATER_MARK[4]; //图片水印配置,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_TRANSCODE_IMAGE_WATER_MARK}
        public int              nImageWaterMarkNum;                   //图片水印配置数量

        public NET_IN_GET_VIRTUALCHANNEL_OF_TRANSCODE(){
            this.dwSize = this.size();
        }
    }

   //转码抓图格式参数
    public static class NET_TRANSCODE_SNAP_FORMAT extends NetSDKLibStructure.SdkStructure {
        public int              nWidth;                               // 抓图宽度
        public int              nHeight;                              // 抓图高度
        public byte[]           byReserved = new byte[128];           // 保留字节
    }

    //水印配置
    public static class NET_TRANSCODE_WATER_MARK extends NetSDKLibStructure.SdkStructure {
        public byte[]           szText = new byte[256];               // 水印文本信息
        public int              bAngle;                               //nAngle字段是否生效
        public int              bOpacity;                             //nOpacity字段是否生效
        public int              bFontSize;                            //nFontSize字段是否生效
        public int              bRows;                                //nRows字段是否生效
        public int              bColumns;                             //nColumns字段是否生效
        public int              bColor;                               //nColor字段是否生效
        public int              nAngle;                               //水印倾斜角，范围-180~180，顺时针旋转为正值，逆时针旋转为负值，默认为0
        public int              nOpacity;                             //水印的不透明度，范围0-100，0表示全透明，默认100
        public int              nFontSize;                            //水印字体大小
        public int              nRows;                                //水印行数
        public int              nColumns;                             //水印列数
        public int              nColor;                               //水印字体颜色的RGB值，默认为0，表示黑色
        public int              bPosition;                            //Position字段是否生效
        public int              bType;                                //nType字段是否生效
        public int              nPosition;                            //水印位置0：左上 1：右上 2：左下 3：右下
        public int              nType;                                //区分水印和OSD，如未指定Type字段或Type为0时表示叠加为水印（效果为铺满全屏）；Type为1时表示OSD叠加（只展示在某个区域），Position字段有效。0：水印1：OSD
        public byte[]           byReserved = new byte[448];           // 保留字节
    }

    // 视频源信息
    public static class NET_VIDEO_SOURCE_INFO extends NetSDKLibStructure.SdkStructure {
        public int              emProtocolType;                       // 设备协议类型,枚举值参考EM_DEV_PROTOCOL_TYPE
        public byte[]           szIp = new byte[64];                  // 前端设备IP地址
        public int              nPort;                                // 前端设备端口号
        public byte[]           szUser = new byte[128];               // 前端设备用户名
        public byte[]           szPwd = new byte[128];                // 前端设备密码
        public int              nChannelID;                           // 前端设备通道号
        public byte[]           szStreamUrl = new byte[512];          // 视频源url地址, emProtocolType为EM_DEV_PROTOCOL_GENERAL 时有效
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // 设备协议类型
    public static class EM_DEV_PROTOCOL_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DEV_PROTOCOL_UNKNOWN = 0;          // 未知
        public static final int   EM_DEV_PROTOCOL_V2 = 1;               // 私有二代
        public static final int   EM_DEV_PROTOCOL_V3 = 2;               // 私有三代
        public static final int   EM_DEV_PROTOCOL_ONVIF = 3;            // onvif
        public static final int   EM_DEV_PROTOCOL_GENERAL = 4;          // general
        public static final int   EM_DEV_PROTOCOL_GB28181 = 5;          // 国标GB28181
        public static final int   EM_DEV_PROTOCOL_EHOME = 6;
        public static final int   EM_DEV_PROTOCOL_HIKVISION = 7;
        public static final int   EM_DEV_PROTOCOL_BSCP = 8;
        public static final int   EM_DEV_PROTOCOL_PRIVATE = 9;          // 私有
        public static final int   EM_DEV_PROTOCOL_RTSP = 10;            // RTSP
        public static final int   EM_DEV_PROTOCOL_HBGK = 11;
        public static final int   EM_DEV_PROTOCOL_LUAN = 12;
    }

    // 转码视频格式
    public static class NET_TRANSCODE_VIDEO_FORMAT extends NetSDKLibStructure.SdkStructure {
        public int              emCompression;                        // 视频压缩格式,枚举值参考EM_TRANSCODE_VIDEO_COMPRESSION
        public int              nWidth;                               // 视频宽度
        public int              nHeight;                              // 视频高度
        public int              emBitRateControl;                     // 码流控制模式,枚举值参考NET_EM_BITRATE_CONTROL
        public int              nBitRate;                             // 视频码流(kbps)
        public float            fFrameRate;                           // 视频帧率
        public int              nIFrameInterval;                      // I帧间隔(1-100)，比如50表示每49个B帧或P帧，设置一个I帧。
        public int              emImageQuality;                       // 图像质量,枚举值参考EM_TRANSCODE_IMAGE_QUALITY
        public byte[]           byReserved = new byte[512];           // 保留字节
    }

    // 视频压缩格式
    public static class EM_TRANSCODE_VIDEO_COMPRESSION extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_TRANSCODE_VIDEO_AUTO = 0;          // auto
        public static final int   EM_TRANSCODE_VIDEO_MPEG4 = 1;         // MPEG4
        public static final int   EM_TRANSCODE_VIDEO_MPEG2 = 2;         // MPEG2
        public static final int   EM_TRANSCODE_VIDEO_MPEG1 = 3;         // MPEG1
        public static final int   EM_TRANSCODE_VIDEO_MJPG = 4;          // MJPG
        public static final int   EM_TRANSCODE_VIDEO_H263 = 5;          // H.263
        public static final int   EM_TRANSCODE_VIDEO_H264 = 6;          // H.264
        public static final int   EM_TRANSCODE_VIDEO_H265 = 7;          // H.265
    }

    // 码流控制模式
    public static class NET_EM_BITRATE_CONTROL extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_BITRATE_CBR = 0;                   // 固定码流
        public static final int   EM_BITRATE_VBR = 1;                   // 可变码流
    }

    // 图像质量
    public static class EM_TRANSCODE_IMAGE_QUALITY extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_SELFADAPT = 0; // 自适应
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q10 = 1;   // 10%
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q30 = 2;   // 30%
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q50 = 3;   // 50%
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q60 = 4;   // 60%
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q80 = 5;   // 80%
        public static final int   EM_TRANSCODE_IMAGE_QUALITY_Q100 = 6;  // 100%
    }

    // 转码音频格式
    public static class NET_TRANSCODE_AUDIO_FORMAT extends NetSDKLibStructure.SdkStructure {
        public int              emCompression;                        // 音频压缩模式,枚举值参考NET_EM_AUDIO_FORMAT
        public int              nFrequency;                           // 音频采样频率
        public byte[]           byReserved = new byte[512];           // 保留字节
    }

    public static class NET_EM_AUDIO_FORMAT extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_AUDIO_FORMAT_UNKNOWN = 0;          // unknown
        public static final int   EM_AUDIO_FORMAT_G711A = 1;            // G711a
        public static final int   EM_AUDIO_FORMAT_PCM = 2;              // PCM
        public static final int   EM_AUDIO_FORMAT_G711U = 3;            // G711u
        public static final int   EM_AUDIO_FORMAT_AMR = 4;              // AMR
        public static final int   EM_AUDIO_FORMAT_AAC = 5;              // AAC
        public static final int   EM_AUDIO_FORMAT_G726 = 6;             // G.726
        public static final int   EM_AUDIO_FORMAT_G729 = 7;             // G.729
        public static final int   EM_AUDIO_FORMAT_ADPCM = 8;            // ADPCM
        public static final int   EM_AUDIO_FORMAT_MPEG2 = 9;            // MPEG2
        public static final int   EM_AUDIO_FORMAT_MPEG2L2 = 10;         // MPEG2-Layer2
        public static final int   EM_AUDIO_FORMAT_OGG = 11;             // OGG
        public static final int   EM_AUDIO_FORMAT_MP3 = 12;             // MP3
        public static final int   EM_AUDIO_FORMAT_G7221 = 13;           // G.722.1
    }

    //CLIENT_GetVirtualChannelOfTransCode 接口输出参数
    public static class NET_OUT_GET_VIRTUALCHANNEL_OF_TRANSCODE extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public int              nVirtualChannel;                      // 虚拟通道号
        public byte[]           szVirtualRtspUrl = new byte[255];     //根据转码任务，获取RTSP地址，RTSP地址采用TOKEN方式

        public NET_OUT_GET_VIRTUALCHANNEL_OF_TRANSCODE(){
            this.dwSize = this.size();
        }
    }

    // 获取转码能力集, pInParam 和pOutParam 由用户申请和释放
    public boolean CLIENT_GetCapsOfTransCode(LLong lLoginID,NET_IN_TRANDCODE_GET_CAPS pInParam,NET_OUT_TRANSCODE_GET_CAPS pOutParam,int nWaitTime);

    // 删除转码虚拟通道号
    public boolean CLIENT_DelVirtualChannelOfTransCode(LLong lLoginID,NET_IN_DEL_VIRTUALCHANNEL_OF_TRANSCODE pInParam,NET_OUT_DEL_VIRTUALCHANNEL_OF_TRANSCODE pOutParam,int nWaitTime);

    // CLIENT_DelVirtualChannelOfTransCode 接口输入参数
    public static class NET_IN_DEL_VIRTUALCHANNEL_OF_TRANSCODE extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public int              nVirtualChannel;                      // 虚拟通道号, -1 表示删除所有虚拟通道

        public NET_IN_DEL_VIRTUALCHANNEL_OF_TRANSCODE(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_DelVirtualChannelOfTransCode 接口输出参数
    public static class NET_OUT_DEL_VIRTUALCHANNEL_OF_TRANSCODE extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_DEL_VIRTUALCHANNEL_OF_TRANSCODE(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_GetCapsOfTransCode 接口输入参数
    public static class NET_IN_TRANDCODE_GET_CAPS extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小

        public NET_IN_TRANDCODE_GET_CAPS(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_GetCapsOfTransCode 接口输出参数
    public static class NET_OUT_TRANSCODE_GET_CAPS extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public int              nMinVirtualChannel;                   // 最小虚拟通道号
        public int              nMaxVirtualChannel;                   // 最大虚拟通道号
        public int              bSupportErrorCode;                    // 是否支持压缩错误码实时上报
        public int              bSupportContinuous;                   // 是否支持持续转码
        public int              bSupportDelByCaller;                  // 是否支持由用户管理虚拟通道
        public int              bSupportSpecifyVirtualChannel;        // 是否支持由调用者指定虚拟通道号，
                                                            // 此项为true时，调用CLIENT_GetVirtualChannelOfTransCode时指定虚拟通道nVirtualChannel，虚拟通道号必须在nMinVirtualChannel~nMaxVirtualChannel的范围内；
                                                            // 当此项为false时，不支持客户端指定虚拟通道号。
        public float            fMaxDownLoadSpeed;                    // 支持最大的压缩下载倍速
        public int              nSupportCompressMaxChannel;           // 设备当前支持的最多压缩通道数
        public byte[]           szSupportCompressResolutionRangeMin = new byte[32]; // 支持压缩的分辨率 最小值
        public byte[]           szSupportCompressResolutionRangeMax = new byte[32]; // 支持压缩的分辨率 最大值
        public int              nSupportCompressFpsRangeMin;          // 支持压缩的帧率 最小值
        public int              nSupportCompressFpsRangeMax;          // 支持压缩的帧率 最大值
        public byte[]           szSupportCompressAudioTypes = new byte[64*32]; // 支持的音频格式
        public int              nSupportCompressAudioTypesNum;        // 支持的音频格式个数
        public int              nSupportCompressCompressionTypesNum;  // 支持的视频压缩格式个数
        public byte[]           szSupportCompressCompressionTypes = new byte[64*32]; // 支持的视频压缩格式
        public BYTE_ARRAY_64[]  szSupportImageOsdTypes = new BYTE_ARRAY_64[64]; //支持的图片OSD
        public int              nSupportImageOsdTypesNum;             //支持的图片OSD个数

        public NET_OUT_TRANSCODE_GET_CAPS(){
            this.dwSize = this.size();
        }
    }

    // 订阅虚拟转码通道状态, pInParam 由用户申请和释放
    public LLong CLIENT_AttachVirtualChannelStatus(LLong lLoginID,NET_IN_ATTACH_VIRTUALCHANNEL_STATUS pInParam,int nWaitTime);

    //CLIENT_AttachVirtualChannelStatus 接口输入参数
    public static class NET_IN_ATTACH_VIRTUALCHANNEL_STATUS extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public byte[]           byReserved = new byte[4];             // 用于字节对齐
        public fVirtualChannelStatusCallBack cbVirtualChannelStatus;  // 虚拟转码通道状态订阅函数
        public Pointer          dwUser;                               // 用户数据

        public NET_IN_ATTACH_VIRTUALCHANNEL_STATUS(){
            this.dwSize = this.size();
        }
    }

    public interface fVirtualChannelStatusCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NET_CB_VIRTUALCHANNEL_STATUS_INFO pstVirChnStatusInfo,Pointer dwUser);
    }

    // 虚拟转码通道状态信息
    public class NET_CB_VIRTUALCHANNEL_STATUS_INFO extends NetSDKLibStructure.SdkStructure {
        public int              nVirChannelID;                        // 虚拟转码通道号
        public int              emVirChannelStatus;                   // 虚拟转码通道状态,枚举值参考EM_VIRCHANNEL_STATUS
        public byte[]           byReserved = new byte[1024];          // 保留字节
// 		 public static class ByValue extends NET_CB_VIRTUALCHANNEL_STATUS_INFO implements SdkStructure.ByValue { }
    }

    public static class EM_VIRCHANNEL_STATUS extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_VIRCHANNEL_STATUS_UNKNOWN = -1;    // 未知
        public static final int   EM_VIRCHANNEL_STATUS_OVER_DECODE = 0; // 超出解码
        public static final int   EM_VIRCHANNEL_STATUS_OVER_COMPRESS = 1; // 超出压缩
        public static final int   EM_VIRCHANNEL_STATUS_NO_ORIGI_STREAM = 2; // 无原始码流
        public static final int   EM_VIRCHANNEL_STATUS_SLAVE_OFFLINE = 3; // 压缩通道所在的从片掉线
        public static final int   EM_VIRCHANNEL_STATUS_UNKNOWN_FAILURE = 255; // 未知的失败原因
    }

    // 取消订阅虚拟转码通道状态, lAttachHandle 为 CLIENT_AttachVirtualChannelStatus 函数的返回值
    public boolean CLIENT_DetachVirtualChannelStatus(LLong lAttachHandle);

    /************************************************************************/
    /* 视频上传交通运输部需求           从这往上                        */
    /************************************************************************/
    // 设置动态子连接断线回调函数,目前SVR设备的预览和回放是短连接的。
    public void CLIENT_SetSubconnCallBack(Callback cbSubDisConnect,Pointer dwUser);

    // 动态子连接断开回调函数原形
    public interface fSubDisConnect extends Callback {
        public void invoke(int emInterfaceType,Boolean bOnline,LLong lOperateHandle,LLong lLoginID,Pointer dwUser);
    }

    // 接口类型,对应CLIENT_SetSubconnCallBack接口
    public static class EM_INTERFACE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   DH_INTERFACE_OTHER = 0;               // 未知接口
        public static final int   DH_INTERFACE_REALPLAY = 1;            // 实时预览接口
        public static final int   DH_INTERFACE_PREVIEW = 2;             // 多画面预览接口
        public static final int   DH_INTERFACE_PLAYBACK = 3;            // 回放接口
        public static final int   DH_INTERFACE_DOWNLOAD = 4;            // 下载接口
        public static final int   DH_INTERFACE_REALLOADPIC = 5;         // 下载智能图片接口
    }

    //人脸开门输入参数
    public static class NET_IN_FACE_OPEN_DOOR extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannel;                             //门通道号
        public int              emCompareResult;                      //比对结果,EM_COMPARE_RESULT
        public NET_OPENDOOR_MATCHINFO stuMatchInfo;                   //匹配信息
        public NET_OPENDOOR_IMAGEINFO stuImageInfo;                   //图片信息

        public NET_IN_FACE_OPEN_DOOR(){
            this.dwSize = this.size();
        }
    }

    //匹配信息
    public static class NET_OPENDOOR_MATCHINFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szUserID = new byte[32];              //远程用户ID
        public byte[]           szUserName = new byte[32];            //用户名
        public int              emUserType;                           //用户类型
        public byte[]           szName = new byte[64];                //门禁名称
        public int              nMatchRate;                           //匹配度，范围为0-100
        public int              emOpenDoorType;                       //开门方式
        public NET_TIME         stuActivationTime;                    //(卡、头像)生效日期
        public NET_TIME         stuExpiryTime;                        //(卡、头像)截止日期
        public int              nScore;                               // 信用积分
        public byte[]           szCompanyName = new byte[NetSDKLibStructure.MAX_COMPANY_NAME_LEN]; //单位名称
        public byte[]           szCompanionName = new byte[120];      //陪同人员姓名
        public byte[]           szCompanionCompany = new byte[NetSDKLibStructure.MAX_COMPANY_NAME_LEN]; //陪同人员单位名称
        public byte[]           szPermissibleArea = new byte[NetSDKLibStructure.MAX_COMMON_STRING_128]; //准许通行区域
        public byte[]           szSection = new byte[200];            //部门名称
        public Pointer          pstuCustomEducationInfo;              // 教育信息,参考NET_CUSTOM_EDUCATION_INFO
        public Pointer          pstuHealthCodeInfo;                   //健康码信息,NET_HEALTH_CODE_INFO
        public byte[]           szRoomNo = new byte[32];              //房间号
        public Pointer          pstuIDCardInfo;                       //证件信息,NET_IDCARD_INFO
        public Pointer          pstuBusStationInfo;                   //公交站信息,NET_BUS_STATION_INFO
        public Pointer          pstuCustomWorkerInfo;                 //工地工人信息,NET_CUSTOM_WORKER_INFO
        public int              bUseMatchInfoEx;                      //否使用匹配信息扩展字段
        public Pointer          pstuMatchInfoEx;                      //匹配信息扩展字段，NET_OPENDOOR_MATCHINFO_EX
        public Pointer          pstuHSJCInfo;                         // 核酸检测信息,NET_HSJC_INFO
        public Pointer          pstuVaccineInfo;                      // 新冠疫苗接种信息,NET_VACCINE_INFO
        public Pointer          pstuTravelInfo;                       // 行程码信息,NET_TRAVEL_INFO
        public Pointer          pstuCustomVisitorInfo;                //访客信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CUSTOM_VISITOR_INFO}
        public int              nRemoteQRCodeType;                    //远程二维码开门时的二维码类型 0-默认值，1-访客二维码，2-一卡通二维码
        public byte[]           byReserved = new byte[4];
    }

    // 教育信息
    public static class NET_CUSTOM_EDUCATION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emInfoType;                           // 信息类型
        public int              nStudentSeatNumber;                   // 座位号,最小值为1
        public byte[]           szInfoContent = new byte[128];        // 消息内容
        public int              emVoiceType;                          // 语音类型,EM_CUSTOM_EDUCATION_VOICE_TYPE
    }

    //图片信息
    public static class NET_OPENDOOR_IMAGEINFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nLibImageLen;                         //人脸库照片长度，限制为150k
        public int              nSnapImageLen;                        //抓拍照片长度，限制为150k
        public Pointer          pLibImage;                            //人脸库照片，内存由用户申请
        public Pointer          pSnapImage;                           //抓拍照片，内存由用户申请
        public byte[]           byReserved = new byte[1024];
    }

    //人脸开门输出参数
    public static class NET_OUT_FACE_OPEN_DOOR extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_FACE_OPEN_DOOR(){
            this.dwSize = this.size();
        }
    }

    //人脸开门
    public boolean CLIENT_FaceOpenDoor(LLong lLoginID,NET_IN_FACE_OPEN_DOOR pInParam,NET_OUT_FACE_OPEN_DOOR pOutParam,int nWaitTime);

    // 水位场景类型
    public static class EM_WATERSTAGE_SCENE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_WATERMONITOR_SCENE_UNKNOWN = 0;    // 未知
        public static final int   EM_WATERMONITOR_SCENE_WATERSTAGE_RULE = 1; // 水位检测, 有水位尺
        public static final int   EM_WATERMONITOR_SCENE_WATERSTAGE_NO_RULE = 2; // 水位检测, 无水位尺
        public static final int   EM_WATERMONITOR_SCENE_WATERLOGG_RULE = 3; // 内涝检测, 有水位尺
        public static final int   EM_WATERMONITOR_SCENE_WATERLOGG_NO_RULE = 4; // 内涝检测, 无水位尺
    }

    // 水面分割掩膜信息
    public static class NET_WATER_SURFACE_MASK_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nColNum;                              // 水面分割掩膜列数
        public int              nOffset;                              // 偏移
        public int              nLength;                              // 长度
        public byte[]           byReserved = new byte[1020];          // 预留字段
    }

    // 水位监测事件, 目前仅用于任务型智能分析
    public static class DEV_EVENT_WATER_STAGE_MONITOR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nRuleID;                              // 智能事件规则编号，用于标示哪个规则触发的事件
        public int              emClassType;                          // 智能事件所属大类
        public int              emSceneType;                          // 水位场景类型
        public double           dbMark;                               // 水尺读数
        public NetSDKLibStructure.NET_POINT        stuCrossPoint;                        // 水尺与水面交点
        public NET_WATER_SURFACE_MASK_INFO stuWaterSurfaceMask;       // 水面分割掩膜信息, emSceneType 为EM_WATERMONITOR_SCENE_WATERSTAGE_NO_RULE 或者EM_WATERMONITOR_SCENE_WATERLOGG_NO_RULE有效
        public byte[]           byReserved = new byte[1020];          // 预留字段
    }

    // 标定线
    public static class NET_CALIBRATE_LINE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.NET_POINT        stuStartPoint;                        // 起点
        public NetSDKLibStructure.NET_POINT        stuEndPoint;                          // 终点
    }

    // 事件类型EVENT_IVS_WATER_STAGE_MONITOR(水位检测事件)对应的规则配置
    public static class NET_WATER_STAGE_MONITOR_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nDetectRegionPoint;                   // 检测区顶点数
        public POINTCOORDINATE[] stuDetectRegion = (POINTCOORDINATE[]) new POINTCOORDINATE().toArray(20); // 检测区域
        public int              dwSceneMask;                          // 使能检测的场景掩码	                                                                             // bit2：内涝检测，有水位尺, bit3：内涝检测，无水位尺
        public NET_CALIBRATE_LINE_INFO stuCalibrateLine;              // 标定线, 仅在人物分析模式下有效
        public byte[]           byReserved = new byte[4096];          //保留字节
    }

    // 事件类型EVENT_IVS_VIOLENT_THROW_DETECTION(暴力抛物检测)对应的数据块描述信息
    public static class DEV_EVENT_VIOLENT_THROW_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public int              nFrameSequence;                       // 视频分析帧序号
        public byte[]           szRegionName = new byte[64];          // 暴力抛物检测区域名称
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage;                     // 大图信息
        public NET_IMAGE_INFO_EX2 stuImageInfo[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        //图片信息个数
        public byte             byReserver[] = new byte[1024];        //预留字节
    }

    // CLIENT_GetHumanRadioCaps 接口输入参数
    public static class NET_IN_GET_HUMAN_RADIO_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannel;

        public NET_IN_GET_HUMAN_RADIO_CAPS(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_GetHumanRadioCaps 接口输出参数
    public static class NET_OUT_GET_HUMAN_RADIO_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              bSupportRegulatorAlarm;

        public NET_OUT_GET_HUMAN_RADIO_CAPS(){
            this.dwSize = this.size();
        }
    }

    // 获取能力级
    public Boolean CLIENT_GetHumanRadioCaps(LLong lLoginID,NET_IN_GET_HUMAN_RADIO_CAPS pInParam,NET_OUT_GET_HUMAN_RADIO_CAPS pOutParam,int nWaitTime);

    // 区域内人员体温信息
    public static class NET_MAN_TEMP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nObjectID;                            // 物体ID
        public NET_RECT         stRect;                               // 人员头肩信息, 8192坐标系
        public double           dbHighTemp;                           // 最高的温度
        public int              nTempUnit;                            // 温度单位(0摄氏度 1华氏度 2开尔文)
        public int              bIsOverTemp;                          // 是否温度异常
        public int              bIsUnderTemp;                         // 是否温度异常
        public int              nOffset;                              // 人脸小图特征值在二进制数据块中的偏移
        public int              nLength;                              // 人脸小图特征值长度, 单位:字节
        public int              emMaskDetectResult;                   // 口罩检测结果(参考EM_MASK_DETECT_RESULT_TYPE)
        public NET_RECT         stThermalRect;                        // 热成像检测人员头肩坐标信息(坐标系：8192)
        public int              nAge;                                 // 年龄
        public int              emSex;                                // 性别(参考EM_DEV_EVENT_FACEDETECT_SEX_TYPE)
        public byte[]           byReserved = new byte[36];            // 预留字段
    }

    // 全景图
    public static class NET_VIS_SCENE_IMAGE extends NetSDKLibStructure.SdkStructure
    {
        public int              nOffset;                              // 在二进制数据块中的偏移
        public int              nLength;                              // 图片大小, 单位字节
        public int              nWidth;                               // 图片宽度, 像素
        public int              nHeight;                              // 图片高度, 像素
        public byte[]           byReserved = new byte[64];            // 预留字段
    }

    // 热成像全景图
    public static class NET_THERMAL_SCENE_IMAGE extends NetSDKLibStructure.SdkStructure
    {
        public int              nOffset;                              // 在二进制数据块中的偏移
        public int              nLength;                              // 图片大小, 单位字节
        public int              nWidth;                               // 图片宽度, 像素
        public int              nHeight;                              // 图片高度, 像素
        public byte[]           byReserved = new byte[64];            // 预留字段
    }

    // 事件类型EVENT_IVS_ANATOMY_TEMP_DETECT(人体测温检测事件)对应的数据块描述信息
    public static class DEV_EVENT_ANATOMY_TEMP_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              emClassType;                          // 智能事件所属大类(对应枚举类型EM_CLASS_TYPE)
        public int              nPresetID;                            // 事件触发的预置点号, 从1开始, 没有该字段,表示预置点未知
        public NET_MAN_TEMP_INFO stManTempInfo;                       // 区域内人员体温信息
        public NET_VIS_SCENE_IMAGE stVisSceneImage;                   // 可见光全景图
        public NET_THERMAL_SCENE_IMAGE stThermalSceneImage;           // 热成像全景图
        public int              nSequence;                            // 帧序号
        public int              nEventRelevanceID;                    // 事件关联ID
        public int              bIsFaceRecognition;                   // 是否做过后智能的目标识别
        public Pointer          pstuImageInfo;                        // 图片信息数组，结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[1004];          // 预留字段
    }

    // 事件类型 ALARM_ANATOMY_TEMP_DETECT_INFO(人体温智能检测事件)对应的数据块描述信息
    public static class ALARM_ANATOMY_TEMP_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              nPresetID;                            // 事件触发的预置点号, 从1开始, 没有该字段,表示预置点未知
        public NET_MAN_TEMP_INFO stManTempInfo;                       // 区域内人员体温信息
        public int              nSequence;                            // 帧序号
        public int              nEventRelevanceID;                    // 事件关联ID
        public int              bIsFaceRecognition;                   // 是否做过后智能的目标识别
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[1020];          // 预留字节
    }

    // 人体测温规则配置
    public static class CFG_ANATOMY_TEMP_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        // 信息
        public byte[]           szRuleName = new byte[NetSDKLibStructure.MAX_NAME_LEN];  // 规则名称,不同规则不能重名
        public int              bRuleEnable;                          // 规则使能
        public int              nObjectTypeNum;                       // 相应物体类型个数
        public byte[]           szObjectTypes = new byte[NetSDKLibStructure.MAX_OBJECT_LIST_SIZE*NetSDKLibStructure.MAX_NAME_LEN]; // 相应物体类型列表
        public int              nPtzPresetId;                         // 云台预置点编号	0~65535
        public NetSDKLibStructure.CFG_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
        public NetSDKLibStructure.CFG_TIME_SECTION[] stuTimeSection = (NetSDKLibStructure.CFG_TIME_SECTION[])new NetSDKLibStructure.CFG_TIME_SECTION().toArray(NetSDKLibStructure.WEEK_DAY_NUM*NetSDKLibStructure.MAX_REC_TSECT_EX); // 事件响应时间段
        public byte             bTrackEnable;                         // 触发跟踪使能,仅对警戒线事件,警戒区规则有效
        public int              nDetectRegionPoint;                   // 多边形顶点数
        public NetSDKLibStructure.CFG_POLYGON[]    stuDetectRegion = (NetSDKLibStructure.CFG_POLYGON[])new NetSDKLibStructure.CFG_POLYGON().toArray(NetSDKLibStructure.MAX_POLYGON_NUM); // 检测区域，多边形
        public int              bHighEnable;                          // 温度异常报警是否开启
        public int              bLowEnable;                           // 温度异常报警是否开启
        public int              fHighThresholdTemp;                   // 温度异常阈值，精度0.1，扩大10倍
        public int              fLowThresholdTemp;                    // 温度异常阈值，精度0.1，扩大10倍
        public int              bIsAutoStudy;                         // 是否自动学习
        public int              fHighAutoOffset;                      // 温度自动学习偏差值，精度0.1，扩大10倍
        public int              fLowAutoOffset;                       // 温度自动学习偏差值，精度0.1，扩大10倍
        public int              nSensitivity;                         // 灵敏度 范围[1, 10]
        public int              bSizeFileter;                         // 规则特定的尺寸过滤器是否有效
        public NetSDKLibStructure.CFG_SIZEFILTER_INFO stuSizeFileter; // 规则特定的尺寸过滤器
        public int              bIsCaptureNormal;                     // 是否上报正常体温信息
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    // 事件类型 ALARM_REGULATOR_ABNORMAL_INFO(标准黑体源异常报警事件)对应的数据块描述信息
    public static class ALARM_REGULATOR_ABNORMAL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public byte[]           szTypes = new byte[NetSDKLibStructure.MAX_COMMON_STRING_32]; // 异常类型
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           byReserved = new byte[1024];          // 预留字节
    }

    // 校准源信息
    public static class NET_REGULATOR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nDistance;                            // 校准源距离, 单位cm
        public int              nTemperature;                         // 校准源温度, 精度0.1, 放大10倍
        public NET_RECT         stRect;                               // 校准源矩形位置取值0-8191
        public int              nHeight;                              // 校准源高度, 单位cm
        public int              nDiffTemperature;                     // 温度偏差值, 精度0.01, 放大100倍
        public byte[]           byReserve = new byte[32];             // 保留字节，用于字节对齐
    }

    // 人体测温标准黑体配置, 对应枚举 NET_EM_CFG_RADIO_REGULATOR
    public static class NET_CFG_RADIO_REGULATOR extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              bEnable;                              // 标准黑体配置使能
        public int              nPresetId;                            // 预置点编号, 对于无预置点设备为0
        public int              nCamerHeight;                         // 热成像相机安装高度, 单位cm
        public int              nCamerAngle;                          // 相机安装角度, 精度0.1, 放大10倍
        public NET_REGULATOR_INFO stRegulatorInfo;                    // 校准源信息

        public NET_CFG_RADIO_REGULATOR(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_BatchAppendFaceRecognition 接口输入参数
    public static class NET_IN_BATCH_APPEND_FACERECONGNITION extends NetSDKLibStructure.SdkStructure
    {
        /**
         * 结构体大小
         */
        public int              dwSize;
        /**
         * 需要添加的人员数量
         */
        public int              nPersonNum;
        /**
         * 人员信息，内存由用户申请，大小为nPersonNum * sizeof(FACERECOGNITION_PERSON_INFOEX)
         */
        public Pointer          pstPersonInfo;
        // 图片二进制数据
        public Pointer          pBuffer;                              // 缓冲地址
        public int              nBufferLen;                           // 缓冲数据长度
        public byte[]           bReserved = new byte[4];              // 字节对齐
        public NET_MULTI_APPEND_EXTENDED_INFO stuInfo = new NET_MULTI_APPEND_EXTENDED_INFO(); //扩展信息

        public NET_IN_BATCH_APPEND_FACERECONGNITION(){
            this.dwSize = this.size();
        }
    }

    // 批量添加人员结果信息
    public static class NET_BATCH_APPEND_PERSON_RESULT extends NetSDKLibStructure.SdkStructure
    {
        public int              nUID;                                 // 人员UID
        public int              dwErrorCode;                          // 错误码信息
        public byte[]           bReserved = new byte[512];            // 保留字段
    }

    // CLIENT_BatchAppendFaceRecognition 接口输出参数
    public static class NET_OUT_BATCH_APPEND_FACERECONGNITION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nResultNum;                           // 批量添加结果个数，由用户指定，数值与NET_IN_BATCH_APPEND_FACERECONGNITION中的nPersonNum一致
        /**
         * 内存大小为结构体数组的大小,对应结构体为{@link NET_BATCH_APPEND_PERSON_RESULT}
         */
        public Pointer          pstResultInfo;                        // 批量添加结果信息
        public int              nUIDType;                             // 指定NET_BATCH_APPEND_PERSON_RESULT中的UID使用字段，不存在本字段或值为0则使用UID字段，若值为1则使用UID2字段

        public NET_OUT_BATCH_APPEND_FACERECONGNITION(){
            this.dwSize = this.size();
        }
    }

    // 添加多个人员信息和人脸样本
    public Boolean CLIENT_BatchAppendFaceRecognition(LLong lLoginID,NET_IN_BATCH_APPEND_FACERECONGNITION pstInParam,NET_OUT_BATCH_APPEND_FACERECONGNITION pstOutParam,int nWaitTime);

    // CLIENT_FindFileEx+DH_FILE_QUERY_SNAPSHOT_WITH_MARK  对应查询参数
    public static class MEDIAFILE_SNAPSHORT_WITH_MARK_PARAM extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public NET_TIME         stuStartTime;                         // 开始时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public int              bOnlySupportRealUTC;                  // 为TRUE表示仅下发stuStartTimeRealUTC和stuEndTimeRealUTC(不下发stuStartTime, stuEndTime), 为FALSE表示仅下发stuStartTime, stuEndTime(不下发stuStartTimeRealUTC和stuEndTimeRealUTC)
        public NET_TIME         stuStartTimeRealUTC;                  // UTC开始时间(标准UTC时间), 与stuEndTimeRealUTC配对使用，与(stuStartTime, stuEndTime)互斥
        public NET_TIME         stuEndTimeRealUTC;                    // UTC结束时间(标准UTC时间), 与stuStartTimeRealUTC配对使用，与(stuStartTime, stuEndTime)互斥

        public MEDIAFILE_SNAPSHORT_WITH_MARK_PARAM(){
            this.dwSize = this.size();
        }
    }

    // 抓图标记信息
    public static class NET_SNAPSHOT_MARK_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.NET_POINT        stuPosition;                          // 标记的坐标位置, 绝对坐标系
        public byte[]           byReserved = new byte[1020];          // 预留字段
    }

    // DH_FILE_QUERY_SNAPSHOT_WITH_MARK 对应 FINDNEXT 查询返回结果
    public static class MEDIAFILE_SNAPSHORT_WITH_MARK_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannelID;                           // 通道号从0开始,-1表示查询所有通道
        public NET_TIME         stuStartTime;                         // 开始时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public int              nFileSize;                            // 文件长度
        public byte[]           szFilePath = new byte[NetSDKLibStructure.MAX_PATH];      // 文件路径
        public NET_SNAPSHOT_MARK_INFO stuMarkInfo;                    // 抓图标记信息
        public int              bRealUTC;                             // 为TRUE表示仅stuStartTimeRealUTC和stuEndTimeRealUTC有效(仅使用stuStartTimeRealUTC和stuEndTimeRealUTC), 为FALSE表示仅stuStartTime和stuEndTime有效(仅使用stuStartTime和stuEndTime)
        public NET_TIME         stuStartTimeRealUTC;                  // UTC开始时间(标准UTC时间), 与stuEndTimeRealUTC配对使用
        public NET_TIME         stuEndTimeRealUTC;                    // UTC结束时间(标准UTC时间), 与stuStartTimeRealUTC配对使用

        public MEDIAFILE_SNAPSHORT_WITH_MARK_INFO(){
            this.dwSize = this.size();
        }
    }

    //////////////////////////////////////无人机航点功能开始///////////////////////////////////////////////////////////////////
    // 获取无人机航点入参
    public static class NET_IN_UAVMISSION_COUNT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_IN_UAVMISSION_COUNT(){
            this.dwSize = this.size();
        }
    }

    // 获取无人机航点出参
    public static class NET_OUT_UAVMISSION_COUNT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nCount;                               // 航点总数

        public NET_OUT_UAVMISSION_COUNT(){
            this.dwSize = this.size();
        }
    }

    // 获取任务入参
    public static class NET_IN_READ_UAVMISSION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_IN_READ_UAVMISSION(){
            this.dwSize = this.size();
        }
    }

    // 航点任务
    public static class NET_UAVMISSION_ITEM extends NetSDKLibStructure.SdkStructure
    {
        public int              nCurrentMode;                         // 使能状态 0-未使能; 1-使能;
        public int              bAutoContinue;                        // 自动执行下一个航点
        public int              nSequence;                            // 航点序号
        public int              emCommand;                            // 航点指令(参考ENUM_UAVCMD_TYPE)
        public NET_UAVCMD_PARAM_BUFFER stuCmdParam;                   // 指令参数
        public byte[]           byReserved = new byte[8];             // 保留字段
    }

    // 获取任务出参
    public static class NET_OUT_READ_UAVMISSION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nItemCount;                           // 有效任务个数
        public Pointer          pstuItems;                            // 任务列表(参考NET_UAVMISSION_ITEM)

        public NET_OUT_READ_UAVMISSION(){
            this.dwSize = this.size();
        }
    }

    // 设置任务入参
    public static class NET_IN_WRITE_UAVMISSION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nItemCount;                           // 有效任务个数
        public Pointer          pstuItems;                            // 任务列表(参考NET_UAVMISSION_ITEM)

        public NET_IN_WRITE_UAVMISSION(){
            this.dwSize = this.size();
        }
    }

    // 设置任务出参
    public static class NET_OUT_WRITE_UAVMISSION extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_OUT_WRITE_UAVMISSION(){
            this.dwSize = this.size();
        }
    }

    // 订阅任务消息入参
    public static class NET_IN_ATTACH_UAVMISSION_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public fUAVMissionStateCallBack cbNotify;                     // 任务状态回调函数
        public Pointer          dwUser;                               // 用户信息

        public NET_IN_ATTACH_UAVMISSION_STATE(){
            this.dwSize = this.size();
        }
    }

    // 订阅任务消息出参
    public static class NET_OUT_ATTACH_UAVMISSION_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 赋值为结构体大小

        public NET_OUT_ATTACH_UAVMISSION_STATE(){
            this.dwSize = this.size();
        }
    }

    // 任务状态类型
    public static class ENUM_UAVMISSION_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAVMISSION_TYPE_UNKNOWN = 0;     // 未知类型
        public static final int   ENUM_UAVMISSION_TYPE_WP_UPLOAD = 1;   // 航点上传
        public static final int   ENUM_UAVMISSION_TYPE_WP_DOWNLOAD = 2; // 航点下载
    }

    // 任务状态
    public static class ENUM_UAVMISSION_STATE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   ENUM_UAVMISSION_STATE_UNKNOWN = 0;    // 未知类型
        public static final int   ENUM_UAVMISSION_STATE_BEGIN = 1;      // 开始
        public static final int   ENUM_UAVMISSION_STATE_UNDERWAY = 2;   // 进行
        public static final int   ENUM_UAVMISSION_STATE_SUCCESS = 3;    // 成功
        public static final int   ENUM_UAVMISSION_STATE_FAIL = 4;       // 失败
    }

    // 任务状态信息
    public static class NET_UAVMISSION_STATE extends NetSDKLibStructure.SdkStructure
    {
        public int              emType;                               // 类型(参见ENUM_UAVMISSION_TYPE枚举类型)
        public int              emState;                              // 状态(参见ENUM_UAVMISSION_STATE)
        public int              nTotalCount;                          // 总数
        public int              nSequence;                            // 当前航点编号
    }

    // 无人机任务状态回调
    public interface fUAVMissionStateCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NET_UAVMISSION_STATE pstuState,int dwStateInfoSize,Pointer dwUser);
    }

    // 无人机通用设置接口
    //emCmdType(参考ENUM_UAVCMD_TYPE枚举)
    //pParam对应ENUM_UAVCMD_TYPE所对应的结构体
    public boolean CLIENT_SendCommandToUAV(LLong lLoginID,int emCmdType,Pointer pParam,int nWaitTime);

    // 获取航点总数
    public boolean CLIENT_GetUAVMissonCount(LLong lLoginID,NET_IN_UAVMISSION_COUNT pstuInParam,NET_OUT_UAVMISSION_COUNT pstuOutParam,int nWaitTime);

    // 获取UAV航点信息
    public boolean CLIENT_ReadUAVMissions(LLong lLoginID,NET_IN_READ_UAVMISSION pstuInParam,NET_OUT_READ_UAVMISSION pstuOutParam,int nWaitTime);

    // 设置UAV航点信息
    public boolean CLIENT_WriteUAVMissions(LLong lLoginID,NET_IN_WRITE_UAVMISSION pstuInParam,NET_OUT_WRITE_UAVMISSION pstuOutParam,int nWaitTime);

    // 订阅UAV航点任务 pstuInParam 和 pstuOutParam 由设备申请释放
    public LLong CLIENT_AttachUAVMissonState(LLong lLoginID,NET_IN_ATTACH_UAVMISSION_STATE pstuInParam,NET_OUT_ATTACH_UAVMISSION_STATE pstuOutParam,int nWaitTime);

    // 退订UAV航点任务 lAttachHandle 是 CLIENT_AttachUAVMissonState 返回值
    public boolean CLIENT_DettachUAVMissonState(LLong lAttachHandle);

//////////////////////////////////////无人机航点功能结束///////////////////////////////////////////////////////////////////
    ////////////////////////////////////云上高速抓图起雾事件开始//////////////////////////////////////////
    // 起雾检测事件数据类型
    public static class EM_FOG_DETECTION_EVENT_TYPE 
    {
        public static final int   EM_FOG_DETECTION_EVENT_TYPE_UNKNOWN = 0; // 未知
        public static final int   EM_FOG_DETECTION_EVENT_TYPE_REAL = 1; // 实时数据
        public static final int   EM_FOG_DETECTION_EVENT_TYPE_ALARM = 2; // 报警数据
    }

    // 雾值
    public static class EM_FOG_LEVEL extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_FOG_LEVEL_UNKNOWN = 0;             // 未知
        public static final int   EM_FOG_LEVEL_NO = 1;                  // 无
        public static final int   EM_FOG_LEVEL_BLUE = 2;                // 蓝色预警
        public static final int   EM_FOG_LEVEL_YELLOW = 3;              // 黄色预警
        public static final int   EM_FOG_LEVEL_ORANGE = 4;              // 橙色预警
        public static final int   EM_FOG_LEVEL_RED = 5;                 // 红色预警
    }

    // 起雾检测事件雾信息
    public static class FOG_DETECTION_FOG_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emFogLevel;                           // 雾等级，参考EM_FOG_LEVEL
        public byte[]           byReserved = new byte[508];           // 预留字段
    }

    // 事件类型EVENT_IVS_FOG_DETECTION(起雾检测事件)对应的数据块描述信息
    public static class DEV_EVENT_FOG_DETECTION extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              emClassType;                          // 智能事件所属大类，参考EM_CLASS_TYPE
        public int              nGroupID;                             // 事件组ID，同一辆车抓拍过程内GroupID相同
        public int              nCountInGroup;                        // 一个事件组内的抓拍张数
        public int              nIndexInGroup;                        // 一个事件组内的抓拍序号
        public int              nPresetID;                            // 预置点号，从1开始有效
        public byte[]           szPresetName = new byte[128];         // 阈值点名称
        public int              emEventType;                          // 事件数据类型，参考EM_FOG_DETECTION_EVENT_TYPE
        public FOG_DETECTION_FOG_INFO stuFogInfo;                     // 雾信息
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stFileInfo;                        // 事件对应文件信息
        public byte[]           byReserved = new byte[1024];          // 预留字段
    }

    ////////////////////////////////////云上高速抓图起雾事件开始//////////////////////////////////////////
    ////////////////////////////////////云上高速抓图开始//////////////////////////////////////////、
    // CLIENT_ManualSnap 接口输入参数
    public static class NET_IN_MANUAL_SNAP extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannel;                             // 抓图通道号
        public int              nCmdSerial;                           // 请求序列号
        public byte[]           szFilePath = new byte[260];           // 抓图保存路径

        public NET_IN_MANUAL_SNAP(){
            this.dwSize = this.size();
        }
    }

    // 抓图图片编码格式
    public static class EM_SNAP_ENCODE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_SNAP_ENCODE_TYPE_UNKNOWN = 0;      // 未知
        public static final int   EM_SNAP_ENCODE_TYPE_JPEG = 1;         // jpeg图片
        public static final int   EM_SNAP_ENCODE_TYPE_MPEG4_I = 2;      // mpeg4的i 帧
    }

    // CLIENT_ManualSnap 接口输出参数
    public static class NET_OUT_MANUAL_SNAP extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nMaxBufLen;                           // pRcvBuf的长度,由用户指定
        public Pointer          pRcvBuf;                              // 接收图片缓冲, 用于存放抓图数据, 空间由用户申请和释放, 申请大小为nMaxBufLen
        public int              nRetBufLen;                           // 实际接收到的图片大小
        public int              emEncodeType;                         // 图片编码格式,参考EM_SNAP_ENCODE_TYPE
        public int              nCmdSerial;                           // 请求序列号
        public byte[]           bReserved = new byte[4];              // 字节对齐

        public NET_OUT_MANUAL_SNAP(){
            this.dwSize = this.size();
        }
    }

    // 手动抓图, 支持并发调用
    public boolean CLIENT_ManualSnap(LLong lLoginID,NET_IN_MANUAL_SNAP pInParam,NET_OUT_MANUAL_SNAP pOutParam,int nWaitTime);

    // 订阅抓图回调信息
    public static class NET_CB_ATTACH_SNAP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public Pointer          pRcvBuf;                              // 接收到的图片数据
        public int              nBufLen;                              // 图片数据长度
        public int              emEncodeType;                         // 图片编码格式,参考EM_SNAP_ENCODE_TYPE
        public int              nCmdSerial;                           // 抓图请求序列号
        public byte[]           byReserved = new byte[1028];          // 保留字节
    }

    // 订阅抓图回调函数原形
    public interface fAttachSnapRev extends Callback {
        public void invoke(LLong lAttachHandle,NET_CB_ATTACH_SNAP_INFO pstAttachCbInfo,Pointer dwUser);
    }

    // CLIENT_AttachSnap 接口输入参数
    public static class NET_IN_ATTACH_INTER_SNAP extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannel;                             // 抓图通道号
        public int              nCmdSerial;                           // 请求序列号
        public int              nIntervalSnap;                        // 定时抓图时间间隔
        public fAttachSnapRev   cbAttachSnapRev;                      // 回调函数
        public Pointer          dwUser;                               // 用户数据

        public NET_IN_ATTACH_INTER_SNAP(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_AttachSnap 接口输出参数
    public static class NET_OUT_ATTACH_INTER_SNAP extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_ATTACH_INTER_SNAP(){
            this.dwSize = this.size();
        }
    }

    // 订阅抓图
    public LLong CLIENT_AttachSnap(LLong lLoginID,NET_IN_ATTACH_INTER_SNAP pInParam,NET_OUT_ATTACH_INTER_SNAP pOutParam);

    // 取消订阅抓图
    public boolean CLIENT_DetachSnap(LLong lAttachHandle);

    ////////////////////////////////////云上高速抓图结束//////////////////////////////////////////
    // 二维码上报事件信息( DH_ALARM_QR_CODE_CHECK )
    public static class ALARM_QR_CODE_CHECK_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nEventID;                             // 事件ID
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public double           dbPTS;                                // 时间戳(单位是毫秒)
        public byte[]           szQRCode = new byte[256];             // 二维码字符串
        public byte[]           byReserved = new byte[1024];          // 预留字节
    }

    /************************************************************************/
    /*                            录像备份回传                                */
    /************************************************************************/
    // 录像备份恢复任务信息
    public static class NET_REC_BAK_RST_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nTaskID;                              // 任务ID
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.NET_DEV_ID_LEN_EX]; // 设备ID
        public int              nChannelID;                           // 通道号
        public NET_TIME         stuStartTime = new NET_TIME();        // 录像开始时间
        public NET_TIME         stuEndTime = new NET_TIME();          // 录像结束时间
        public int              nState;                               // 当前备份状态, 0-等待, 1-进行中, 2-完成, 3-失败, 4-暂停
        public NET_RECORD_BACKUP_PROGRESS stuProgress = new NET_RECORD_BACKUP_PROGRESS(); // 当前备份进度
        public int              emFailReason;                         // 失败的原因, 当nState字段为3的情况下有效,参考EM_RECORD_BACKUP_FAIL_REASON
        public NET_TIME         stuTaskStartTime = new NET_TIME();    // 任务开始时间, nState为"进行中"、"已完成"、"失败"的情况下该时间点有效;
        public NET_TIME         stuTaskEndTime = new NET_TIME();      // 任务结束时间, nState为"已完成"、"失败"的情况下该时间点有效;
        public  int             nRemoteChannel;                       // 备份源通道
        public double           dbLength;                             //该任务的总长度,单位字节, -1表示未知

        public NET_REC_BAK_RST_TASK(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_AddRecordBackupRestoreTask接口输入参数
    public static class NET_IN_ADD_REC_BAK_RST_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Pointer          pszDeviceID;                          // 设备ID
        public Pointer          pnChannels;                           // 通道数组
        public int              nChannelCount;                        // 通道数组大小,由用户申请内存,大小为sizeof(int)*nChannelCount
        public NET_TIME         stuStartTime;                         // 起始时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public byte[]           szUrl = new byte[256];                // RTSP URL
        public int              bIsOffline;                           //是否为前端断网续传任务
        public int              nStreamTypeNum;                       //码流类型数量
        public int[]            nStreamType = new int[16];            //码流类型数组 0:默认（主码流） 1：Jpg图片流；2：Main主码流 3：Extra1辅码流1 4：Extra2码流2
        public byte[]           szRecordSource = new byte[32];        //录像来源 "ChannelIDDirect"表示从ChannelID本身获取  "ChannelIDSubordinate"表示从ChannelID的从属获取
        public Pointer          pszChannelIDs;                        //通道ID列表,由用户申请内存,大小为64*nChannelIDNum,每个元素的长度都固定为：  64
        public int              nChannelIDNum;                        //通道ID列表数组大小

        public NET_IN_ADD_REC_BAK_RST_TASK(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_RemoveRecordBackupRestoreTask接口输入参数
    public static class NET_IN_REMOVE_REC_BAK_RST_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Pointer          pnTaskIDs;                            // 任务ID数组,由用户申请内存，大小为sizeof(int)*nTaskCount
        public int              nTaskCount;                           // 任务数量

        public NET_IN_REMOVE_REC_BAK_RST_TASK(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_QueryRecordBackupRestoreTask接口输入参数
    public static class NET_IN_QUERY_REC_BAK_RST_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;

        public NET_IN_QUERY_REC_BAK_RST_TASK(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_QueryRecordBackupRestoreTask接口输出参数
    public static class NET_OUT_QUERY_REC_BAK_RST_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public Pointer          pTasks;                               // 任务数组,由用户申请内存，大小为sizeof(NET_REC_BAK_RST_TASK)*nMaxCount
        public int              nMaxCount;                            // 数组大小
        public int              nReturnCount;                         // 返回的任务数量

        public NET_OUT_QUERY_REC_BAK_RST_TASK(){
            this.dwSize = this.size();
        }
    }

    // 开始录像备份恢复
    public LLong CLIENT_StartRecordBackupRestore(LLong lLoginID);

    // 停止录像备份恢复
    public void CLIENT_StopRecordBackupRestore(LLong lRestoreID);

    // 添加录像备份恢复任务,pInParam内存由用户申请释放
    public boolean CLIENT_AddRecordBackupRestoreTask(LLong lRestoreID,NET_IN_ADD_REC_BAK_RST_TASK pInParam,int nWaitTime);

    // 删除录像备份恢复任务,pInParam->NET_IN_REMOVE_REC_BAK_RST_TASK 内存由用户申请释放
    public boolean CLIENT_RemoveRecordBackupRestoreTask(LLong lRestoreID,Pointer pInParam,int nWaitTime);

    // 获取录像备份恢复任务信息,pInParam与pOutParam内存由用户申请释放
    // pInParam->NET_IN_QUERY_REC_BAK_RST_TASK pOutParam->NET_OUT_QUERY_REC_BAK_RST_TASK
    public boolean CLIENT_QueryRecordBackupRestoreTask(LLong lRestoreID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 导入配置文件(以JSON格式) pSendBuf: 待发送数据,用户分配内存, nSendBufLen: 待发送长度, reserved: 保留参数
    public boolean CLIENT_ImportConfigFileJson(LLong lLoginID,Pointer pSendBuf,int nSendBufLen,Pointer reserved,int nWaitTime);

    // 导出配置文件(以JSON格式) pOutBuffer: 接收缓冲,用户分配内存, maxlen: 接收缓冲长度, nRetlen: 实际导出长度, reserved: 保留参数
    public boolean CLIENT_ExportConfigFileJson(LLong lLoginID,Pointer pOutBuffer,int maxlen,IntByReference nRetlen,Pointer reserved,int nWaitTime);

    // web信息上传接口
    public boolean CLIENT_TransmitInfoForWeb(LLong lLoginID,Pointer szInBuffer,int dwInBufferSize,Pointer szOutBuffer,int dwOutBufferSize,Pointer pExtData,int waittime);

    //================================================GIP200413016开始============================================
    // 线圈信息（主要是里面的车辆信息）
    public static class COILS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nCarId;                               //  车辆Id（不是车牌号，ID是设备检测到物体记录的编号)
        public byte[]           PlateNum = new byte[64];              //  车牌号
        public int              emCarType;                            //  车辆类型(參考EM_NET_CARTYPE)
        public byte[]           byReserved = new byte[1024];          // 预留字节
    }

    // 车道信息
    public static class LANE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nLane;                                // 物理车道号（范围0~4）
        public int              nLaneType;                            // 车道类型，虚线车道：0， 实线车道：1
        public double           dbLaneOcc;                            // 车道空间占有率,范围[0.0~1.0]
        public int              nRoadwayNumber;                       // 自定义车道号（范围0~128）
        public int              nCurrentLaneVehicleNum;               // 当前车道车的数量
        public int              nVehicleNum;                          // 从上次统计结束到现在，通过车的辆数(设备0.5秒下发一次)
        public int              nCarId;                               // 保留最近有效过车的ID（不是车牌号），CarId是设备检测到物体记录的编号
        public double           dbCarEnterTime;                       // 编号CarId车辆进入虚线车道的时间
        public double           dbCarLeaveTime;                       // 编号CarId车辆离开实线车道的时间
        public int              nCarDistance;                         // 编号CarId车辆行驶的距离，单位：米
        public int              nQueueLen;                            // 车辆等待时的排队长度，单位：米
        public double           dbCarSpeed;                           // 编号CarId车辆平均车速，单位：米/秒
        public int              nCoilsInfoNum;                        // 实际返回线圈信息个数
        public COILS_INFO[]     stuCoilsInfo = (COILS_INFO[])new COILS_INFO().toArray(70*2); // 线圈信息（主要是线圈内的车辆信息）
        public int              nRetSolidLanNum;                      // 实际返回虚线车道个数
        public int[]            nSolidLaneNum = new int[6];           // 虚线车道对应的实线车道自定义车道号
        public int              nVehicleNumByTypeNum;                 // 实际返回车辆类型统计个数
        public int[]            nVehicleNumByType = new int[64];      // 类型车辆统计,数组下标对应不同车型（车型参考 EM_NET_CARTYPE），下标值对应车辆类型统计的数量
        public int              nEndLen;                              // 车辆运行时，尾部车辆位置距离停车线的距离 ，单位：米
        public byte[]           byReserved = new byte[1024];          // 预留字节
    }

    // 交通态势事件（NET_ALARM_TRAFFIC_XINKONG）
    public static class ALARM_TRAFFIC_XINKONG_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 只有一个事件动作0，表示脉冲事件
        public double           dbPTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           byReserved1 = new byte[4];            // 字节对齐
        public int              nLaneInfoNum;                         // 实际上报多少车道信息
        public LANE_INFO[]      stuLaneInfo = (LANE_INFO[])new LANE_INFO().toArray(6); // 车道信息
        public byte[]           byReserved = new byte[1024];          // 预留字节
    }

    //================================================GIP200413016结束============================================
    //================================================ERR200412034开始============================================
    // 同轴IO控制类型
    public static class EM_COAXIAL_CONTROL_IO_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_COAXIAL_CONTROL_IO_TYPE_UNKNOWN = 0; // 未知
        public static final int   EM_COAXIAL_CONTROL_IO_TYPE_LIGHT = 1; // 白光灯
        public static final int   EM_COAXIAL_CONTROL_IO_TYPE_SPEAKER = 2; // speak音频
    }

    // 同轴IO控制开关
    public static class EM_COAXIAL_CONTROL_IO_SWITCH extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_COAXIAL_CONTROL_IO_SWITCH_UNKNOWN = 0; // 未知
        public static final int   EM_COAXIAL_CONTROL_IO_SWITCH_OPEN = 1; // 开
        public static final int   EM_COAXIAL_CONTROL_IO_SWITCH_CLOSE = 2; // 关
    }

    // 同轴IO触发方式
    public static class EM_COAXIAL_CONTROL_IO_TRIGGER_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_COAXIAL_CONTROL_IO_TRIGGER_MODE_UNKNOWN = 0; // 未知
        public static final int   EM_COAXIAL_CONTROL_IO_TRIGGER_MODE_LINKAGE_TRIGGER = 1; // 联动触发
        public static final int   EM_COAXIAL_CONTROL_IO_TRIGGER_MODE_MANUAL_TRIGGER = 2; // 手动触发
    }

    // 同轴IO信息结构体
    public static class NET_COAXIAL_CONTROL_IO_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emType;                               // 同轴IO控制类型参考EM_COAXIAL_CONTROL_IO_TYPE
        public int              emSwicth;                             // 同轴IO控制开关参考EM_COAXIAL_CONTROL_IO_SWITCH
        public int              emMode;                               // 同轴IO触发方式参考EM_COAXIAL_CONTROL_IO_TRIGGER_MODE
        public byte[]           byReserved = new byte[128];           // 保留字节
    }

    // 发送同轴IO控制命令, CLIENT_ControlDeviceEx 入参 对应 CTRLTYPE_CTRL_COAXIAL_CONTROL_IO
    public static class NET_IN_CONTROL_COAXIAL_CONTROL_IO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannel;                             // 通道号
        public int              nInfoCount;                           // 同轴IO信息个数
        public NET_COAXIAL_CONTROL_IO_INFO[] stInfo = (NET_COAXIAL_CONTROL_IO_INFO[])new NET_COAXIAL_CONTROL_IO_INFO().toArray(NetSDKLibStructure.MAX_COAXIAL_CONTROL_IO_COUNT); // 同轴IO信息

        public NET_IN_CONTROL_COAXIAL_CONTROL_IO(){
            this.dwSize = this.size();
        }
    }

    // 发送同轴IO控制命令, CLIENT_ControlDeviceEx 出参 对应 CTRLTYPE_CTRL_COAXIAL_CONTROL_IO
    public static class NET_OUT_CONTROL_COAXIAL_CONTROL_IO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_CONTROL_COAXIAL_CONTROL_IO(){
            this.dwSize = this.size();
        }
    }

    //================================================ERR200412034结束============================================
    //================================================ERR200410078 DH-TPC-BF2221开始============================================
    // 火灾配置类型
    public static class NET_EM_FIREWARNING_MODE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   NET_EM_FIREWARNING_TYPE_PTZPRESET = 0; // 云台预置点模式（默认）
        public static final int   NET_EM_FIREWARNING_TYPE_SPACEEXCLUDE = 1; // 空间排除模式
    }

    // 火灾预警模式配置 NET_EM_CFG_FIRE_WARNINGMODE
    public static class NET_FIREWARNING_MODE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              emFireWarningMode;                    // 火灾预警模式 参考NET_EM_FIREWARNING_MODE_TYPE

        public NET_FIREWARNING_MODE_INFO(){
            this.dwSize = this.size();
        }
    }

    // 时间表信息
    public static class NET_CFG_TIME_SCHEDULE extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnableHoliday;                       // 是否支持节假日配置，默认为不支持，除非获取配置后返回为TRUE，不要使能假日配置
        public NetSDKLibStructure.NET_TSECT[]      stuTimeSection = (NetSDKLibStructure.NET_TSECT[])new NetSDKLibStructure.NET_TSECT().toArray(NetSDKLibStructure.NET_N_SCHEDULE_TSECT*NetSDKLibStructure.NET_N_REC_TSECT); // 第一维前7个元素对应每周7天，第8个元素对应节假日，每天最多6个时间段
    }

    // 火灾预警联动项
    public static class NET_FIREWARN_EVENTHANDLE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_CFG_TIME_SCHEDULE stuTimeSection;                  // 报警时间段
        public int              bRecordEnable;                        // 录像使能，必须同时有RecordChannels。使能为TRUE，且事件action为start开始录像，stop停止录像。如果FALSE，则不做录像
        public int              nRecordChannelNum;                    // 录像通道个数
        public int[]            nRecordChannels = new int[32];        // 录像通道号列表
        public int              nRecordLatch;                         // 录像延时时间（单位：秒）范围[10,300]
        public int              bAlarmOutEnable;                      // 报警输出使能
        public int              nAlarmOutChannelNum;                  // 报警输出通道个数
        public int[]            nAlarmOutChannels = new int[32];      // 报警输出通道号列表
        public int              nAlarmOutLatch;                       // 报警输入停止后，输出延时时间（单位：秒）范围[10, 300]
        public int              nPtzLinkNum;                          // 云台配置数
        public NetSDKLibStructure.SDK_PTZ_LINK[]   struPtzLink = (NetSDKLibStructure.SDK_PTZ_LINK[])new NetSDKLibStructure.SDK_PTZ_LINK().toArray(16); // 云台联动
        public int              bPtzLinkEnable;                       // 云台联动使能
        public int              bSnapshotEnable;                      // 快照使能
        public int              nSnapshotChannelNum;                  // 快照通道个数
        public int[]            nSnapshotChannels = new int[32];      // 快照通道号列表
        public int              bMailEnable;                          // 发送邮件，如果有图片，作为附件
        public NET_PTZ_LINK[]   stuPtzLinkEx = new NET_PTZ_LINK[16];  //云台联动项,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_PTZ_LINK}
        public NET_FIREWARN_PTZ_LINK_ENABLE[] stuPtzLinkEnable = new NET_FIREWARN_PTZ_LINK_ENABLE[16]; //云台联动项各参数使能，若要使用NET_PTZ_LINK中的参数，需将NET_FIREWARN_PTZ_LINK_ENABLE对应字段置为TRUE,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_FIREWARN_PTZ_LINK_ENABLE}
        public byte[]           byReserved = new byte[448];           // 保留字节
    }

    public static class NET_POSTIONF extends NetSDKLibStructure.SdkStructure
    {
        public float            fHorizontalAngle;                     // 水平角度 [-1,1]
        public float            fVerticalAngle;                       // 垂直角度 [-1,1]
        public float            fMagnification;                       // 放大倍数 [-1,1]
    }

    // 火警检测窗口
    public static class NET_FIREWARN_DETECTWND_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nRgnNum;                              // 检测区域的个数
        public byte[]           byReservedAlign = new byte[4];        // 保留字节
        public long[]           nRegions = new long[NetSDKLibStructure.MAX_FIREWARNING_DETECTRGN_NUM]; // 检测区域
        public NET_POSTIONF     stuPostion;                           // 空间排除信息
        public int              nTargetSize;                          // 目标的尺寸(火警配置为:Normal有效,单位：像素)
        public int              nSensitivity;                         // 检测灵敏度（火警配置为:Normal有效）
        public int              nWindowsID;                           // 窗口ID
        public byte[]           szName = new byte[32];                // 窗口名称
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // 火灾预警规则信息
    public static class NET_FIREWARN_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 火灾预警功能是否开启
        public int              nPresetId;                            // 预置点编号,火灾预警模式为预置点模式生效
        public int              nRow;                                 // 火灾检测区域的行数
        public int              nCol;                                 // 火灾检测区域的列数
        public int              emFireWarningDetectMode;              // 火警检测模式 参考NET_EM_FIREWARNING_DETECTMODE_TYPE
        public int              emFireWarningDetectTragetType;        // 火警检测目标类型 参考NET_EM_FIREWARNING_DETECTTARGET_TYPE
        public int              bTimeDurationEnable;                  // 是否启用持续时间
        public int              nFireDuration;                        // 观察火情持续时间，单位秒。水平旋转组检测火点时，为避免同一点重复检测，
        // 设置超时时间，超过此时间，跳过此点
        public NET_FIREWARN_EVENTHANDLE_INFO stuEventHandler;         // 火警联动信息
        public int              nDetectWindowNum;                     // 窗口个数
        public NET_FIREWARN_DETECTWND_INFO[] stuDetectWnd = (NET_FIREWARN_DETECTWND_INFO[])new NET_FIREWARN_DETECTWND_INFO().toArray(NetSDKLibStructure.MAX_FIREWARNING_DETECTWND_NUM); // 火警检测窗口
        public int              nGlobalSensitivity;                   //全局灵敏度，表示火情检测全局阈值，范围0-100，默认90
        public int              nShieldRegionNum;                     //屏蔽区域内存申请个数，最大1024
        public int              nShieldRegionRetNum;                  //屏蔽区域实际返回个数，获取配置时使用
        public Pointer          pstuShieldRegion;                     //火警屏蔽区域，需由用户申请内存，申请空间为sizeof(NET_FIREWARN_SHIELDREGION_INFO)*nShieldRegionNum,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_FIREWARN_SHIELDREGION_INFO}
        public byte[]           byReserved = new byte[236];           // 保留字节
    }

    // 火灾预警配置(结构体较大，建议用New分配内存) NET_EM_CFG_FIRE_WARNING
    public static class NET_FIRE_WARNING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nFireWarnRuleNum;                     // 火灾预警配置个数
        public NET_FIREWARN_RULE_INFO[] stuFireWarnRule = (NET_FIREWARN_RULE_INFO[])new NET_FIREWARN_RULE_INFO().toArray(NetSDKLibStructure.MAX_FIREWARNING_RULE_NUM); // 火灾预警配置规则

        public NET_FIRE_WARNING_INFO(){
            this.dwSize = this.size();
        }
    }

    //================================================ERR200410078 DH-TPC-BF2221结束============================================
    //================================================ERR200420018============================================
    // 事件类型 EVENT_IVS_VIDEOABNORMALDETECTION(视频异常事件)对应的数据块描述信息
    public static class DEV_EVENT_VIDEOABNORMALDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte             bType;                                // 异常类型, 255-无意义（通常是设备了返回错误值）0-视频丢失, 1-视频冻结, 2-摄像头遮挡, 3-摄像头移动, 4-过暗, 5-过亮, 6-图像偏色, 7-噪声干扰, 8-条纹检测
        public byte[]           byReserved = new byte[1];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage = new NetSDKLibStructure.SCENE_IMAGE_INFO_EX(); // 全景广角图信息
        public NET_IMAGE_INFO_EX2 stuImageInfo[] = (NET_IMAGE_INFO_EX2[])new NET_IMAGE_INFO_EX2().toArray(32); //图片信息数组
        public int              nImageInfoNum;                        //图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte             byReserved2[] = new byte[1024];       //预留字节
    }

    // 人员信息
    public static class NET_HUMAN extends NetSDKLibStructure.SdkStructure
    {
        public NET_RECT         stuBoundingBox;                       // 包围盒(8192坐标系)
        public int              nObjectID;                            // 物体ID
        public byte[]           szSerialUUID = new byte[22];          // 智能物体全局唯一物体标识
        // 有效数据位21位，包含’\0’
        // 前2位%d%d:01-视频片段, 02-图片, 03-文件, 99-其他
        // 中间14位YYYYMMDDhhmmss:年月日时分秒
        // 后5位%u%u%u%u%u：物体ID，如00001
        public byte[]           bReserved = new byte[230];            // 保留字节
    }

    // 事件类型 EVENT_IVS_STAY_ALONE_DETECTION (单人独处事件) 对应的数据块描述信息
    public static class DEV_EVENT_STAY_ALONE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public int              emClassType;                          // 智能事件所属大类参考EM_CLASS_TYPE
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              UTCMS;                                // UTC时间对应的毫秒数
        public int              nEventID;                             // 事件ID
        public NET_HUMAN        stuHuman;                             // 人员信息
        public NetSDKLibStructure.SCENE_IMAGE_INFO_EX stuSceneImage;                     // 全景图
        public int              nDetectRegionNum;                     // 检测区域顶点数
        public DH_POINT[]       stuDetectRegion = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 检测区域
        public int              nImageInfoNum;                        // 图片信息个数
        public Pointer          pstuImageInfo;                        // 图片信息数组, refer to {@link NET_IMAGE_INFO_EX3}
        public Pointer          pstuHumanAttributesEx;                //人体属性信息扩展,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.HUMAN_ATTRIBUTES_INFO_EX}
        public HUMAN_ATTRIBUTES_INFO stuHumanAttributes = new HUMAN_ATTRIBUTES_INFO(); //人体属性信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.HUMAN_ATTRIBUTES_INFO}
        public NetSDKLibStructure.HUMAN_IMAGE_INFO stuHumanImage = new NetSDKLibStructure.HUMAN_IMAGE_INFO(); //人体图片信息,有人体信息时携带,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.HUMAN_IMAGE_INFO}
        public NetSDKLibStructure.FACE_IMAGE_INFO  stuFaceImage = new NetSDKLibStructure.FACE_IMAGE_INFO(); //人脸图片信息,有人脸信息时携带,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.FACE_IMAGE_INFO}
        public NetSDKLibStructure.FACE_SCENE_IMAGE stuFaceSceneImage = new NetSDKLibStructure.FACE_SCENE_IMAGE(); //人脸全景图,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.FACE_SCENE_IMAGE}
        public NetSDKLibStructure.NET_FACE_ATTRIBUTE stuFaceAttributes = new NetSDKLibStructure.NET_FACE_ATTRIBUTE(); //人脸属性,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_FACE_ATTRIBUTE}
        public byte[]           byReserved = new byte[360-2*NetSDKLibStructure.POINTERSIZE]; // 保留字节
    }

    // 事件类型 EVENT_IVS_PSRISEDETECTION (囚犯起身事件) 对应的数据块描述信息
    public static class DEV_EVENT_PSRISEDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public DH_MSG_OBJECT    stuObject;                            // 检测到的物体
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public DH_POINT[]       DetectRegion = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public double           dInitialUTC;                          // 事件初始UTC时间    UTC为事件的UTC (1970-1-1 00:00:00)秒数。
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public byte[]           szSerialUUID = new byte[22];          // 智能物体全局唯一物体标识
        // 有效数据位21位，包含’\0’
        // 前2位%d%d:01-视频片段, 02-图片, 03-文件, 99-其他
        // 中间14位YYYYMMDDhhmmss:年月日时分秒
        // 后5位%u%u%u%u%u：物体ID，如00001
        public byte[]           bReserved = new byte[594];            // 保留字节,留待扩展.
    }

    //--------------------------------------------------------ERR200420018结束------------------------------------------------------------------------//
    //--------------------------------------------------------ERR200424047------------------------------------------------------------------------//
    // 逻辑屏显示内容
    public static class NET_TRAFFIC_LATTICE_SCREEN_SHOW_CONTENTS extends NetSDKLibStructure.SdkStructure
    {
        public int              emContents;                           // 逻辑屏显示的内容：参考NET_EM_SCREEN_SHOW_CONTENTS
        public byte[]           byReserved1 = new byte[4];            // 字节对齐
        public byte[]           szCustomStr = new byte[32];           // 自定义内容，emContents为	EM_TRAFFIC_LATTICE_SCREEN_CUSTOM 时有效
        public byte[]           byReserved = new byte[32];            // 预留
    }

    // 点阵屏显示信息
    public static class NET_TRAFFIC_LATTICE_SCREEN_SHOW_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_TRAFFIC_LATTICE_SCREEN_SHOW_CONTENTS[] stuContents = (NET_TRAFFIC_LATTICE_SCREEN_SHOW_CONTENTS[]) new NET_TRAFFIC_LATTICE_SCREEN_SHOW_CONTENTS().toArray(64); // 逻辑屏显示内容
        public int              nContentsNum;                         // 逻辑屏个数
        public byte[]           byReserved = new byte[1020];          // 预留
    }

    // 点阵屏显示信息配置, 对应枚举 NET_EM_CFG_TRAFFIC_LATTICE_SCREEN
    public static class NET_CFG_TRAFFIC_LATTICE_SCREEN_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nStatusChangeTime;                    // 状态切换间隔，单位：秒,取值10 ~ 60
        public NET_TRAFFIC_LATTICE_SCREEN_SHOW_INFO stuNormal = new NET_TRAFFIC_LATTICE_SCREEN_SHOW_INFO(); // 常态下
        public NET_TRAFFIC_LATTICE_SCREEN_SHOW_INFO stuCarPass = new NET_TRAFFIC_LATTICE_SCREEN_SHOW_INFO(); // 过车时
        public int              emShowType;                           /** 显示方式 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_LATTICE_SCREEN_SHOW_TYPE}*/
        public int              emControlType;                        /** 控制方式 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_LATTICE_SCREEN_CONTROL_TYPE}*/
        public int              emBackgroundMode;                     /** 逻辑屏背景风格模式 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_EM_LATTICE_SCREEN_BACKGROUND_MODE} */
        public byte[]           szPlayList = new byte[10*64];         // 资源文件播放列表,支持视频文件和图片文件播放,按照数组顺序循环播放
        public int              nPlayListNum;                         // 资料文件个数
        public NET_TRAFFIC_LATTICE_SCREEN_LOGO_INFO stuLogoInfo = new NET_TRAFFIC_LATTICE_SCREEN_LOGO_INFO(); // Logo信息
        public   NET_TRAFFIC_LATTICE_SCREEN_ALARM_NOTICE_INFO stuAlarmNoticeInfo = new NET_TRAFFIC_LATTICE_SCREEN_ALARM_NOTICE_INFO(); // 报警提示显示信息

        public NET_CFG_TRAFFIC_LATTICE_SCREEN_INFO(){
            this.dwSize = this.size();
        }
    }

    // 车位灯色
    public static class NET_PARKINGSPACELIGHT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nRed;                                 // 红灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nYellow;                              // 黄灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nBlue;                                // 蓝灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nGreen;                               // 绿灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nPurple;                              // 紫灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nWhite;                               // 白灯: -1:无效, 0/灭, 1/亮, 2/闪烁
        public int              nPink;                                // 粉等: -1:无效, 0/灭, 1/亮, 2/闪烁
        public byte             nColorCount;                          //Color个数
        public byte[]           nColor = new byte[8];                 //颜色：0:未知, 1:灭或闪烁, 2:红灯, 3:黄灯, 4:蓝灯, 5:绿灯, 6:紫灯, 7:白灯, 8:粉灯, 9:青灯
        public byte             szReserved1;                          //字节对齐
        public int              nLightKeepTime;                       //车位状态亮灯时间 单位：秒，取值范围 -1 ~ 300，0表示不亮，-1表示常亮
        public byte[]           byReserved = new byte[16];            // 保留字节

        public void setInfo(int nRed, int nYellow, int nBlue, int nGreen, int nPurple, int nWhite,int nPink) {
            this.nRed = nRed;
            this.nYellow = nYellow;
            this.nBlue = nBlue;
            this.nGreen = nGreen;
            this.nPurple = nPurple;
            this.nWhite = nWhite;
            this.nPink = nPink;
        }
    }

    // 网络异常状态灯色
    public static class NET_NETWORK_EXCEPTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public NET_PARKINGSPACELIGHT_INFO[] stNetPortAbortInfo = (NET_PARKINGSPACELIGHT_INFO[]) new NET_PARKINGSPACELIGHT_INFO().toArray(5); // 网口断开状态灯色
        public int              nRetNetPortAbortNum;                  // 实际返回的个数
        public NET_PARKINGSPACELIGHT_INFO stuSpaceSpecialInfo;        // 车位专用状态灯色
        public NET_PARKINGSPACELIGHT_INFO stuSpaceChargingInfo;       // 充电车位状态灯色
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // 车位状态对应的车位指示灯色 对应 NET_EM_CFG_PARKINGSPACELIGHT_STATE
    public static class NET_PARKINGSPACELIGHT_STATE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public NET_PARKINGSPACELIGHT_INFO stuSpaceFreeInfo;           // 车位空闲状态灯色
        public NET_PARKINGSPACELIGHT_INFO stuSpaceFullInfo;           // 车位占满状态灯色
        public NET_PARKINGSPACELIGHT_INFO stuSpaceOverLineInfo;       // 车位压线状态灯色
        public NET_PARKINGSPACELIGHT_INFO stuSpaceOrderInfo;          // 车位预定状态灯色
        public NET_NETWORK_EXCEPTION_INFO stuNetWorkExceptionInfo;    // 网络异常状态灯色
        public NET_ABNORMAL_ALARM_INFO stuAbnormalAlarmInfo = new NET_ABNORMAL_ALARM_INFO(); //设备异常报警状态灯色控制,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_ABNORMAL_ALARM_INFO}
        public NET_PARKINGSPACELIGHT_INFO stuSpaceAlarmInfo = new NET_PARKINGSPACELIGHT_INFO(); //车位报警状态灯色,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_PARKINGSPACELIGHT_INFO}

        public NET_PARKINGSPACELIGHT_STATE_INFO(){
            this.dwSize = this.size();
        }
    }

    // 车位监管状态
    public static class EM_CFG_LANE_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CFG_LANE_STATUS_UNKOWN = -1;       // 状态未知
        public static final int   EM_CFG_LANE_STATUS_UNSUPERVISE = 0;   // 不监管
        public static final int   EM_CFG_LANE_STATUS_SUPERVISE = 1;     // 监管
    }

    // 单个车位指示灯本机配置
    public static class CFG_PARKING_SPACE_LIGHT_GROUP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 为TRUE时该配置生效，为FALSE时该配置无效
        public int[]            emLaneStatus = new int[NetSDKLibStructure.MAX_LANES_NUM]; // 灯组监管的车位，下标表示车位号；参考EM_CFG_LANE_STATUS
        public int              nLanesNum;                            // 有效的车位数量（可以设为监管或不监管的车位数量）
        public int              bAcceptNetCtrl;                       // 是否接受远程控制
    }

    // 车位指示灯本机配置 CFG_CMD_PARKING_SPACE_LIGHT_GROUP
    public static class CFG_PARKING_SPACE_LIGHT_GROUP_INFO_ALL extends NetSDKLibStructure.SdkStructure
    {
        public int              nCfgNum;                              // 获取到的配置个数
        public CFG_PARKING_SPACE_LIGHT_GROUP_INFO[] stuLightGroupInfo = (CFG_PARKING_SPACE_LIGHT_GROUP_INFO[]) new CFG_PARKING_SPACE_LIGHT_GROUP_INFO().toArray(NetSDKLibStructure.MAX_LIGHT_GROUP_INFO_NUM); // 车位指示灯本机配置
        public CFG_PARKING_SPACE_LIGHT_GROUP_INFO[] stuLightGroupInfoEx = new CFG_PARKING_SPACE_LIGHT_GROUP_INFO[16]; //车位指示灯本机配置扩展,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.CFG_PARKING_SPACE_LIGHT_GROUP_INFO}
        public int              nCfgNumEx;                            //获取到的配置个数扩展
        public int              bIsUseLightGroupInfoEx;               //使用扩展配置下发配置
        public byte[]           szResvered = new byte[508];           //保留字节
    }

    //--------------------------------------------------------ERR200424047结束------------------------------------------------------------------------//
    //--------------------------------------------------------ERR200426006------------------------------------------------------------------------//
    // 灯光设备类型
    public static class EM_LIGHT_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_LIGHT_TYPE_UNKNOWN = 0;            // 未知类型
        public static final int   EM_LIGHT_TYPE_COMMLIGHT = 1;          // 普通灯光
        public static final int   EM_LIGHT_TYPE_LEVELLIGHT = 2;         // 可调光
    }

    // 串口地址
    public static class CFG_COMMADDR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nAddressNum;                          // 串口地址个数
        public int[]            nAddress = new int[NetSDKLibStructure.MAX_ADDRESS_NUM];  // 地址描述,不同厂商地址位不同，用数组表示
    }

    // 灯光设备配置信息 (对应 CFG_CMD_LIGHT )
    public static class CFG_LIGHT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.MAX_DEVICE_ID_LEN]; // 设备编码,惟一标识符
        public byte[]           szName = new byte[NetSDKLibStructure.MAX_DEVICE_MARK_LEN]; // 设备描述
        public byte[]           szBrand = new byte[NetSDKLibStructure.MAX_BRAND_NAME_LEN]; // 设备品牌
        public CFG_COMMADDR_INFO stuCommAddr;                         // 串口地址
        public int              nPositionID;                          // 设备在区域中编号
        public NetSDKLibStructure.CFG_POLYGON      stuPosition;       // 坐标
        public int              nState;                               // 设备状态: 1-打开,0-关闭
        public int              nRange;                               // 灯亮度幅度值 0-7 , emType 为 EM_LIGHT_TYPE_ADJUSTABLE 有意义
        public int              emType;                               // 灯光设备类型;参考EM_LIGHT_TYPE
    }

    // 近光灯信息
    public static class CFG_NEARLIGHT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 是否使能，TRUE使能，FALSE不使能
        public int              dwLightPercent;                       // 灯光亮度百分比值(0~100)
        public int              dwAnglePercent;                       // 灯光角度百分比值(0~100)
    }

    // 远光灯信息
    public static class CFG_FARLIGHT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 是否使能，TRUE使能，FALSE不使能
        public int              dwLightPercent;                       // 灯光亮度百分比值(0~100)
        public int              dwAnglePercent;                       // 灯光角度百分比值(0~100)
    }

    // 灯光设置详情
    public static class CFG_LIGHTING_DETAIL extends NetSDKLibStructure.SdkStructure
    {
        public int              nCorrection;                          // 灯光补偿 (0~4) 倍率优先时有效
        public int              nSensitive;                           // 灯光灵敏度(0~5)倍率优先时有效，默认为3
        public int              emMode;                               // 灯光模式,参考EM_CFG_LIGHTING_MODE
        public int              nNearLight;                           // 近光灯有效个数
        public CFG_NEARLIGHT_INFO[] stuNearLights = (CFG_NEARLIGHT_INFO[]) new CFG_NEARLIGHT_INFO().toArray(NetSDKLibStructure.MAX_LIGHTING_NUM); // 近光灯列表
        public int              nFarLight;                            // 远光灯有效个数
        public CFG_FARLIGHT_INFO[] stuFarLights = (CFG_FARLIGHT_INFO[]) new CFG_FARLIGHT_INFO().toArray(NetSDKLibStructure.MAX_LIGHTING_NUM); // 远光灯列表
    }

    // 灯光模式
    public static class EM_CFG_LIGHTING_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_CFG_LIGHTING_MODE_UNKNOWN = 0;     // 未知
        public static final int   EM_CFG_LIGHTING_MODE_MANUAL = 1;      // 手动
        public static final int   EM_CFG_LIGHTING_MODE_ZOOMPRIO = 2;    // 倍率优先
        public static final int   EM_CFG_LIGHTING_MODE_TIMING = 3;      // 定时模式
        public static final int   EM_CFG_LIGHTING_MODE_AUTO = 4;        // 自动
        public static final int   EM_CFG_LIGHTING_MODE_OFF = 5;         // 关闭模式
    }

    // 灯光设置(对应 CFG_CMD_LIGHTING 命令)
    public static class CFG_LIGHTING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nLightingDetailNum;                   // 灯光设置有效个数
        public CFG_LIGHTING_DETAIL[] stuLightingDetail = (CFG_LIGHTING_DETAIL[]) new CFG_LIGHTING_DETAIL().toArray(NetSDKLibStructure.MAX_LIGHTING_DETAIL_NUM); // 灯光设置信息列表
    }

    //--------------------------------------------------------ERR200426006结束------------------------------------------------------------------------//
    //--------------------------------------------------------ERR200427081开始------------------------------------------------------------------------//
    // 事件类型EVENT_IVS_HIGH_TOSS_DETECT(高空抛物检测事件)对应的数据块描述信息
    public static class DEV_EVENT_HIGH_TOSS_DETECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        /**
         *通道号
         */
        public int              nChannelID;
        /**
         * 0:脉冲
         * 1:开始
         * 2:停止
         */
        public int              nAction;
        /**
         * 事件名称
         */
        public byte[]           szName = new byte[128];
        /**
         * 时间戳(单位是毫秒)
         */
        public double           PTS;
        /**
         * 事件发生的时间
         */
        public NET_TIME_EX      UTC;
        /**
         * 事件ID
         */
        public int              nEventID;
        /**
         * 智能事件规则编号，用于标示哪个规则触发的事件
         */
        public int              nRuleID;
        /**
         * 智能事件所属大类,枚举值参考{@link EM_CLASS_TYPE}
         */
        public int              emClassType;
        /**
         * 物体信息
         */
        public NET_HIGHTOSS_OBJECT_INFO[] stuObjInfos = (NET_HIGHTOSS_OBJECT_INFO[]) new NET_HIGHTOSS_OBJECT_INFO().toArray(50);
        /**
         * 物体个数
         */
        public int              nObjNum;
        /**
         * 检测区域顶点数
         */
        public int              nDetectRegionNum;
        /**
         * 检测区域,[0,8192)
         */
        public NetSDKLibStructure.NET_POINT[]      stuDetectRegion = (NetSDKLibStructure.NET_POINT[])new NetSDKLibStructure.NET_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM);
        /**
         * 视频分析帧序号
         */
        public int              nFrameSequence;
        /**
         * 事件组ID, 同一物体抓拍过程内GroupID相同
         */
        public int              nGroupID;
        /**
         * 抓拍序号，从1开始
         */
        public int              nIndexInGroup;
        /**
         * 抓拍张数
         */
        public int              nCountInGroup;
        /**
         * 图片信息
         */
        public NET_EVENT_IMAGE_OFFSET_INFO stuImageInfo;
        /**
         * 是否上传大图, true表示第一个图片为全景大图，信息由ImageInfo来表示
         */
        public int              bIsGlobalScene;
        /**
         * 用于标记抓拍帧
         */
        public int              nMark;
        /**
         * 预留字节
         */
        public byte[]           byReserved = new byte[384];

        @Override
        public String toString() {
            return "DEV_EVENT_HIGH_TOSS_DETECT_INFO{" +
                    "通道号=" + nChannelID +
                    ", nAction=" + nAction +
                    ", 事件名称=" + new String(szName, Charset.forName("GBK")).trim() +
                    ", s事件戳=" + PTS +
                    ", 事件发生时间=" + UTC.toString() +
                    ", nEventID=" + nEventID +
                    ", 智能事件规则编号=" + nRuleID +
                    ", 智能事件所属大类=" + emClassType +
                    ", stuObjInfos=" + Arrays.toString(stuObjInfos) +
                    ", 物体个数=" + nObjNum +
                    ", 检测区域顶点数=" + nDetectRegionNum +
                    ", stuDetectRegion=" + Arrays.toString(stuDetectRegion) +
                    ", 帧序号=" + nFrameSequence +
                    ", 事件组ID=" + nGroupID +
                    ", 抓拍序号=" + nIndexInGroup +
                    ", 抓拍张数=" + nCountInGroup +
                    ", 图片信息=" + stuImageInfo +
                    ", 是否大图=" + bIsGlobalScene +
                    ", 标记抓拍帧=" + nMark +
                    '}';
        }
    }

    public static class NET_EVENT_IMAGE_OFFSET_INFO extends NetSDKLibStructure.SdkStructure {
        /**
         * 偏移
         */
        public int              nOffSet;
        /**
         * 图片大小,单位字节
         */
        public int              nLength;
        /**
         * 图片宽度
         */
        public int              nWidth;
        /**
         * 图片高度
         */
        public int              nHeight;
        /**
         * 图片路径
         */
        public byte[]           szPath = new byte[260];
        public int              nIndexInData;                         //在上传图片数据中的图片序号
        /**
         * 保留字节
         */
        public byte[]           byReserved = new byte[248];

        @Override
        public String toString() {
            return "NET_EVENT_IMAGE_OFFSET_INFO{" +
                    "nOffSet=" + nOffSet +
                    ", nLength=" + nLength +
                    ", nWidth=" + nWidth +
                    ", nHeight=" + nHeight +
                    ", szPath=" + new String(szPath,Charset.forName("GBK")) +
                    '}';
        }
    }

    // 高空抛物物体信息
    public static class NET_HIGHTOSS_OBJECT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nObjectID;                            // 物体ID
        public int              emObjAction;                          // 物体动作类型;参考EM_HIGHTOSS_ACTION_TYPE
        public NET_RECT         stuBoundingBox;                       // 包围盒
        public int              nConfidence;                          // 置信度
        public int              emObjectType;                         // 物体类型;参考EM_ANALYSE_OBJECT_TYPE
        public NetSDKLibStructure.NET_POINT        stuCenter;         // 物体型心
        public NET_EVENT_IMAGE_OFFSET_INFO stuImageInfo;              // 抓拍小图
        public byte[]           byReserved = new byte[1516];          // 预留字节 		// 预留字节

        @Override
        public String toString() {
            return "NET_HIGHTOSS_OBJECT_INFO{" +
                    "nObjectID=" + nObjectID +
                    ", 动作类型=" + emObjAction +
                    ", stuBoundingBox=" + stuBoundingBox +
                    ", 置信度=" + nConfidence +
                    ", 物体类型=" + emObjectType +
                    ",物体型心="+stuCenter +
                    ",抓拍小图="+stuImageInfo.toString()+
                    '}';
        }
    }

    // 登陆时TLS加密模式
    public static class EM_LOGIN_TLS_TYPE extends NetSDKLibStructure.SdkStructure {
        public static final int   EM_LOGIN_TLS_TYPE_NO_TLS = 0;         // 不走tls加密, 默认方式
        public static final int   EM_LOGIN_TLS_TYPE_TLS_ADAPTER = 1;    // 自适应tls加密
        public static final int   EM_LOGIN_TLS_TYPE_TLS_COMPEL = 2;     // 强制tls加密
        public static final int   EM_LOGIN_TLS_TYPE_TLS_MAIN_ONLY = 3;  // 部分tls加密
    }

    // CLIENT_LoginWithHighLevelSecurity 输入参数
    public static class NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public byte[]           szIP = new byte[64];                  // IP
        public int              nPort;                                // 端口
        public byte[]           szUserName = new byte[64];            // 用户名
        public byte[]           szPassword = new byte[64];            // 密码
        public int              emSpecCap;                            // 登录模式
        public byte[]           byReserved = new byte[4];             // 字节对齐
        public Pointer          pCapParam;                            // 见 CLIENT_LoginEx 接口 pCapParam 与 nSpecCap 关系
        public int              emTLSCap;                             //登录的TLS模式，参考EM_LOGIN_TLS_TYPE，目前仅支持EM_LOGIN_SPEC_CAP_TCP，EM_LOGIN_SPEC_CAP_SERVER_CONN 模式下的 tls登陆
        public byte[]           szLocalIP = new byte[64];             //本地ip

        public NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_LoginWithHighLevelSecurity 输出参数
    public static class NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public NetSDKLibStructure.NET_DEVICEINFO_Ex stuDeviceInfo;                       // 设备信息
        public int              nError;                               // 错误码，见 CLIENT_Login 接口错误码
        public byte[]           byReserved = new byte[132];           // 预留字段

        public NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 高安全级别登陆
    public LLong CLIENT_LoginWithHighLevelSecurity(NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam,NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam);

    // GDPR使能全局开关
    public void CLIENT_SetGDPREnable(boolean bEnable);

    // 合成通道配置(对应CFG_CMD_COMPOSE_CHANNEL)
    public static class CFG_COMPOSE_CHANNEL extends NetSDKLibStructure.SdkStructure
    {
        public int              emSplitMode;                          // 分割模式,写入枚举值 枚举值在 CFG_SPLITMODE 类中定义，不要自己写
        public int[]            nChannelCombination = new int[NetSDKLibStructure.MAX_VIDEO_CHANNEL_NUM]; // 割模式下的各子窗口显示内容  最大 MAX_VIDEO_CHANNEL_NUM
        public int              nChannelCount;                        // 分割窗口数量
        public byte[]           szPlanName = new byte[16];            //合成通道使用的预案
        public int              nAudioSrcNum;                         //合成通道的音频源个数
        public int[]            nAudioSrc = new int[32];              //合成通道的音频源
        public byte[]           szAudioType = new byte[16];           //音频源的编码类型
        public int              nWindowNum;                           //合成通道窗口个数
        public CFG_COMPOSE_CHANNEL_WINDOW[] stuWindow = new CFG_COMPOSE_CHANNEL_WINDOW[16]; //合成通道各个窗口的信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.CFG_COMPOSE_CHANNEL_WINDOW}
        public byte[]           szReserved = new byte[1024];          //预留字节
    }

    // 画中画方案
    public static class CFG_PICINPIC_INFO extends NetSDKLibStructure.SdkStructure
    {
        public      int         nMaxSplit;                            // 内存申请的CFG_SPLIT_INFO个数,最大值通过CLIENT_GetSplitCaps接口获取，见nModeCount
        public      int         nReturnSplit;                         // 解析得到实际使用的或封装发送的CFG_SPLIT_INFO个数
        public      Pointer     pSplits;                              // 分割方案，指向 CFG_SPLIT_INFO
    }

    // 分割方案
    public static class CFG_SPLIT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emSplitMode;                          // CFG_SPLITMODE分割模式，通过CLIENT_GetSplitCaps接口获取，见emSplitMode
        public int              nMaxChannels;                         // 申请内存CFG_SPLIT_CHANNEL_INFO个数, 比如有16个通道，nMaxChannels就是16，SPLITMODE_4模式，则按顺序依次分为4组
        public int              nReturnChannels;                      // 解析返回通道个数,要封装发送的通道个数
        public Pointer          pSplitChannels;                       // 分割通道信息,指向 CFG_SPLIT_CHANNEL_INFO
    }

    // 分割通道
    public static class CFG_SPLIT_CHANNEL_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 使能
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.AV_CFG_Device_ID_Len]; // 设备ID
        public int              nChannelID;                           // 通道号(0开始)
        public int              nMaxSmallChannels;                    // 小画面通道个数，每个通道一个CFG_SMALLPIC_INFO,这里最大应该是设备通道数减一
        public int              nReturnSmallChannels;                 // 解析返回的或封装发送的小画面通道个数
        public Pointer          pPicInfo;                             // 小画面信息 CFG_SMALLPIC_INFO
    }

    // 审讯画中画需求
    // 小画面窗口信息
    public static class CFG_SMALLPIC_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szDeviceID = new byte[NetSDKLibStructure.AV_CFG_Device_ID_Len]; // 设备ID
        public int              nChannelID;                           // 通道号(0开始)
        public int              bAudio;                               // 大画面是否混合小画面音频
        public NetSDKLibStructure.CFG_RECT         stuPosition;       // 使用相对坐标体系，取值均为0-8192,在整个屏幕上的位置
    }

    //--------------------------------------------------------ERR200507125开始------------------------------------------------------------------------//
    // 空闲动作配置信息
    public static class CFG_IDLE_MOTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 使能
        public int              nTime;                                // 启动空闲动作的时间1~60分钟
        public int              emFunction;                           // 空闲动作功能,见枚举 EM_CFG_IDLEMOTION_FUNCTION
        public int              nPresetId;                            // 预置点编号,   范围参照CFG_PTZ_PROTOCOL_CAPS_INFO的wPresetMin和wPresetMax
        public int              nScanId;                              // 自动线扫编号, 范围参照CFG_PTZ_PROTOCOL_CAPS_INFO的wAutoScanMin和wAutoScanMax
        public int              nTourId;                              // 巡航编号,     范围参照CFG_PTZ_PROTOCOL_CAPS_INFO的wTourMin和wTourMax
        public int              nPatternId;                           // 自动巡迹编号, 范围参照CFG_PTZ_PROTOCOL_CAPS_INFO的wPatternMin和wPatternMax
        public int              nSecond;                              // 启动空闲动作的时长（秒数）范围0-59秒,总时长为nTime * 60 + nSecond
    }

    //--------------------------------------------------------ERR200507125结束------------------------------------------------------------------------//
    //--------------------------------------------------------ERR200513038-TASK1开始------------------------------------------------------------------------//
    // 每个通道的RTMP信息
    public static class NET_CHANNEL_RTMP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 是否使能
        public int              nChannel;                             // 通道号（URL中的Channel）
        public byte[]           szUrl = new byte[512];                // RTMP连接URL
        public byte[]           byReserved = new byte[1024];          // 预留字段
    }

    // RTMP 配置
    public static class NET_CFG_RTMP_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小，赋值为sizeof(NET_CFG_RTMP_INFO)
        public int              bEnable;                              // RTMP配置是否开启
        public byte[]           szAddr = new byte[256];               // RTMP服务器地址
        public int              nPort;                                // RTMP服务器端口
        public byte[]           byReserved = new byte[4];             // 字节对齐
        public byte[]           szCustomPath = new byte[256];         // 路径名
        public byte[]           szStreamPath = new byte[256];         // 码流路径前缀:不同通道以后缀数字区分
        public byte[]           szKey = new byte[128];                // 获取RTMP地址时的Key
        public Pointer          pstuMainStream;                       // 主码流信息，用户分配内存，内存大小为 sizeof(NET_CHANNEL_RTMP_INFO) * nMainStream
        public int              nMainStream;                          // pstuMainStream 个数
        public int              nMainStreamRet;                       // 返回的 pstuMainStream 个数（获取配置时有效）
        public Pointer          pstuExtra1Stream;                     // 辅码流1信息，用户分配内存，内存大小为 sizeof(NET_CHANNEL_RTMP_INFO) * nExtra1Stream
        public int              nExtra1Stream;                        // pstuExtra1Stream 个数
        public int              nExtra1StreamRet;                     // 返回的 nExtra1StreamRet 个数（获取配置时有效）
        public Pointer          pstuExtra2Stream;                     // 辅码流2信息，用户分配内存，内存大小为 sizeof(NET_CHANNEL_RTMP_INFO) * nExtra2Stream
        public int              nExtra2Stream;                        // pstuExtra2Stream 个数
        public int              nExtra2StreamRet;                     // 返回的 nExtra2StreamRet 个数（获取配置时有效）

        public NET_CFG_RTMP_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //--------------------------------------------------------ERR200513038开始------------------------------------------------------------------------//
    //--------------------------------------------------------GIP200520016实现------------------------------------------------------------------------//
    // 轮询任务对象
    public static class NET_POLLING_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emSourceType;                         // 数据源类型 参考EM_DATA_SOURCE_TYPE
        public Pointer          pSourceData;                          // 数据源信息, 根据emSouceType对应不一样的结构体
        public byte[]           szUserData = new byte[64];            // 视频源数据，标示视频源信息。在返回结果时，原封不动的带上。当任务的包含多个视频源时，attachResult每个视频源单独上报结果
        public byte[]           byReserved = new byte[256];           // 保留字节
    }

    // 接口输出参数
    public static class NET_IN_ADD_POLLING_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nIntervalTime;                        // 每个视频源的检测执行时间，单位为秒，1~65535
        public int              nLoopCount;                           // 诊断轮询次数, 0代表永久轮询
        public int              nInfoCount;                           // 任务对象个数
        public Pointer          pInfoList;                            // 任务对象列表(参考NET_POLLING_INFO)

        public NET_IN_ADD_POLLING_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 接口输出参数
    public static class NET_OUT_ADD_POLLING_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID

        public NET_OUT_ADD_POLLING_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    public static class NET_IN_UPDATE_POLLING_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID
        public int              nIntervalTime;                        // 每个视频源的检测执行时间，单位为秒，1~65535
        public int              nLoopCount;                           // 诊断轮询次数, 0代表永久轮询
        public int              nInfoCount;                           // 任务对象个数
        public int              nReserved;                            // 字节对齐
        public Pointer          pInfoList;                            // 任务对象列表(参考NET_POLLING_INFO)

        public NET_IN_UPDATE_POLLING_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 接口输出参数
    public static class NET_OUT_UPDATE_POLLING_ANALYSE_TASK extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_UPDATE_POLLING_ANALYSE_TASK()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //获取剩余智能分析资源入参
    public static class NET_IN_REMAIN_ANAYLSE_RESOURCE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_IN_REMAIN_ANAYLSE_RESOURCE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //智能分析剩余能力具体信息
    public static class NET_REMAIN_ANALYSE_CAPACITY extends NetSDKLibStructure.SdkStructure
    {
        public int              nMaxStreamNum;                        // 剩余能分析的视频流数目
        public int              emClassType;                          // 大类业务方案(参考EM_SCENE_CLASS_TYPE)
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    //获取剩余智能分析资源出参
    public static class NET_OUT_REMAIN_ANAYLSE_RESOURCE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nRetRemainCapNum;                     // 返回的能力格式
        public NET_REMAIN_ANALYSE_CAPACITY[] stuRemainCapacities = (NET_REMAIN_ANALYSE_CAPACITY[])new NET_REMAIN_ANALYSE_CAPACITY().toArray(32); // 智能分析剩余能力
        public NET_REMAIN_ANALYSE_TOTAL_CAPACITY[] stuTotalCapacity = (NET_REMAIN_ANALYSE_TOTAL_CAPACITY[])new NET_REMAIN_ANALYSE_TOTAL_CAPACITY().toArray(32); //
        // 可供任务调度的总的智能能力
        public int              nTotalCapacityNum;
        public byte[]           byReserved = new byte[60684];         // 保留字节

        public NET_OUT_REMAIN_ANAYLSE_RESOURCE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    public static class NET_IN_REMOTEDEVICE_CAPS extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        //public byte[]                   szSubClassID=new byte[32];                                      // 空表示管理远程通道的设备列表                                                                                                // "EmbeddedPlatform": 表示管理嵌入式管理平台的设备管理器

        public NET_IN_REMOTEDEVICE_CAPS()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    public static class NET_OUT_REMOTEDEVICE_CAP extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nRetCount;                            // 返回的pnProtocal 有效个数
        public int[]            snProtocal = new int[512];            // 协议类型 值同 EM_STREAM_PROTOCOL_TYPE

        public NET_OUT_REMOTEDEVICE_CAP()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //算法独立升级能力
    public static class NET_ANALYSE_CAPS_SUPPORT_ALGORITHM_UPGRADE extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              bSupportOnlyAlgorithmUpgrade;         // 是否支持算法独立升级能力
        public int              nMaxUpgradeAINum;                     // AI 方案最大个数, 由用户指定,  最大支持128
        public int              nRetUpgradeAINum;                     // 实际返回的AI 方案个数, 即pstUpgradeAIInfo 数组的有效元素个数
        /**
         * 指针内传入结构体数组,结构体为{@link NET_ALGORITHM_UPGRADE_AI_INFO}
         */
        public Pointer          pstUpgradeAIInfo;                     // 独立算法升级支持的AI方案信息, 内存由用户申请和释放, 申请大小sizeof(NET_ALGORITHM_UPGRADE_AI_INFO)*nMaxUpgradeAINum
        public int              nRetStorageNum;                       // 实际返回的设备分区个数, 即stuStorageInfo 数组的有效元素个数
        public NET_ALGORITHM_DEV_STORAGE_INFO[] stuStorageInfos = (NET_ALGORITHM_DEV_STORAGE_INFO[])new NET_ALGORITHM_DEV_STORAGE_INFO().toArray(16); // 设备的分区信息
        public NET_ALGORITHM_BUILD_INFO stuBuildInfo;                 // 算法构建信息

        public NET_ANALYSE_CAPS_SUPPORT_ALGORITHM_UPGRADE()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //智能分析总能力
    public static class NET_TOTAL_CAP extends NetSDKLibStructure.SdkStructure
    {
        public int              emClassType;                          // 业务大类(参考EM_SCENE_CLASS_TYPE)
        public int[]            dwRuleTypes = new int[NetSDKLibStructure.MAX_ANALYSE_RULE_COUNT]; // 规则类型, 详见dhnetsdk.h中"智能分析事件类型"
        public int              nRuleNum;                             // 规则数量
        public int              nMaxStreamNum;                        // 最多支持同时分析的视频流数目
        public byte[]           byReserved = new byte[1024];          // 保留字节
    }

    //智能分析的总能力
    public static class NET_ANALYSE_CAPS_TOTAL extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public NET_TOTAL_CAP[]  stuTotalCaps = (NET_TOTAL_CAP[])new NET_TOTAL_CAP().toArray(NetSDKLibStructure.MAX_ANALYSE_TOTALCAPS_NUM); // 智能分析总能力
        public int              nTotalCapsNum;                        // 智能分析总能力个数
        public int              nTotalDecodeCaps;                     // 总解码能力，即总解码资源个数
        public int              nTotalComputingCaps;                  // 总算力
        public int              nSingleTaskComputingCaps;             // 单任务最大算力

        public NET_ANALYSE_CAPS_TOTAL()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 算法版本信息
    public static class NET_ALGORITHM_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              emClassType;                          // 业务大类(参考EM_SCENE_CLASS_TYPE)
        public byte[]           szVersion = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 算法版本
        public int              emAlgorithmVendor;                    // 算法厂商(参考EM_ALGORITHM_VENDOR)
        public byte[]           szAlgorithmLibVersion = new byte[NetSDKLibStructure.NET_COMMON_STRING_32]; // 算法库文件版本
        public byte[]           byReserved = new byte[992];           // 保留字节
    }

    //智能分析的算法版本信息
    public static class NET_ANALYSE_CAPS_ALGORITHM extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public NET_ALGORITHM_INFO[] stuAlgorithmInfos = (NET_ALGORITHM_INFO[])new NET_ALGORITHM_INFO().toArray(NetSDKLibStructure.MAX_ANALYSE_ALGORITHM_NUM); // 算法版本信息
        public int              nAlgorithmNum;                        // 算法个数

        public NET_ANALYSE_CAPS_ALGORITHM()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 智能分析服务能力类型
    public static class EM_ANALYSE_CAPS_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ANALYSE_CAPS_ALGORITHM = 1;        // 算法版本, 对应输出结构体 NET_ANALYSE_CAPS_ALGORITHM
        public static final int   EM_ANALYSE_CAPS_TOTALCAPS = 2;        // 智能分析总能力, 对应输出结构体 NET_ANALYSE_CAPS_TOTAL
        public static final int   EM_ANALYSE_CAPS_SUPPORT_ALGORITHM_UPGRADE = 3; // 算法独立升级能力, 对应输出结构体 NET_ANALYSE_CAPS_SUPPORT_ALGORITHM_UPGRADE
    }

    //CLIENT_SetAnalyseTaskCustomData 接口输入参数
    public static class NET_IN_SET_ANALYSE_TASK_CUSTOM_DATA extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nTaskID;                              // 任务ID
        public NET_TASK_CUSTOM_DATA stuTaskCustomData;                // 自定义数据

        public NET_IN_SET_ANALYSE_TASK_CUSTOM_DATA()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    //CLIENT_SetAnalyseTaskCustomData 接口输出参数
    public static class NET_OUT_SET_ANALYSE_TASK_CUSTOM_DATA extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_SET_ANALYSE_TASK_CUSTOM_DATA()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 获取视频分析服务智能能力集, pstOutParam根据emCapsType的值取对应的结构体(参考EM_ANALYSE_CAPS_TYPE), pstOutParam 资源由用户申请和释放
    public boolean CLIENT_GetAnalyseCaps(LLong lLoginID,int emCapsType,Pointer pOutParam,int nWaitTime);

    // 添加轮询检测任务 (入参NET_IN_ADD_POLLING_ANALYSE_TASK，出参NET_OUT_ADD_POLLING_ANALYSE_TASK)
    public boolean CLIENT_AddPollingAnalyseTask(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 更新轮询检测任务规则(入参NET_IN_UPDATE_POLLING_ANALYSE_TASK，出参NET_OUT_UPDATE_POLLING_ANALYSE_TASK)
    public boolean CLIENT_UpdatePollingAnalyseTask(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 获取剩余智能分析资源(入参NET_IN_REMAIN_ANAYLSE_RESOURCE，出参NET_OUT_REMAIN_ANAYLSE_RESOURCE)
    public boolean CLIENT_GetRemainAnalyseResource(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 设置任务的自定义数据(入参NET_IN_SET_ANALYSE_TASK_CUSTOM_DATA,出参NET_OUT_SET_ANALYSE_TASK_CUSTOM_DATA)
    public boolean CLIENT_SetAnalyseTaskCustomData(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    public static class NET_VIDEOABNORMALDETECTION_RULE_INFO extends NetSDKLibStructure.SdkStructure {
        /**
         * 最短持续时间	单位：秒，0~65535
         */
        public int              nMinDuration;
        /**
         * 灵敏度, 取值1-10，值越小灵敏度越低(只对检测类型视频遮挡，过亮，过暗，场景变化有效)
         */
        public int              nSensitivity;
        /**
         * 检测类型数
         */
        public int              nDetectType;
        public int              nReserved;
        /**
         * 异常检测阈值,范围1~100
         */
        public int[]            nThreshold = new int[32];
        /**
         * 检测类型,0-视频丢失, 1-视频遮挡, 2-画面冻结, 3-过亮, 4-过暗, 5-场景变化
         * 6-条纹检测 , 7-噪声检测 , 8-偏色检测 , 9-视频模糊检测 , 10-对比度异常检测
         * 11-视频运动 , 12-视频闪烁 , 13-视频颜色 , 14-虚焦检测 , 15-过曝检测, 16-场景巨变
         */
        public byte[]           bDetectType = new byte[32];
        /**
         * 保留字节
         */
        public byte[]           byReserved = new byte[4096];
    }

    //--------------------------------------------------------ERR200529144------------------------------------------------------------------------//
    // 热度图灰度数据
    public static class NET_CB_HEATMAP_GRAY_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nWidth;                               // 图片宽度
        public int              nHeight;                              // 图片高度
        public NET_TIME         stuStartTime;                         // 开始时间
        public NET_TIME         stuEndTime;                           // 结束时间
        public int              nMax;                                 // 最大值
        public int              nMin;                                 // 最小值
        public int              nAverage;                             // 平均值
        public int              nLength;                              // 灰度图数据长度
        public Pointer          pGrayInfo;                            // 灰度图数据
        public byte[]           byReserved = new byte[512];           // 保留字节
    }

    // 热度图灰度数据回调函数, lAttachHandle 为 CLIENT_AttachHeatMapGrayInfo 返回的结果(pstGrayInfo参考NET_CB_HEATMAP_GRAY_INFO)
    public interface fHeatMapGrayCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstGrayInfo,Pointer dwUser);
    }

    // CLIENT_AttachHeatMapGrayInfo 接口输入参数
    public static class NET_IN_GRAY_ATTACH_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nChannelID;                           // 通道号
        public fHeatMapGrayCallBack cbHeatMapGray;                    // 热度图灰度数据回调函数
        public Pointer          dwUser;                               // 用户信息

        public NET_IN_GRAY_ATTACH_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // CLIENT_AttachHeatMapGrayInfo接口输出参数
    public static class NET_OUT_GRAY_ATTACH_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_GRAY_ATTACH_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 订阅热度图灰度数据接口,pInParam与pOutParam内存由用户申请释放(pInParam参考NET_IN_GRAY_ATTACH_INFO,pOutParam参考NET_OUT_GRAY_ATTACH_INFO)
    public LLong CLIENT_AttachHeatMapGrayInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 退订热度图灰度数据
    public boolean CLIENT_DetachHeatMapGrayInfo(LLong lAttachHandle);

    // 事件类型EVENT_IVS_RETROGRADEDETECTION(人员逆行事件)对应的数据块描述信息
    public static class DEV_EVENT_RETROGRADEDETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[128];               // 事件名称
        public byte[]           bReserved1 = new byte[4];             // 字节对齐
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public DH_MSG_OBJECT    stuObject;                            // 检测到的物体
        public int              nTrackLineNum;                        // 物体运动轨迹顶点数
        public DH_POINT[]       TrackLine = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_TRACK_LINE_NUM); // 物体运动轨迹
        public int              nDirectionPointNum;                   // 规则里规定的方向顶点数
        public DH_POINT[]       stuDirections = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_LINE_NUM); // 规则里规定的方向
        public int              nDetectRegionNum;                     // 规则检测区域顶点数
        public DH_POINT[]       DetectRegion = (DH_POINT[])new DH_POINT().toArray(NetSDKLibStructure.NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public NetSDKLibStructure.NET_EVENT_FILE_INFO stuFileInfo;                       // 事件对应文件信息
        public byte             bEventAction;                         // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]           byReserved = new byte[2];
        public byte             byImageIndex;                         // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int              dwSnapFlagMask;                       // 抓图标志(按位),具体见NET_RESERVED_COMMON
        public int              nSourceIndex;                         // 事件源设备上的index,-1表示数据无效
        public byte[]           szSourceDevice = new byte[NetSDKLibStructure.MAX_PATH];  // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int              nOccurrenceCount;                     // 事件触发累计次数
        public NetSDKLibStructure.EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;            // 智能事件公共信息
        public Pointer          pstuImageInfo;                        // 图片信息数组 ,结构体NET_IMAGE_INFO_EX2数组
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); //事件公共扩展字段结构体
        public byte[]           bReserved = new byte[604];            // 保留字节,留待扩展.
    }

    // 智能交通语音播报配置 对应枚举 NET_EM_CFG_TRAFFIC_VOICE_BROADCAST
    public static class NET_CFG_TRAFFIC_VOICE_BROADCAST_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nEnableCount;                         // 使能播报个数
        public int[]            emEnable = new int[NetSDKLibStructure.NET_MAX_PLATEENABLE_NUM]; // 使能过车车牌播报,见枚举 NET_EM_PLATEENABLE_TYPE
        public byte[]           szNormalCar = new byte[NetSDKLibStructure.MAX_PATH];     // 普通车辆过车播报内容,例如:播报语音文件"欢迎光临.wav"
        public byte[]           szTrustCar = new byte[NetSDKLibStructure.MAX_PATH];      // 信任车辆过车播报内容,例如:播报语音文件"欢迎光临.wav"
        public byte[]           szSuspiciousCar = new byte[NetSDKLibStructure.MAX_PATH]; // 嫌疑车辆过车播报内容,例如:播报语音文件"非注册车辆.wav"
        public NET_TRAFFIC_VOICE_BROADCAST_ELEMENT[] stuElement = (NET_TRAFFIC_VOICE_BROADCAST_ELEMENT[])new  NET_TRAFFIC_VOICE_BROADCAST_ELEMENT().toArray(NetSDKLibStructure.NET_MAX_BROADCAST_ELEMENT_NUM); // 播报元素
        public int              nElementNum;                          // stuElement中有效数据个数

        public NET_CFG_TRAFFIC_VOICE_BROADCAST_INFO()
        {
            this.dwSize = this.size();
        }// 此结构体大小
    }

    // 播报元素
    public static class NET_TRAFFIC_VOICE_BROADCAST_ELEMENT extends NetSDKLibStructure.SdkStructure
    {
        public int              emType;                               // 类型(参考NET_EM_VOICE_BROADCAST_ELEMENT_TYPE)
        public byte[]           byReserved1 = new byte[4];            // 字节对齐
        public byte[]           szPrefix = new byte[512];             // 前缀字符串
        public byte[]           szPostfix = new byte[512];            // 后缀字符串
        public byte[]           byReserved = new byte[1024];          // 预留
    }

    // 485串口协议设备配置信息
    public static class NET_CFG_DHRS_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nDeviceNum;                           // 串口设备个数
        public NET_CFG_DHRS_DEVICE_INFO[] stuDHRSDeviceInfo = (NET_CFG_DHRS_DEVICE_INFO[])new NET_CFG_DHRS_DEVICE_INFO().toArray(32); // 串口设备信息
    }

    public static class NET_CFG_DHRS_DEVICE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              bEnable;                              // 串口设备是否启用
        public int              emType;                               // 串口设备类型(参考EM_DHRS_DEVICE_TYPE)
        public NET_CFG_LATTICE_SCREEN_CONFIG stuLatticeScreenConfig;  // 485串口点阵屏配置
        public byte[]           byReserved = new byte[4096];          //预留字节
    }

    // 485串口点阵屏配置
    public static class NET_CFG_LATTICE_SCREEN_CONFIG extends NetSDKLibStructure.SdkStructure
    {
        public int              nAddress;                             // 配置对应设备的地址, 范围[1,31]
        public int              emRollSpeedLevel;                     // 点阵屏滚动速度级别(参考EM_ROLL_SPEED_LEVEL)
        public int              nLogicScreenNum;                      // 逻辑屏个数
        public NET_LOGIC_SCREEN[] stuLogicScreens = (NET_LOGIC_SCREEN[])new NET_LOGIC_SCREEN().toArray(8); // 逻辑屏信息, 划分物理屏的某一区域为逻辑屏
        public int              nOutPutVoiceVolume;                   // 语音播报音量大小, 范围：[0 - 100]
        public int              nOutPutVoiceSpeed;                    // 语音播报速度， 范围：[0-100]
        public byte[]           byReserved = new byte[1024];
    }

    // 串口设备类型
    public static class EM_DHRS_DEVICE_TYPE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DHRS_DEVICE_TYPE_UNKNOWN = 0;      // 未知
        public static final int   EM_DHRS_DEVICE_TYPE_STEADYLIGHT = 1;  // 常亮灯
        public static final int   EM_DHRS_DEVICE_TYPE_STROBELIGHT = 2;  // 可以通过485控制的频闪灯
        public static final int   EM_DHRS_DEVICE_TYPE_POWERMODULE = 3;  // 电源模块
        public static final int   EM_DHRS_DEVICE_TYPE_LATTICESCREEN = 4; // 点阵屏
        public static final int   EM_DHRS_DEVICE_TYPE_INDICATORLIGHT = 5; // 指示灯
        public static final int   EM_DHRS_DEVICE_TYPE_RAINBRUSH = 6;    // 雨刷洗涤模块
        public static final int   EM_DHRS_DEVICE_TYPE_FLASHLAMP = 7;    // 爆闪灯
        public static final int   EM_DHRS_DEVICE_TYPE_RFID = 8;         // 射频识别
        public static final int   EM_DHRS_DEVICE_TYPE_COMMON = 9;       // 通用485
    }

    // 逻辑屏信息
    public static class NET_LOGIC_SCREEN extends NetSDKLibStructure.SdkStructure
    {
        public NET_RECT         stuRegion;                            // 逻辑屏区域, 实际点阵屏坐标
        public int              emDisplayMode;                        // 显示动作(参考EM_DISPLAY_MODE)
        public int              emDisplayColor;                       // 显示颜色(参考EM_DISPLAY_COLOR)
        public byte[]           byReserved = new byte[512];           // 预留字节
    }

    // 点阵屏滚动速度级别
    public static class EM_ROLL_SPEED_LEVEL extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_ROLL_SPEED_LEVEL_UNKNOWN = 0;      // 未知
        public static final int   EM_ROLL_SPEED_LEVEL_SLOW = 1;         // 慢
        public static final int   EM_ROLL_SPEED_LEVEL_SLOWER = 2;       // 较慢
        public static final int   EM_ROLL_SPEED_LEVEL_MEDIUM = 3;       // 中等
        public static final int   EM_ROLL_SPEED_LEVEL_FASTER = 4;       // 较快
        public static final int   EM_ROLL_SPEED_LEVEL_FAST = 5;         // 快
    }

    // 显示动作
    public static class EM_DISPLAY_MODE extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DISPLAY_MODE_UNKNOWN = 0;          // 未知
        public static final int   EM_DISPLAY_MODE_ROLL = 1;             // 滚动
        public static final int   EM_DISPLAY_MODE_INTERCEPT = 2;        // 截取
    }

    // 显示颜色
    public static class EM_DISPLAY_COLOR extends NetSDKLibStructure.SdkStructure
    {
        public static final int   EM_DISPLAY_COLOR_UNKNOWN = 0;         // 未知
        public static final int   EM_DISPLAY_COLOR_RED = 1;             // 红
        public static final int   EM_DISPLAY_COLOR_GREEN = 2;           // 绿
        public static final int   EM_DISPLAY_COLOR_YELLOW = 3;          // 黄
    }

    public static class NET_FACEANALYSIS_RULE_INFO extends NetSDKLibStructure.SdkStructure {
        public int              dwSize;                               // 结构体大小
        public int              nDetectRegionPoint;                   // 检测区顶点数
        public POINTCOORDINATE[] stuDetectRegion = new POINTCOORDINATE[20]; // 检测区
        public int              nSensitivity;                         // 灵敏度,范围[1,10],灵敏度越高越容易检测
        public int              nLinkGroupNum;                        // 联动布控个数
        public NET_CFG_LINKGROUP_INFO[] stuLinkGroup = new NET_CFG_LINKGROUP_INFO[20]; // 联动的布控组
        public NET_CFG_STRANGERMODE_INFO stuStrangerMode;             // 陌生人布防模式
        public int              bSizeFileter;                         // 规则特定的尺寸过滤器是否有效
        public NET_CFG_SIZEFILTER_INFO stuSizeFileter;                // 规则特定的尺寸过滤器
        public int              bFeatureEnable;                       // 是否开启人脸属性识别, IPC增加
        public int              nFaceFeatureNum;                      // 需要检测的人脸属性个数
        public int[]            emFaceFeatureType = new int[32];      // 需检测的人脸属性 NET_EM_FACEFEATURE_TYPE
        public int              bFeatureFilter;                       // 在人脸属性开启前提下，如果人脸图像质量太差，是否不上报属性
        // true-图像太差不上报属性 false-图像很差也上报属性(可能会非常不准，影响用户体验)
        public int              nMinQuality;                          // 人脸图片质量阈值,和bFeatureFilter一起使用 范围[1,100]
        public NET_CFG_FACE_BEAUTIFICATION stuFaceBeautification = new NET_CFG_FACE_BEAUTIFICATION(); //人Lian美化,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CFG_FACE_BEAUTIFICATION}

        public NET_FACEANALYSIS_RULE_INFO(){
            for(int i = 0; i < stuDetectRegion.length; i++ ){
                stuDetectRegion[i] = new POINTCOORDINATE();
            }
            for(int i = 0; i < stuLinkGroup.length; i++){
                stuLinkGroup[i] = new NET_CFG_LINKGROUP_INFO();
            }
            dwSize  =   this.size();
        }
    }

    // 联动的布控组
    public static class NET_CFG_LINKGROUP_INFO extends NetSDKLibStructure.SdkStructure {
        public int              bEnable;                              // 布控组是否启用
        public byte[]           szGroupID = new byte[64];             // 布控组ID
        public byte             bySimilarity;                         // 相似度阈值 1-100
        public byte[]           bReserved1 = new byte[3];             // 字节对齐
        public byte[]           szColorName = new byte[32];           // 事件触发时绘制人脸框的颜色
        public int              bShowTitle;                           // 事件触发时规则框上是否显示报警标题
        public int              bShowPlate;                           // 事件触发时是否显示比对面板
        public NetSDKLibStructure.NET_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
        public byte[]           bReserved = new byte[512];            // 保留字段
    }

    // 陌生人布防模式
    public static class NET_CFG_STRANGERMODE_INFO extends NetSDKLibStructure.SdkStructure {
        public int              bEnable;                              // 模式是否启用
        public byte[]           szColorHex = new byte[8];             // 事件触发时绘制人脸框的颜色
        public int              bShowTitle;                           // 事件触发时规则框上是否显示报警标题
        public int              bShowPlate;                           // 事件触发时是否显示比对面板
        public NetSDKLibStructure.NET_ALARM_MSG_HANDLE stuEventHandler;                  // 报警联动
        public byte[]           bReserved = new byte[512];            // 保留字段
    }

    // 人脸属性类型
    public static class NET_EM_FACEFEATURE_TYPE 
    {
        public final static int   NET_EM_FACEFEATURE_UNKNOWN = 0;       // 未知
        public final static int   NET_EM_FACEFEATURE_SEX = 1;           // 性别
        public final static int   NET_EM_FACEFEATURE_AGE = 2;           // 年龄
        public final static int   NET_EM_FACEFEATURE_EMOTION = 3;       // 表情
        public final static int   NET_EM_FACEFEATURE_GLASSES = 4;       // 眼镜状态
        public final static int   NET_EM_FACEFEATURE_EYE = 5;           // 眼睛状态
        public final static int   NET_EM_FACEFEATURE_MOUTH = 6;         // 嘴巴状态
        public final static int   NET_EM_FACEFEATURE_MASK = 7;          // 口罩状态
        public final static int   NET_EM_FACEFEATURE_BEARD = 8;         // 胡子状态
        public final static int   NET_EM_FACEFEATURE_ATTRACTIVE = 9;    // 魅力值
    }

    // 事件类型EM_ANALYSE_EVENT_FEATURE_ABSTRACT(特征提取)对应的数据块描述信息
    public static class DEV_EVENT_FEATURE_ABSTRACT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public int              emClassType;                          // 智能事件所属大类 EM_CLASS_TYPE
        public int              nFeatureNum;                          // 特征值数量
        public NET_FEATURE_VECTOR_INFO[] stuFeatureVectorList = new NET_FEATURE_VECTOR_INFO[10]; // 特征值数组，同一个图片需要进行多个版本的特征向量提取，在一个事件中返回
        public byte[]           byReserved = new byte[1024];          // 预留字节

        public DEV_EVENT_FEATURE_ABSTRACT_INFO(){
            for(int i=0;i<stuFeatureVectorList.length;i++){
                stuFeatureVectorList[i] = new NET_FEATURE_VECTOR_INFO();
            }
        }
    }

    // 特征值信息
    public static class NET_FEATURE_VECTOR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szFeatureVersion = new byte[32];      // 特征版本版本号
        public int              emFeatureErrCode;                     // 特征建模失败错误码 EM_FEATURE_ERROR_CODE
        public NetSDKLibStructure.NET_FEATURE_VECTOR stuFeatureVector;// 特征值的偏移和大小信息
        public NET_FACE_ATTRIBUTES stuFaceAttribute;                  // 人脸属性 ,当提取人脸特征向量成功时上报
        public byte[]           byReserved = new byte[968];           // 预留字节
    }

    // 特征建模失败错误码
    public static class EM_FEATURE_ERROR_CODE 
    {
        public final static int   EM_FEATURE_ERROR_SUCCESS = 0;         // 成功
        public final static int   EM_FEATURE_ERROR_UNKNOWN = 1;         // 未知
        public final static int   EM_FEATURE_ERROR_IMAGE_FORMAT_ERROR = 2; // 图片格式问题
        public final static int   EM_FEATURE_ERROR_NOFACE_OR_NOTCLEAR = 3; // 无人脸或不够清晰
        public final static int   EM_FEATURE_ERROR_MULT_FACES = 4;      // 多个人脸
        public final static int   EM_FEATURE_ERROR_IMAGH_DECODE_FAILED = 5; // 图片解码失败
        public final static int   EM_FEATURE_ERROR_NOT_SUGGEST_STORAGE = 6; // 不推荐入库
        public final static int   EM_FEATURE_ERROR_DATABASE_OPERATE_FAILED = 7; // 数据库操作失败
        public final static int   EM_FEATURE_ERROR_GET_IMAGE_FAILED = 8; // 获取图片失败
        public final static int   EM_FEATURE_ERROR_SYSTEM_EXCEPTION = 9; // 系统异常（如Licence失效、建模分析器未启动导致的失败）
    }

    // 人脸属性
    public static class NET_FACE_ATTRIBUTES extends NetSDKLibStructure.SdkStructure
    {
        public int[]            nAngle = new int[3];                  // 人脸抓拍角度,三个角度分别是：仰俯角,偏航角,翻滚角；默认值[999,999,999]表示无此数据
        public int              nFaceQuality;                         // 人脸抓拍质量分数,取值范围 0~10000
        public int              nFaceAlignScore;                      // 人脸对齐得分分数,取值范围 0~10000，-1为无效值
        public byte[]           byReserved = new byte[36];            // 预留字节
    }

    // 事件类型EVENT_IVS_FEATURE_ABSTRACT(提取特征)对应的规则配置
    public static class NET_FEATURE_ABSTRACT_RULE_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              nFeature;                             // 特征的数量
        public NET_FEATURE_ABSTRACT_VERSION[] szFeatureVersions = new NET_FEATURE_ABSTRACT_VERSION[10]; // 对图片进行特征向量提取时使用，需要对图片进行同一种特征向量多个版本进行提取,最大是10个版本
        public int              emAbstractType;                       // 进行特征提取的类型 EM_FEATURE_ABSTRACT_TYPE

        public NET_FEATURE_ABSTRACT_RULE_INFO(){
            for(int i = 0;i<szFeatureVersions.length;i++){
                szFeatureVersions[i] = new NET_FEATURE_ABSTRACT_VERSION();
            }
            this.dwSize = this.size();
        }
    }

    public static class NET_FEATURE_ABSTRACT_VERSION extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szFeatureVersion = new byte[32];
    }

    // 进行特征提取的类型
    public static class EM_FEATURE_ABSTRACT_TYPE 
    {
        public final static int   EM_FEATURE_ABSTRACT_UNKNOWN = -1;     // 未知
        public final static int   EM_FEATURE_ABSTRACT_FACE = 0;         // 人脸
        public final static int   EM_FEATURE_ABSTRACT_HUMAN_TRAIT = 1;  // 人体
        public final static int   EM_FEATURE_ABSTRACT_VEHICLE = 2;      // 机动车
        public final static int   EM_FEATURE_ABSTRACT_NON_MOTOR_VEHICLE = 3; // 非机动车
    }

    public static class ALARM_TRAFFIC_PARKING_TIMEOUT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 0:脉冲 1:开始 2:停止
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public NET_TIME         stuInParkTime;                        // 进场时间
        public NET_TIME         stuOutParkTime;                       // 出场时间
        public int              nParkingTime;                         // 停车时长，单位秒
        public byte[]           byReserved = new byte[1024];          // 预留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;       // 交通车辆的数据库记录
    }

    // 嫌疑车辆上报事件, 对应事件类型 DH_ALARM_TRAFFIC_SUSPICIOUSCAR
    public static class ALARM_TRAFFIC_SUSPICIOUSCAR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nAction;                              // 事件动作, -1:未知,0:Start, 1:Stop, 2:Pulse
        public DH_MSG_OBJECT    stuVehicle;                           // 车身信息
        public NetSDKLibStructure.NET_TRAFFIC_LIST_RECORD stuCarInfo; // 车辆的禁止名单信息
        public NetSDKLibStructure.EVENT_COMM_INFO  stCommInfo;        // 公共信息

        public ALARM_TRAFFIC_SUSPICIOUSCAR_INFO(){
            this.dwSize = this.size();
        }
    }

    // 事件类型 DH_ALARM_PARKING_LOT_STATUS_DETECTION (室外停车位状态检测事件) 对应的数据块描述信息
    public static class ALARM_PARKING_LOT_STATUS_DETECTION extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public int              nAction;                              // 1:开始 2:停止
        public byte[]           szName = new byte[128];               // 事件名称
        public int              emClassType;                          // 智能事件所属大类 EM_CLASS_TYPE
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public int              nEventID;                             // 事件ID
        public int              nRuleID;                              // 智能事件规则编号，用于标示哪个规则触发的事件
        public int              nSequence;                            // 帧序号
        public int              nParkingStatusNum;                    // 室外停车位个数
        public NET_PARKING_STATUS[] stuParkingStatus = new NET_PARKING_STATUS[100]; // 室外停车位状态
        public byte[]           byReserved = new byte[1020];          // 预留字节

        public ALARM_PARKING_LOT_STATUS_DETECTION(){
            for(int i = 0;i<stuParkingStatus.length;i++){
                stuParkingStatus[i] = new NET_PARKING_STATUS();
            }
        }
    }

    // 室外停车位状态
    public static class NET_PARKING_STATUS extends NetSDKLibStructure.SdkStructure
    {
        public byte[]           szName = new byte[32];                // 车位名称
        public int              nID;                                  // 车位ID，范围:[0,99]
        public int              nParkedNumber;                        // 车位内已停车位数量，范围:[0,255]
        public int              emChangeStatus;                       // 车位内已停车位数量相对上次上报的变化状态 EM_PARKING_NUMBER_CHANGE_STATUS
        public byte[]           reserved = new byte[252];             // 预留字节
    }

    // 车位内已停车位数量相对上次上报的变化状态
    public static class EM_PARKING_NUMBER_CHANGE_STATUS 
    {
        public final static int   EM_PARKING_NUMBER_CHANGE_STATUS_UNKNOWN = -1; // 未知
        public final static int   EM_PARKING_NUMBER_CHANGE_STATUS_NO_CHANGE = 0; // 无变化
        public final static int   EM_PARKING_NUMBER_CHANGE_STATUS_INCREASE = 1; // 数量增加
        public final static int   EM_PARKING_NUMBER_CHANGE_STATUS_DECREASE = 2; // 数量减少
    }

    // 停车超时检测配置
    public static class NET_CFG_PARKING_TIMEOUT_DETECT extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public int              bEnable;                              // 是否使能停车超时检测
        public int              nParkingTime;                         // 可停车时长, 单位为秒, 默认值为604800. 范围:3600-604800, 超过指定时长则判断为超时停车
        public int              nDetectInterval;                      // 上报超时停车间隔, 单位为秒, 默认值为86400(24小时). 最小值为600, 最大值为86400

        public NET_CFG_PARKING_TIMEOUT_DETECT(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_RemoveParkingCarInfo 接口输入参数
    public static class NET_IN_REMOVE_PARKING_CAR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小
        public NetSDKLibStructure.DEV_OCCUPIED_WARNING_INFO stuParkingCarInfo;           // 车位信息

        public NET_IN_REMOVE_PARKING_CAR_INFO(){
            this.dwSize = this.size();
        }
    }

    // CLIENT_RemoveParkingCarInfo 接口输出参数
    public static class NET_OUT_REMOVE_PARKING_CAR_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 结构体大小

        public NET_OUT_REMOVE_PARKING_CAR_INFO(){
            this.dwSize = this.size();
        }
    }

  //对应CLIENT_StartSearchDevicesEx接口
    public static class DEVICE_NET_INFO_EX2 extends NetSDKLibStructure.SdkStructure
    {
        public NetSDKLibStructure.DEVICE_NET_INFO_EX stuDevInfo;      // 设备信息结构体
        public byte[]           szLocalIP = new byte[64];             // 搜索到设备的本地IP地址
        public byte[]           szDeviceSubClass = new byte[16];      //设备子类型
        public byte[]           szSID = new byte[32];                 //SID信息
        public byte             byRole;                               //传输产品中设备的主从角色
        public byte[]           szReserved = new byte[3];             //字节对齐
        public int              nBridgeNetCardsMacListNum;            //网卡内部网卡信息个数
        public NET_BRIDGE_NET_CARDS_MAC_LIST[] stuBridgeNetCardsMacList = new NET_BRIDGE_NET_CARDS_MAC_LIST[64]; //网卡内部网卡信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_BRIDGE_NET_CARDS_MAC_LIST}
        public byte[]           cReserved = new byte[1992];           // 扩展字段
    }

    // 异步搜索设备回调(pDevNetInfo内存由SDK内部申请释放, 参考DEVICE_NET_INFO_EX2)
    public interface fSearchDevicesCBEx extends Callback {
        public void invoke(LLong lSearchHandle,Pointer pDevNetInfo,Pointer pUserData);
    }

    // 异步搜索设备(参考NET_IN_STARTSERACH_DEVICE,NET_OUT_STARTSERACH_DEVICE)
    public LLong CLIENT_StartSearchDevicesEx(Pointer pInBuf,Pointer pOutBuf);

    // 同步跨网段搜索设备IP (pIpSearchInfo内存由用户申请释放)
    // szLocalIp为本地IP，可不做输入, fSearchDevicesCB回调
    // 接口调用1次只发送搜索信令1次
    public boolean CLIENT_SearchDevicesByIPs(NetSDKLibStructure.DEVICE_IP_SEARCH_INFO pIpSearchInfo,Callback cbSearchDevices,Pointer dwUserData,String szLocalIp,int dwWaitTime);

    /**
     *
     * @param lLoginID
     * @param pInParam
     * @param pOutParam
     * @param nWaitTime
     * @return boolean
     */
    public boolean CLIENT_AsyncAddCustomDevice(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /***
     *
     * @param lLoginID
     * @param nChannelID
     * @param dwFocusCommand
     * @param nFocus
     * @param nZoom
     * @param reserved
     * @param waittime
     * @return
     * 镜头聚焦控制  dwFocusCommand = 0
     * 为聚焦调节dwFocusCommand = 1
     * 为连续聚焦调节dwFocusCommand = 2,为自动聚焦调节,调节焦点至最佳位置。nFocus和nZoom无效。
     */
    public boolean CLIENT_FocusControl(LLong lLoginID,int nChannelID,int dwFocusCommand,double nFocus,double nZoom,Pointer reserved,int waittime);

    //事件类型 EVENT_IVS_TUMBLE_DETECTION(倒地报警事件)对应数据块描述信息
    public static class DEV_EVENT_TUMBLE_DETECTION_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              nChannelID;                           // 通道号
        public byte[]           szName = new byte[NetSDKLibStructure.NET_EVENT_NAME_LEN]; // 事件名称
        public int              nAction;                              // 事件动作,1表示持续性事件开始,2表示持续性事件结束;
        public double           PTS;                                  // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                  // 事件发生的时间
        public int              nEventID;                             // 事件ID
        public int              UTCMS;                                // UTC时间对应的毫秒数
        public int              emClassType;                          // 智能事件所属大类 EM_CLASS_TYPE
        public int              nObjectID;                            // 目标ID
        public byte[]           szObjectType = new byte[NetSDKLibStructure.NET_COMMON_STRING_16]; // 物体类型,支持以下:
        //"Unknown", "Human", "Vehicle", "Fire", "Smoke", "Plate", "HumanFace",
        // "Container", "Animal", "TrafficLight", "PastePaper", "HumanHead", "BulletHole", "Entity"
        public NET_RECT         stuBoundingBox;                       // 物体包围盒
        public byte[]           szSerialUUID = new byte[22];          // 智能物体全局唯一物体标识
        // 有效数据位21位，包含’\0’
        // 前2位%d%d:01-视频片段, 02-图片, 03-文件, 99-其他
        // 中间14位YYYYMMDDhhmmss:年月日时分秒
        // 后5位%u%u%u%u%u：物体ID，如00001
        public NetSDKLibStructure.SCENE_IMAGE_INFO stuSceneImage;     // 全景广角图
        public Pointer          pstuImageInfo;                        // 图片信息数组,结构体NET_IMAGE_INFO_EX2数组指针
        public int              nImageInfoNum;                        // 图片信息个数
        public NET_EVENT_INFO_EXTEND stuEventInfoEx = new NET_EVENT_INFO_EXTEND(); // 事件公共扩展字段结构体
        public int              nDetectRegionNum;                     //检测区个数
        public NET_POINT_EX[]   stuDetectRegion = new NET_POINT_EX[20]; //检测区
        public int              nIndexInDataInHumanImage;             //人体图片序号
        public int              nIndexInDataInFaceImage;              //人脸图片序号
        public int              nIndexInDataInFaceSceneImage;         //人脸全景图片序号
        public Pointer          pstuHumanAttributes;                  //人体属性信息,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.HUMAN_ATTRIBUTES_INFO}
        public Pointer          pstuHumanAttributesEx;                //人体属性信息扩展,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.HUMAN_ATTRIBUTES_INFO_EX}
        public Pointer          pstuFaceAttributes;                   //人脸属性,参见结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_FACE_ATTRIBUTE}
        public byte[]           bReserved = new byte[822-3*NetSDKLibStructure.POINTERSIZE]; // 保留字节

    	public DEV_EVENT_TUMBLE_DETECTION_INFO() {
    		for (int i = 0; i < stuDetectRegion.length; i++) {
    			stuDetectRegion[i] = new NET_POINT_EX();
    		}		
    	}
    }

    /**
     * 设置二维码信息
     * @param lLoginID 登录句柄
     * @param pInParam 入参,对应结构体{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_2DCODE}
     * @param pOutParam 出参,对应结构体{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_2DCODE}
     * @param nWaitTime 超时时间
     * @return
     */
    public boolean CLIENT_Set2DCode(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 订阅热度图数据,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参,对应结构体 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_VIDEOSTAT_HEATMAP}
     * @Param pOutParam 出参,对应结构体{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_VIDEOSTAT_HEATMAP}
     * 也可使用{@link com.coalbot.camera.sdk.sdk.dahua.structure.EmptyStructure}
     * @param nWaitTime 超时时间
     * @return
     */
    public  LLong CLIENT_AttachVideoStatHeatMap(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取热图数据,pInParam与pOutParam内存由用户申请释放
     * @param lAttachHandle 热度图订阅句柄
     * @param pInParam 入参,对应结构体 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_VIDEOSTAT_HEATMAP}
     * @param pOutParam 出参,对应结构体{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_VIDEOSTAT_HEATMAP}
     * @param nWaitTime 超时时间
     * @return
     */
    public boolean CLIENT_GetVideoStatHeatMap(LLong lAttachHandle,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 取消订阅热度图数据
     * @param lAttachHandle 订阅句柄
     * @return
     */
    public boolean CLIENT_DetachVideoStatHeatMap(LLong lAttachHandle);

    //
    /**
     *
     * @param lLoginID
     * @param pstInParam
     *
     * @param nWaitTime = NET_INTERFACE_DEFAULT_TIMEOUT
     * @return
     */
    /**
     * 计算两张人脸图片的相似度faceRecognitionServer.matchTwoFace,pstInParam与pstOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pstInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MATCH_TWO_FACE_IN}
     * @param pstOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MATCH_TWO_FACE_OUT}
     * @param nWaitTime 接口超时时间,默认超时时间为3000
     * @return
     */
    public boolean CLIENT_MatchTwoFaceImage(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 设置相机参数, pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_CAMERA_CFG}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_CAMERA_CFG}
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_SetCameraCfg(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取相机参数, pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CAMERA_CFG}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CAMERA_CFG}
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_GetCameraCfg(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置通道参数, pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_CHANNEL_CFG}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_CHANNEL_CFG}
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_SetChannelCfg(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取通道参数, pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CHANNEL_CFG}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CHANNEL_CFG}
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_GetChannelCfg(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 订阅安全门报警统计信息
     *  CLIENT_NET_API LLONG CALL_METHOD CLIENT_SecurityGateAttachAlarmStatistics(LLONG lLoginID, const NET_IN_SECURITYGATE_ATTACH_ALARM_STATISTICS* pInParam, NET_OUT_SECURITYGATE_ATTACH_ALARM_STATISTICS* pOutParam, int nWaitTime);
     *
     */
    public LLong CLIENT_SecurityGateAttachAlarmStatistics(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

/**
 *  订阅X光机包裹数量统计信息
    CLIENT_NET_API LLONG CALL_METHOD CLIENT_XRayAttachPackageStatistics(LLONG lLoginID, const NET_IN_XRAY_ATTACH_PACKAGE_STATISTICS* pInParam, NET_OUT_XRAY_ATTACH_PACKAGE_STATISTICS* pOutParam, int nWaitTime);
*/
    public LLong CLIENT_XRayAttachPackageStatistics(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 取消订阅X光机包裹数量统计信息
     * CLIENT_NET_API BOOL CALL_METHOD CLIENT_XRayDetachPackageStatistics(LLONG lAttachHandle);
      */
    public boolean CLIENT_XRayDetachPackageStatistics(LLong lAttachHandle);

    /**
     * 交通灯信号检测-获取相机信息, pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CAMERA_INFO}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CAMERA_INFO}
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_GetCameraInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 主动获取每个热成像点的像素温度
     * @param lLoginID 登录句柄
     * @param pstInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_HEATMAPS_INFO}
     * @param pstOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_HEATMAPS_INFO}
     * @param nWaitTime 接口超时时间,默认超时时间为 3000
     * @return
     */
    public boolean CLIENT_GetHeatMapsDirectly(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /******************************************************************************
    功能描述	:	透传扩展接口,按透传类型走对应透传方式接口，目前支持F6纯透传, 同时兼容CLIENT_TransmitInfoForWeb接口
    参数定义	:
        lLoginID:       登录接口返回的句柄
        pInParam:       透传扩展接口输入参数
        pOutParam       透传扩展接口输出参数
        nWaittime       接口超时时间
    返 回 值	：	BOOL  TRUE :成功; FALSE :失败
    ******************************************************************************/
    /**
     *
     * @param lLoginID
     * @param pInParam NET_IN_TRANSMIT_INFO
     * @param pOutParam NET_OUT_TRANSMIT_INFO
     * @return
     */
    public boolean CLIENT_TransmitInfoForWebEx(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaittime);

    // CLIENT_TransmitInfoForWebEx输入参数
    public static class NET_IN_TRANSMIT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 用户使用该结构体，dwSize需赋值为sizeof(NET_IN_TRANSMIT_INFO)
        public int              emType;                               // 透传类型,参考NET_TRANSMIT_INFO_TYPE
        public String           szInJsonBuffer;                       // Json请求数据,用户申请空间
        public int              dwInJsonBufferSize;                   // Json请求数据长度
        public Pointer          szInBinBuffer;                        // 二进制请求数据，用户申请空间
        public int              dwInBinBufferSize;                    // 二进制请求数据长度
        public int              emEncryptType;                        // 加密类型(参考EM_TRANSMIT_ENCRYPT_TYPE)

   	 public NET_IN_TRANSMIT_INFO()
	 {
	     this.dwSize = this.size();
	 }// 此结构体大小
    }

    // CLIENT_TransmitInfoForWebEx输出参数
    public static class NET_OUT_TRANSMIT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;                               // 用户使用该结构体时，dwSize需赋值为sizeof(NET_OUT_TRANSMIT_INFO)
        public Pointer          szOutBuffer;                          // 应答数据缓冲空间, 用户申请空间
        public int              dwOutBufferSize;                      // 应答数据缓冲空间长度
        public int              dwOutJsonLen;                         // Json应答数据长度
        public int              dwOutBinLen;                          // 二进制应答数据长度

       public NET_OUT_TRANSMIT_INFO()
  	 {
  	     this.dwSize = this.size();
  	 }// 此结构体大小
    }

    // 透传类型
    public static class NET_TRANSMIT_INFO_TYPE 
    {
        public static final int   NET_TRANSMIT_INFO_TYPE_DEFAULT = 0;   // 默认类型，即CLIENT_TransmitInfoForWeb接口的兼容透传方式
        public static final int   NET_TRANSMIT_INFO_TYPE_F6 = 1;        // F6纯透传
    }

    // 透传加密类型
    public static class EM_TRANSMIT_ENCRYPT_TYPE 
    {
        public static final int   EM_TRANSMIT_ENCRYPT_TYPE_UNKNOWN = -1; // 未知
        public static final int   EM_TRANSMIT_ENCRYPT_TYPE_NORMAL = 0;  // SDK内部自行确定是否加密，默认
        public static final int   EM_TRANSMIT_ENCRYPT_TYPE_MULTISEC = 1; // 设备支持加密的场景下，走multiSec加密
        public static final int   EM_TRANSMIT_ENCRYPT_TYPE_BINARYSEC = 2; // 设备支持加密的场景下，走binarySec加密，二进制部分不加密
    }

    /**
     * 批量下载文件,pstInParam与pstOutParam内存由用户申请释放
     * 入参 NET_IN_DOWNLOAD_MULTI_FILE 出参 NET_OUT_DOWNLOAD_MULTI_FILE
     */
    public boolean CLIENT_DownLoadMultiFile(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int waittime);

    //JNA Callback方法定义,断线回调
    public interface fMultiFileDownLoadPosCB extends Callback {
        public void invoke(LLong lDownLoadHandle,int dwID,int dwFileTotalSize,int dwDownLoadSize,int nError,Pointer dwUser,Pointer pReserved);
    }

    /**
     * 订阅摄像头状态,pstInParam与pstOutParam内存由用户申请释放
     * pstInParam->NET_IN_CAMERASTATE ; pstOutParam->NET_OUT_CAMERASTATE
     */
    public LLong CLIENT_AttachCameraState(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 停止订阅摄像头状态,lAttachHandle是CLIENT_AttachCameraState返回值
     */
    public boolean CLIENT_DetachCameraState(LLong lAttachHandle);

    // CLIENT_AttachCameraState()回调函数原形, 每次1条,pBuf->NET_CB_CAMERASTATE dwSize == nBufLen
    public interface fCameraStateCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

     /**
      * 获取IPC设备的存储信息
      * @param lLoginID 登录句柄
      * @param pstInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_DEVICE_AII_INFO}
      * @param pstOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_DEVICE_AII_INFO}
      * @param nWaitTime
      * @return
      */
    public boolean CLIENT_GetDeviceAllInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    // 交通信号灯回调函数 lLoginID - 登录句柄 lAttchHandle - 订阅句柄
    public interface fTrafficLightState extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer pBuf,long dwUser);
    }

    /**
     * 订阅交通信号灯状态 , pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_TRAFFICLIGHT_INFO}
     * @param pOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_TRAFFICLIGHT_INFO}
     * @param nWaitTime
     * @return
     */
    public LLong CLIENT_AttachTrafficLightState(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 退订交通信号灯状态
     * @param lAttchHandle 订阅句柄
     * @return
     */
    public boolean CLIENT_DetachTrafficLightState(LLong lAttchHandle);

    /**
     * 订阅雷达的报警点信息 , pInParam 和pOutParam 资源由用户申请和释放
     * @param lLoginID 登录句柄
     * @param pstInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RADAR_ALARMPOINTINFO}
     * @param pstOutParam 出参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RADAR_ALARMPOINTINFO}
     * @param nWaitTime
     * @return
     */
    public LLong CLIENT_AttachRadarAlarmPointInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 取消订阅雷达的报警点信息
     * @param lAttachHandle 订阅句柄
     * @return
     */
    public boolean CLIENT_DetachRadarAlarmPointInfo(LLong lAttachHandle);

    /**
     * 雷达报警点信息回调函数
     */
    public interface fRadarAlarmPointInfoCallBack extends Callback {
/**
         *
         * @param lLoginId 登录句柄
         * @param lAttachHandle 订阅句柄
         * @param pBuf {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RADAR_NOTIFY_ALARMPOINTINFO}
         * @param dwBufLen pBuf中结构体的长度
         * @param pReserved 保留数据
         * @param dwUser 用户自定义数据
         */
        public void invoke(LLong lLoginId,LLong lAttachHandle,Pointer pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    /**
     * 查询系统状态(pstuStatus内存由用户申请释放)
     * @param lLoginID
     * @param pstInParam NET_SYSTEM_STATUS
     * @param nWaitTime
     * @return
     */
    public boolean CLIENT_QuerySystemStatus(LLong lLoginID,Pointer pstInParam,int nWaitTime);

    /**
     * 订阅云台元数据接口,pstuInPtzStatusProc与pstuOutPtzStatusProc内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pstInParam 入参 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_PTZ_STATUS_PROC}
     * @param pstOutParam 出参{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_PTZ_STATUS_PROC}
     * @param nWaitTime
     * @return
     */
    public LLong CLIENT_AttachPTZStatusProc(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 停止订阅云台元数据接口,lAttachHandle是CLIENT_AttachPTZStatusProc返回值
     * @param lAttachHandle 订阅句柄
     * @return
     */
    public boolean CLIENT_DetachPTZStatusProc(LLong lAttachHandle);

    /**
     * 订阅云台元数据接口回调函数原型
     * pBuf 现阶段主要为 NET_PTZ_LOCATION_INFO 类型 {@link NET_PTZ_LOCATION_INFO}
     */
    public interface fPTZStatusProcCallBack extends Callback {
        public void invoke(LLong lLoginId,LLong lAttachHandle,Pointer pBuf,int dwBufLen,long dwUser);
    }

    /**
     * 查询某月的各天是否存在录像文件,
     *
     * @param lLoginID
     * @param nChannelId
     * @param nRecordFileType EM_QUERY_RECORD_TYPE 的枚举值
     *        nRecordFileType == EM_RECORD_TYPE_CARD，pchCardid输入卡号，限制字符长度 59 字节
     *        nRecordFileType == EM_RECORD_TYPE_FIELD，pchCardid输入自定义字段，限制字符长度 256 字节
     * @param tmMonth Pointer -> NET_TIME
     * @param pchCardid Pointer -> byte[]
     * @param pRecordStatus Poiter -> NET_RECORD_STATUS
     * @param waittime
     * @return boolean
     */
    public boolean CLIENT_QueryRecordStatus(LLong lLoginID,int nChannelId,int nRecordFileType,Pointer tmMonth,Pointer pchCardid,Pointer pRecordStatus,int waittime);

   //设置SDK本地参数,在CLIENT_Init之前调用，szInBuffer内存由用户申请释放，里面存放被设置的信息，具体见NET_EM_SDK_LOCAL_CFG_TYPE类型对应结构体
    boolean CLIENT_SetSDKLocalCfg(int emCfgType,Pointer szInBuffer);

    /**
     * 开启重定向服务扩展接口
     * @param pInParam {@link NET_IN_START_REDIRECT_SERVICE}
     * @param pOutParam NET_OUT_START_REDIRECT_SERVICE,空结构体,可使用{@link EmptyStructure}
     * @return
     */
    LLong CLIENT_StartRedirectServiceEx(Pointer pInParam,Pointer pOutParam);

    /**
     * 停止重定向服务
     * @param lServerHandle 服务句柄
     * @return
     */
    boolean CLIENT_StopRedirectService(LLong lServerHandle);

    /**
    * 设置重定向服务器的IP和Port
    * @param lDevHandle 重定向设备句柄
    * @param ARSIP 重定向设备IP
    * @param ARSPort 重定向设备端口
    * @param nRetry 设备主动注册尝试次数
    * @return
    */
    boolean CLIENT_SetAutoRegisterServerInfo(LLong lDevHandle,String ARSIP,short ARSPort,short nRetry);

    /**
    * 强制I帧,用于拉流优化
    * @param lLoginID 登录句柄
    * @param nChannelID 通道号
    * @param nSubChannel 码流类型:0:主码流,1:子码流1
    * @return
    */
    boolean CLIENT_MakeKeyFrame(LLong lLoginID,int nChannelID,int nSubChannel);

    /**
     * 关闭设备
     */
    boolean CLIENT_ShutDownDev(LLong lLoginID);

    /**
     * 设置通道录像状态(pRsBuffer内存由用户申请释放)
     */
    boolean CLIENT_SetupRecordState(LLong lLoginID,Pointer pRSBuffer,int nRSBufferlen);

    /**
     * 设置通道辅码流录像状态(pRsBuffer内存由用户申请释放)
     */
    boolean CLIENT_SetupExtraRecordState(LLong lLoginID,Pointer pRSBuffer,int nRSBufferlen,Pointer pReserved);

    /**
     * 增加远程录像备份任务, pInParam pOutParam 内存有用户申请释放
     * @param lRestoreID restoreId
     * @param pInParam -> NET_IN_ADD_REC_BAK_RST_REMOTE_TASK
     * @param pOutParam -> NET_OUT_ADD_REC_BAK_RST_REMOTE_TASK
     * @param nWaitTime 超时时间
     * @return 添加是否成功
     */
    boolean CLIENT_AddRecordBackupRestoreRemoteTask(LLong lRestoreID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置停车规则
     */
    boolean CLIENT_SetParkingRule(LLong lLoginID,NET_IN_SET_PARKINGRULE_INFO pstInParam,NET_OUT_SET_PARKINGRULE_INFO pstOutParam,int nWaitTime);

  // 设置运行模式参数,在CLIENT_Init之后调用 pstuRunParams->NET_RUNMODE_PARAMS
    boolean CLIENT_SetRunModeParams(Pointer pstuRunParams);

    public boolean CLIENT_DownloadPieceFile(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

  // 清除当前时间段内人数统计信息, 重新从0开始计算
  //CLIENT_ControlDevice接口的 DH_CTRL_CLEAR_SECTION_STAT命令参数
    public static class NET_CTRL_CLEAR_SECTION_STAT_INFO extends NetSDKLibStructure.SdkStructure
    {
        public int              dwSize;
        public int              nChannel;                             // 视频通道号

      public NET_CTRL_CLEAR_SECTION_STAT_INFO(){
          this.dwSize = this.size();
      }
    }

//刻录设备回调函数原形,lAttachHandle是CLIENT_AttachBurnState返回值, 每次1条,pBuf->dwSize == nBufLen (pBuf->NET_CB_BACKUPTASK_STATE)
    public interface fAttachBackupTaskStateCB extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pBuf,Pointer dwUser);
    }

//开始备份任务,pstInParam(NET_IN_START_BACKUP_TASK_INFO)与pstOutParam(NET_OUT_START_BACKUP_TASK_INFO)内存由用户申请释放
    public boolean CLIENT_StartBackupTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

//停止备份任务public boolean CLIENT_StopBackupTask(LLong lBackupSession);
//订阅备份状态,pstInParam(NET_IN_ATTACH_BACKUP_STATE)与pstOutParam(NET_OUT_ATTACH_BACKUP_STATE)内存由用户申请释放
    public LLong CLIENT_AttachBackupTaskState(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

//取消订阅备份状态,lAttachHandle是CLIENT_AttachBackupTaskState返回值
    public boolean CLIENT_DetachBackupTaskState(LLong lAttachHandle);

   // 获取安检门人数统计信息
  //CLIENT_NET_API BOOL CALL_METHOD CLIENT_GetPopulationStatistics(LLONG lLoginID, const NET_IN_GET_POPULATION_STATISTICS *pInParam, NET_OUT_GET_POPULATION_STATISTICS *pOutParam, int nWaitTime);
    public boolean CLIENT_GetPopulationStatistics(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

  // 订阅安检门人数变化信息,pstInParam与pstOutParam内存由用户申请释放
  //CLIENT_NET_API LLONG CALL_METHOD CLIENT_AttachPopulationStatistics(LLONG lLoginID, NET_IN_ATTACH_GATE_POPULATION_STATISTICS_INFO* pstInParam, NET_OUT_ATTACH_GATE_POPULATION_STATISTICS_INFO* pstOutParam , int nWaitTime);
    public LLong CLIENT_AttachPopulationStatistics(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

  // 取消订阅安检门人数变化信息 lPopulationStatisticsHandle 为 CLIENT_AttachPopulationStatistics 返回的句柄
  //CLIENT_NET_API BOOL CALL_METHOD CLIENT_DetachPopulationStatistics(LLONG lPopulationStatisticsHandle);
    public boolean CLIENT_DetachPopulationStatistics(LLong lPopulationStatisticsHandle);

    //创建车辆组
   //   CLIENT_NET_API BOOL CALL_METHOD CLIENT_CreateGroupForVehicleRegisterDB(LLONG lLoginID, const NET_IN_CREATE_GROUP_FOR_VEHICLE_REG_DB *pInParam, NET_OUT_CREATE_GROUP_FOR_VEHICLE_REG_DB *pOutParam, int nWaitTime);
    public  boolean CLIENT_CreateGroupForVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    //删除车辆组
   //  CLIENT_NET_API BOOL CALL_METHOD CLIENT_DeleteGroupFromVehicleRegisterDB(LLONG lLoginID, const NET_IN_DELETE_GROUP_FROM_VEHICLE_REG_DB *pInParam, NET_OUT_DELETE_GROUP_FROM_VEHICLE_REG_DB *pOutParam, int nWaitTime);
    public boolean CLIENT_DeleteGroupFromVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    //向车牌库添加车辆信息
   //  CLIENT_NET_API BOOL CALL_METHOD CLIENT_MultiAppendToVehicleRegisterDB(LLONG lLoginID, const NET_IN_MULTI_APPEND_TO_VEHICLE_REG_DB *pInParam, NET_OUT_MULTI_APPEND_TO_VEHICLE_REG_DB *pOutParam, int nWaitTime);
    public boolean CLIENT_MultiAppendToVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 开包工作台上报开包检查信息
    //  CLIENT_NET_API BOOL CALL_METHOD CLIENT_UploadUnpackingCheckInfo(LLONG lLoginID, const NET_IN_UPLOAD_UPPACKING_CHECK_INFO* pInParam, NET_OUT_UPLOAD_UPPACKING_CHECK_INFO* pOutParam, int nWaitTime);
    public  boolean CLIENT_UploadUnpackingCheckInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 网络应用组件,公司内部接口
     * 可用于获取前端设备的网络资源数据,如网络收发数据的速率等,pstuIn与pstuOut内存由用户申请释放，大小参照emType对应的结构体
     * @param lLoginID 登录句柄
     * @param emType 网络应用组件 操作类型 EM_RPC_NETAPP_TYPE
     * @param pstuIn 对应操作入参
     * @param pstuOut 对应操作出参
     * @param nWaitTime 超时时间
     * @return 添加是否成功
     */
    public boolean CLIENT_RPC_NetApp(LLong lLoginID,int emType,Pointer pstuIn,Pointer pstuOut,int nWaitTime);

    /**
     * 雷达操作
     * @param lLoginID 登录句柄
     * @param emType 网络应用组件 操作类型 EM_RADAR_OPERATE_TYPE
     * @param pInBuf 对应操作入参
     * @param pOutBuf 对应操作出参
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_RadarOperate(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 设置信号机备份模式,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_SET_BACKUP_MODE
     * @param pOutParam -> NET_OUT_SET_BACKUP_MODE
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_SetRtscBackupMode(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置信号机运行模式,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_SET_RUNNING_MODE
     * @param pOutParam -> NET_OUT_SET_RUNNING_MODE
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_SetRtscRunningMode(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取信号机运行模式,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_GET_RUNNING_MODE
     * @param pOutParam -> NET_OUT_GET_RUNNING_MODE
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_GetRtscRunningMode(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取信号机全局配置,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_GET_GLOBAL_PARAMETER
     * @param pOutParam -> NET_OUT_GET_GLOBAL_PARAMETER
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_GetRtscGlobalParam(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置信号机全局配置,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_SET_GLOBAL_PARAMETER
     * @param pOutParam -> NET_OUT_SET_GLOBAL_PARAMETER
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_SetRtscGlobalParam(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取信号机运行信息,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pInParam -> NET_IN_GET_RUNNING_INFO
     * @param pOutParam -> NET_OUT_GET_RUNNING_INFO
     * @param nWaitTime 超时时间
     * @return 是否成功
     */
    public boolean CLIENT_GetRtscRunningInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    // 手动选择球机要跟踪的轨迹目标
   // CLIENT_NET_API BOOL CALL_METHOD CLIENT_RadarManualTrack(LLONG lLoginID, const NET_IN_RADAR_MANUAL_TRACK* pstInParam, NET_OUT_RADAR_MANUAL_TRACK* pstOutParam, int nWaitTime);
    public boolean CLIENT_RadarManualTrack(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 西欧报警主机获取操作
     * @param lLoginID
     * @param emType NET_EM_GET_ALARMREGION_INFO 的枚举值
     * @param nWaitTime
     * @return boolean
     */
    public boolean CLIENT_GetAlarmRegionInfo(LLong lLoginID,int emType,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 标定球机和蓄水池污点位置
     * @param lLoginID
     * @param pstInParam -> NET_IN_SET_PTZ_WASH_POSISTION_INFO
     * @param pstOutParam -> NET_OUT_SET_PTZ_WASH_POSISTION_INFO
     * @param dwWaitTime
     * @return boolean
     */
    public boolean CLIENT_PtzSetWashPosistion(LLong lLoginID,NET_IN_SET_PTZ_WASH_POSISTION_INFO pstInParam,NET_OUT_SET_PTZ_WASH_POSISTION_INFO pstOutParam,int dwWaitTime);

    /**
     * 获取标定后的冲洗信息
     * @param lLoginID
     * @param pstInParam -> NET_IN_GET_PTZ_WASH_INFO
     * @param pstOutParam -> NET_OUT_GET_PTZ_WASH_INFO
     * @param dwWaitTime
     * @return boolean
     */
    public boolean CLIENT_PtzGetWashInfo(LLong lLoginID,NET_IN_GET_PTZ_WASH_INFO pstInParam,NET_OUT_GET_PTZ_WASH_INFO pstOutParam,int dwWaitTime);

    /**
     *按文件方式回放(lpRecordFile内存由用户申请释放)
     * @param lLoginID
     * @param lpRecordFile -> LPNET_RECORDFILE_INFO
     * @param hWnd -> Pointer
     * @param cbDownLoadPos -> fDownLoadPosCallBack
     * @param dwUserData
     * @return boolean
     */
    public LLong CLIENT_PlayBackByRecordFile(LLong lLoginID,NetSDKLibStructure.NET_RECORDFILE_INFO lpRecordFile,Pointer hWnd,fDownLoadPosCallBack cbDownLoadPos,Pointer dwUserData);

    // 开始查找录像文件
    public LLong CLIENT_FindFile(LLong lLoginID,int nChannelId,int nRecordFileType,Pointer cardid,NET_TIME time_start,NET_TIME time_end,boolean bTime,int waittime);

    /**
     * 订阅人群分布图实时统计信息
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_ATTACH_CROWDDISTRI_MAP_INFO  输入参数, 由用户申请资源
     * @param pstOutParam -> NET_OUT_ATTACH_CROWDDISTRI_MAP_INFO  输出参数, 由用户申请资源
     * @param nWaitTime  等待超时时间
     * @return LLong  订阅句柄
     */
    public LLong CLIENT_AttachCrowdDistriMap(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 订阅人群分布图实时统计信息回调函数原型,
     * lAttachHandle为CLIENT_AttachCrowdDistriMap接口的返回值,
     * pstResult 解析结构体为 NET_CB_CROWD_DISTRI_STREAM_INFO
     */
    public interface fCrowdDistriStream extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstResult,Pointer dwUser);
    }

    /**
     * 取消订阅人群分布图实时统计信息
     * @param lAttachHandle  订阅句柄，为接口CLIENT_AttachCrowdDistriMap的返回值
     * @return boolean
     */
    public boolean CLIENT_DetachCrowdDistriMap(LLong lAttachHandle);

    /**
     * 获取人群分布图全局和区域实时人数统计值
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_GETSUMMARY_CROWDDISTRI_MAP_INFO  接口输入参数
     * @param pstOutParam -> NET_OUT_GETSUMMARY_CROWDDISTRI_MAP_INFO 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_GetSummaryCrowdDistriMap(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 下发人员信息录入结果
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_PERSON_INFO_INPUT_RESULT  接口输入参数
     * @param pstOutParam -> NET_OUT_PERSON_INFO_INPUT_RESULT 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_SetPersonInfoInputResult(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 根据查询条件返回录像备份任务的信息表,pInParam与pOutParam内存由用户申请释放
     * @param lLoginID  登陆句柄
     * @param pInParam -> NET_IN_FIND_REC_BAK_RST_TASK  接口输入参数
     * @param pOutParam -> NET_OUT_FIND_REC_BAK_RST_TASK 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_FindRecordBackupRestoreTaskInfos(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 功能描述:异步纯透传订阅接口
     * @param lLoginID  登录接口返回的句柄
     * @param pInParam -> NET_IN_ATTACH_TRANSMIT_INFO  异步纯透传接口输入参数
     * @param pOutParam -> NET_OUT_ATTACH_TRANSMIT_INFO 异步纯透传接口输出参数
     * @param nWaittime  接口超时时间
     * @return LLong 异步纯透传句柄
     */
    public LLong CLIENT_AttachTransmitInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * CLIENT_AttachTransmitInfo()回调函数原型，第一个参数lAttachHandle是CLIENT_AttachTransmitInfo返回值,
     */
    public interface AsyncTransmitInfoCallBack extends Callback {
        public void invoke(LLong lAttachHandle,NET_CB_TRANSMIT_INFO pTransmitInfo,Pointer dwUser);
    }

    /**
     * 功能描述:异步纯透传取消订阅接口
     * @param lAttachHandle  异步纯透传句柄，即CLIENT_AttachTransmitInfo接口的返回值
     * @param pInParam -> NET_IN_DETACH_TRANSMIT_INFO  异步纯透传取消订阅接口输入参数
     * @param pOutParam -> NET_OUT_DETACH_TRANSMIT_INFO 异步纯透传取消订阅接口输出参数
     * @param nWaittime  接口超时时间
     * @return boolean TRUE :成功; FALSE :失败
     */
    public boolean CLIENT_DetachTransmitInfo(LLong lAttachHandle,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 雷达订阅RFID卡片信息
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_ATTACH_RADAR_RFIDCARD_INFO  接口输入参数
     * @param pstOutParam -> NET_OUT_ATTACH_RADAR_RFIDCARD_INFO 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return LLong
     */
    public LLong CLIENT_AttachRadarRFIDCardInfo(LLong lLoginID,NET_IN_ATTACH_RADAR_RFIDCARD_INFO pstInParam,NET_OUT_ATTACH_RADAR_RFIDCARD_INFO pstOutParam,int nWaitTime);

    /**
     * 雷达取消订阅RFID卡片信息
     * @param lAttachHandle  订阅句柄
     * @return boolean
     */
    public boolean CLIENT_DetachRadarRFIDCardInfo(LLong lAttachHandle);

    /**
     * 查询RFID的工作模式
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_RADAR_GET_RFID_MODE  接口输入参数
     * @param pstOutParam -> NET_OUT_RADAR_GET_RFID_MODE 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_GetRadarRFIDMode(LLong lLoginID,NET_IN_RADAR_GET_RFID_MODE pstInParam,NET_OUT_RADAR_GET_RFID_MODE pstOutParam,int nWaitTime);

    /**
     * 设置RFID的工作模式
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_RADAR_SET_RFID_MODE  接口输入参数
     * @param pstOutParam -> NET_OUT_RADAR_SET_RFID_MODE 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_SetRadarRFIDMode(LLong lLoginID,NET_IN_RADAR_SET_RFID_MODE pstInParam,NET_OUT_RADAR_SET_RFID_MODE pstOutParam,int nWaitTime);

    /**
     *	按条件删除车牌库中的车牌
     * @param lLoginID  登陆句柄
     * @param pstInParam -> NET_IN_DEL_BY_CONDITION_FROM_VEHICLE_REG_DB  接口输入参数
     * @param pstOutParam -> NET_OUT_DEL_BY_CONDITION_FROM_VEHICLE_REG_DB 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_DeleteByConditionFromVehicleRegisterDB(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 设置消费结果
     * @param lLoginID  登陆句柄
     * @param pInParam -> NET_IN_SET_CONSUME_RESULT  接口输入参数
     * @param pOutParam -> NET_OUT_SET_CONSUME_RESULT 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_SetConsumeResult(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 远程休眠模式
     * @param lLoginID  登录句柄
     * @param pInParam -> NET_IN_REMOTE_SLEEP  接口输入参数
     * @param pOutParam -> NET_OUT_REMOTE_SLEEP 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return boolean
     */
    public boolean CLIENT_RemoteSleep(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 自定义定时抓图订阅接口(目前智慧养殖猪温检测在用)
     * @param lLoginID  登录句柄
     * @param pInParam -> NET_IN_ATTACH_CUSTOM_SNAP_INFO  接口输入参数
     * @param pOutParam -> NET_OUT_ATTACH_CUSTOM_SNAP_INFO 接口输出参数
     * @param nWaitTime  等待超时时间
     * @return LLong
     */
    public LLong CLIENT_AttachCustomSnapInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 自定义定时抓图订阅回调函数原型, 
     * @param lAttachHandle  为 CLIENT_AttachCustomSnapInfo 接口的返回值
     * @param pstResult      参考结构体 NET_CB_CUSTOM_SNAP_INFO
     */
    public interface fAttachCustomSnapInfo extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstResult,Pointer pBuf,int dwBufSize,Pointer dwUser);
    }

    /**
     * 取消自定义定时抓图订阅接口(目前智慧养殖猪温检测在用)
     * @param lAttachHandle 订阅句柄
     * @return boolean
     */
    public boolean CLIENT_DetachCustomSnapInfo(LLong lAttachHandle);

	/**
     * 物模型属性订阅回调函数原型, lAttachHandle为CLIENT_AttachThingsInfo接口的返回值
	 * @param lAttachHandle 订阅句柄
     * @param pstResult 物模型属性订阅回调信息, 参考{@link NET_CB_THINGS_INFO}
	 * @return void
	 */
    public interface fThingsCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstResult,Pointer dwUserData);
    }

    /**
     * 停止获取设备历史数据
     * @param lFindHandle 查找句柄
	 * @return TRUE表示成功  FALSE表示失败
	 */
    public boolean CLIENT_StopThingsHistoryData(LLong lFindHandle);

    /**
     * 获取设备历史数据结果接口
     * @param lFindHandle 查找句柄
     * @param pstInParam 接口输入参数，参考{@link NET_IN_THINGS_DOFIND_HISTORYDATA}
     * @param pstOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_DOFIND_HISTORYDATA}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_DoFindThingsHistoryData(LLong lFindHandle,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 开始获取设备历史数据接口
     * @param lLoginID 登录句柄
     * @param pstInParam 接口输入参数，参考{@link NET_IN_THINGS_START_HISTORYDATA}
     * @param pstOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_START_HISTORYDATA}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public LLong CLIENT_StartThingsHistoryData(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 智慧用电Things物模型服务调用接口
     * @param lLoginID 登录句柄
     * @param emType 物模型服务类型,参考{@link EM_THINGS_SERVICE_TYPE}
     * @param pInBuf 接口输入参数，参考emType类型，内存资源由用户申请和释放
     * @param pOutBuf 接口输出参数，参考emType类型，内存资源由用户申请和释放
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_ThingsServiceOperate(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * 智慧用电Things获取设备连接状态信息接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_GET_NETSTATE}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_GET_NETSTATE}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_GetThingsNetState(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 智慧用电Things获取设备列表接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_GET_DEVLIST}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_GET_DEVLIST}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_GetThingsDevList(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 智慧用电Things获取设备能力集接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_GET_CAPS}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_GET_CAPS}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_GetThingsCaps(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 智慧用电Things取消物模型属性订阅接口
     * @param lAttachHandle 订阅句柄
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_DetachThingsInfo(LLong lAttachHandle);

    /**
     * 智慧用电Things物模型属性订阅接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_ATTACH}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_ATTACH}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return 订阅句柄
	 */
    public LLong CLIENT_AttachThingsInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 智慧用电Things配置设置接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_SET}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_SET}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_SetThingsConfig(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 智慧用电Things配置获取接口
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_THINGS_GET}
     * @param pOutParam 接口输出参数, 参考{@link NET_OUT_THINGS_GET}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_GetThingsConfig(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取流量统计信息,pstInParam与pstOutParam内存由用户申请释放
     * @param lLoginID 登录句柄
     * @param pstInParam 接口输入参数，参考{@link NET_IN_TRAFFICSTARTFINDSTAT}
     * @param pstOutParam 接口输出参数, 参考{@link NET_OUT_TRAFFICSTARTFINDSTAT}
     * @return 查询句柄
     */
    public LLong CLIENT_StartFindFluxStat(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam);

    /**
     *@brief 继续查询流量统计,pstInParam与pstOutParam内存由用户申请释放
     * @param lFindHandle 查询句柄
     * @param pstInParam 接口输入参数，参考{@link NET_IN_TRAFFICDOFINDSTAT}
     * @param pstOutParam 接口输出参数, 参考{@link NET_OUT_TRAFFICDOFINDSTAT}
     * @return
     */
    public int CLIENT_DoFindFluxStat(LLong lFindHandle,Pointer pstInParam,Pointer pstOutParam);

    /**
     * 结束查询流量统计
     */
    public boolean CLIENT_StopFindFluxStat(LLong lFindHandle);

    /**
     * 获取智能订阅参数CustomInfo的格式化字符串能力
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_GET_CUSTOMINFO_CAPS}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_GET_CUSTOMINFO_CAPS}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetCustomInfoCaps(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取温度值
     * @param lLoginID 登录句柄
     * @param pInParam 接口输入参数，参考{@link NET_IN_GET_TEMPERATUREEX}
     * @param pstOutParam 接口输出参数, 参考{@link NET_OUT_GET_TEMPERATUREEX}
     * @return 查询句柄
     */
    public boolean CLIENT_FaceBoard_GetTemperatureEx(LLong lLoginID,Pointer pInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 考试计划
     * CLIENT_SetExamRecordingPlans 接口入参
     * CLIENT_SetExamRecordingPlans 接口出参
     * 添加考试录像计划
     *param[in]		lLoginID:		登录句柄
     *param[in]		pstuInParam:	接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SET_EXAM_RECORDING_PLANS}
     *param[out]		pstuOutParam:	接口输出参数, 内存资源由用户申请和释放  {@link NET_OUT_SET_EXAM_RECORDING_PLANS}
     *param[in]		nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetExamRecordingPlans(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取SMART扫描信息
     * @param lLoginID 登录句柄
     * @param pstuInParam 接口输入参数，参考{@link NET_IN_GET_DEV_STORAGE_SMART_VALUE}
     * @param pstuOutParam 接口输出参数, 参考{@link NET_OUT_GET_DEV_STORAGE_SMART_VALUE}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return 
     */
    public boolean CLIENT_GetDevStorageSmartValue(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取设备各网卡的上传与发送速率
     * @param lLoginID 登录句柄
     * @param pstuInParam 接口输入参数，参考{@link NET_IN_GET_DEVICE_ETH_BAND_INFO}
     * @param pstuOutParam 接口输出参数, 参考{@link NET_OUT_GET_DEVICE_ETH_BAND_INFO}
     * @param nWaitTime 接口超时时间, 单位毫秒
     * @return 
     */
    public boolean CLIENT_GetDeviceEthBandInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     *@ingroup       functions
     *@brief         销毁业务sdk模块
     *@param[in]     lSubBizHandle  业务sdk句柄，由CLIENT_CreateSubBusinessModule接口返回
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_DestroySubBusinessModule(LLong lSubBizHandle);

    /**
     *@ingroup       functions
     *@brief         创建业务sdk模块
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 NET_IN_CREAT_SUB_BUSINESS_MDL_INFO
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 NET_OUT_CREAT_SUB_BUSINESS_MDL_INFO
     *@retval LLONG
     *@return	业务sdk句柄，非0表示成功  0表示失败
     */
    public LLong CLIENT_CreateSubBusinessModule(Pointer pInParam,Pointer pOutParam);

    /**
     *@ingroup       functions
     *@brief         启动子连接监听服务
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 NET_IN_START_SUBLINK_LISTEN_SERVER
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 NET_OUT_START_SUBLINK_LISTEN_SERVER
     *@retval LLONG
     *@return	子链接监听服务句柄, 非0表示成功  0表示失败
     */
    public LLong CLIENT_StartSubLinkListenServer(Pointer pInParam,Pointer pOutParam);

    /**
     *@ingroup       functions
     *@brief         停止子连接监听服务
     *@param[in]     lListenServer  监听服务句柄，由CLIENT_StartSubLinkListenServer接口返回
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_StopSubLinkListenServer(LLong lListenServer);

    /**
     *@ingroup       functions
     *@brief         发送创建子连接所需的信息给设备, 由主业务模块调用
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 NET_IN_TRANSFER_SUBLINK_INFO
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 NET_OUT_TRANSFER_SUBLINK_INFO
     *@param[in]     nWaitTime 接口超时时间, 单位毫秒
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_TransferSubLinkInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     *@ingroup       functions
     *@brief         设置私有透传通道参数
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 NET_IN_SET_TRANSMITTUNNEL_PARAM
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 NET_OUT_SET_TRANSMITTUNNEL_PARAM
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_SetTransmitTunnelParam(Pointer pInParam,Pointer pOutParam);

    /**
     *@ingroup       functions
     *@brief         创建隧道
     *@param[in]     lSubBizHandle    业务sdk句柄，由CLIENT_CreateSubBusinessModule接口返回
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 NET_IN_CREATE_TRANSMIT_TUNNEL
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 NET_OUT_CREATE_TRANSMIT_TUNNEL
     *@retval LLONG
     *@return	透传隧道业务句柄，非0表示成功  0表示失败
     */
    public LLong CLIENT_CreateTransmitTunnel(LLong lSubBizHandle,Pointer pInParam,Pointer pOutParam);

    /**
     *@ingroup       functions
     *@brief         销毁隧道
     *@param[in]     lTransmitHandle  透传隧道业务句柄，由CLIENT_CreateTransmitTunnel接口返回
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_DestroyTransmitTunnel(LLong lTransmitHandle);

    /**
     *@ingroup       callback
     *@brief         子连接监听服务回调函数原型
     *@param[out]    lListenServer 子链接监听服务句柄, 由CLIENT_StartSubLinkListenServer接口返回
     *@param[out]    lSubBizHandle 分压业务sdk句柄, 由CLIENT_CreateSubBusinessModule接口返回
     *@param[out]    pstSubLinkCallBack 子链接监听服务回调信息 NET_SUBLINK_SERVER_CALLBACK
     *@retval void
     */
    public interface fSubLinkServiceCallBack extends Callback {
        public void invoke(LLong lListenServer,LLong lSubBizHandle,Pointer pstSubLinkCallBack);
    }

    /**
     *@ingroup       callback
     *@brief         隧道业务连接断开回调
     *@param[out]    lSubBizHandle 下载句柄, 由CLIENT_CreateSubBusinessModule接口返回
     *@param[out]    lOperateHandle 业务句柄
     *@param[out]    pstDisConnectInfo 断线回调数据 NET_TRANSMIT_DISCONNECT_CALLBACK
     *@retval void
     */
    public interface fTransmitDisConnectCallBack extends Callback {
        public void invoke(LLong lSubBizHandle,LLong lOperateHandle,Pointer pstDisConnectInfo);
    }

    /**
     *@ingroup       functions
     *@brief         获取/设置解码窗口输出OSD信息扩展接口(pInparam, pOutParam内存由用户申请释放)
     *@param[in]     lLoginHandle    登录句柄
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SPLIT_GET_OSD_EX}
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SPLIT_GET_OSD_EX}
     *@param[in]     nWaitTime 接口超时时间, 单位毫秒
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_GetSplitOSDEx(LLong lLoginHandle,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     *@ingroup       functions
     *@brief         获取/设置解码窗口输出OSD信息扩展接口(pInparam, pOutParam内存由用户申请释放)
     *@param[in]     lLoginHandle    登录句柄
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SPLIT_SET_OSD_EX}
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SPLIT_SET_OSD_EX}
     *@param[in]     nWaitTime 接口超时时间, 单位毫秒
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_SetSplitOSDEx(LLong lLoginHandle,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     *@ingroup       functions
     *@brief         销毁隧道
     *@param[in]     lLoginHandle    登录句柄
     *@param[in]     emType    入参类型枚举，{@link NET_SPLIT_OPERATE_TYPE}
     *@param[in]     pInParam  接口输入参数, 内存资源由用户申请和释放，类型参考枚举注释{@link NET_SPLIT_OPERATE_TYPE}
     *@param[out]    pOutParam 接口输出参数, 内存资源由用户申请和释放，类型参考枚举注释{@link NET_SPLIT_OPERATE_TYPE}
     *@param[in]     nWaitTime 接口超时时间, 单位毫秒
     *@retval BOOL
     *@return	TRUE表示成功  FALSE表示失败
     */
    public boolean CLIENT_OperateSplit(LLong lLoginHandle,int emType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 根据中心公钥获取锁具随机公钥
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放  {@link NET_IN_GET_DYNAMIC_LOCK_RANDOM_PUBLICKEY_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_GET_DYNAMIC_LOCK_RANDOM_PUBLICKEY_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetDynamicLockRandomPublicKey(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置通讯秘钥
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SET_DYNAMIC_LOCK_COMMUNICATIONKEY_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SET_DYNAMIC_LOCK_COMMUNICATIONKEY_INFO}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetDynamicLockCommunicationKey(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置开锁密钥
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link  NET_IN_SET_DYNAMIC_LOCK_OPENKEY_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SET_DYNAMIC_LOCK_OPENKEY_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetDynamicLockOpenKey(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置临时身份码
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放  {@link NET_IN_SET_DYNAMIC_LOCK_TEMP_USERID_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SET_DYNAMIC_LOCK_TEMP_USERID_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetDynamicLockTempUserID(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置开锁码
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SET_DYNAMIC_LOCK_OPEN_CODE_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SET_DYNAMIC_LOCK_OPEN_CODE_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetDynamicLockOpenCode(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 推送智能分析图片文件和规则信息，当CLIENT_AddAnalyseTask的数据源类型emDataSourceType为 EM_DATA_SOURCE_PUSH_PICFILE_BYRULE 时使用
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_PUSH_ANALYSE_PICTURE_FILE_BYRULE}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_PUSH_ANALYSE_PICTURE_FILE_BYRULE}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_PushAnalysePictureFileByRule(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 指定智能事件解析所用的结构体 用于解决java大结构体new对象慢导致的问题.该接口全局有效,建议在SDK初始化前调用
     * @param pInParam 接口输入参数, 内存资源由用户申请和释放，参考{@link NET_IN_SET_IVSEVENT_PARSE_INFO}
     * @return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetIVSEventParseType(NET_IN_SET_IVSEVENT_PARSE_INFO pInParam);

    /**
     * 平台下发轮询配置
     * param[in] lLoginID 登录句柄
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link NET_IN_SET_POLLING_CONFIG}
     * param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_SET_POLLING_CONFIG}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetPollingConfig(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 按通道获取设备智能业务的运行状态
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数, 内存资源由用户申请和释放   {@link NET_IN_GET_CHANNEL_STATE}
     *param[out] pstuOutParam 接口输出参数, 内存资源由用户申请和释放  {@link NET_OUT_GET_CHANNEL_STATE}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetChannelState(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 获取隐私遮挡列表
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_GET_PRIVACY_MASKING}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_GET_PRIVACY_MASKING}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetPrivacyMasking(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 设置隐私遮挡列表
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_SET_PRIVACY_MASKING}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_SET_PRIVACY_MASKING}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetPrivacyMasking(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 使能或关闭所有隐私遮挡块
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_SET_PRIVACY_MASKING_ENABLE}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_SET_PRIVACY_MASKING_ENABLE}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetPrivacyMaskingEnable(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 获取隐私遮挡总开关使能状态
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_GET_PRIVACY_MASKING_ENABLE}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_GET_PRIVACY_MASKING_ENABLE}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetPrivacyMaskingEnable(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 跳转到隐私遮档块
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_GOTO_PRIVACY_MASKING}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_GOTO_PRIVACY_MASKING}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GotoPrivacyMasking(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 删除隐私遮档块
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_DELETE_PRIVACY_MASKING}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_DELETE_PRIVACY_MASKING}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DeletePrivacyMasking(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 清除遮挡
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数 ,{@link NET_IN_CLEAR_PRIVACY_MASKING}
     * param[out]pstuOutParam 接口输出参数,,{@link NET_OUT_CLEAR_PRIVACY_MASKING}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_ClearPrivacyMasking(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 远程复位（消防）
     * param[in] lLoginID 登录句柄
     * param[in] emType 操作类型枚举，{@link EM_RADAR_OPERATE_TYPE}
     * param[in] pInParam 接口输入参数 ,参考emType
     * param[out]pOutParam 接口输出参数 ,参考emType
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_RadarOperate(LLong lLoginID,EM_RADAR_OPERATE_TYPE emType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 订阅mini雷达的报警点信息
     * param[in] lLoginID 登录句柄
     * param[in] pInParam 接口输入参数 ,{@link NET_IN_MINI_RADAR_ALARMPOINTINFO}
     * param[out]pOutParam 接口输出参数 ,{@link NET_OUT_MINI_RADAR_ALARMPOINTINFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_AttachMiniRadarAlarmPointInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 远程复位（消防）
     * param[in] lLoginID 登录句柄
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachMiniRadarAlarmPointInfo(LLong lAttachHandle);

    /**
     * 远程复位（消防）
     * param[in] lLoginID 登录句柄
     * param[in] pInParam 接口输入参数 ,{@link NET_IN_SMOKE_REMOTE_REBOOT_INFO}
     * param[out]pOutParam 接口输出参数 ,{@link NET_OUT_SMOKE_REMOTE_REBOOT_INFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SmokeRemoteReboot(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * mini雷达报警点信息
     * 上报的mini雷达报警点信息
     * mini雷达报警点信息回调函数指针
     */
    public interface fMiniRadarAlarmPointInfoCallBack extends Callback {
/**
         *
         * @param lLoginId 登录句柄
         * @param lAttachHandle 订阅句柄
         * @param pBuf {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_MINI_RADAR_NOTIFY_ALARMPOINTINFO}
         * @param dwBufLen pBuf中结构体的长度
         * @param pReserved 保留数据
         * @param dwUser 用户自定义数据
         */
        public void invoke(LLong lLoginId,LLong lAttachHandle,Pointer pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    /**
     * 获取业务库管理的舱位信息
     *param[in] lLoginID 登录句柄
     *param[in] pInParam 接口输入参数, 内存资源由用户申请和释放 {@link  NET_IN_GET_FINANCIAL_CABIN_INFO}
     *param[out] pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link  NET_OUT_GET_FINANCIAL_CABIN_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetFinancialCabinInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取金库门状态
     *param[in]		lLoginID:		登录句柄
     *param[in]		pstuInParam:	接口输入参数, 内存资源由用户申请和释放   {@link  NET_IN_GET_VAULTDOOR_STATE_INFO}
     *param[out]		pstuOutParam:	接口输出参数, 内存资源由用户申请和释放 {@link  NET_OUT_GET_VAULTDOOR_STATE_INFO}
     *param[in]		nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功FALSE表示失败
     */
    public boolean CLIENT_GetVaultDoorState(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取金融柜体设备状态
     *param[in]		lLoginID:		登录句柄
     *param[in]		pstuInParam:	接口输入参数, 内存资源由用户申请和释放  {@link  NET_IN_GET_CABINET_STATE_INFO}
     *param[out]		pstuOutParam:	接口输出参数, 内存资源由用户申请和释放  {@link  NET_OUT_GET_CABINET_STATE_INFO}
     *param[in]		nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功FALSE表示失败
     */
    public boolean CLIENT_GetFinancialCabinetState(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取当前电梯运行信息
     *param[in]		lLoginID:		登录句柄
     *param[in]		pstuInParam:	接口输入参数, 内存资源由用户申请和释放  {@link  NET_IN_GET_ELEVATOR_WORK_INFO}
     *param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放  {@link  NET_OUT_GET_ELEVATOR_WORK_INFO}
     *param[in]		nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功FALSE表示失败
     */
    public boolean CLIENT_GetElevatorWorkInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取水质检测能力
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_WATERDATA_STAT_SERVER_GETCAPS_INFO}
     *param[out]pstuOutParam 接口输出参数  {@link  NET_OUT_WATERDATA_STAT_SERVER_GETCAPS_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_GetWaterDataStatServerCaps(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 水质检测实时数据获取
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_WATERDATA_STAT_SERVER_GETDATA_INFO}
     *param[out]pstuOutParam 接口输出参数  {@link  NET_OUT_WATERDATA_STAT_SERVER_GETDATA_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_GetWaterDataStatServerWaterData(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 开始水质检测报表数据查询
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_START_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[out]pstuOutParam 接口输出参数 {@link  NET_OUT_START_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_StartFindWaterDataStatServer(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 水质检测报表数据查询
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_DO_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[out]pstuOutParam 接口输出参数  {@link  NET_OUT_DO_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_DoFindWaterDataStatServer(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 停止水质检测报表数据查询
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_STOP_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[out]pstuOutParam 接口输出参数 {@link  NET_OUT_STOP_FIND_WATERDATA_STAT_SERVER_INFO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_StopFindWaterDataStatServer(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取设备状态, DMSS专用接口, pInParam与pOutParam内存由用户申请释放
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_UNIFIEDINFOCOLLECT_GET_DEVSTATUS}
     *param[out]pstuOutParam 接口输出参数 {@link  NET_OUT_UNIFIEDINFOCOLLECT_GET_DEVSTATUS}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetUnifiedStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 主从联动组, 操作接口,pInParam与pOutParam内存由用户申请释放,大小参照emOperateType对应的结构体
     * param[in] lLoginID 登录句柄
     * param[in] emOperateType 接口输入参数 ,参考枚举 {@link  EM_MSGROUP_OPERATE_TYPE}
     * param[in] pInParam 接口输入参数 ,参考枚举对应的入参
     * param[out] pOutParam 接口输出参数 ,参考枚举对应的出参
     * param[in] nWaitTime 接口超时时间，默认3000, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_OperateMasterSlaveGroup(LLong lLoginID,int emOperateType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取token, pstuInParam与pstuOutParam内存由用户申请释放
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数   {@link  NET_IN_MAKE_TOKEN}
     *param[out]pstuOutParam 接口输出参数   {@link  NET_OUT_MAKE_TOKEN}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_MakeToken(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取共享文件夹工作目录信息
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数  {@link  NET_IN_NAS_DIRECTORY_GET_INFO}
     *param[out]  pstOutParam 接口输出参数  {@link  NET_OUT_NAS_DIRECTORY_GET_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetNASDirectoryInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 根据文件路径获取外部导入文件信息
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数  {@link  NET_IN_GET_FILE_INFO_BY_PATH_INFO}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_GET_FILE_INFO_BY_PATH_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetFileManagerExFileInfoByPath(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /***************************工装合规接口Start***************************************************/    
    /** 
     * 创建工装合规组
     *param[in]   lLoginID 登录句柄
     *param[in]   pInParam 接口输入参数 {@link  NET_IN_CREATE_WORKSUIT_COMPARE_GROUP}
     *param[out]  pOutParam 接口输出参数 {@link  NET_OUT_CREATE_WORKSUIT_COMPARE_GROUP}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_CreateWorkSuitCompareGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 删除工装合规组
     *param[in]   lLoginID 登录句柄
     *param[in]   pInParam 接口输入参数 {@link  NET_IN_DELETE_WORKSUIT_COMPARE_GROUP}
     *param[out]  pOutParam 接口输出参数 {@link  NET_OUT_DELETE_WORKSUIT_COMPARE_GROUP}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DeleteWorkSuitCompareGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 查找工装合规组信息
     *param[in]   lLoginID 登录句柄
     *param[in]   pInParam 接口输入参数 {@link  NET_IN_FIND_WORKSUIT_COMPARE_GROUP}
     *param[out]  pOutParam 接口输出参数 {@link  NET_OUT_FIND_WORKSUIT_COMPARE_GROUP}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_FindWorkSuitCompareGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 批量添加工装合规样本
     *param[in]   lLoginID 登录句柄
     *param[in]   pInParam 接口输入参数 {@link  NET_IN_MULTI_APPEND_TO_WORKSUIT_GROUP}
     *param[out]  pOutParam 接口输出参数 {@link  NET_OUT_MULTI_APPEND_TO_WORKSUIT_GROUP}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_MultiAppendToWorkSuitCompareGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 通过全景图唯一标识符删除工装合规样本
     *param[in]   lLoginID 登录句柄
     *param[in]   pInParam 接口输入参数 {@link  NET_IN_DELETE_WORKSUIT_BY_SOURCEUID}
     *param[out]  pOutParam 接口输出参数 {@link  NET_OUT_DELETE_WORKSUIT_BY_SOURCEUID}
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DeleteWorkSuitBySourceUID(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /***************************工装合规接口End***************************************************/
    /** 
     * 区分报表查询, 单独实现一套全量查询数据接口
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数 {@link  NET_IN_START_FIND_DETAIL_CLUSTER}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_START_FIND_DETAIL_CLUSTER}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_StartFindDetailNumberStatCluster(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
     * 分批查询全量记录
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数 {@link  NET_IN_DO_FIND_DETAIL_CLUSTER}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_DO_FIND_DETAIL_CLUSTER}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DoFindDetailNumberStatCluster(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
     * 停止查询
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数{@link  NET_IN_STOP_FIND_DETAIL_CLUSTER_INFO}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_STOP_FIND_DETAIL_CLUSTER_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_StopFindDetailNumberStatCluster(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
     * 平台主动获取设备聚档状态
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数{@link  NET_IN_GET_CLUSTER_STATE_INFO}
     *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_GET_CLUSTER_STATE_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetClusterState(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
     * 获取烟感数据
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数{@link  NET_IN_GET_SMOKE_DATA}
     *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_GET_SMOKE_DATA}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetSmokeData(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 修改车辆组
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数{@link  NET_IN_MODIFY_GROUP_FOR_VEHICLE_REG_DB}
     *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_MODIFY_GROUP_FOR_VEHICLE_REG_DB}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_ModifyGroupForVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

     /**
       * 查找车辆组信息
       *param[in]   lLoginID 登录句柄
       *param[in]   pstInParam 接口输入参数{@link  NET_IN_FIND_GROUP_FROM_VEHICLE_REG_DB}
       *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_FIND_GROUP_FROM_VEHICLE_REG_DB}
       *param[in]   nWaitTime 接口超时时间, 单位毫秒
       *return TRUE表示成功 FALSE表示失败
      */
    public boolean CLIENT_FindGroupFormVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

      /**
         * 修改车辆信息
         *param[in]   lLoginID 登录句柄
         *param[in]   pstInParam 接口输入参数{@link  NET_IN_MODIFY_VEHICLE_FOR_VEHICLE_REG_DB}
         *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_MODIFY_VEHICLE_FOR_VEHICLE_REG_DB}
         *param[in]   nWaitTime 接口超时时间, 单位毫秒
         *return TRUE表示成功 FALSE表示失败
         */
    public boolean CLIENT_ModifyVehicleForVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

        /**
         * 删除车辆信息
         *param[in]   lLoginID 登录句柄
         *param[in]   pstInParam 接口输入参数{@link  NET_IN_DELETE_VEHICLE_FROM_VEHICLE_REG_DB}
         *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_DELETE_VEHICLE_FROM_VEHICLE_REG_DB}
         *param[in]   nWaitTime 接口超时时间, 单位毫秒
         *return TRUE表示成功 FALSE表示失败
         */
    public boolean CLIENT_DeleteVehicleFromVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

        /**
         * 向指定注册库查询车辆
         *param[in]   lLoginID 登录句柄
         *param[in]   pstInParam 接口输入参数{@link  NET_IN_START_FIND_VEHICLE_REG_DB}
         *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_START_FIND_VEHICLE_REG_DB}
         *param[in]   nWaitTime 接口超时时间, 单位毫秒
         *return TRUE表示成功 FALSE表示失败
         */
    public boolean CLIENT_StartFindVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

        /**
         * 获取车辆查询结果信息
         *param[in]   lLoginID 登录句柄
         *param[in]   pstInParam 接口输入参数{@link  NET_IN_DO_FIND_VEHICLE_REG_DB}
         *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_DO_FIND_VEHICLE_REG_DB}
         *param[in]   nWaitTime 接口超时时间, 单位毫秒
         *return TRUE表示成功 FALSE表示失败
         */
    public boolean CLIENT_DoFindVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

       /**
        * 结束车辆查询
        *param[in]   lLoginID 登录句柄
        *param[in]   pstInParam 接口输入参数{@link  NET_IN_STOP_FIND_VEHICLE_REG_DB}
        *param[out]  pstOutParam 接口输出参数{@link  NET_OUT_STOP_FIND_VEHICLE_REG_DB}
        *param[in]   nWaitTime 接口超时时间, 单位毫秒
        *return TRUE表示成功 FALSE表示失败
        */
    public boolean CLIENT_StopFindVehicleRegisterDB(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     *订阅陀螺仪数据接口回调函数原型, lAttachGyroHandle为CLIENT_AttachGyro接口的返回值
     *param[out] lAttachGyroHandle 订阅句柄
     *param[out] pstuGyroDataInfo 订阅的陀螺仪数据回调信息   {@link NET_NOTIFY_GYRO_DATA_INFO}
     *param[out] dwUser 用户信息
     *return void
     */
    public interface fNotifyGyroData extends Callback {
        public void invoke(LLong lAttachGyroHandle,Pointer pstuGyroDataInfo,Pointer dwUser);
    }

    /**
     * 订阅陀螺仪数据接口
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_ATTACH_GYRO}
     *param[out] pstuOutParam 接口输出参数  {@link  NET_OUT_ATTACH_GYRO}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return 返回订阅句柄
     */
    public LLong CLIENT_AttachGyro(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 取消陀螺仪数据订阅接口
     *param[in] lAttachHandle 订阅句柄
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachGyro(LLong lAttachHandle);

   /** 
    * 立体行为-视频统计信息回调函数原形，
    * param[out] lAttachHandle 是 CLIENT_AttachVideoStatistics返回值
    * param[out] emType 是枚举{@link NET_EM_VS_TYPE}的值
    * param[out] pBuf  是对应结构体数据指针，参考枚举值描述
    */
    public interface fVideoStatisticsInfoCallBack extends Callback {
        public void invoke(LLong lAttachHandle,int emType,Pointer pBuf,int dwBufLen,Pointer dwUser);
    }

    /**
     * 订阅客流统计服务实时数据
     *param[in] lLoginID 登录句柄
     *param[in] pstuInParam 接口输入参数  {@link  NET_IN_ATTACH_VIDEO_STATISTICS}
     *param[out] pstuOutParam 接口输出参数  {@link  NET_OUT_ATTACH_VIDEO_STATISTICS}
     *param[in] nWaitTime 接口超时时间, 单位毫秒
     *return 返回订阅句柄
     */
    public LLong CLIENT_AttachVideoStatistics(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 取消订阅客流统计服务实时数据
     *param[in] lAttachHandle 订阅句柄
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachVideoStatistics(LLong lAttachHandle);

    /**
     * 智能事件开始查询
     *param[in]   lLoginID 登录句柄
     *param[in]   pstInParam 接口输入参数 {@link  NET_IN_IVSEVENT_FIND_INFO}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_IVSEVENT_FIND_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return 查询句柄 非0表示成功,0表示失败
     */
    public LLong CLIENT_IVSEventFind(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 智能事件信息查询
     *param[in]   lFindHandle 查询句柄
     *param[in]   pstInParam 接口输入参数 {@link  NET_IN_IVSEVENT_NEXTFIND_INFO}
     *param[out]  pstOutParam 接口输出参数 {@link  NET_OUT_IVSEVENT_NEXTFIND_INFO}
     *param[in]   nWaitTime 接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_IVSEventNextFind(LLong lFindHandle,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 智能事件结束查询
     *param[in]   lFindHandle 查询句柄
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_IVSEventFindClose(LLong lFindHandle);

    /** 
     * 按时间回放进度回调函数原形的扩展
     * 参数recordfileinfoEx 指针对应结构体NET_RECORDFILE_INFOEX
     */
    public interface fTimeDownLoadPosCallBackEx extends Callback {
        public void invoke(LLong lPlayHandle,int dwTotalSize,int dwDownLoadSize,int index,Pointer recordfileinfoEx,Pointer dwUser);
    }

    /** 
     * VK信息回调(pBuffer内存由SDK内部申请释放),dwError值可以dhnetsdk.h中找到相应的解释,比如NET_NOERROR,NET_ERROR_VK_INFO_DECRYPT_FAILED等
     * 参数pBuffer指针对应结构体NET_VKINFO
     */
    public interface fVKInfoCallBack extends Callback {
        public void invoke(LLong lRealHandle,Pointer pBuffer,int dwError,Pointer dwUser,Pointer pReserved);
    }

    /** 
     * 分压业务连接断线回调
     *param[out]    lSubBizHandle 分压业务sdk句柄, 由CLIENT_CreateSubBusinessModule接口返回
     *param[out]    lOperateHandle 业务句柄
     *param[out]    pstDisConnectInfo 断线回调数据  对应结构体NET_SUBBIZ_DISCONNECT_CALLBACK
     *return void
     */
    public interface fSubBizDisConnectCallBack extends Callback {
        public void invoke(LLong lSubBizHandle,LLong lOperateHandle,Pointer pstDisConnectInfo);
    }

    /**
     * 订阅大图检测小图进度,配合CLIENT_FaceRecognitionDetectMultiFace使用, pstInParam与pstOutParam内存由用户申请释放
     * param[in] pstuInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_MULTIFACE_DETECT_STATE}
     * param[out]pstuOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_MULTIFACE_DETECT_STATE}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public LLong CLIENT_AttachDetectMultiFaceState(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 订阅大图检测小图进度回调函数原型,
     * pstStates指针对应结构体NET_CB_MULTIFACE_DETECT_STATE
     */
    public interface fMultiFaceDetectState extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstStates,Pointer dwUser);
    }

    /**
     * 订阅大图检测小图进度回调函数原型
     * pstStates指针对应结构体NET_CB_MULTIFACE_DETECT_STATE_EX
     */
    public interface fMultiFaceDetectStateEx extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstStates,Pointer dwUser);
    }

    /**
     * 开始目标检测/注册库的多通道查询
     * param[in] lLoginID 登录句柄
     * param[in] pstuInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_STARTMULTIFIND_FACERECONGNITION_EX}
     * param[out]pstuOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_STARTMULTIFIND_FACERECONGNITION_EX}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_StartMultiFindFaceRecognitionEx(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 获取人脸查询结果信息
     * param[in] pstuInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_DOFIND_FACERECONGNITION_EX}
     * param[out]pstuOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_DOFIND_FACERECONGNITION_EX}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DoFindFaceRecognitionEx(Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 向服务器提交多张大图，从中检测人脸图片, pstInParam与pstOutParam内存由用户申请释放
     * param[in] pstuInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_FACE_RECOGNITION_DETECT_MULTI_FACE_INFO}
     * param[out]pstuOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_FACE_RECOGNITION_DETECT_MULTI_FACE_INFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_FaceRecognitionDetectMultiFace(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 取消订阅大图检测小图进度, lAttachHandle为CLIENT_AttachDetectMultiFaceState 返回的句柄
     */
    public boolean CLIENT_DetachDetectMultiFaceState(LLong lAttachHandle);

    /**
     * 获取安检机安全等级信息,pstInParam与pstOutParam内存由用户申请释放
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_GET_XRAY_MULTILEVEL_DETECT_INFO}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_GET_XRAY_MULTILEVEL_DETECT_INFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     */
    public LLong CLIENT_GetXRayMultiLevelDetectCFG(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 设置安检机安全等级信息,pstInParam与pstOutParam内存由用户申请释放
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_SET_XRAY_MULTILEVEL_DETECT_INFO}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_SET_XRAY_MULTILEVEL_DETECT_INFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     */
    public LLong CLIENT_SetXRayMultiLevelDetectCFG(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 动环遥测数据订阅
     * param[in] lLoginID 登录句柄
     * param[in] pstInParam 接口输入参数,{@link NET_IN_ATTACH_SCADA_DATA_INFO}
     * param[out] pstOutParam 接口输出参数,{@link NET_OUT_ATTACH_SCADA_DATA_INFO}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     * return 返回订阅句柄
     */
    public LLong CLIENT_AttachSCADAData(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 动环遥测数据退订
     * param[in] lSCADADataHandle 订阅句柄
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachSCADAData(LLong lSCADADataHandle);

    /**
     * 动环遥测数据订阅回调函数原型, lSCADADataHandle 为 CLIENT_AttachSCADAData 接口的返回值
     * param[out] lSCADADataHandle 订阅句柄
     * param[out] pstuSCADADataNotifyInfo 订阅的遥测数据,{@link NET_NOTIFY_SCADA_DATA_INFO}
     * param[out] dwUser 用户信息
     * return void
     */
    public interface fNotifySCADAData extends Callback {
        public void invoke(LLong lSCADADataHandle,Pointer pstuSCADADataNotifyInfo,Pointer dwUser);
    }

    /**
     * 订阅统计通道数据,pInParam与pOutParam内存由用户申请释放
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_ATTACH_VIDEOSTAT_STREAM}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放,{@link NET_OUT_ATTACH_VIDEOSTAT_STREAM}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     */
    public LLong CLIENT_AttachVideoStatStream(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

	/**
	 * 统计通道数据回调函数,参数pBuf 指针对应结构体NET_CB_VIDEOSTAT_STREAM
	 */
    public interface fVideoStatStreamCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

    /**
     * 取消订阅统计通道数据, lAttachHandle为CLIENT_AttachVideoStatStream 返回的句柄
     */
    public boolean CLIENT_DetachVideoStatStream(LLong lAttachHandle);

    /**
     * 获取电视墙预案,pInParam与pOutParam内存由用户申请释放
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link com.coalbot.camera.sdk.sdk.dahua.structure.optimized.NET_IN_WM_GET_COLLECTIONS_V1}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link com.coalbot.camera.sdk.sdk.dahua.structure.optimized.NET_OUT_WM_GET_COLLECTIONS_V1}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
     */
    public boolean CLIENT_GetMonitorWallCollectionsV1(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

	/**
	 * 门禁设备开始捕获新卡
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_ACCESSCONTROL_CAPTURE_NEWCARD}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_ACCESSCONTROL_CAPTURE_NEWCARD}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
	 */
    public boolean CLIENT_AccessControlCaptureNewCard(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

	/**
	 * 门禁人证设备获取人脸
     * param[in] pInParam 接口输入参数, 内存资源由用户申请和释放,{@link NET_IN_ACCESSCONTROL_CAPTURE_CMD}
     * param[out]pOutParam 接口输出参数, 内存资源由用户申请和释放 {@link NET_OUT_ACCESSCONTROL_CAPTURE_CMD}
     * param[in] nWaitTime 接口超时时间, 单位毫秒
	 */
    public boolean CLIENT_AccessControlCaptureCmd(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 订阅智能分析结果
     * param[in] 	lLoginID 登录句柄
     * param[in] 	pstInParam 接口输入参数，{@link NET_IN_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC}
     * param[out]	pstOutParam 接口输出参数，{@link NET_OUT_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC}
     * param[in] 	nWaitTime 接口超时时间, 单位毫秒
     * return 返回订阅句柄
     */
    public LLong CLIENT_AttachVideoAnalyseAnalyseProc(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * 取消订阅智能分析结果
     * param[in] 	lAttachHandle 订阅句柄
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachVideoAnalyseAnalyseProc(LLong lAttachHandle);

    /**
     * 智能分析结果的回调函数
     * param[out] lAttachHandle 订阅句柄
     * param[out] pstuVideoAnalyseTrackProc 智能分析结果的信息,{@link NET_VIDEO_ANALYSE_ANALYSE_PROC}
     * param[out] dwUser 用户信息
     * return void
     */
    public interface fVideoAnalyseAnalyseProc extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuVideoAnalyseTrackProc,Pointer dwUser);
    }

    /**
     * 开始升级设备程序--扩展支持G以上文件升级
     */
    public LLong CLIENT_StartUpgradeEx2(LLong lLoginID,int emType,Pointer pchFileName,fUpgradeCallBackEx cbUpgrade,Pointer dwUser);

    /** 
    * 报警主机设置操作
    * param[in] lLoginID 登录句柄
    * param[in] emType 设置的操作类型,{@link NET_EM_SET_ALARMREGION_INFO}
    * param[in] pstuInParam 枚举对应的入参
    * param[out] pstuOutParam 枚举对应的出参
    * param[in] nWaitTime 接口超时时间, 单位毫秒
    * return void
    */
    public boolean CLIENT_SetAlarmRegionInfo(LLong lLoginID,int emType,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 获取设备序列号
    * param[in] lLoginID 登录句柄
    * param[in] pstInParam 接口输入参数,{@link NET_IN_GET_DEVICESERIALNO_INFO}
    * param[out] pstOutParam 接口输出参数,{@link NET_OUT_GET_DEVICESERIALNO_INFO}
    * param[in] nWaitTime 接口超时时间, 单位毫秒
    * return void
    */
    public boolean CLIENT_GetDeviceSerialNo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
    * 获取设备类型
    * param[in] lLoginID 登录句柄
    * param[in] pstInParam 接口输入参数,{@link NET_IN_GET_DEVICETYPE_INFO}
    * param[out] pstOutParam 接口输出参数,{@link NET_OUT_GET_DEVICETYPE_INFO}
    * param[in] nWaitTime 接口超时时间, 单位毫秒
    * return void
    */
    public boolean CLIENT_GetDeviceType(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /** 
     * RPC测试
     *param[in]	lLoginID:		登录句柄
     *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_TRANSMIT_CMD}
     *param[out] pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_TRANSMIT_CMD}
     *param[in]	nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_TransmitCmd(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 手动测试
     *param[in]	lLoginID:		登录句柄
     *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_MANUAL_TEST}
     *param[out]pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_MANUAL_TEST}
     *param[in]	nWaitTime:		接口超时时间, 单位毫秒
     *return TRUE表示成功 FALSE表示失败
    */
    public boolean CLIENT_ManualTest(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 添加报警用户
    *param[in]	lLoginID:		登录句柄
    *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_ADD_ALARM_USER}
    *param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_ADD_ALARM_USER}
    *param[in]	nWaitTime:		接口超时时间, 单位毫秒
    *return TRUE表示成功 FALSE表示失败
    */
    public boolean CLIENT_AddAlarmUser(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 修改报警用户
    *param[in]	lLoginID:		登录句柄
    *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_MODIFY_ALARM_USER}
    *param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_MODIFY_ALARM_USER}
    *param[in]	nWaitTime:		接口超时时间, 单位毫秒
    *return TRUE表示成功 FALSE表示失败
    */
    public boolean CLIENT_ModifyAlarmUser(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 修改报警用户密码
    *param[in]	lLoginID:		登录句柄
    *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_MODIFY_ALARM_USER_PASSWORD}
    *param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_MODIFY_ALARM_USER_PASSWORD}
    *param[in]	nWaitTime:		接口超时时间, 单位毫秒
    *return TRUE表示成功 FALSE表示失败
    */
    public boolean CLIENT_ModifyAlarmUserPassword(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 删除报警用户
    *param[in]	lLoginID:		登录句柄
    *param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_DELETE_ALARM_USER}
    *param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_DELETE_ALARM_USER}
    *param[in]	nWaitTime:		接口超时时间, 单位毫秒
    *return TRUE表示成功 FALSE表示失败
    */
    public boolean CLIENT_DeleteAlarmUser(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 订阅无线对码信息接口,pstInParam与pstOutParam内存由用户申请释放
    *param[in]	lLoginID:		登录句柄
    *param[in]	pstInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_ATTACH_LOWRATEWPAN}
    *param[out]	pstOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_ATTACH_LOWRATEWPAN}
    *param[in]	nWaitTime:		接口超时时间, 单位毫秒
    *return 返回订阅句柄
    */
    public LLong CLIENT_AttachLowRateWPAN(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
    * 订阅无线对码信息回调函数原形,lAttachHandle是CLIENT_AttachLowRateWPAN返回值
    *param[out]	lLoginID        登录句柄
    *param[out]	lAttachHandle	订阅句柄
    *param[out]	lAttachHandle	对码信息, 参考{@link NET_CODEID_INFO}
    *param[out]	emError	                对码错误类型, 参考{@link NET_CODEID_ERROR_TYPE}
    *param[out]	dwUser	                用户数据
    */
    public interface fAttachLowRateWPANCB extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer stuBuf,int emError,Pointer dwUser);
    }

    /**
     * 取消订阅无线对码信息接口,lAttachHandle是CLIENT_AttachLowRateWPAN返回值
     * param[in] 	lAttachHandle 订阅句柄
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachLowRateWPAN(LLong lAttachHandle);

    /**
     * 获取画面中心位置目标的距离,pInBuf与pOutBuf内存由用户申请释放
     * param[out] pInBuf 接口输入参数，{@link NET_IN_GET_LASER_DISTANCE}
     * param[out] pOutBuf 接口输出参数，{@link NET_OUT_GET_LASER_DISTANCE}
     * param[out] nWaitTime 接口超时时间, 单位毫秒
     * return void
     */
    public boolean CLIENT_GetLaserDistance(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /** 
     * 获取已添加的设备状态
     * param[in]		lLoginID:			登录句柄
     * param[in]		pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_GET_DEVICE_INFO_EX}
     * param[out]	    pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_GET_DEVICE_INFO_EX}
     * param[in]		nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetDeviceInfoEx(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 获取目标检测令牌
     * param[in]		lLoginID:	登录句柄
     * param[in]		pInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_FACERSERVER_GETDETEVTTOKEN}
     * param[out]	      pOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_FACERSERVER_GETDETEVTTOKEN}
     * param[in]		nWaitTime:	接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_FaceRServerGetDetectToken(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 查询设备日志条数(pInParam, pOutParam内存由用户申请释放)
     *      * param[in]		lLoginID:	登录句柄
     *      * param[in]		pInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link NET_IN_GETCOUNT_LOG_PARAM}
     *      * param[out]	      pOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link NET_OUT_GETCOUNT_LOG_PARAM}
     *      * param[in]		nWaitTime:	接口超时时间, 单位毫秒
     */
    public boolean CLIENT_QueryDevLogCount(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int waittime);

    /** 
     * 设置状态信息接口
     * param[in]	lLoginID:		登录句柄
     * param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_SET_STATEMANAGER_INFO}
     * param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_SET_STATEMANAGER_INFO}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetStateManager(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 获取状态信息接口
     * param[in]	lLoginID:		登录句柄
     * param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_GET_STATEMANAGER_INFO}
     * param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_GET_STATEMANAGER_INFO}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetStateManager(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * 定位录像回放起始点
     */
    public boolean CLIENT_SeekPlayBack(LLong lPlayHandle,int offsettime,int offsetbyte);

    /**
     * 按时间定位回放起始点
     */
    public boolean CLIENT_SeekPlayBackByTime(LLong lPlayHandle,NET_TIME lpSeekTime);

    /**
     * 多通道预览回放(pParam内存由用户申请释放)
     */
    public LLong CLIENT_MultiPlayBack(LLong lLoginID,Pointer pParam);

    /**
     * 操作设备标定信息
     * param[in]	lLoginID:		登录句柄
     * param[in]	emType:	        入参枚举，决定后续指针参数类型,参考{@link EM_CALIBRATEINFO_OPERATE_TYPE}
     * param[in]	pstuInParam:	接口输入参数, 内存资源由用户申请和释放,参考{@link EM_CALIBRATEINFO_OPERATE_TYPE}
     * param[out]	pstuOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{@link EM_CALIBRATEINFO_OPERATE_TYPE}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_OperateCalibrateInfo(LLong lLoginID,int emType,Pointer pStuInParam,Pointer pStuOutParam,int nWaitTime);

    /**
     * 订阅云台可视域回调函数原型, pBuf -> {@link DH_OUT_PTZ_VIEW_RANGE_STATUS}
     */
    public interface fViewRangeStateCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

    /**
     * 订阅云台可视域,pstuInViewRange与pstuOutViewRange内存由用户申请释放
     * {@link NET_IN_VIEW_RANGE_STATE},{@link NET_OUT_VIEW_RANGE_STATE}
     */
    public LLong CLIENT_AttachViewRangeState(LLong lLoginID,Pointer pstuInViewRange,Pointer pstuOutViewRange,int nWaitTime);

    /**
     * 停止订阅云台可视域,lAttachHandle是CLIENT_AttachViewRangeState返回值
     */
    public boolean CLIENT_DetachViewRangeState(LLong lAttachHandle);

    /** 
     * 开始查询日志(目前只支持门禁BSC系列,支持报警主机日志分类查询),pInParam与pOutParam内存由用户申请释放
     * param[in]	lLoginID:		登录句柄
     * param[in]	pInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_START_QUERYLOG}
     * param[out]	pOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_START_QUERYLOG}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return  返回查询句柄
     */
    public LLong CLIENT_StartQueryLog(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 获取日志(目前只支持门禁BSC系列),pInParam与pOutParam内存由用户申请释放
     * param[in]	lLogID:		查询句柄，CLIENT_StartQueryLog的返回值
     * param[in]	pInParam:	接口输入参数, 内存资源由用户申请和释放,参考{ @link NET_IN_QUERYNEXTLOG}
     * param[out]	pOutParam:	接口输出参数, 内存资源由用户申请和释放,参考{ @link NET_OUT_QUERYNEXTLOG}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_QueryNextLog(LLong lLogID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 结束查询日志(目前只支持门禁BSC系列)
     * param[in]	lLogID:		查询句柄，CLIENT_StartQueryLog的返回值
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_StopQueryLog(LLong lLogID);

    /** 
     * 获取窗口位置(pInparam, pOutParam内存由用户申请释放)
     * param[in]	lLoginID:		登录句柄
     * param[in]	pInParam:	        接口输入参数, 内存资源由用户申请和释放,参考{ @link DH_IN_SPLIT_GET_RECT}
     * param[out]	pOutParam:	        接口输出参数, 内存资源由用户申请和释放,参考{ @link DH_OUT_SPLIT_GET_RECT}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetSplitWindowRect(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 设置窗口位置(pInparam, pOutParam内存由用户申请释放)
     * param[in]	lLoginID:		登录句柄
     * param[in]	pInParam:	        接口输入参数, 内存资源由用户申请和释放,参考{ @link DH_IN_SPLIT_SET_RECT}
     * param[out]	pOutParam:	        接口输出参数, 内存资源由用户申请和释放,参考{ @link DH_OUT_SPLIT_SET_RECT}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetSplitWindowRect(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /** 
     * 设置窗口次序(pInparam, pOutParam内存由用户申请释放)
     * param[in]	lLoginID:		登录句柄
     * param[in]	pInParam:	        接口输入参数, 内存资源由用户申请和释放,参考{@link DH_IN_SPLIT_SET_TOP_WINDOW}
     * param[out]	pOutParam:	        接口输出参数, 内存资源由用户申请和释放,参考{@link DH_OUT_SPLIT_SET_TOP_WINDOW}
     * param[in]	nWaitTime:		接口超时时间, 单位毫秒
     * return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetSplitTopWindow(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * 获取网卡信息(lpInParam, lpOutParam内存由用户申请释放,大小参照emType对应的结构体)
     */
    public boolean CLIENT_QueryNetStat(LLong lLoginID,int emType,Pointer lpInParam,int nInParamLen,Pointer lpOutParam,int nOutParamLen,Pointer pError,int nWaitTime);

    /**
     * 订阅统计区域内的车辆数据或者排队长度信息,pstInParam与pstOutParam内存由用户申请释放
     */
    public LLong CLIENT_AttachVehiclesDistributionData(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam);

    /**
     * 取消订阅统计区域内的车辆数据或者排队长度信息,pstInParam与pstOutParam内存由用户申请释放
     */
    public boolean CLIENT_DetachVehiclesDistributionData(LLong lAttachHandle);

    /**
     * 接口 CLIENT_AttachVehiclesDistributionData 回调函数,pBuf是json和图片数据,nBufLen是pBuf相应长度,用于转发服务
     */
    public interface fNotifyVehiclesDistributionData extends Callback {
        public int invoke(LLong lVehiclesHandle,Pointer pDiagnosisInfo,Pointer dwUser);
    }

    /**
     * @param		pInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RADIOMETRY_CURRENTHOTCOLDSPOT_INFO}
     * @param		pOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RADIOMETRY_CURRENTHOTCOLDSPOT_INFO}
     * @description  获取热成像当前冷（最低的温度）、热（最高的温度）点信息
     */
    public boolean CLIENT_RadiometryGetCurrentHotColdSpotInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @param		pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_ZONE_ARMODE_INFO}
     * @param		pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_ZONE_ARMODE_INFO}
     * @description  设置单防区布撤防状态
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetZoneArmMode(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param		pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_ZONE_ARMODE_INFO}
     * @param		pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_ZONE_ARMODE_INFO}
     * @description  获取单防区布撤防状态
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetZoneArmMode(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @description 智能分析结果的回调函数
     * @description param[out] lAttachHandle 订阅句柄
     * @description param[out] pstuVideoAnalyseTrackProc 智能分析结果的信息
     * @description param[out] dwUser 用户信息
     * @description return void
     */
    public interface fVehicleInOutAnalyseProc extends Callback {
/**
         * @param lAttachHandle
         * @param pstuVehicleInOutAnalyseProc: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_VEHICLE_INOUT_ANALYSE_PROC}
         * @param dwUser
         */
        public void invoke(LLong lAttachHandle,Pointer pstuVehicleInOutAnalyseProc,Pointer dwUser);
    }

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_TRAFFIC_FLOW_STAT_REAL_FLOW}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_TRAFFIC_FLOW_STAT_REAL_FLOW}
     * @description 订阅交通流量统计
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstInParam 接口输入参数
     * @description param[out] pstOutParam 接口输出参数
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return 返回订阅句柄
     */
    public LLong CLIENT_AttachTrafficFlowStatRealFlow(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @description 取消订阅交通流量统计
     * @description param[in] lAttachHandle 订阅句柄
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachTrafficFlowStatRealFlow(LLong lAttachHandle);

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_BIND_MODE_INFO}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_BIND_MODE_INFO}
     * @description 设置绑定模式
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetBindMode(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_BIND_MODE_INFO}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_BIND_MODE_INFO}
     * @description 获取绑定模式
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetBindMode(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param		pstInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_VIDEO_ANALYSE_TRACK_PROC}
     * @param		pstOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_VIDEO_ANALYSE_TRACK_PROC}
     * @description  订阅外部轨迹
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstInParam 接口输入参数
     * @description param[out]pstOutParam 接口输出参数
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return 订阅句柄
     */
    public LLong CLIENT_AttachVideoAnalyseTrackProc(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @param		pstuVideoAnalyseTrackProc:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_VIDEO_ANALYSE_TRACK_PROC}
     * @description  外部轨迹的回调函数
     */
    public interface fVideoAnalyseTrackProc extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuVideoAnalyseTrackProc,Pointer dwUser);
    }

    /**
     * @description  取消订阅外部轨迹
     * @description param[in] lAttachHandle 订阅句柄
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachVideoAnalyseTrackProc(LLong lAttachHandle);

    /**
     * @param        pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_GPS_STATUS_INFO}
     * @param        pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_GPS_STATUS_INFO}
     * @description 获取GPS定位信息
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetGpsStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_PTZ_SET_ZOOM_VALUE}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_PTZ_SET_ZOOM_VALUE}
     * @description 设置云台变倍
     * @description param[in] lLoginID: 登录句柄
     * @description param[in] pstuInParam: 接口输入参数, 内存资源由用户申请和释放
     * @description param[out] pstuOutParam: 接口输出参数, 内存资源由用户申请和释放
     * @description param[in] nWaitTime: 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_PTZSetZoomValue(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_VTO_MANAGER_RELATION}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_VTO_MANAGER_RELATION}
     * @description 设置组织节点表
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_SetVTOManagerRelation(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param pstuInParam:  参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_VTO_MANAGER_RELATION}
     * @param pstuOutParam: 参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_VTO_MANAGER_RELATION}
     * @description 获取组织树节点
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetVTOManagerRelation(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param        pstInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ADD_SOFT_TOUR_POINT_INFO}
     * @param        pstOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ADD_SOFT_TOUR_POINT_INFO}
     * @description 增加软巡航预置点
     * @description param[in]   lLoginID 登录句柄
     * @description param[in]   pstInParam 接口输入参数
     * @description param[out]  pstOutParam 接口输出参数
     * @description param[in]   nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_AddSoftTourPoint(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @param        pstInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOVE_SOFT_TOUR_POINT_INFO}
     * @param        pstOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOVE_SOFT_TOUR_POINT_INFO}
     * @description 清除软巡航预置点
     * @description param[in]   lLoginID 登录句柄
     * @description param[in]   pstInParam 接口输入参数
     * @description param[out]  pstOutParam 接口输出参数
     * @description param[in]   nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_RemoveTourPoint(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @param		pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_INSERT_MULTI_TALK_DEV}
     * @param		pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_INSERT_MULTI_TALK_DEV}
     * @description  下发设备信息
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_InsertMultiTalkDev(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param		pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_UPDATE_MULTI_TALK_DEV}
     * @param		pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_UPDATE_MULTI_TALK_DEV}
     * @description  批量更新设备信息接口
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstuInParam 接口入参
     * @description param[out] pstuOutParam 接口出参
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_UpdateMultiTalkDev(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @param		pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DO_FIND_TALK_DEV}
     * @param		pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DO_FIND_TALK_DEV}
     * @description  执行信息查询, lFindID为CLIENT_StartFindTalkDev接口返回的查找ID
     * @description param[in] lFindID 查询句柄
     * @description param[in] pstuInParam 接口输入参数
     * @description param[out]pstuOutParam 接口输出参数
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DoFindTalkDev(LLong lFindID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /** 
     * 录像下载--扩展Ex3接口，将所有参数整合在一个结构体，方便后续扩展
     * @param   lLoginID 登录句柄
     * @param	pstuInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DOWNLOAD}
     * @param	pstuOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DOWNLOAD}
     * @param   dwWaitTime 接口超时时间, 单位毫秒
     * @return 下载句柄
     */
    public LLong CLIENT_DownloadByRecordFileEx3(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

 /**
     * @param		pstInParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_HYGROTHERMOGRAPH}
     * @param		pstOutParam:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_HYGROTHERMOGRAPH}
     * @description  订阅温湿度实时检测数据
     * @description param[in] lLoginID 登录句柄
     * @description param[in] pstInParam 接口输入参数
     * @description param[out]pstOutParam 接口输出参数
     * @description param[in] nWaitTime 接口超时时间, 单位毫秒
     * @description return 返回订阅句柄
     */
    public LLong CLIENT_AttachHygrothermograph(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @param		pstuHygrothermographInfo:	参考{@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_HYGROTHERMOGRAPH_INFO}
     * @description  订阅温湿度实时检测数据回调函数原型, lHygrothermographHandle为CLIENT_AttachHygrothermograph接口的返回值
     * @description param[out] lHygrothermographHandle 订阅句柄
     * @description param[out] pstuHygrothermographInfo 订阅温湿度实时检测数据回调信息
     * @description param[out] dwUser 用户信息
     * @description return void
     */
    public interface fNotifyHygrothermograph extends Callback {
        public void invoke(LLong lHygrothermographHandle,Pointer pstuHygrothermographInfo,Pointer dwUser);
    }

    /**
     * @description  退订温湿度实时检测数据
     * @description param[in] lHygrothermographHandle 订阅句柄
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_DetachHygrothermograph(LLong lHygrothermographHandle);

    /**
     * @description  查询录像下载进度
     * @description param[in] lFileHandle  下载句柄
     * @description param[out] nTotalSize  总文件大小
     * @description param[out] nDownLoadSize  已下载文件大小
     * @description return TRUE表示成功 FALSE表示失败
     */
    public boolean CLIENT_GetDownloadPos(LLong lFileHandle,Pointer nTotalSize,Pointer nDownLoadSize);

    /**
     * @brief 二次录像分析实时结果订阅函数原型
     * @param pstAnalyseResultInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_ANALYSE_RESULT_INFO)
     */
    public interface fAnalyseResultCallBack extends Callback {
        public int invoke(LLong lAnalyseHandle,Pointer pstAnalyseResultInfo,Pointer pBuffer,int dwBufSize);
    }

    /**
     * @brief 测温温度数据状态回调函数
     * @param pBuf ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RADIOMETRY_TEMPER_DATA)
     */
    public interface fRadiometryAttachTemperCB extends Callback {
        public void invoke(LLong lAttachTemperHandle,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

    /**
     * @brief 订阅气象信息回调函数原型
     * @param pBuf ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_WEATHER_INFO)
     */
    public interface fWeatherInfoCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lAttachHandle,Pointer pBuf,int nBufLen,Pointer dwUser);
    }

    /**
     * @brief 订阅电梯内实时数据接口回调函数原型, lAttachHandle 为 CLIENT_AttachElevatorFloorCounter 接口的返回值
     * @param pstuElevatorFloorCounterInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_ELEVATOR_FLOOR_COUNTER_INFO)
     */
    public interface fNotifyElevatorFloorCounterdata extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuElevatorFloorCounterInfo,Pointer dwUser);
    }

    /**
     * @brief 事件详细信息回调
     * @param pstuNotifyIVSEventDetailInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_IVSEVENT_DETAIL_INFO)
     */
    public interface fNotifyIVSEventDetail extends Callback {
        public void invoke(Pointer pstuNotifyIVSEventDetailInfo,Pointer dwUser);
    }

    /**
     * @brief 收到低功耗设备保活包回调函数原型
     * @param pstLowPowerKeepAliveCallBackInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_LOW_POWER_KEEPALIVE_CALLBACK_INFO)
     */
    public interface fLowPowerKeepAliveCallBack extends Callback {
        public void invoke(LLong lLowPowerHandle,Pointer pstLowPowerKeepAliveCallBackInfo);
    }

    /**
     * @brief 订阅历史库以图搜图回调函数原型, lAttachHandle为CLIENT_AttachResultOfFindHistoryByPic接口的返回值
     * @param pstesult ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_RESULT_OF_FIND_HISTORY_BYPIC)
     */
    public interface fResultOfFindHistory extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstesult,Pointer pBinBuf,int nBinBufLen,Pointer dwUser);
    }

    /**
     * @brief 订阅485实时数据接口回调函数原型, lAttachHandle 为 CLIENT_AttachIotboxComm 接口的返回值
     * @param pstuIotboxComm ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_IOTBOX_COMM)
     */
    public interface fNotifyIotboxRealdata extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuIotboxComm,Pointer dwUser);
    }

    /**
     * @brief 订阅485实时数据接口回调函数原型, lAttachHandle 为 CLIENT_AttachIotboxComm 接口的返回值
     * @param pstuIotboxCommEx ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_IOTBOX_COMM_EX)
     */
    public interface fNotifyIotboxRealdataEx extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuIotboxCommEx,Pointer dwUser);
    }

    /**
     * @brief 区域流量数据的回调函数
     * @param pstuNotifyAreaFlowInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_AREA_FLOW_INFO)
     */
    public interface fNotifyAreaFlowInfo extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuNotifyAreaFlowInfo,Pointer dwUser);
    }

    /**
     * @brief 接口 CLIENT_AttachTalkState 的回调函数
     * @param pstuState ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_TALK_STATE)
     */
    public interface fNotifyTalkState extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuState,int nLen,Pointer dwUser);
    }

    /**
     * @brief 屏幕叠加回调函数原形
     */
    public interface fDrawCallBack extends Callback {
        public void invoke(LLong lLoginID,LLong lPlayHandle,Pointer hDC,Pointer dwUser);
    }

    /**
     * @brief CLIENT_AttachStartStreamData的回调函数
     * @param pstuStartStreamData ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_START_STREAM_DATA_INFO)
     */
    public interface fStartStreamDataCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuStartStreamData,Pointer dwUser,Pointer pBuffer,int dwBufSize);
    }

    /**
     * @brief 调试日志回调函数
     */
    public interface fAttachSniffer extends Callback {
        public void invoke(LLong lAttchHandle,Pointer pBuffer,int dwBufSize,Pointer dwUser);
    }

    /**
     * @brief 调试日志回调函数
     */
    public interface fDebugInfoCallBack extends Callback {
        public void invoke(LLong lAttchHandle,Pointer pBuffer,int dwBufSize,Pointer dwUser);
    }

    /**
     * @brief 智能分析结果的回调函数
     * @param pstuVideoAnalyseTrackProcEx ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_VIDEO_ANALYSE_ANALYSE_PROC_EX)
     */
    public interface fVideoAnalyseAnalyseProcEx extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuVideoAnalyseTrackProcEx,Pointer dwUser);
    }

    /**
     * @brief TTLV异步搜索设备回调(pDevNetInfo内存由SDK内部申请释放)
     * @param pDevNetInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.DEVICE_NET_INFO_TTLV)
     */
    public interface fSearchDevicesCBTTLV extends Callback {
        public void invoke(LLong lSearchHandle,Pointer pDevNetInfo,Pointer pUserData);
    }

    /**
     * @brief 向客户端发送录像文件回调函数, lAttachHandle 为 CLIENT_AttachRecordManagerState 返回的结果
     * @param pstuState ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RECORDMANAGER_NOTIFY_INFO)
     */
    public interface fRecordManagerStateCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuState,int dwStateSize,Pointer dwUser);
    }

    /**
     * @brief CLIENT_AttachNormalUsingJson的回调函数
     * @param pstuAttachNormalInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_ATTACH_NORMAL_INFO)
     */
    public interface fAttachNormalCallBack extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuAttachNormalInfo,Pointer pBuffer,int dwBufSize,Pointer dwUser);
    }

    /**
     * @brief CLIENT_PostLoginTask 登录结果回调
     * @param pOutParam ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_POST_LOGIN_TASK)
     */
    public interface fPostLoginTask extends Callback {
        public void invoke(int dwTaskID,Pointer pOutParam,Pointer dwUser);
    }

    /**
     * @brief 帧信息回调
     * @param pInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_FRAME_INFO_CALLBACK_INFO)
     */
    public interface fFrameInfoCallBackEx extends Callback {
        public void invoke(LLong lHandle,Pointer pInfo,Pointer dwUser);
    }

    /**
     * @brief CLIENT_AttachDockEvents 接口回调函数
     * @param pUASDockEvents ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_UAS_DOCK_EVENTS)
     */
    public interface fUAVDockEvents extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pUASDockEvents,Pointer dwUser);
    }

    /**
     * @brief CLIENT_AttachDockInfo 接口输入参数
     * @param pUASDockInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_UAS_DOCK_INFO)
     */
    public interface fUAVDockInfo extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pUASDockInfo,Pointer dwUser);
    }

    /**
     * @brief CLIENT_AttachDockStatus 接口回调函数
     * @param pUASDockInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_UAS_DOCK_STATUS)
     */
    public interface fUAVDockStatus extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pUASDockInfo,Pointer dwUser);
    }

    /**
     * @brief 开包检查结果回调函数
     * @param pInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_XRARY_UNPACKING_INFO)
     */
    public interface fXRayUnpackingResult extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pInfo,Pointer dwUser);
    }

    /**
     * @brief 通知导出包裹, lAttachHandle 为 CLIENT_AttachXRayPackageManualExport 接口的返回值
     * @param pstuNotifyInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_PACKAGE_MANUAL_EXPORT)
     */
    public interface fNotifyPackageManualExport extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuNotifyInfo,Pointer dwUser);
    }

    /**
     * @brief 工作状态信息, lAttachHandle 为 CLIENT_AttachModuleStatus 接口的返回值
     * @param pstuNotifyInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_MODULE_STATUS)
     */
    public interface fNotifyModuleStatus extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuNotifyInfo,Pointer dwUser);
    }

    /**
     * @brief 订阅人体历史库以图搜图回调函数原型, lAttachHandle为CLIENT_AttachResultOfHumanHistoryByPic接口的返回值
     * @param pstesult ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_RESULT_OF_HUMAN_HISTORY_BYPIC)
     */
    public interface fResultOfHumanHistory extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstesult,Pointer pBinBuf,int nBinBufLen,Pointer dwUser);
    }

    /**
     * @brief 订阅车辆历史库以图搜图回调函数原型, lAttachHandle为CLIENT_AttachResultOfVehicleHistoryByPic接口的返回值
     * @param pstuResult ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_RESULT_OF_VEHICLE_HISTORY_BYPIC)
     */
    public interface fResultOfVehicleHistory extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuResult,Pointer pBinBuf,int nBinBufLen,Pointer dwUser);
    }

    /**
     * @brief 订阅内容检索查询状态
     * @param pstuNotifyLargeModeFindResultInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_NOTIFY_LARGE_MODE_SERVER_FIND_RESULT_INFO)
     */
    public interface fNotifyLargeModeFindResult extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuNotifyLargeModeFindResultInfo,Pointer dwUser);
    }

    /**
     * @brief 订阅人体历史库以图搜图回调函数原型, lAttachHandle为CLIENT_AttachResultOfHumanHistoryByPic接口的返回值
     * @param pstuResult ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_CB_RESULT_OF_HUMAN_HISTORY_BYPIC_EX)
     */
    public interface fResultOfHumanHistoryEx extends Callback {
        public void invoke(LLong lAttachHandle,Pointer pstuResult,Pointer pBinBuf,int nBinBufLen,Pointer dwUser);
    }

    /**
     * @brief 开始异步文件上传至前端的调函数原形,lRemoteUploadFileID 为 CLIENT_StartRemoteUploadFile 接口返回值
     * @param emStatus， 参考枚举定义  {@link NETAEM_CARD_STATE}
     */
    public interface fRemoteUploadFileCallBack extends Callback {
        public void invoke(LLong lRemoteUploadFileID,int nTotalSize,int nSendSize,int emStatus,Pointer dwUser);
    }

    /**
     * @brief 雷达AIS信息回调函数指针
     * @param pBuf ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_RADAR_NOTIFY_AIS_INFO)
     */
    public interface fRadarAISInfoCallBack extends Callback {
        public void invoke(LLong lLoginId,LLong lAttachHandle,Pointer pBuf,int dwBufLen,Pointer pReserved,Pointer dwUser);
    }

    /**
     * @brief 
     * @param pDevNetInfo ， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.DEVICE_NET_INFO_TTLV)
     */
    public interface fSearchDevicesCB4th extends Callback {
        public void invoke(LLong lSearchHandle,Pointer pDevNetInfo,Pointer pUserData);
    }

    /**
     * @brief 获取当前所有规则温度信息
     * @param pInParam 接口输入参数， 参考结构体定义 NET_IN_RADIOMETRY_GET_CUR_TEMPER_ALL_INFO
     * @param pOutParam 接口输出参数， 参考结构体定义 NET_OUT_RADIOMETRY_GET_CUR_TEMPER_ALL_INFO
     */
    public boolean CLIENT_RadiometryGetCurTemperAll(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取测温项温度的参数值
     * @param pInParam 接口输入参数， 参考结构体定义 NET_IN_RADIOMETRY_GET_TEMPER_INFO
     * @param pOutParam 接口输出参数， 参考结构体定义 NET_OUT_RADIOMETRY_GET_TEMPER_INFO
     */
    public boolean CLIENT_RadiometryGetTemper(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取EAS设备能力集
     * @param pstuInParam 接口输入参数， 参考结构体定义 NET_IN_GET_EAS_DEVICE_CAPS_INFO
     * @param pstuOutParam 接口输出参数， 参考结构体定义 NET_OUT_GET_EAS_DEVICE_CAPS_INFO
     */
    public boolean CLIENT_GetEASDeviceCaps(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设备导出文件接口
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECURITY_EXPORT_DATA_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECURITY_EXPORT_DATA_INFO}
     */
    public boolean CLIENT_SecurityExportDataEx(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 设备导入文件接口
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECURITY_IMPORT_DATA_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECURITY_IMPORT_DATA_INFO}
     */
    public boolean CLIENT_SecurityImportDataEx(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 准备生成导出文件接口
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECURITY_PREPARE_EXPORT_DATA_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECURITY_PREPARE_EXPORT_DATA_INFO}
     */
    public boolean CLIENT_SecurityPrepareExportData(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 查询特定数据类型的任务状态接口
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECURITY_GETCAPS_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECURITY_GETCAPS_INFO}
     */
    public boolean CLIENT_SecurityGetCaps(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 查询当前支持的导入导出数据类型
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECURITY_GET_TASK_STATUS_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECURITY_GET_TASK_STATUS_INFO}
     */
    public boolean CLIENT_SecurityGetTaskStatus(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 添加添加本地分析任务 pstInParam与pstOutParam内存由用户申请释放
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ADD_LOCAL_ANALYSE_TASK}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ADD_LOCAL_ANALYSE_TASK}
     */
    public boolean CLIENT_RecordSecondaryAnalyseAddLocalAnalyseTask(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取设备点位信息
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SCADA_GET_ATTRIBUTE_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SCADA_GET_ATTRIBUTE_INFO}
     */
    public boolean CLIENT_SCADAGetAttributeInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 订阅录像二次分析实时结果 pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_SECONDARY_ANALYSE_RESULT}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_SECONDARY_ANALYSE_RESULT}
     */
    public LLong CLIENT_AttachRecordSecondaryAnalyseResult(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 开始录像二次分析任务 pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECONDARY_ANALYSE_STARTTASK}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECONDARY_ANALYSE_STARTTASK}
     */
    public boolean CLIENT_RecordSecondaryAnalyseStartTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 设置日志加密密码
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_LOG_ENCRYPT_KEY_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_LOG_ENCRYPT_KEY_INFO}
     */
    public boolean CLIENT_SetLogEncryptKey(LLong lLogID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 订阅测温温度数据,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RADIOMETRY_ATTACH_TEMPER}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RADIOMETRY_ATTACH_TEMPER}
     */
    public LLong CLIENT_RadiometryAttachTemper(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 取消订阅录像二次分析实时结果
     */
    public boolean CLIENT_DetachRecordSecondaryAnalyseResult(LLong lAttachHandle);

    /**
     * @brief 删除录像二次分析任务 pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECONDARY_ANALYSE_REMOVETASK}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECONDARY_ANALYSE_REMOVETASK}
     */
    public boolean CLIENT_RecordSecondaryAnalyseRemoveTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 暂停录像二次分析任务 pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SECONDARY_ANALYSE_PAUSETASK}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SECONDARY_ANALYSE_PAUSETASK}
     */
    public boolean CLIENT_RecordSecondaryAnalysePauseTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取闸机状态
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ACCESS_GET_ASG_STATE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ACCESS_GET_ASG_STATE}
     */
    public boolean CLIENT_GetASGState(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 清除闸机的统计信息方法，pstInParam与pstOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ASGMANAGER_CLEAR_STATISTICS}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ASGMANAGER_CLEAR_STATISTICS}
     */
    public boolean CLIENT_ASGManagerClearStatistics(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 开始放音接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_REMOTE_SPEAK_PLAY}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_REMOTE_SPEAK_PLAY}
     */
    public boolean CLIENT_StartRemoteSpeakPlay(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 停止放音接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_STOP_REMOTE_SPEAK_PLAY}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_STOP_REMOTE_SPEAK_PLAY}
     */
    public boolean CLIENT_StopRemoteSpeakPlay(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 单条测温规则设置更新
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_RADIOMETRY_RULE_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_RADIOMETRY_RULE_INFO}
     */
    public boolean CLIENT_SetRadiometryRule(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 订阅气象信息,pstuInParam与pstuOutParam内存由用户申请释放
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_WEATHER_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_WEATHER_INFO}
     */
    public LLong CLIENT_AttachWeatherInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 停止订阅气象信息,lAttachHandle是CLIENT_AttachWeatherInfo返回值
     */
    public boolean CLIENT_DetachWeatherInfo(LLong lAttachHandle);

    /**
     * @brief 获取气象信息,pInBuf与pOutBuf内存由用户申请释放
     * @param pInBuf 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_ATOMSPHDATA}
     * @param pOutBuf 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_ATOMSPHDATA}
     */
    public boolean CLIENT_GetAtomsphData(LLong lLoginID,Pointer pInBuf,Pointer pOutBuf,int nWaitTime);

    /**
     * @brief 获取测温区域的参数值, pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RADIOMETRY_RANDOM_REGION_TEMPER}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RADIOMETRY_RANDOM_REGION_TEMPER}
     */
    public boolean CLIENT_RadiometryGetRandomRegionTemper(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 分组人员统计--获取摘要信息
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_NUMBERSTATGROUPSUMMARY_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_NUMBERSTATGROUPSUMMARY_INFO}
     */
    public boolean CLIENT_GetNumberStatGroupSummary(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int waittime);

    /**
     * @brief 订阅电梯内实时数据
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_ELEVATOR_FLOOR_COUNTER_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_ELEVATOR_FLOOR_COUNTER_INFO}
     */
    public LLong CLIENT_AttachElevatorFloorCounter(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取临时token
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_TEMPORARY_TOKEN}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_TEMPORARY_TOKEN}
     */
    public boolean CLIENT_GetTemporaryToken(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅电梯内实时数据
     */
    public boolean CLIENT_DetachElevatorFloorCounter(LLong lAttachHandle);

    /**
     * @brief 获取事件详细信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_IVSEVENT_DETAIL_INFO}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_IVSEVENT_DETAIL_INFO}
     */
    public boolean CLIENT_GetIVSEventDetail(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 得到远程指定目录下文件信息（含文件/子目录）pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOTE_LIST}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOTE_LIST}
     */
    public boolean CLIENT_RemoteList(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 唤醒设备
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_WAKE_UP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_WAKE_UP_INFO}
     */
    public boolean CLIENT_DoWakeUpLowPowerDevcie(LLong lChannelHandle,Pointer pInParam,Pointer pOutParam);

    /**
     * @brief 创建低功耗通道
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_CREATE_LOW_POWER_CHANNEL}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_CREATE_LOW_POWER_CHANNEL}
     */
    public LLong CLIENT_CreateLowPowerChannel(LLong lSubBizHandle,Pointer pInParam,Pointer pOutParam);

    /**
     * @brief 销毁低功耗通道
     */
    public boolean CLIENT_DestoryLowPowerChannel(LLong lChannelHandle);

    /**
     * @brief 拒绝休眠
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REFUSE_SLEEP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REFUSE_SLEEP_INFO}
     */
    public boolean CLIENT_RefuseLowPowerDevSleep(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取X射线源能力
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_XRAY_SOURCE_CAPS_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_XRAY_SOURCE_CAPS_INFO}
     */
    public boolean CLIENT_GetXRaySourceCaps(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取X射线源累积出束时间
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_XRAY_SOURCE_CUMULATE_TIME_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_XRAY_SOURCE_CUMULATE_TIME_INFO}
     */
    public boolean CLIENT_GetXRaySourceCumulateTime(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 查找记录:nFilecount:需要查询的条数, 一般情况出参中nRetRecordNum<nFilecount则相应时间段内的文件查询完毕,但部分设备性能限制,nRetRecordNum<nFilecount不代表查询完毕,建议和CLIENT_QueryRecordCount配套使用,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FIND_SEEK_NEXT_RECORD_PARAM}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FIND_SEEK_NEXT_RECORD_PARAM}
     */
    public boolean CLIENT_FindSeekNextRecord(Pointer pInParam,Pointer pOutParam,int waittime);

    /**
     * @brief 查询人脸库总空间大小、剩余空间大小、人脸库可导入总条数和剩余可导入条数
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_FACE_RECOGNITION_GROUP_SPACE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_FACE_RECOGNITION_GROUP_SPACE_INFO}
     */
    public boolean CLIENT_GetFaceRecognitionGroupSpaceInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 人员重新建模, pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FACE_RECOGNITION_REABSTRACT_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FACE_RECOGNITION_REABSTRACT_INFO}
     */
    public boolean CLIENT_FaceRecognitionReAbstract(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 终止建模
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_STOP_FACE_RECOGNITION_REABSTRACT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_STOP_FACE_RECOGNITION_REABSTRACT}
     */
    public boolean CLIENT_StopFaceRecognitionReAbstract(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 目标组重新建模, pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FACE_RECOGNITION_GROUP_REABSTRACT_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FACE_RECOGNITION_GROUP_REABSTRACT_INFO}
     */
    public boolean CLIENT_FaceRecognitionGroupReAbstract(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取目标导入令牌
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_FACE_RECOGNITION_APPEND_TOKEN}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_FACE_RECOGNITION_APPEND_TOKEN}
     */
    public boolean CLIENT_GetFaceRecognitionAppendToken(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 组成要设置的智能配置信息(lpInBuffer，szOutBuffer内存由用户申请释放)
     */
    public boolean CLIENT_PacketIntelliSchemeData(Pointer szCommand,Pointer lpInBuffer,int dwInBufferSize,Pointer szOutBuffer,int dwOutBufferSize);

    /**
     * @brief 解析查询到的智能配置信息(szInBuffer，lpOutBuffer内存由用户申请释放)
     */
    public boolean CLIENT_ParseIntelliSchemeData(Pointer szCommand,Pointer szInBuffer,Pointer lpOutBuffer,int dwOutBufferSize,Pointer pReserved);

    /**
     * @brief 新配置接口，查询配置信息(以Json格式，具体见配置SDK)(szOutBuffer内存由用户申请释放)
     */
    public boolean CLIENT_IntelliSchemeGetNewDevConfig(LLong lLoginID,Pointer szCommand,int nChannelID,Pointer szOutBuffer,int dwOutBufferSize,IntByReference error,int waittime,Pointer pReserved,int nSchemeID);

    /**
     * @brief 设置指定的智能套餐方案的配置信息
     */
    public boolean CLIENT_IntelliSchemeSetNewDevConfig(LLong lLoginID,Pointer szCommand,int nChannelID,Pointer szInBuffer,int dwInBufferSize,IntByReference error,IntByReference restart,int waittime,int nTransactionID,int nSchemeID);

    /**
     * @brief 订阅历史库以图搜图查询结果, 配合CLIENT_StartFindFaceRecognition使用, pstInParam和pstOutParam由用户申请和释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_RESULT_FINDHISTORY_BYPIC}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_RESULT_FINDHISTORY_BYPIC}
     */
    public LLong CLIENT_AttachResultOfFindHistoryByPic(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 取消订阅历史库以图搜图查询结果, lFindHandle 为 CLIENT_AttachResultOfFindRegisterByPic接口返回的值
     */
    public boolean CLIENT_DetachResultOfFindHistoryByPic(LLong lFindHandle);

    /**
     * @brief 获取智能套餐方案的基础信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_SCHEME_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_SCHEME_INFO}
     */
    public boolean CLIENT_GetSchemeInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 下发https服务器地址，用于公交线路、报站信息的上报,pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DISPATCH_BUS_HTTPS_SERVERS_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DISPATCH_BUS_HTTPS_SERVERS_INFO}
     */
    public boolean CLIENT_DispatchBusHttpsServers(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 订阅485实时数据
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_IOTBOX_COMM}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_IOTBOX_COMM}
     */
    public LLong CLIENT_AttachIotboxComm(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅实时数据
     */
    public boolean CLIENT_DetachIotboxComm(LLong lAttachHandle);

    /**
     * @brief 订阅485实时数据扩展
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_IOTBOX_COMM_EX}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_IOTBOX_COMM_EX}
     */
    public LLong CLIENT_AttachIotboxCommEx(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅实时数据扩展
     */
    public boolean CLIENT_DetachIotboxCommEx(LLong lAttachHandle);

    /**
     * @brief 订阅区域流量数据
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_AREA_FLOW}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_AREA_FLOW}
     */
    public LLong CLIENT_AttachAreaFlow(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 订阅前端设备对讲状态,pstInParam与pstOutParam内存由用户申请释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_TALK_STATE}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_TALK_STATE}
     */
    public LLong CLIENT_AttachTalkState(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 取消订阅前端设备对讲状态, lAttachHandle为CLIENT_AttachTalkState返回的句柄
     */
    public boolean CLIENT_DetachTalkState(LLong lAttachHandle);

    /**
     * @brief 获取安检机录像文件
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_XRAY_DOWNLOAD_RECORD_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_XRAY_DOWNLOAD_RECORD_INFO}
     */
    public boolean CLIENT_GetXRayDownloadRecord(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置屏幕叠加回调
     * @param cbDraw 接口输入参数， 参考回调函数定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.fDrawCallBack}
     */
    public void CLIENT_RigisterDrawFun(fDrawCallBack cbDraw,Pointer dwUser);

    /**
     * @brief 上传二进制地图图片
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_UPLOAD_MAP_PIC}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_UPLOAD_MAP_PIC}
     */
    public boolean CLIENT_UploadMapPic(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 加载地图图片
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_LOAD_MAP_PIC}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_LOAD_MAP_PIC}
     */
    public boolean CLIENT_LoadMapPic(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取地图已标记信息列表
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_PIC_MAP_MARK_LIST}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_PIC_MAP_MARK_LIST}
     */
    public boolean CLIENT_GetPicMapMarkList(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 地图上标记通道位置信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MARK_PIC_MAP_CHANNEL}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MARK_PIC_MAP_CHANNEL}
     */
    public boolean CLIENT_MarkPicMapChannel(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消通道的地图标记
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_UNMARK_PIC_MAP_CHANNEL}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_UNMARK_PIC_MAP_CHANNEL}
     */
    public boolean CLIENT_UnmarkPicMapChannel(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 重置清空地图（包括地图图片，标记信息）
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RESET_PIC_MAP}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RESET_PIC_MAP}
     */
    public boolean CLIENT_ResetPicMap(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 收藏目标事件记录
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS}
     */
    public boolean CLIENT_MarkObjectFavoritesLibraryObjectRecords(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消收藏目标事件记录
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_UNMARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_UNMARK_OBJECT_FAVORITES_LIBRARY_OBJECT_RECORDS}
     */
    public boolean CLIENT_UnmarkObjectFavoritesLibraryObjectRecords(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 检查目标事件记录的收藏状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_CHECK_OBJECT_FAVORITES_LIBRARY_MARK_STATUS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_CHECK_OBJECT_FAVORITES_LIBRARY_MARK_STATUS}
     */
    public boolean CLIENT_CheckObjectFavoritesLibraryMarkStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 开始按条件，查找收藏夹内容数据
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_FIND_OBJECT_FAVORITES_LIBRARY}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_FIND_OBJECT_FAVORITES_LIBRARY}
     */
    public LLong CLIENT_StartFindObjectFavoritesLibrary(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取收藏夹内容数据, lFindID为CLIENT_StartFindObjectFavoritesLibrary接口返回的查找ID
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DO_FIND_OBJECT_FAVORITES_LIBRARY}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DO_FIND_OBJECT_FAVORITES_LIBRARY}
     */
    public boolean CLIENT_DoFindObjectFavoritesLibrary(LLong lFindID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 停止查找收藏夹内容数据，清空查询会话, lFindID为CLIENT_StartFindObjectFavoritesLibrary接口返回的查找ID
     */
    public boolean CLIENT_StopFindObjectFavoritesLibrary(LLong lFindID);

    /**
     * @brief 根据目标轨迹过滤规则，开始查询
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_FIND_OBJECT_MEDIA_FIND}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_FIND_OBJECT_MEDIA_FIND}
     */
    public LLong CLIENT_StartFindObjectMediaFind(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取录像查询结果
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FIND_OBJECT_MEDIA_FIND_FILE}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FIND_OBJECT_MEDIA_FIND_FILE}
     */
    public boolean CLIENT_FindObjectMediaFindFile(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取目标事件查询结果
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FIND_OBJECT_MEDIA_FIND_EVENT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FIND_OBJECT_MEDIA_FIND_EVENT}
     */
    public boolean CLIENT_FindObjectMediaFindEvent(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 停止查询
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_STOP_FIND_OBJECT_MEDIA_FIND}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_STOP_FIND_OBJECT_MEDIA_FIND}
     */
    public boolean CLIENT_StopFindObjectMediaFind(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取系统参数
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_BOOT_PARAMETER}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_BOOT_PARAMETER}
     */
    public boolean CLIENT_GetBootParameter(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 一键操作录播录像的开启/暂停/关闭
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MANUAL_CONTROL_COURSE_RECORD_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MANUAL_CONTROL_COURSE_RECORD_INFO}
     */
    public boolean CLIENT_ManualControlCourseRecord(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置车位检测器车灯亮灯计划,pNetDataIn与pNetDataOut由用户申请内存
     * @param pNetDataIn 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_PARKING_SPACE_LIGHT_PLAN}
     * @param pNetDataOut 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_PARKING_SPACE_LIGHT_PLAN}
     */
    public boolean CLIENT_SetParkingSpaceLightPlan(LLong lLoginID,Pointer pNetDataIn,Pointer pNetDataOut,int nWaitTime);

    /**
     * @brief 修改采集文件备注
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MODIFY_MEDIA_FILE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MODIFY_MEDIA_FILE_INFO}
     */
    public boolean CLIENT_ModifyMediaFile(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 删除采集文件
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DELETE_MEDIA_FILE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DELETE_MEDIA_FILE_INFO}
     */
    public boolean CLIENT_DeleteMediaFile(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 开始查询采集文件信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_QUERY_MEDIA_FILE_OPEN_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_QUERY_MEDIA_FILE_OPEN_INFO}
     */
    public boolean CLIENT_QueryMediaFileOpen(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 查询采集文件信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_QUERY_MEDIA_FILE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_QUERY_MEDIA_FILE_INFO}
     */
    public boolean CLIENT_QueryMediaFile(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 关闭采集文件查询
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_QUERY_MEDIA_FILE_CLOSE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_QUERY_MEDIA_FILE_CLOSE_INFO}
     */
    public boolean CLIENT_QueryMediaFileClose(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 云运维异步诊断接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ASYNC_CHECK_FAULT_CHECK}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ASYNC_CHECK_FAULT_CHECK}
     */
    public boolean CLIENT_AsyncCheckFaultCheck(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 四合一烟感平台下发整机重启
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REBOOT_DEVICE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REBOOT_DEVICE}
     */
    public boolean CLIENT_RebootDevice(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 重启系统,恢复出厂默认(包括清空配置和删除账户)并重启，实现硬复位功能
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RESET_SYSTEM}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RESET_SYSTEM}
     */
    public boolean CLIENT_ResetSystem(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取设备当前状态并绑定导出接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_START_STREAM_DATA}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_START_STREAM_DATA}
     */
    public LLong CLIENT_AttachStartStreamData(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 订阅抓包数据,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_SNIFFER}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_SNIFFER}
     */
    public LLong CLIENT_AttachSniffer(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 订阅调试日志回调
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_DBGINFO}
     * @param pOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_DBGINFO}
     */
    public LLong CLIENT_AttachDebugInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 注销一键导出处理
     */
    public boolean CLIENT_DetachStopStreamData(LLong lAttachHandle);

    /**
     * @brief 退订抓包数据
     */
    public boolean CLIENT_DetachSniffer(LLong lAttachHandle);

    /**
     * @brief 退订调试日志回调
     */
    public boolean CLIENT_DetachDebugInfo(LLong lAttachHanle);

    /**
     * @brief 添加Onvif用户， pstInParam、pstOutParam 内存由用户申请、释放
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ADD_ONVIF_USER_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ADD_ONVIF_USER_INFO}
     */
    public boolean CLIENT_AddOnvifUser(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取子模块信息
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_SUBMODULES_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_SUBMODULES_INFO}
     */
    public boolean CLIENT_GetSubModuleInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取软件版本
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_SOFTWAREVERSION_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_SOFTWAREVERSION_INFO}
     */
    public boolean CLIENT_GetSoftwareVersion(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 开始抓包,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_SNIFFER}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_SNIFFER}
     */
    public LLong CLIENT_StartSniffer(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 停止抓包
     */
    public boolean CLIENT_StopSniffer(LLong lLoginID,LLong lSnifferID);

    /**
     * @brief 获取抓包状态,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_SNIFFER_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_SNIFFER_INFO}
     */
    public boolean CLIENT_GetSnifferInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取抓包能力,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_SNIFFER_CAP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_SNIFFER_CAP}
     */
    public boolean CLIENT_GetSnifferCaps(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取从片版本信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_PERIPHERAL_CHIP_VERSION}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_PERIPHERAL_CHIP_VERSION}
     */
    public boolean CLIENT_GetPeripheralChipVersion(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 调节光圈
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_PTZ_ADJUST_IRIS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_PTZ_ADJUST_IRIS}
     */
    public boolean CLIENT_PTZAdjustIris(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取CPU温度
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CPU_TEMPERATURE_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CPU_TEMPERATURE_INFO}
     */
    public boolean CLIENT_GetCPUTemperature(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 订阅智能分析结果
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_VIDEO_ANALYSE_ANALYSE_PROC_EX}
     */
    public LLong CLIENT_AttachVideoAnalyseAnalyseProcEx(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 取消订阅智能分析结果
     */
    public boolean CLIENT_DetachVideoAnalyseAnalyseProcEx(LLong lAttachHandle);

    /**
     * @brief 持续对焦
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FOCUS_PTZ_CONTINUOUSLY_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FOCUS_PTZ_CONTINUOUSLY_INFO}
     */
    public boolean CLIENT_FocusPTZContinuously(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置电视墙场景,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MONITORWALL_SET_SCENE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MONITORWALL_SET_SCENE}
     */
    public boolean CLIENT_MonitorWallSetScene(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取电视墙场景,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MONITORWALL_GET_SCENE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MONITORWALL_GET_SCENE}
     */
    public boolean CLIENT_MonitorWallGetScene(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 设置窗口场景信息
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SPLIT_SET_WINDOWS_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SPLIT_SET_WINDOWS_INFO}
     */
    public boolean CLIENT_SetSplitWindowsInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 根据执法人ID获取该执法人绑定的执法记录仪序列号列表
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_COLLECT_DEVICES_INFO_BY_TSFID_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_COLLECT_DEVICES_INFO_BY_TSFID_INFO}
     */
    public boolean CLIENT_GetCollectDevicesInfoByTsfId(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取所有 Onvif 用户信息，pstInParam、pstOutParam 内存由用户申请、释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GETONVIF_USERINFO_ALL_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GETONVIF_USERINFO_ALL_INFO}
     */
    public boolean CLIENT_GetOnvifUserInfoAll(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取蜂窝邻区信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_NEIGHBOUR_CELL_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_NEIGHBOUR_CELL_INFO}
     */
    public boolean CLIENT_GetNeighbourCellInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置窗口轮巡显示源(pInparam, pOutParam内存由用户申请释放)
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_TOUR_SOURCE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_TOUR_SOURCE}
     */
    public boolean CLIENT_SetTourSource(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取窗口轮巡显示源(pInparam, pOutParam内存由用户申请释放)
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_TOUR_SOURCE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_TOUR_SOURCE}
     */
    public boolean CLIENT_GetTourSource(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 楼层一键标定
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_AUTO_CALIBRATE}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_AUTO_CALIBRATE}
     */
    public boolean CLIENT_AutoCalibrate(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 手动修改楼层高度并生成气压差信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MODIFY_FLOOR_HEIGHT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MODIFY_FLOOR_HEIGHT}
     */
    public boolean CLIENT_ModifyFloorHeight(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取ElevatorFloorCounter.autoCalibrate下发后的标定状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CALIBRATE_STATUS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CALIBRATE_STATUS}
     */
    public boolean CLIENT_GetCalibrateStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 启动程序
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_APP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_APP}
     */
    public boolean CLIENT_StartApp(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 停止程序运行
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_STOP_APP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_STOP_APP}
     */
    public boolean CLIENT_StopApp(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 卸载程序
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOVE_APP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOVE_APP}
     */
    public boolean CLIENT_RemoveApp(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 第三方APP升级进度和结果的实时上报
     * @param emType 接口输出参数， 参考枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_NET_UPGRADE_INSTALL_TYPE}
     */
    public boolean CLIENT_UpgraderInstall(LLong lLoginID,int emType,Pointer pInBuf,Pointer pOutBuf,int nWaittime);

    /**
     * @brief 获取安装的应用列表
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_INSTALLED_APP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_INSTALLED_APP_INFO}
     */
    public boolean CLIENT_GetInstalledAppInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 通用RPC接口，支持Json传参
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_NORMAL_RPCCALL_USING_JSON}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_NORMAL_RPCCALL_USING_JSON}
     */
    public boolean CLIENT_NormalRpcCallUsingJson(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取存储端口信息
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_PORTINFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_PORTINFO}
     */
    public boolean CLIENT_GetStoragePortInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int waittime);

    /**
     * @brief 订阅录像状态变化,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RECORDMANAGER_ATTACH_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RECORDMANAGER_ATTACH_INFO}
     */
    public LLong CLIENT_AttachRecordManagerState(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取外部文件上传的文件名和路径
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_UPLOAD_PATH_INFO}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_UPLOAD_PATH_INFO}
     */
    public boolean CLIENT_GetUploadPath(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取指定盘组下的设备成员信息
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_WORK_GROUP_DEVICE_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_WORK_GROUP_DEVICE_INFO}
     */
    public boolean CLIENT_GetWorkGroupDeviceInfos(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief json通用订阅接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_NORMAL_USING_JSON}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_NORMAL_USING_JSON}
     */
    public LLong CLIENT_AttachNormalUsingJson(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief json通用订阅退订接口
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DETACH_NORMAL_USING_JSON}
     */
    public boolean CLIENT_DetachNormalUsingJson(LLong lLoginID,Pointer pstuInParam);

    /**
     * @brief 开启音频录音并得到录音名
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_AUDIO_RECORD_MANAGER_CHANNEL}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_AUDIO_RECORD_MANAGER_CHANNEL}
     */
    public boolean CLIENT_StartAudioRecordManagerChannel(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 关闭即时录音
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_STOP_AUDIO_RECORD_MANAGER_CHANNEL}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_STOP_AUDIO_RECORD_MANAGER_CHANNEL}
     */
    public boolean CLIENT_StopAudioRecordManagerChannel(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 按组获取视频通道
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CAMERA_ALL_BY_GROUP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CAMERA_ALL_BY_GROUP}
     */
    public boolean CLIENT_MatrixGetCameraAllByGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 投递异步登录任务
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_POST_LOGIN_TASK}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_POST_LOGIN_TASK}
     */
    public int CLIENT_PostLoginTask(Pointer pInParam,Pointer pOutParam);

    /**
     * @brief 取消 CLIENT_PostLoginTask 接口的异步登录任务，dwTaskID 为 CLIENT_PostLoginTask 返回值
     */
    public boolean CLIENT_CancelLoginTask(int dwTaskID);

    /**
     * @brief 设置优化方案 pParam内存由用户申请释放，大小参照emType对应的结构体
     * @param emType 接口输入参数， 参考枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.NET_SYS_ABILITY}
     */
    public boolean CLIENT_SetOptimizeMode(int emType,Pointer pParam);

    /**
     * @brief 查询部门信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_DEPARTMENT_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_DEPARTMENT_INFO}
     */
    public boolean CLIENT_GetTipStaffManagerDepartmentInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 查询执法人信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_TIP_STAFF_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_TIP_STAFF_INFO}
     */
    public boolean CLIENT_GetTipStaffManagerTipStaffInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取雷达检测目标数据
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_WATERRADAR_OBJECTINFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_WATERRADAR_OBJECTINFO}
     */
    public boolean CLIENT_GetWaterRadarObjectInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 报警上传功能,启动服务；dwTimeOut参数已无效
     * @param pfscb 接口输入参数， 参考回调函数定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.fServiceCallBack}
     */
    public LLong CLIENT_StartService(int wPort,Pointer pIp,fServiceCallBack pfscb,int dwTimeOut,Pointer dwUserData);

    /**
     * @brief 设置实时预览帧信息回调
     * @param cbFrameInfo 接口输入参数， 参考回调函数定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.fFrameInfoCallBackEx}
     */
    public boolean CLIENT_SetRealFrameInfoCallBack(LLong lRealHandle,fFrameInfoCallBackEx cbFrameInfo,Pointer dwUser);

    /**
     * @brief 发送按键消息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SEND_XRAY_KEY_MANAGER_KEY_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SEND_XRAY_KEY_MANAGER_KEY_INFO}
     */
    public boolean CLIENT_SendXRayKeyManagerKey(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅测温温度数据,lAttachTemperHandle是 CLIENT_RadiometryAttachTemper 的返回值
     */
    public boolean CLIENT_RadiometryDetachTemper(LLong lAttachTemperHandle);

    /**
     * @brief 获取车流统计摘要信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_FIND_VEHICLE_FLOW_STAT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_FIND_VEHICLE_FLOW_STAT}
     */
    public LLong CLIENT_StartFindVehicleFlowStat(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取车流量统计结果信息, lFindID为 CLIENT_StartFindVehicleFlowStat 接口返回的查询ID
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DO_FIND_VEHICLE_FLOW_STAT}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DO_FIND_VEHICLE_FLOW_STAT}
     */
    public boolean CLIENT_DoFindVehicleFlowStat(LLong lFindID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 结束车流量统计结果查询
     */
    public boolean CLIENT_StopFindVehicleFlowStat(LLong lFindID);

    /**
     * @brief 设置水平旋转组边界值
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_PAN_GROUP_LIMIT_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_PAN_GROUP_LIMIT_INFO}
     */
    public boolean CLIENT_PTZSetPanGroupLimit(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int dwWaitTime);

    /**
     * @brief 获取电视墙上屏幕窗口解码信息,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MW_GET_WINODW_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MW_GET_WINDOW_INFO}
     */
    public boolean CLIENT_MonitorWallGetWindowInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取工作目录组名
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_WORKDIRECTORY_GETGROUP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_WORKDIRECTORY_GETGROUP_INFO}
     */
    public boolean CLIENT_WorkDirectoryGetGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 设置工作目录组名
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_WORKDIRECTORY_SETGROUP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_WORKDIRECTORY_SETGROUP_INFO}
     */
    public boolean CLIENT_WorkDirectorySetGroup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取存储设备信息,pDevice内存由用户申请释放
     * @param pDevice 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_STORAGE_DEVICE}
     */
    public boolean CLIENT_GetStorageDeviceInfo(LLong lLoginID,Pointer pszDevName,Pointer pDevice,int nWaitTime);

    /**
     * @brief 异步格式化设备,格式化进度通过CLIENT_AttachDevStorageDevFormat接口的回调获取  pszDevName与CLIENT_GetStorageDeviceInfo中的pszDevName保持一致
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DEVSTORAGE_FORMAT_PARTITION_ASYN}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DEVSTORAGE_FORMAT_PARTITION_ASYN}
     */
    public boolean CLIENT_DevStorageFormatPartitionAsyn(LLong lLoginID,Pointer pszDevName,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取所有盘组信息
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_STORAGE_ASSISTANT_GROUP_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_STORAGE_ASSISTANT_GROUP_INFO}
     */
    public boolean CLIENT_GetStorageAssistantGroupInfos(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 设置工作目录组名
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_WORK_DIRECTORY_GROUP}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_WORK_DIRECTORY_GROUP}
     */
    public boolean CLIENT_SetWorkDirectoryGoup(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取工作组内的工作目录名称列表
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_WORK_GROUP_DIRECTORIES}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_WORK_GROUP_DIRECTORIES}
     */
    public boolean CLIENT_GetWorkGroupDirectories(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 添加盘组
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ADD_STORAGE_ASSISTANT_WORK_GROUP}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ADD_STORAGE_ASSISTANT_WORK_GROUP}
     */
    public boolean CLIENT_AddStorageAssistantWorkGroup(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 删除盘组
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DELETE_STORAGE_ASSISTANT_WORK_GROUP}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DELETE_STORAGE_ASSISTANT_WORK_GROUP}
     */
    public boolean CLIENT_DeleteStorageAssistantWorkGroup(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_DOCK_EVENTS}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_DOCK_EVENTS}
     */
    public LLong CLIENT_AttachDockEvents(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     */
    public boolean CLIENT_DetachDockEvents(LLong lAttachHandle);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_DOCK_INFO}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_DOCK_INFO}
     */
    public LLong CLIENT_AttachDockInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     */
    public boolean CLIENT_DetachDockInfo(LLong lAttachHandle);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_DOCK_STATUS}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_DOCK_STATUS}
     */
    public LLong CLIENT_AttachDockStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     */
    public boolean CLIENT_DetachDockStatus(LLong lAttachHandle);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DOCK_EVENTS_REPLY}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DOCK_EVENTS_REPLY}
     */
    public boolean CLIENT_DockEventsReply(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DOCK_PROPERTY_SET}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DOCK_PROPERTY_SET}
     */
    public boolean CLIENT_DockPropertySet(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DOCK_STATUS_REPLY}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DOCK_STATUS_REPLY}
     */
    public boolean CLIENT_DockStatusReply(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FLIGHT_TASK_PREPARE}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FLIGHT_TASK_PREPARE}
     */
    public boolean CLIENT_FlightTaskPrepare(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FLIGHT_TASK_EXECUTE}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FLIGHT_TASK_EXECUTE}
     */
    public boolean CLIENT_FlightTaskExecute(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_RETURN_HOME}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_RETURN_HOME}
     */
    public boolean CLIENT_ReturnHome(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置运单信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_WAYBILL_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_WAYBILL_INFO}
     */
    public boolean CLIENT_SetWaybillInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置开包检查结果带图片
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_UNPACKING_RESULT_WITH_PACKET}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_UNPACKING_RESULT_WITH_PACKET}
     */
    public boolean CLIENT_SetUnpackingResultWithPacket(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief X光机集中判图时 订阅开包检查结果
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_XRAY_ATTACH_UNPACKING}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_XRAY_ATTACH_UNPACKING}
     */
    public LLong CLIENT_XRay_AttachUnpackingResult(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief X光机集中判图时 退订开包检查结果
     */
    public boolean CLIENT_XRay_DetachUnpackingResult(LLong lAttachHandle);

    /**
     * @brief 设置运单开始/结束状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_WAYBILL_STATUS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_WAYBILL_STATUS}
     */
    public boolean CLIENT_SetWaybillStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取工作目录信息
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_STORAGE_INFO_BY_FILE_TYPE}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_STORAGE_INFO_BY_FILE_TYPE}
     */
    public boolean CLIENT_GetStorageInfoByFileType(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief X光机集中判图时，开包台客户端设置开包检查结果
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_UNPACKING_RESULT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_UNPACKING_RESULT}
     */
    public boolean CLIENT_SetUnpackingResult(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 订阅包裹
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_XRAY_PACKAGE_MANUAL_EXPORT}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_XRAY_PACKAGE_MANUAL_EXPORT}
     */
    public LLong CLIENT_AttachXRayPackageManualExport(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 托管备份任务
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_MANDATE_BACKUP_TASK_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_MANDATE_BACKUP_TASK_INFO}
     */
    public boolean CLIENT_MandateBackupTask(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置音频输出模式(pInparam, pOutParam内存由用户申请释放)
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_AUDIO_OUTPUT}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_AUDIO_OUTPUT}
     */
    public boolean CLIENT_SetSplitAudioOuput(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取音频输出模式(pInparam, pOutParam内存由用户申请释放)
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_AUDIO_OUTPUT}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_AUDIO_OUTPUT}
     */
    public boolean CLIENT_GetSplitAudioOuput(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 智能跟踪球控制接口
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_CONTROL_INTELLITRACKER}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_CONTROL_INTELLITRACKER}
     */
    public boolean CLIENT_ControlIntelliTracker(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam);

    /**
     * @brief 断油断电下发
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_FUEL_POWER_CUTOFF_COMMAND}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_FUEL_POWER_CUTOFF_COMMAND}
     */
    public boolean CLIENT_FuelPowerCutoffCommand(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 获取刻录状态扩展
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_BURN_GET_STATE_EX}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.NetSDKLib.NET_OUT_BURN_GET_STATE}
     */
    public boolean CLIENT_BurnGetStateEx(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 获取SIM卡信息接口
     * @param emType 接口输入参数， 参考枚举定义 {@link com.coalbot.camera.sdk.sdk.dahua.enumeration.EM_SIMINFO_TYPE}
     */
    public boolean CLIENT_GetMobileSIMInfo(LLong lLoginID,int emType,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 订阅雷达模块是否正常工作
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_MODULE_STATUS}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_MODULE_STATUS}
     */
    public LLong CLIENT_AttachModuleStatus(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅雷达模块工作状态
     */
    public boolean CLIENT_DetachModuleStatus(LLong lAttachHandle);

    /**
     * @brief 获取智能卡的运行使用情况
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_CHIP_USAGE_INFO}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_CHIP_USAGE_INFO}
     */
    public boolean CLIENT_GetChipUsageInfo(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 订阅人体历史库以图搜图查询结果, 配合CLIENT_StartFindFaceRecognition使用, pstInParam和pstOutParam由用户申请和释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_RESULT_HUMAN_HISTORY_BYPIC}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_RESULT_HUMAN_HISTORY_BYPIC}
     */
    public LLong CLIENT_AttachResultOfHumanHistoryByPic(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 订阅车辆历史库以图搜图查询结果, 配合CLIENT_StartFindFaceRecognition使用, pstInParam和pstOutParam由用户申请和释放
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_RESULT_VEHICLE_HISTORY_BYPIC}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_RESULT_VEHICLE_HISTORY_BYPIC}
     */
    public LLong CLIENT_AttachResultOfVehicleHistoryByPic(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 大模型数据库内容检索
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_FIND_LARGE_MODE_SERVER}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_FIND_LARGE_MODE_SERVER}
     */
    public LLong CLIENT_StartFindLargeModeServer(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 结束内容检索查询
     */
    public boolean CLIENT_StopFindLargeModeServer(LLong lFindID);

    /**
     * @brief 订阅内容检索查询状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_LARGE_MODE_SERVER_FIND_RESULT}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_LARGE_MODE_SERVER_FIND_RESULT}
     */
    public LLong CLIENT_AttachLargeModeServerFindResult(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 取消订阅内容检索查询状态
     */
    public boolean CLIENT_DetachLargeModeServerFindResult(LLong lAttachHandle);

    /**
     * @brief 主动查询最新比对结果
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DO_FIND_LARGE_MODE_SERVER}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DO_FIND_LARGE_MODE_SERVER}
     */
    public boolean CLIENT_DoFindLargeModeServer(LLong lFindID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 查询所有备份任务简要状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_BACKUP_MANAGER_GET_TASK_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_BACKUP_MANAGER_GET_TASK_INFO}
     */
    public boolean CLIENT_BackupManagerGetTaskInfo(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 解绑码解绑绑定的SIM卡
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_WIRELESS_UNBIND}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_WIRELESS_UNBIND}
     */
    public boolean CLIENT_WirelessUnbind(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置CLIENT_AttachResultOfHumanHistoryByPic接口新的回调函数, 用于解决java大结构体new对象慢导致的问题.该接口在CLIENT_AttachResultOfHumanHistoryByPic接口调用后调用
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_ATTACH_FIND_RESULT_HISTORY_CB_EX}
     */
    public boolean CLIENT_SetAttachFindResultHistoryCBEx(Pointer pInParam);

    /**
     * @brief 控制外部报警设备电源状态
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_CONTROL_MISC_POWER_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_CONTROL_MISC_POWER_INFO}
     */
    public boolean CLIENT_ControlMiscPower(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 创建备份任务实例，和CLIENT_DestroyBackupTask成对使用
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_CREATE_BACKUP_TASK_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_CREATE_BACKUP_TASK_INFO}
     */
    public boolean CLIENT_CreateBackupTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 开始备份任务扩展，该接口内部不会调create和destroy
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_START_BACKUP_TASK_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_START_BACKUP_TASK_INFO}
     */
    public boolean CLIENT_StartBackupTaskEx(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 销毁备份任务实例
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_DESTROY_BACKUP_TASK_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_DESTROY_BACKUP_TASK_INFO}
     */
    public boolean CLIENT_DestroyBackupTask(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 锁定云台
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_LOCKPTZ_INFO}
     * @param pstuOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_LOCKPTZ_INFO}
     */
    public boolean CLIENT_LockPtz(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

    /**
     * @brief 设置变倍映射值参数
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_SET_PTZ_MOVE_ZOOM_FOCUS_MAP_VALUE_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_SET_PTZ_MOVE_ZOOM_FOCUS_MAP_VALUE_INFO}
     */
    public boolean CLIENT_SetPtzMoveZoomFocusMapValue(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 开始异步通过存储设备上传文件到前端设备 pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOTE_UPLOAD_FILE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOTE_UPLOAD_FILE}
     */
    public LLong CLIENT_StartRemoteUploadFile(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 重命名远程文件 pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOTE_RENAME}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOTE_RENAME}
     */
    public boolean CLIENT_RemoteRename(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 删除多个远程文件或目录 pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOTE_REMOVE_FILES}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOTE_REMOVE_FILES}
     */
    public boolean CLIENT_RemoteRemoveFiles(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 通过存储设备远程预上传文件至前端设备,用于识别前端设备是否具备接收文件的条件,pInParam与pOutParam内存由用户申请释放
     * @param pInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_REMOTE_PREUPLOAD_FILE}
     * @param pOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_REMOTE_PREUPLOAD_FILE}
     */
    public boolean CLIENT_RemotePreUploadFile(LLong lLoginID,Pointer pInParam,Pointer pOutParam,int nWaitTime);

    /**
     * @brief 获取人数统计摘要信息
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_VIDEO_STAT_SERVER_SUMMARY_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_VIDEO_STAT_SERVER_SUMMARY_INFO}
     */
    public boolean CLIENT_GetVideoStatServerSummary(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 雷达订阅AIS信息
     * @param pstInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_ATTACH_RADAR_AIS_INFO}
     * @param pstOutParam 接口输出参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_ATTACH_RADAR_AIS_INFO}
     */
    public LLong CLIENT_AttachRadarAISInfo(LLong lLoginID,Pointer pstInParam,Pointer pstOutParam,int nWaitTime);

    /**
     * @brief 雷达取消订阅AIS信息
     */
    public boolean CLIENT_DetachRadarAISInfo(LLong lAttachHandle);

    /**
     * @brief 获取激光测距能力
     * @param pstuInParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_IN_GET_LASER_DIST_MEASURE_CAPS_INFO}
     * @param pstuOutParam 接口输入参数， 参考结构体定义 {@link com.coalbot.camera.sdk.sdk.dahua.structure.NET_OUT_GET_LASER_DIST_MEASURE_CAPS_INFO}
     */
    public boolean CLIENT_GetLaserDistMeasureCaps(LLong lLoginID,Pointer pstuInParam,Pointer pstuOutParam,int nWaitTime);

}
