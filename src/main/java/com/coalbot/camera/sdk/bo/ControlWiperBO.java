package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：ControlWiperBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:17
 * @Description: 雨刷控制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlWiperBO {

    /**
     * 雨刷控制：on.开启 off.关闭
     */
    private String command;

}
