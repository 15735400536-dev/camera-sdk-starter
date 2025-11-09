package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：ControlLightBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:17
 * @Description: 灯光控制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlLightBO {

    /**
     * 灯光控制：on.开灯 off.关灯
     */
    private String command;

}
