package com.coalbot.camera.sdk.autoconfigure;

import com.coalbot.camera.sdk.autoconfigure.properties.CameraSdkProperties;
import com.coalbot.camera.sdk.handler.impl.DahuaHandlerImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：DahuaAutoConfiguration
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 16:42
 * @Description: 大华SDK自动配置类
 */
@Configuration
@EnableConfigurationProperties(CameraSdkProperties.class)
@ConditionalOnProperty(prefix = "camera.sdk", name = "dahua", havingValue = "true", matchIfMissing = true) // 存在大华配置时生效
public class DahuaAutoConfiguration {

    @Bean
    public DahuaHandlerImpl dahuaHandler() {
        return new DahuaHandlerImpl();
    }

    // 注册初始化钩子，程序启动时初始化SDK实例
    @Bean
    public CommandLineRunner dahuaInitRunner() {
        return args -> {
            DahuaHandlerImpl.initSDK();
            System.out.println("大华SDK实例初始化");
        };
    }

    // 注册销毁钩子，程序退出时释放SDK实例
    @Bean
    public CommandLineRunner dahuaDestroyRunner() {
        return args -> Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    DahuaHandlerImpl.releaseSDK();
                    System.out.println("大华SDK实例销毁");
                }
        ));
    }

}
