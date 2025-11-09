package com.coalbot.camera.sdk.autoconfigure;

import com.coalbot.camera.sdk.autoconfigure.properties.CameraSdkProperties;
import com.coalbot.camera.sdk.handler.impl.HikvisionHandlerImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：HikvisionAutoConfiguration
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 16:23
 * @Description: 海康威视SDK自动配置类
 */
@Configuration
@EnableConfigurationProperties(CameraSdkProperties.class)
@ConditionalOnProperty(prefix = "camera.sdk", name = "hikvision", havingValue = "true", matchIfMissing = true) // 存在海康配置时生效
public class HikvisionAutoConfiguration {

    @Bean
    public HikvisionHandlerImpl hikvisionHandler() {
        return new HikvisionHandlerImpl();
    }

    // 注册初始化钩子，程序启动时初始化SDK实例
    @Bean
    public CommandLineRunner hikvisionInitRunner() {
        return args -> {
            HikvisionHandlerImpl.initSDK();
            System.out.println("海康威视SDK实例初始化");
        };
    }

    // 注册销毁钩子，程序退出时释放SDK实例
    @Bean
    public CommandLineRunner hikvisionDestroyRunner() {
        return args -> Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    HikvisionHandlerImpl.releaseSDK();
                    System.out.println("海康威视SDK实例销毁");
                }
        ));
    }

}
