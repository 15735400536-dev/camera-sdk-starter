package com.coalbot.camera.sdk.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName：CameraProperties
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:10
 * @Description: 摄像头SDK配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "camera.sdk")
public class CameraSdkProperties {

    /**
     * 海康威视SDK启用开关：true.启用 false.禁用 默认false.禁用
     */
    private boolean hikvision = false;
    /**
     * 大华SDK启用开关：true.启用 false.禁用 默认false.禁用
     */
    private boolean dahua = false;
    /**
     * 宇视SDK启用开关：true.启用 false.禁用 默认false.禁用
     */
    private boolean uniview = false;
    /**
     * 格物优信SDK启用开关：true.启用 false.禁用 默认false.禁用
     */
    private boolean gewuxin = false;

}
