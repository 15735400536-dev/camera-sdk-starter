package com.coalbot.camera.sdk.util;

import com.coalbot.camera.sdk.enums.CameraBrand;
import com.coalbot.camera.sdk.enums.OsType;
import com.coalbot.camera.sdk.exception.CameraSdkException;

import java.io.File;

/**
 * @ClassName：CameraSdkUtils
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:24
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
public class CameraSdkUtils {

    /**
     * Windows环境摄像头SDK文件夹路径
     */
    private static final String WINDOWS_CAMERA_SDK_FOLDER = "C:\\Coalbot\\CameraSdk";
    /**
     * Linux环境摄像头SDK文件夹路径
     */
    private static final String LINUX_CAMERA_SDK_FOLDER = "/home/Coalbot/CameraSdk";
    /**
     * Mac环境摄像头SDK文件夹路径
     */
    private static final String MAC_CAMERA_SDK_FOLDER = "/Applications/Coalbot/CameraSdk";

    /**
     * 根据摄像头品牌和文件名称获取摄像头SDK路径
     * @param brand 摄像头品牌
     * @param fileName 文件名称
     * @return 摄像头SDK路径
     */
    public static String getSdkPath(CameraBrand brand, String fileName) {
        String sdkPath = null;
        OsType currentOs = OsType.getCurrentOs();
        String cameraSdkFolder = getCameraSdkFolder();
        switch (brand) {
            case Hikvision:
                sdkPath = cameraSdkFolder + File.separator + "Hikvision" + File.separator + fileName + currentOs.getLibSuffix();
                break;
            case Dahua:
                sdkPath = cameraSdkFolder + File.separator + "DaHua" + File.separator + fileName + currentOs.getLibSuffix();
                break;
            case Uniview:
                sdkPath = cameraSdkFolder + File.separator + "Uniview" + File.separator + fileName + currentOs.getLibSuffix();
                break;
            case Gewuxin:
                sdkPath = cameraSdkFolder + File.separator + "Yoseenir" + File.separator + fileName + currentOs.getLibSuffix();
                break;
            default:
                throw new CameraSdkException("不支持的摄像头SDK！");
        }
        return sdkPath;
    }

    /**
     * 根据摄像头品牌获取摄像头SDK路径
     *
     * @param brand 摄像头品牌
     * @return 摄像头SDK路径
     */
    public static String getSdkPath(CameraBrand brand) {
        String sdkPath = null;
        OsType currentOs = OsType.getCurrentOs();
        String cameraSdkFolder = getCameraSdkFolder();
        switch (brand) {
            case Hikvision:
                sdkPath = cameraSdkFolder + File.separator + "Hikvision" + File.separator + "HCNetSDK" + currentOs.getLibSuffix();
                break;
            case Dahua:
                sdkPath = cameraSdkFolder + File.separator + "DaHua" + File.separator + "dhnetsdk" + currentOs.getLibSuffix();
                break;
            case Uniview:
                sdkPath = cameraSdkFolder + File.separator + "Uniview" + File.separator + "Uniview" + currentOs.getLibSuffix();
                break;
            case Gewuxin:
                sdkPath = cameraSdkFolder + File.separator + "Yoseenir" + File.separator + "YoseenSDK" + currentOs.getLibSuffix();
                break;
            default:
                throw new CameraSdkException("不支持的摄像头SDK！");
        }
        return sdkPath;
    }

    /**
     * 根据摄像头品牌获取摄像头SDK日志路径
     *
     * @param brand 摄像头品牌
     * @return 摄像头SDK日志路径
     */
    public static String getSdkLogPath(CameraBrand brand) {
        String sdkLogPath = null;
        String cameraSdkFolder = getCameraSdkFolder();
        switch (brand) {
            case Hikvision:
                sdkLogPath = cameraSdkFolder + File.separator + "Hikvision" + File.separator + "logs";
                break;
            case Dahua:
                sdkLogPath = cameraSdkFolder + File.separator + "DaHua" + File.separator + "logs";
                break;
            case Uniview:
                sdkLogPath = cameraSdkFolder + File.separator + "Uniview" + File.separator + "logs";
                break;
            case Gewuxin:
                sdkLogPath = cameraSdkFolder + File.separator + "Yoseenir" + File.separator + "logs";
                break;
            default:
                throw new CameraSdkException("不支持的摄像头SDK！");
        }
        return sdkLogPath;
    }

    /**
     * 获取默认摄像头SDK文件夹路径
     *
     * @return 摄像头SDK文件夹路径
     */
    public static String getCameraSdkFolder() {
        // 使用系统默认路径
        OsType currentOs = OsType.getCurrentOs();
        if (currentOs == OsType.WINDOWS) {
            return WINDOWS_CAMERA_SDK_FOLDER;
        } else if (currentOs == OsType.LINUX) {
            return LINUX_CAMERA_SDK_FOLDER;
        } else if (currentOs == OsType.MAC) {
            return MAC_CAMERA_SDK_FOLDER;
        } else {
            throw new CameraSdkException("加载摄像头SDK路径失败，未知操作系统！");
        }
    }

}
