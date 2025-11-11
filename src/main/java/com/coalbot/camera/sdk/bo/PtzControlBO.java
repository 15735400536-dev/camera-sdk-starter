package com.coalbot.camera.sdk.bo;

import com.coalbot.camera.sdk.enums.PtzCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：PtzControl
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:10
 * @Description: PTZ控制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PtzControlBO {

    /**
     * 云台控制命令
     */
    private PtzCommand command;
    /**
     * 速度，海康威视范围[1,10]，宇视范围[1,9]
     */
    private int speed = 0;

}
