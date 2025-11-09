package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：IrisBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:12
 * @Description: 光圈控制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrisBO {

    /**
     * 变倍命令：iris+ iris-
     */
    private String command;
    /**
     * 光圈速度
     */
    private int speed;

}
