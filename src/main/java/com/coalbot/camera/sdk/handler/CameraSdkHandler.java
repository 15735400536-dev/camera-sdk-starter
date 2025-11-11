package com.coalbot.camera.sdk.handler;

import com.coalbot.camera.sdk.bo.*;
import com.coalbot.camera.sdk.bo.preset.DeletePresetBO;
import com.coalbot.camera.sdk.bo.preset.GotoPresetBO;
import com.coalbot.camera.sdk.bo.preset.QueryPresetBO;
import com.coalbot.camera.sdk.bo.preset.SetPresetBO;

import java.util.List;
import java.util.Map;

public interface CameraSdkHandler {

    /**
     * 设备登录
     *
     * @param param 登录参数
     * @return 登录句柄
     */
    int login(DeviceLoginBO param);

    /**
     * 设备登出
     *
     * @param handle 登录句柄
     */
    void logout(int handle);

    /**
     * 开始预览
     *
     * @return 预览句柄
     */
    int startPlay();

    /**
     * 停止预览
     *
     * @param previewHandle 预览句柄
     */
    void stopPlay(int previewHandle);

    /**
     * PTZ控制
     *
     * @param param PTZ控制参数
     */
    void ptzControl(PtzControlBO param);

    /**
     * 停止PTZ控制
     *
     * @param param 停止PTZ控制参数
     */
    void stopPtz(PtzControlBO param);

    /**
     * 变倍
     *
     * @param param 参数
     */
    void zoom(ZoomBO param);

    /**
     * 停止变倍
     *
     * @param param 参数
     */
    void stopZoom(ZoomBO param);

    /**
     * 变焦
     *
     * @param param 参数
     */
    void focus(FocusBO param);

    /**
     * 停止变焦
     *
     * @param param 参数
     */
    void stopFocus(FocusBO param);

    /**
     * 光圈控制
     *
     * @param param 参数
     */
    void iris(IrisBO param);

    /**
     * 停止光圈控制
     * @param param 参数
     */
    void stopIris(IrisBO param);

    /**
     * 查询预置点
     *
     * @param param 参数
     * @return
     */
    List<Map<String, Object>> queryPresets(QueryPresetBO param);

    /**
     * 设置预置点
     *
     * @param param 参数
     * @return 预置点ID
     */
    int setPreset(SetPresetBO param);

    /**
     * 删除预置点
     *
     * @param param 参数
     */
    void deletePreset(DeletePresetBO param);

    /**
     * 调用预置点
     *
     * @param param 参数
     */
    void gotoPreset(GotoPresetBO param);

    /**
     * 灯光控制
     *
     * @param param 参数
     */
    void controlLight(ControlLightBO param);

    /**
     * 雨刷控制
     *
     * @param param 参数
     */
    void controlWiper(ControlWiperBO param);

    /**
     * 辅助聚焦
     *
     * @param param 参数
     */
    void auxiliaryFocus(AuxiliaryFocusBO param);

    /**
     * 镜头初始化
     *
     * @param param 参数
     */
    void initLens(InitLensBO param);

    /**
     * 抓图
     *
     * @param param 参数
     * @return 图片地址
     */
    String captureImage(CaptureImageBO param);

    /**
     * 精准控制
     * @param param 参数
     */
    void absolute(AbsoluteBO param);

}
