package com.coalbot.camera.sdk.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：CaptureImageBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 18:18
 * @Description: 抓图
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptureImageBO {

    /**
     * 通道ID
     */
    private int channelId;
    /**
     * 保存图片地址
     */
    private String imgPath;

}
