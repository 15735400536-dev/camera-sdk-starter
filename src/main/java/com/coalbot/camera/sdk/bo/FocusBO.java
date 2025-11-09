package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：FocusBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:11
 * @Description: 变焦
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FocusBO {

    /**
     * 变倍命令：focus+ focus-
     */
    private String command;
    /**
     * 变焦速度
     */
    private int speed;

}
