package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：DeviceLoginBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:15
 * @Description: 设备登录BO（项目内部使用）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLoginBO {

    private String ip = "127.0.0.1";
    private int port = 80;
    private String username = "admin";
    private String password = "123456";

}
