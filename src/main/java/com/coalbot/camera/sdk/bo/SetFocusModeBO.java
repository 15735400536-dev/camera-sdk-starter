package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：SetFocusMode
 * @Author: XinHai.Ma
 * @Date: 2025/11/10 15:15
 * @Description: 设置聚焦模式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetFocusModeBO {

    /**
     * 聚焦模式：AUTO.自动 MANUAL.手动
     */
    private String command;

}
