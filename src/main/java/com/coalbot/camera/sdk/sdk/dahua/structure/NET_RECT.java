package com.coalbot.camera.sdk.sdk.dahua.structure;


import com.coalbot.camera.sdk.sdk.dahua.NetSDKLibStructure;

/**
 * 
 * 
 * @author ： 260611
 * @since ： Created in 2021/10/19 19:35
 */
public class NET_RECT extends NetSDKLibStructure.SdkStructure {
    /**
     *    int             nLeft;
     */
    public int              nLeft;
    /**
     *    int             nTop;
     */
    public int              nTop;
    /**
     *    int             nRight;
     */
    public int              nRight;
    /**
     *    int             nBottom;
     */
    public int              nBottom;

    @Override
    public String toString() {
        return "NET_RECT{" +
                "nLeft=" + nLeft +
                ", nTop=" + nTop +
                ", nRight=" + nRight +
                ", nBottom=" + nBottom +
                '}';
    }
}

