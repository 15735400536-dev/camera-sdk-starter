package com.coalbot.camera.sdk.enums;

import lombok.Getter;

/**
 * @ClassName：OsType
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 15:21
 * @Description: 操作系统类型枚举（用于匹配SDK库文件后缀）
 */
@Getter
public enum OsType {

    WINDOWS("windows", ".dll"),
    LINUX("linux", ".so"),
    MAC("mac", ".dylib"),
    UNKNOWN("unknown", "");

    /**
     * 系统名称关键字
     */
    private final String osName;
    /**
     * 库文件后缀
     */
    private final String libSuffix;

    OsType(String osName, String libSuffix) {
        this.osName = osName;
        this.libSuffix = libSuffix;
    }

    /**
     * 根据当前系统获取OsType
     */
    public static OsType getCurrentOs() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains(WINDOWS.osName)) {
            return WINDOWS;
        } else if (osName.contains(LINUX.osName)) {
            return LINUX;
        } else if (osName.contains(MAC.osName)) {
            return MAC;
        } else {
            return UNKNOWN;
        }
    }

}
