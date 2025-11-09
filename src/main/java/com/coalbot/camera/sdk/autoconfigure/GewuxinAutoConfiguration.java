package com.coalbot.camera.sdk.autoconfigure;

import com.coalbot.camera.sdk.autoconfigure.properties.CameraSdkProperties;
import com.coalbot.camera.sdk.handler.impl.GewuxinHandlerImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：HikvisionAutoConfiguration
 * @Author: XinHai.Ma
 * @Date: 2025/11/6 16:23
 * @Description: 格物优信SDK自动配置类
 */
@Configuration
@EnableConfigurationProperties(CameraSdkProperties.class)
@ConditionalOnProperty(prefix = "camera.sdk", name = "gewuxin", havingValue = "true", matchIfMissing = true) // 存在宇视配置时生效
public class GewuxinAutoConfiguration {

    @Bean
    public GewuxinHandlerImpl gewuxinHandler() {
        return new GewuxinHandlerImpl();
    }

    // 注册初始化钩子，程序启动时初始化SDK实例
    @Bean
    public CommandLineRunner gewuxinInitRunner() {
        return args -> {
            System.out.println("格物优信SDK实例初始化");
        };
    }

    // 注册销毁钩子，程序退出时释放SDK实例
    @Bean
    public CommandLineRunner gewuxinDestroyRunner() {
        return args -> Runtime.getRuntime().addShutdownHook(new Thread(
                () -> System.out.println("格物优信SDK实例销毁")
        ));
    }

}
