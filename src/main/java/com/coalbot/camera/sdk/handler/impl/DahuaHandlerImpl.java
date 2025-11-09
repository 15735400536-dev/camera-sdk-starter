package com.coalbot.camera.sdk.handler.impl;

import com.coalbot.camera.sdk.bo.*;
import com.coalbot.camera.sdk.bo.preset.DeletePresetBO;
import com.coalbot.camera.sdk.bo.preset.GotoPresetBO;
import com.coalbot.camera.sdk.bo.preset.QueryPresetBO;
import com.coalbot.camera.sdk.bo.preset.SetPresetBO;
import com.coalbot.camera.sdk.handler.CameraSdkHandler;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：DahuaHandlerImpl
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 17:28
 * @Description: 大华摄像头SDK实现类
 */
public class DahuaHandlerImpl implements CameraSdkHandler {

    public static void initSDK() {
        System.out.println("大华SDK初始化");
    }

    public static void releaseSDK() {

    }

    @Override
    public int login(DeviceLoginBO param) {
        System.out.println("大华设备登录");
        return 0;
    }

    @Override
    public void logout(int handle) {
        System.out.println("大华设备登出");
    }

    @Override
    public int startPlay() {
        return 0;
    }

    @Override
    public void stopPlay(int previewHandle) {

    }

    @Override
    public void ptzControl(PtzControlBO param) {

    }

    @Override
    public void stopPtz(PtzControlBO param) {

    }

    @Override
    public void zoom(ZoomBO param) {

    }

    @Override
    public void stopZoom(ZoomBO param) {

    }

    @Override
    public void focus(FocusBO param) {

    }

    @Override
    public void stopFocus(FocusBO param) {

    }

    @Override
    public void iris(IrisBO param) {

    }

    @Override
    public void stopIris(IrisBO param) {

    }

    @Override
    public List<Map<String, Object>> queryPresets(QueryPresetBO param) {
        return null;
    }

    @Override
    public int setPreset(SetPresetBO param) {
        return 0;
    }

    @Override
    public void deletePreset(DeletePresetBO param) {

    }

    @Override
    public void gotoPreset(GotoPresetBO param) {

    }

    @Override
    public void controlLight(ControlLightBO param) {

    }

    @Override
    public void controlWiper(ControlWiperBO param) {

    }

    @Override
    public void auxiliaryFocus(AuxiliaryFocusBO param) {

    }

    @Override
    public void initLens(InitLensBO param) {

    }

    @Override
    public String captureImage(CaptureImageBO param) {
        return null;
    }
}
