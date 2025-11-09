package com.coalbot.camera.sdk.exception;

/**
 * @ClassName：CameraSdkException
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:33
 * @Description: 统一摄像头SDK异常类，捕获厂商 SDK 的错误码并封装
 */
public class CameraSdkException extends RuntimeException {

    public CameraSdkException(String message) {
        super(message);
    }
    public CameraSdkException(String message, Throwable cause) {
        super(message, cause);
    }

}
