package com.coalbot.camera.sdk.bo.preset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：GotoPresetBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:13
 * @Description: 调用预置点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GotoPresetBO {

    /**
     * 预置点ID
     */
    private int presetId;
    /**
     * 速度
     */
    private int speed;

}
