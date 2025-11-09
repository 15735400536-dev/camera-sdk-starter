package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：ZoomBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:11
 * @Description: 变倍
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoomBO {

    /**
     * 变倍命令：zoom+ zoom-
     */
    private String command;
    /**
     * 变倍速度
     */
    private int speed;

}
