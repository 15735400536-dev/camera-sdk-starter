package com.coalbot.camera.sdk.bo;

import lombok.Data;

/**
 * @ClassName：AbsoluteBO
 * @Author: XinHai.Ma
 * @Date: 2025/11/11 18:00
 * @Description: 精准控制
 */
@Data
public class AbsoluteBO {

    private int pan = 0;
    private int tilt = 0 ;
    private int zoom = 0 ;
    private int speed = 1;

}
