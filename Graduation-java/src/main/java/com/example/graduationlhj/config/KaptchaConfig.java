package com.example.graduationlhj.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * 验证码配置
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.image.height", "38");
        properties.put("kaptcha.image.width", "150");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.font.size", "32");
//        propertis.put("kaptcha.background.clear.from","light grey");
//        propertis.put("kaptcha.background.clear.to","white");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
