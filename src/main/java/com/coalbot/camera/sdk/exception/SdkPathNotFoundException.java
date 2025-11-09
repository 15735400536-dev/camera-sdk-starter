package com.coalbot.camera.sdk.exception;

import lombok.Getter;

/**
 * @ClassName：SdkPathNotFoundException
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 16:07
 * @Description: SDK路径不存在异常（当用户配置路径或默认路径均无效时抛出）
 */
@Getter
public class SdkPathNotFoundException extends CameraSdkException {

    private final String brand; // 厂商名称

    public SdkPathNotFoundException(String brand, String message) {
        super(String.format("[%s] SDK路径错误：%s", brand, message));
        this.brand = brand;
    }

}
