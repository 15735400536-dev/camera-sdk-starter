package com.coalbot.camera.sdk.exception;


import com.coalbot.camera.sdk.enums.CameraBrand;
import lombok.Getter;

/**
 * @ClassName：SdkUnsupportedException
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:34
 * @Description: 摄像头SDK功能不支持异常
 */
@Getter
public class SdkUnsupportedException extends CameraSdkException {

    /**
     * 厂商标识（如：Hikvision/Dahua/Uniview/Gewuxin）
     */
    private final CameraBrand brand;

    /**
     * 不支持的功能名称（如VideoParamAttribute/PTZControl）
     */
    private final String feature;

    /**
     * 无参构造（不推荐，建议携带具体信息）
     */
    public SdkUnsupportedException() {
        super("SDK does not support the specified feature");
        this.brand = CameraBrand.Unknown;
        this.feature = "unknown";
    }

    /**
     * 携带厂商和功能的构造器（推荐）
     *
     * @param brand   厂商名称（如Hikvision）
     * @param feature 不支持的功能（如VideoParamAttribute）
     */
    public SdkUnsupportedException(CameraBrand brand, String feature) {
        super(String.format("[%s] SDK does not support feature: %s", brand, feature));
        this.brand = brand;
        this.feature = feature;
    }

    /**
     * 携带详细消息、原因、厂商和功能的构造器（用于包装底层异常）
     *
     * @param message 详细描述
     * @param cause   底层异常（如厂商SDK抛出的不支持错误）
     * @param brand   厂商名称
     * @param feature 不支持的功能
     */
    public SdkUnsupportedException(String message, Throwable cause, CameraBrand brand, String feature) {
        super(message, cause);
        this.brand = brand;
        this.feature = feature;
    }

}
