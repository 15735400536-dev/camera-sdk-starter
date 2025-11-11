package com.coalbot.camera.sdk.handler.impl;

import com.coalbot.camera.sdk.bo.*;
import com.coalbot.camera.sdk.bo.preset.DeletePresetBO;
import com.coalbot.camera.sdk.bo.preset.GotoPresetBO;
import com.coalbot.camera.sdk.bo.preset.QueryPresetBO;
import com.coalbot.camera.sdk.bo.preset.SetPresetBO;
import com.coalbot.camera.sdk.enums.CameraBrand;
import com.coalbot.camera.sdk.enums.PtzCommand;
import com.coalbot.camera.sdk.exception.CameraSdkException;
import com.coalbot.camera.sdk.exception.SdkUnsupportedException;
import com.coalbot.camera.sdk.handler.CameraSdkHandler;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLib;
import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;
import com.coalbot.camera.sdk.sdk.dahua.ToolKits;
import com.coalbot.camera.sdk.sdk.dahua.Utils;
import com.coalbot.camera.sdk.sdk.dahua.demo.module.LoginModule;
import com.coalbot.camera.sdk.util.CameraSdkUtils;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：DahuaHandlerImpl
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 17:28
 * @Description: 大华摄像头SDK实现类
 */
public class DahuaHandlerImpl implements CameraSdkHandler {

    // 摄像头SDK实例
    private static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
    // 登陆句柄
    private static NetSDKLib.LLong loginHandle = new NetSDKLib.LLong(0);
    // 设备信息
    private static NetSDKLibStructure.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLibStructure.NET_DEVICEINFO_Ex();
    // SDK实例初始化标记
    private static boolean initResult = false;
    // 日志开启标记
    private static boolean logOpenResult = false;
    /**
     * 预览句柄
     */
    private static NetSDKLib.LLong lpreviewHandle = new NetSDKLib.LLong(0);

    // 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private static class DisConnect implements NetSDKLib.fDisConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            // 断线提示
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }
    }

    // 网络连接恢复，设备重连成功回调
    // 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
    private static class HaveReConnect implements NetSDKLib.fHaveReConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            // 重连提示
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        }
    }

    /**
     * 初始化摄像头SDK实例
     */
    public static void initSDK() {
        // 设备断线通知回调
        initResult = netsdk.CLIENT_Init(new DisConnect(), null);
        if (!initResult) {
            System.out.println("Initialize SDK failed");
            return;
        }

        // 打开日志，可选
        NetSDKLibStructure.LOG_SET_PRINT_INFO setLog = new NetSDKLibStructure.LOG_SET_PRINT_INFO();
        String logPath = CameraSdkUtils.getSdkLogPath(CameraBrand.Dahua);
        setLog.nPrintStrategy = 0;
        setLog.bSetFilePath = 1;
        System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0, logPath.getBytes().length);
        System.out.println(logPath);
        setLog.bSetPrintStrategy = 1;
        logOpenResult = netsdk.CLIENT_LogOpen(setLog);
        if (!logOpenResult) {
            System.err.println("Failed to open NetSDK log");
        }

        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        netsdk.CLIENT_SetAutoReconnect(new HaveReConnect(), null);

        //设置登录超时时间和尝试次数，可选
        int waitTime = 5000; //登录请求响应超时时间设置为5S
        int tryTimes = 1;    //登录时尝试建立链接1次
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);


        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLibStructure.NET_PARAM netParam = new NetSDKLibStructure.NET_PARAM();
        netParam.nConnectTime = 10000;      // 登录时尝试建立链接的超时时间
        netParam.nGetConnInfoTime = 3000;   // 设置子连接的超时时间
        netParam.nGetDevInfoTime = 3000;    // 获取设备信息超时时间，为0默认1000ms
        netsdk.CLIENT_SetNetworkParam(netParam);
    }

    /**
     * 释放摄像头SDK实例
     */
    public static void releaseSDK() {
        if (logOpenResult) {
            netsdk.CLIENT_LogClose();
        }

        if (initResult) {
            netsdk.CLIENT_Cleanup();
        }
    }

    @Override
    public int login(DeviceLoginBO param) {
        //入参
        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstInParam.szIP = param.getIp().getBytes();
        pstInParam.nPort = param.getPort();
        pstInParam.szUserName = param.getUsername().getBytes();
        pstInParam.szPassword = param.getPassword().getBytes();
        //出参
        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
        loginHandle = netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
        if (loginHandle.longValue() == 0) {
            System.err.printf("Login Device[%s] Port[%d]Failed. %s\n", param.getIp(), param.getPort(), ToolKits.getErrorCodePrint());
        } else {
            System.out.println("Login Success [ " + param.getIp() + " ]");
        }
        System.out.println("大华设备登录");
        return 0;
    }

    @Override
    public void logout(int handle) {
        if (loginHandle.longValue() == 0) {
            return;
        }

        boolean result = netsdk.CLIENT_Logout(loginHandle);
        if (result) {
            loginHandle.setValue(0);
        }
        System.out.println("大华设备登出");
    }

    @Override
    public int startPlay() {
        NetSDKLib.LLong m_hPlayHandle = LoginModule.netsdk.CLIENT_RealPlayEx(LoginModule.m_hLoginHandle, 0, null, NetSDKLibStructure.NET_RealPlayType.NET_RType_Realplay);
        if (m_hPlayHandle.longValue() == 0) {
            System.err.println("开始实时预览失败，错误码" + ToolKits.getErrorCodePrint());
        }
        return 0;
    }

    @Override
    public void stopPlay(int previewHandle) {
        if (lpreviewHandle.longValue() == 0) {
            return;
        }

        boolean bRet = LoginModule.netsdk.CLIENT_StopRealPlayEx(lpreviewHandle);
        if (bRet) {
            lpreviewHandle.setValue(0);
        }
    }

    /**
     * 通用PTZ指令转化为摄像头SDK PTZ指令
     *
     * @param command 通用PTZ控制命令
     * @return
     */
    private int commandConvert(PtzCommand command) {
        int ptzCommand = -1;
        switch (command) {
            case UP:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_UP_CONTROL;
                break;
            case DOWN:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_DOWN_CONTROL;
                break;
            case LEFT:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_LEFT_CONTROL;
                break;
            case RIGHT:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_RIGHT_CONTROL;
                break;
            case UP_LEFT:
                ptzCommand = NetSDKLibStructure.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTTOP;
                break;
            case UP_RIGHT:
                ptzCommand = NetSDKLibStructure.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTTOP;
                break;
            case DOWN_LEFT:
                ptzCommand = NetSDKLibStructure.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTDOWN;
                break;
            case DOWN_RIGHT:
                ptzCommand = NetSDKLibStructure.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTDOWN;
                break;
            case ZOOM_IN:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_ZOOM_DEC_CONTROL;
                break;
            case ZOOM_OUT:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_ZOOM_ADD_CONTROL;
                break;
            case FOCUS_NEAR:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_FOCUS_DEC_CONTROL;
                break;
            case FOCUS_FAR:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_FOCUS_ADD_CONTROL;
                break;
            case IRIS_OPEN:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_APERTURE_ADD_CONTROL;
                break;
            case IRIS_CLOSE:
                ptzCommand = NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_APERTURE_DEC_CONTROL;
                break;
        }
        return ptzCommand;
    }

    @Override
    public void ptzControl(PtzControlBO param) {
        if (loginHandle.longValue() == 0) {
            throw new CameraSdkException("请先设备登录，再进行云台控制！");
        }

        int ptzCommand = commandConvert(param.getCommand());
        boolean result = LoginModule.netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, ptzCommand,
                0, param.getSpeed(), 0, 0);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
    }

    @Override
    public void stopPtz(PtzControlBO param) {
        if (loginHandle.longValue() == 0) {
            throw new CameraSdkException("请先设备登录，再进行云台控制！");
        }

        int ptzCommand = commandConvert(param.getCommand());
        boolean result = LoginModule.netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, ptzCommand,
                0, param.getSpeed(), 0, 1);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
    }

    @Override
    public void zoom(ZoomBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "zoom+":
                ptzControlBO.setCommand(PtzCommand.ZOOM_OUT);
                break;
            case "zoom-":
                ptzControlBO.setCommand(PtzCommand.ZOOM_IN);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        ptzControl(ptzControlBO);
    }

    @Override
    public void stopZoom(ZoomBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "zoom+":
                ptzControlBO.setCommand(PtzCommand.ZOOM_OUT);
                break;
            case "zoom-":
                ptzControlBO.setCommand(PtzCommand.ZOOM_IN);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        stopPtz(ptzControlBO);
    }

    @Override
    public void focus(FocusBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "focus+":
                ptzControlBO.setCommand(PtzCommand.FOCUS_FAR);
                break;
            case "focus-":
                ptzControlBO.setCommand(PtzCommand.FOCUS_NEAR);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        ptzControl(ptzControlBO);
    }

    @Override
    public void stopFocus(FocusBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "focus+":
                ptzControlBO.setCommand(PtzCommand.FOCUS_FAR);
                break;
            case "focus-":
                ptzControlBO.setCommand(PtzCommand.FOCUS_NEAR);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        stopPtz(ptzControlBO);
    }

    @Override
    public void iris(IrisBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "iris+":
                ptzControlBO.setCommand(PtzCommand.IRIS_OPEN);
                break;
            case "iris-":
                ptzControlBO.setCommand(PtzCommand.IRIS_CLOSE);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        ptzControl(ptzControlBO);
    }

    @Override
    public void stopIris(IrisBO param) {
        PtzControlBO ptzControlBO = new PtzControlBO();
        ptzControlBO.setSpeed(param.getSpeed());
        switch (param.getCommand()) {
            case "iris+":
                ptzControlBO.setCommand(PtzCommand.IRIS_OPEN);
                break;
            case "iris-":
                ptzControlBO.setCommand(PtzCommand.IRIS_CLOSE);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }

        stopPtz(ptzControlBO);
    }

    @Override
    public List<Map<String, Object>> queryPresets(QueryPresetBO param) {
        List<Map<String, Object>> presetList = new ArrayList<>();
        NetSDKLibStructure.NET_PTZ_PRESET_LIST ptzPresetList = new NetSDKLibStructure.NET_PTZ_PRESET_LIST();
        ptzPresetList.dwSize = ptzPresetList.size();
        ptzPresetList.dwMaxPresetNum = 255;
        ptzPresetList.dwRetPresetNum = 10;
        // 分配内存给预置点列表指针
        Pointer presetMemory = new Memory(ptzPresetList.dwMaxPresetNum * new NetSDKLibStructure.NET_PTZ_PRESET().size());
        ptzPresetList.pstuPtzPorsetList = presetMemory;
        ptzPresetList.write();

        //进行申请释放内存
        IntByReference pRetLen = new IntByReference(0);
        boolean result = netsdk.CLIENT_QueryRemotDevState(loginHandle, NetSDKLibStructure.NET_DEVSTATE_PTZ_PRESET_LIST, 0,
                ptzPresetList.getPointer(), ptzPresetList.size(), pRetLen, 1000);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
            return presetList;
        }
        ptzPresetList.read();
        int returnedPresetNum = ptzPresetList.dwRetPresetNum;
        Pointer presetListPointer = ptzPresetList.pstuPtzPorsetList;
        for (int i = 0; i < returnedPresetNum; i++) {
            NetSDKLibStructure.NET_PTZ_PRESET preset = new NetSDKLibStructure.NET_PTZ_PRESET();
            Pointer presetPointer = presetListPointer.share(i * preset.size());
            // 使用 Pointer 读取数据并设置到结构体
            preset.nIndex = presetPointer.getInt(0);
            preset.szName = presetPointer.getByteArray(4, NetSDKLibStructure.PTZ_PRESET_NAME_LEN);//偏移量计算名称
            preset.szReserve = presetPointer.getByteArray(4 + NetSDKLibStructure.PTZ_PRESET_NAME_LEN, 64);//偏移量算预留64位
            System.out.println("预置点编号：" + preset.nIndex + "名称: " + new String(preset.szName, Charset.forName("GBK")).trim());
            presetList.add(Map.of("presetId", preset.nIndex, "presetName", new String(preset.szName, Charset.forName("GBK")).trim()));
        }
        return presetList;
    }

    @Override
    public int setPreset(SetPresetBO param) {
        boolean result = netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_POINT_SET_CONTROL,
                0, param.getPresetId(), 0, 0);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
        return param.getPresetId();
    }

    @Override
    public void deletePreset(DeletePresetBO param) {
        boolean result = netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_POINT_DEL_CONTROL,
                0, param.getPresetId(), 0, 0);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
    }

    @Override
    public void gotoPreset(GotoPresetBO param) {
        boolean result = netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_POINT_MOVE_CONTROL,
                0, param.getPresetId(), 0, 0);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
    }

    @Override
    public void controlLight(ControlLightBO param) {
        switch (param.getCommand()) {
            case "on":
                netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_LAMP_CONTROL,
                        0, 0, 0, 0);
                break;
            case "off":
                netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_LAMP_CONTROL,
                        0, 0, 0, 1);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void controlWiper(ControlWiperBO param) {
        switch (param.getCommand()) {
            case "on":
                netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_LAMP_CONTROL,
                        0, 0, 0, 0);
                break;
            case "off":
                LoginModule.netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, NetSDKLibStructure.NET_PTZ_ControlType.NET_PTZ_LAMP_CONTROL,
                        0, 0, 0, 1);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void auxiliaryFocus(AuxiliaryFocusBO param) {
        throw new SdkUnsupportedException(CameraBrand.Dahua, "功能【辅助聚焦】暂不支持！");
    }

    @Override
    public void initLens(InitLensBO param) {
        throw new SdkUnsupportedException(CameraBrand.Dahua, "功能【镜头初始化】暂不支持！");
    }

    @Override
    public String captureImage(CaptureImageBO param) {
        // 构建请求参数
        NetSDKLibStructure.NET_IN_SNAP_PIC_TO_FILE_PARAM netInSnapPicToFileParam = new NetSDKLibStructure.NET_IN_SNAP_PIC_TO_FILE_PARAM();
        // 发送抓图命令给前端设备，抓图的信息
        NetSDKLibStructure.SNAP_PARAMS snapParams = new NetSDKLibStructure.SNAP_PARAMS();
        snapParams.Channel = param.getChannelId(); // 抓图通道
        snapParams.mode = 0;                       // 抓图模式
        snapParams.Quality = 3;                    // 画质
        snapParams.InterSnap = 0;                  // 定时抓图时间间隔
        snapParams.CmdSerial = 0;                  // 请求序列号，有效值范围 0~65535，超过范围会被截断为
        netInSnapPicToFileParam.stuParam = snapParams;
        netInSnapPicToFileParam.szFilePath = param.getImgPath().getBytes();
        Pointer pInBuf = new Memory(netInSnapPicToFileParam.size());
        ToolKits.SetStructDataToPointer(netInSnapPicToFileParam, pInBuf, 0);
        // 构建响应参数
        NetSDKLibStructure.NET_OUT_SNAP_PIC_TO_FILE_PARAM netOutSnapPicToFileParam = new NetSDKLibStructure.NET_OUT_SNAP_PIC_TO_FILE_PARAM();
        Pointer pOutBuf = new Memory(netOutSnapPicToFileParam.size());
        ToolKits.SetStructDataToPointer(netOutSnapPicToFileParam, pOutBuf, 0);
        // 抓图
        boolean result = netsdk.CLIENT_SnapPictureToFile(loginHandle, pInBuf, pOutBuf, 0);
        if (!result) {
            throw new CameraSdkException("抓图失败！");
        }
        return param.getImgPath();
    }

    @Override
    public void absolute(AbsoluteBO param) {
        boolean result = netsdk.CLIENT_DHPTZControlEx2(loginHandle, 0, NetSDKLibStructure.NET_EXTPTZ_ControlType.NET_EXTPTZ_BASE_MOVE_ABSOLUTELY,
                0, param.getSpeed(), 0, 0, null);
        if (!result) {
            System.out.println("Failed!" + ToolKits.getErrorCodePrint());
        }
        //throw new SdkUnsupportedException(CameraBrand.Dahua, "功能【精准控制】暂不支持！");
    }
}
