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
import com.coalbot.camera.sdk.sdk.uniview.NetDEVSDKLib;
import com.coalbot.camera.sdk.util.CameraSdkUtils;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.util.*;

/**
 * @ClassName：HikvisionHandlerImpl
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:22
 * @Description: 宇视摄像头SDK实现类
 */
public class UniviewHandlerImpl implements CameraSdkHandler {

    // 摄像头SDK实例
    private static NetDEVSDKLib netdevsdk = NetDEVSDKLib.NETDEVSDK_INSTANCE;
    // SDK实例标志
    private static boolean initResult = false;
    // 日志设置标志
    private static boolean logResult = false;
    // 登录句柄
    private static Pointer loginHandle = null;
    // 预览句柄
    private static Pointer lpreviewHandle = null;

    public static void initSDK() {
        // 设置SDK日志路径
        String strLogPath = CameraSdkUtils.getSdkLogPath(CameraBrand.Uniview);
        logResult = netdevsdk.NETDEV_SetLogPath(strLogPath);
        if (!logResult) {
            System.out.printf("NETDEV_SetLogPath failed:%d", netdevsdk.NETDEV_GetLastError());
        }

        // 初始化SDK
        initResult = netdevsdk.NETDEV_Init();
        if (!initResult) {
            System.out.printf("Initialize failed:%d", netdevsdk.NETDEV_GetLastError());
            return;
        }
        System.out.println("初始化宇视SDK");
    }

    public static void releaseSDK() {
        if (initResult) {
            netdevsdk.NETDEV_Cleanup();
            initResult = false;
        }
    }

    @Override
    public int login(DeviceLoginBO param) {
        NetDEVSDKLib.NETDEV_DEVICE_LOGIN_INFO_S stDevLoginInfo = new NetDEVSDKLib.NETDEV_DEVICE_LOGIN_INFO_S();
        NetDEVSDKLib.NETDEV_SELOG_INFO_S stSELogInfo = new NetDEVSDKLib.NETDEV_SELOG_INFO_S();
        System.arraycopy(param.getIp().getBytes(), 0, stDevLoginInfo.szIPAddr, 0, param.getIp().getBytes().length);
        stDevLoginInfo.dwPort = param.getPort();
        System.arraycopy(param.getUsername().getBytes(), 0, stDevLoginInfo.szUserName, 0, param.getUsername().getBytes().length);
        System.arraycopy(param.getPassword().getBytes(), 0, stDevLoginInfo.szPassword, 0, param.getPassword().getBytes().length);
        loginHandle = netdevsdk.NETDEV_Login_V30(stDevLoginInfo, stSELogInfo);
        if (Objects.isNull(loginHandle)) {
            System.out.print("error code" + netdevsdk.NETDEV_GetLastError());
        }
        System.out.println("宇视设备登录");
        return 0;
    }

    @Override
    public void logout(int handle) {
        if (Objects.nonNull(loginHandle)) {
            netdevsdk.NETDEV_Logout(loginHandle);
        }
        System.out.println("宇视设备登出");
    }

    @Override
    public int startPlay() {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行预览操作！");
        }

        if (Objects.nonNull(lpreviewHandle)) {
            return 1;
        }

        /* 创建实况预览参数并赋值 */
        NetDEVSDKLib.NETDEV_PREVIEWINFO_S stPreviewInfo = new NetDEVSDKLib.NETDEV_PREVIEWINFO_S();
        stPreviewInfo.dwChannelID = 1;
        stPreviewInfo.dwStreamType = 0;
        stPreviewInfo.dwLinkMode = 1;
        stPreviewInfo.hPlayWnd = null; // 窗口句柄为空
        stPreviewInfo.dwFluency = 0;
        stPreviewInfo.dwStreamMode = 0;
        stPreviewInfo.dwLiveMode = 0;
        stPreviewInfo.dwDisTributeCloud = 0;
        stPreviewInfo.dwallowDistribution = 0;

        /* 启动实时预览 */
        lpreviewHandle = netdevsdk.NETDEV_RealPlay(loginHandle, stPreviewInfo, null, null);
        if (Objects.isNull(lpreviewHandle)) {
            System.out.println("RealPlay failed,error code" + netdevsdk.NETDEV_GetLastError());
        }
        return 1;
    }

    @Override
    public void stopPlay(int previewHandle) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行停止预览操作！");
        }

        if (Objects.nonNull(lpreviewHandle)) {
            netdevsdk.NETDEV_StopRealPlay(lpreviewHandle);
        }
    }

    @Override
    public void ptzControl(PtzControlBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行云台控制操作！");
        }

        int ptzCommanded = commandConvert(param.getCommand());
        boolean result = netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, ptzCommanded, param.getSpeed());
        if (!result) {
            System.out.println("Check if the corresponding device supports PTZ or if the selected channel is correct.error code" + netdevsdk.NETDEV_GetLastError());
        }
    }

    @Override
    public void stopPtz(PtzControlBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行云台控制操作！");
        }

        boolean result = netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_ALLSTOP, param.getSpeed());
        if (!result) {
            System.out.println("Check if the corresponding device supports PTZ or if the selected channel is correct.error code" + netdevsdk.NETDEV_GetLastError());
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
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行预置点操作！");
        }

        NetDEVSDKLib.NETDEV_PTZ_ALLPRESETS_S stPtzPresets = new NetDEVSDKLib.NETDEV_PTZ_ALLPRESETS_S();
        boolean result = netdevsdk.NETDEV_GetPTZPresetList(loginHandle, 1, stPtzPresets);
        if (!result) {
            System.out.printf("NETDEV_GetPTZPresetList failed:%d\n", netdevsdk.NETDEV_GetLastError());
        }
        List<Map<String, Object>> presets = new ArrayList<>();
        for (int i = 0; i < stPtzPresets.dwSize; i++) {
            Map<String, Object> preset = new HashMap<>();
            preset.put("presetId", stPtzPresets.astPreset[i].dwPresetID);
            preset.put("presetName", stPtzPresets.astPreset[i].szPresetName);
            presets.add(preset);
        }
        return presets;
    }

    @Override
    public int setPreset(SetPresetBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行预置点操作！");
        }

        boolean result = netdevsdk.NETDEV_PTZPreset_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_PRESETCMD_E.NETDEV_PTZ_SET_PRESET, param.getPresetName(), param.getPresetId());
        if (!result) {
            System.out.printf("NETDEV_PTZPreset_Other failed:%d\n", netdevsdk.NETDEV_GetLastError());
        }
        return 0;
    }

    @Override
    public void deletePreset(DeletePresetBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行预置点操作！");
        }

        boolean result = netdevsdk.NETDEV_PTZPreset_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_PRESETCMD_E.NETDEV_PTZ_CLE_PRESET, "", param.getPresetId());
        if (!result) {
            System.out.printf("NETDEV_PTZPreset_Other failed:%d\n", netdevsdk.NETDEV_GetLastError());
        }
    }

    @Override
    public void gotoPreset(GotoPresetBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行预置点操作！");
        }

        boolean result = netdevsdk.NETDEV_PTZPreset_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_PRESETCMD_E.NETDEV_PTZ_GOTO_PRESET, "", param.getPresetId());
        if (!result) {
            System.out.printf("NETDEV_PTZPreset_Other failed:%d\n", netdevsdk.NETDEV_GetLastError());
        }
    }

    @Override
    public void controlLight(ControlLightBO param) {
        switch (param.getCommand()) {
            case "on":
                netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_LIGHTON, 9);
                break;
            case "off":
                netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_LIGHTOFF, 9);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void controlWiper(ControlWiperBO param) {
        switch (param.getCommand()) {
            case "on":
                netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_BRUSHON, 9);
                break;
            case "off":
                netdevsdk.NETDEV_PTZControl_Other(loginHandle, 1, NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_BRUSHOFF, 9);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void auxiliaryFocus(AuxiliaryFocusBO param) {
        throw new SdkUnsupportedException(CameraBrand.Uniview, "功能【辅助聚焦】暂不支持！");
    }

    @Override
    public void initLens(InitLensBO param) {
        throw new SdkUnsupportedException(CameraBrand.Uniview, "功能【镜头初始化】暂不支持！");
    }

    @Override
    public String captureImage(CaptureImageBO param) {
        boolean result = netdevsdk.NETDEV_CaptureNoPreview(loginHandle, param.getChannelId(), 0, param.getImgPath(), 1);
        if (!result) {
            throw new CameraSdkException("Make sure the device supports non-preview capture");
        }
        return param.getImgPath();
    }

    /**
     * 获取聚焦模式
     */
    public String getFocusMode() {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行聚焦模式操作！");
        }

        String mode = "UNKNOWN";
        NetDEVSDKLib.NETDEV_FOCUS_INFO_S focusMode = new NetDEVSDKLib.NETDEV_FOCUS_INFO_S();
        focusMode.write();
        IntByReference dwBytesReturned = new IntByReference();
        boolean result = netdevsdk.NETDEV_GetDevConfig(loginHandle, 1, NetDEVSDKLib.NETDEV_CONFIG_COMMAND_E.NETDEV_GET_FOCUSINFO,
                focusMode.getPointer(), focusMode.size(), dwBytesReturned);
        if (!result) {
            System.out.printf("NETDEV_GetDevConfig failed:%d\n", netdevsdk.NETDEV_GetLastError());
        } else {
            focusMode.read();
            if (focusMode.enFocusMode == 1) {
                mode = "AUTO";
            } else if (focusMode.enFocusMode == 2) {
                mode = "MANUAL";
            }
        }
        return mode;
    }

    /**
     * 设置聚焦模式
     *
     * @param param 设置聚焦模式参数
     */
    public void setFocusMode(SetFocusModeBO param) {
        if (Objects.isNull(loginHandle)) {
            throw new CameraSdkException("请先设备登录，再进行聚焦模式操作！");
        }

        NetDEVSDKLib.NETDEV_FOCUS_INFO_S focusMode = new NetDEVSDKLib.NETDEV_FOCUS_INFO_S();
        int mode = 0;
        switch (param.getCommand()) {
            case "AUTO":
                mode = NetDEVSDKLib.NETDEV_FOCUS_MODE_E.NETDEV_FOCUS_AUTO;
                break;
            case "MANUAL":
                mode = NetDEVSDKLib.NETDEV_FOCUS_MODE_E.NETDEV_FOCUS_MANUAL;
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
        focusMode.enFocusMode = mode;
        focusMode.write();
        boolean result = netdevsdk.NETDEV_SetDevConfig(loginHandle, 1, NetDEVSDKLib.NETDEV_CONFIG_COMMAND_E.NETDEV_SET_FOCUSINFO,
                focusMode.getPointer(), focusMode.size());
        if (!result) {
            System.out.printf("NETDEV_SetDevConfig failed:%d\n", netdevsdk.NETDEV_GetLastError());
        }
    }

    /**
     * 通用PTZ指令转化为摄像头SDK PTZ指令
     *
     * @param command 通用PTZ控制命令
     * @return 摄像头厂家PTZ控制命令
     */
    private int commandConvert(PtzCommand command) {
        int ptzCommand = -1;
        switch (command) {
            case UP:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_TILTUP;
                break;
            case DOWN:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_TILTDOWN;
                break;
            case LEFT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_PANLEFT;
                break;
            case RIGHT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_PANRIGHT;
                break;
            case UP_LEFT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_LEFTUP;
                break;
            case UP_RIGHT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_RIGHTUP;
                break;
            case DOWN_LEFT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_LEFTDOWN;
                break;
            case DOWN_RIGHT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_RIGHTDOWN;
                break;
            case ZOOM_IN:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_ZOOMWIDE;
                break;
            case ZOOM_OUT:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_ZOOMTELE;
                break;
            case FOCUS_NEAR:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_FOCUSNEAR;
                break;
            case FOCUS_FAR:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_FOCUSFAR;
                break;
            case IRIS_OPEN:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_IRISOPEN;
                break;
            case IRIS_CLOSE:
                ptzCommand = NetDEVSDKLib.NETDEV_PTZ_E.NETDEV_PTZ_IRISCLOSE;
                break;
        }
        return ptzCommand;
    }
}
