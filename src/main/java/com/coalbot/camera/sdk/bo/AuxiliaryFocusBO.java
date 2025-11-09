package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：AuxiliaryFocusBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:17
 * @Description: 辅助聚焦
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuxiliaryFocusBO {

    /**
     * 辅助聚焦开关：true.开启 false.关闭
     */
    private boolean auxiliaryFocus;

}
