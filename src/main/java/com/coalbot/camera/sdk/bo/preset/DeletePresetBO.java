package com.coalbot.camera.sdk.bo.preset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：DeletePreset
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:13
 * @Description: 删除预置点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePresetBO {

    /**
     * 预置点ID
     */
    private int presetId;

}
