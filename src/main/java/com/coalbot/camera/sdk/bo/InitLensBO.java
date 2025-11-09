package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：InitLensBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:18
 * @Description: 镜头初始化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitLensBO {

    /**
     * 通道ID
     */
    private int channelId = 1;

}
