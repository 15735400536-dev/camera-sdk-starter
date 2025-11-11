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
import com.coalbot.camera.sdk.sdk.hikvision.HCNetSDK;
import com.coalbot.camera.sdk.util.CameraSdkUtils;
import com.sun.jna.Native;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：HikvisionHandlerImpl
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:22
 * @Description: 海康威视摄像头SDK实现类
 */
public class HikvisionHandlerImpl implements CameraSdkHandler {

    /**
     * 海康威视SDK实例
     */
    private static HCNetSDK hCNetSDK = null;
    /**
     * 登录句柄
     */
    private static int loginHandle = -1;
    /**
     * 预览句柄
     */
    private static int previewHandle = -1;

    public static void initSDK() {
        if (Objects.isNull(hCNetSDK)) {
            synchronized (HCNetSDK.class) {
                String sdkPath = CameraSdkUtils.getSdkPath(CameraBrand.Hikvision);
                hCNetSDK = (HCNetSDK) Native.loadLibrary(sdkPath, HCNetSDK.class);
                boolean initResult = hCNetSDK.NET_DVR_Init();
                if (!initResult) {
                    throw new CameraSdkException("海康威视SDK初始化失败");
                }
                // 设置海康威视SDK日志路径
                hCNetSDK.NET_DVR_SetLogToFile(3, CameraSdkUtils.getSdkLogPath(CameraBrand.Hikvision), false);
            }
        }
    }

    public static void releaseSDK() {
        if (Objects.nonNull(hCNetSDK)) {
            hCNetSDK.NET_DVR_Cleanup();
        }
    }

    @Override
    public int login(DeviceLoginBO param) {
        if (loginHandle > -1) {
            // 先注销
            logout(loginHandle);
        }

        HCNetSDK.NET_DVR_DEVICEINFO_V30 dvrDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        loginHandle = hCNetSDK.NET_DVR_Login_V30(param.getIp(), (short) param.getPort(), param.getUsername(), param.getPassword(), dvrDeviceInfo);

        if (loginHandle == -1) {
            int error = hCNetSDK.NET_DVR_GetLastError();
            System.out.println("注册失败,错误码：" + error);
        }

        System.out.println("海康威视设备登录");
        return loginHandle;
    }

    @Override
    public void logout(int handle) {
        // 如果在预览,先停止预览,释放句柄
        stopPlay(previewHandle);

        // 如果已经注册,注销
        if (handle > -1) {
            hCNetSDK.NET_DVR_Logout_V30(handle);
            loginHandle = -1;
            System.out.println("海康威视设备登出");
        }
    }

    @Override
    public int startPlay() {
        if (loginHandle == -1) {
            throw new CameraSdkException("请先设备登录，再进行预览！");
        }
        if (previewHandle > -1) {
            return previewHandle;
        }

        HCNetSDK.NET_DVR_PREVIEWINFO previewinfo = new HCNetSDK.NET_DVR_PREVIEWINFO();
        previewinfo.read();
        previewinfo.lChannel = 1;   //通道号
        previewinfo.dwStreamType = 0; //0-主码流，1-子码流，2-三码流，3-虚拟码流，以此类推
        previewinfo.dwLinkMode = 0;   //连接方式：0- TCP方式，1- UDP方式，2- 多播方式，3- RTP方式，4- RTP/RTSP，5- RTP/HTTP，6- HRUDP（可靠传输） ，7- RTSP/HTTPS，8- NPQ
        previewinfo.bBlocked = 1;     //0- 非阻塞取流，1- 阻塞取流
        previewinfo.write();
        previewHandle = hCNetSDK.NET_DVR_RealPlay_V40(loginHandle, previewinfo, null, null);
        if (previewHandle == -1) {
            int error = hCNetSDK.NET_DVR_GetLastError();
            System.out.println("开启预览失败,错误码：" + error);
        }
        return previewHandle;
    }

    @Override
    public void stopPlay(int previewHandle) {
        // 如果在预览,先停止预览,释放句柄
        if (previewHandle > -1) {
            hCNetSDK.NET_DVR_StopRealPlay(previewHandle);
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
                ptzCommand = HCNetSDK.TILT_UP;
                break;
            case DOWN:
                ptzCommand = HCNetSDK.TILT_DOWN;
                break;
            case LEFT:
                ptzCommand = HCNetSDK.PAN_LEFT;
                break;
            case RIGHT:
                ptzCommand = HCNetSDK.PAN_RIGHT;
                break;
            case UP_LEFT:
                ptzCommand = HCNetSDK.UP_LEFT;
                break;
            case UP_RIGHT:
                ptzCommand = HCNetSDK.UP_RIGHT;
                break;
            case DOWN_LEFT:
                ptzCommand = HCNetSDK.DOWN_LEFT;
                break;
            case DOWN_RIGHT:
                ptzCommand = HCNetSDK.DOWN_RIGHT;
                break;
            case ZOOM_IN:
                ptzCommand = HCNetSDK.ZOOM_IN;
                break;
            case ZOOM_OUT:
                ptzCommand = HCNetSDK.ZOOM_OUT;
                break;
            case FOCUS_NEAR:
                ptzCommand = HCNetSDK.FOCUS_NEAR;
                break;
            case FOCUS_FAR:
                ptzCommand = HCNetSDK.FOCUS_FAR;
                break;
            case IRIS_OPEN:
                ptzCommand = HCNetSDK.IRIS_OPEN;
                break;
            case IRIS_CLOSE:
                ptzCommand = HCNetSDK.IRIS_CLOSE;
                break;
        }
        return ptzCommand;
    }

    @Override
    public void ptzControl(PtzControlBO param) {
        // 开启预览
        startPlay();

        // 开始PTZ控制
        int ptzCommand = commandConvert(param.getCommand());
        if (param.getSpeed() >= 1) {
            // 带速度的PTZ控制
            hCNetSDK.NET_DVR_PTZControlWithSpeed(previewHandle, ptzCommand, 0, param.getSpeed());
        } else {
            // 使用云台默认速度
            hCNetSDK.NET_DVR_PTZControl(previewHandle, ptzCommand, 0);
        }
    }

    @Override
    public void stopPtz(PtzControlBO param) {
        int ptzCommand = commandConvert(param.getCommand());
        if (param.getSpeed() >= 1) {
            // 带速度的PTZ控制
            hCNetSDK.NET_DVR_PTZControlWithSpeed(previewHandle, ptzCommand, 1, param.getSpeed());
        } else {
            // 使用云台默认速度
            hCNetSDK.NET_DVR_PTZControl(previewHandle, ptzCommand, 1);
        }
    }

    @Override
    public void zoom(ZoomBO param) {
        // 开启预览
        startPlay();

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
        // 开启预览
        startPlay();

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
        // 开启预览
        startPlay();

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
        // 开启预览
        startPlay();

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
        // 开启预览
        startPlay();

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
        // 开启预览
        startPlay();

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
        return null;
    }

    @Override
    public int setPreset(SetPresetBO param) {
        // 开启预览
        startPlay();

        boolean result = hCNetSDK.NET_DVR_PTZPreset(previewHandle, HCNetSDK.SET_PRESET, param.getPresetId());
        if (!result) {
            System.out.println("设置预置点失败！");
        }
        return param.getPresetId();
    }

    @Override
    public void deletePreset(DeletePresetBO param) {
        boolean result = hCNetSDK.NET_DVR_PTZPreset(previewHandle, HCNetSDK.CLE_PRESET, param.getPresetId());
        if (!result) {
            System.out.println("删除预置点失败！");
        }
    }

    @Override
    public void gotoPreset(GotoPresetBO param) {
        boolean result = hCNetSDK.NET_DVR_PTZPreset(previewHandle, HCNetSDK.GOTO_PRESET, param.getPresetId());
        if (!result) {
            System.out.println("预置点调用失败！");
        }
    }

    @Override
    public void controlLight(ControlLightBO param) {
        // 开启预览
        startPlay();

        switch (param.getCommand()) {
            case "on":
                hCNetSDK.NET_DVR_PTZControl(previewHandle, HCNetSDK.LIGHT_PWRON, 0);
                break;
            case "off":
                hCNetSDK.NET_DVR_PTZControl(previewHandle, HCNetSDK.LIGHT_PWRON, 1);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void controlWiper(ControlWiperBO param) {
        // 开启预览
        startPlay();

        switch (param.getCommand()) {
            case "on":
                hCNetSDK.NET_DVR_PTZControl(previewHandle, HCNetSDK.WIPER_PWRON, 0);
                break;
            case "off":
                hCNetSDK.NET_DVR_PTZControl(previewHandle, HCNetSDK.WIPER_PWRON, 1);
                break;
            default:
                throw new CameraSdkException("不合法命令！");
        }
    }

    @Override
    public void auxiliaryFocus(AuxiliaryFocusBO param) {
        throw new SdkUnsupportedException(CameraBrand.Hikvision, "功能【辅助聚焦】暂不支持！");
    }

    @Override
    public void initLens(InitLensBO param) {
        throw new SdkUnsupportedException(CameraBrand.Hikvision, "功能【镜头初始化】暂不支持！");
    }

    @Override
    public String captureImage(CaptureImageBO param) {
        // 开启预览
        startPlay();

        boolean result = hCNetSDK.NET_DVR_PlayBackCaptureFile(previewHandle, param.getImgPath());
        if (!result) {
            throw new CameraSdkException("抓图失败");
        }
        return param.getImgPath();
    }

    @Override
    public void absolute(AbsoluteBO param) {

    }
}
