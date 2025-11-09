package com.coalbot.camera.sdk.bo.preset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：SetPresetBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:13
 * @Description: 设置预置点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetPresetBO {

    /**
     * 预置点ID
     */
    private int presetId;
    /**
     * 预置点名称
     */
    private String presetName;

}
